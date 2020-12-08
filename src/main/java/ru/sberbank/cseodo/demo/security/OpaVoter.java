package ru.sberbank.cseodo.demo.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OpaVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final String URI = "http://localhost:8181/v1/data/authz/allow";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        String name = authentication.getName();
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableList());
        String method = filterInvocation.getRequest().getMethod();
        String[] fullPath = filterInvocation.getRequest().getRequestURI().replaceAll("^/|/$", "").split("/");
        String[] path = Arrays.copyOfRange(fullPath, 1, fullPath.length);

        Map<String, Object> input = Map.of(
                "name", name,
                "authorities", authorities,
                "method", method,
                "path", path
        );

        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.set("input", objectMapper.valueToTree(input));
        log.debug("Authorization request:\n {}", requestNode.toPrettyString());

        JsonNode responseNode = Objects.requireNonNull(restTemplate.postForObject(URI, requestNode, JsonNode.class));
        log.debug("Authorization response:\n {}", responseNode.toPrettyString());

        if (responseNode.has("result") && responseNode.get("result").asBoolean()) {
            return ACCESS_GRANTED;
        } else {
            return ACCESS_DENIED;
        }
    }
}
