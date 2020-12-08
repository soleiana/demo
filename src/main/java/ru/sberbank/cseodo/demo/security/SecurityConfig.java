package ru.sberbank.cseodo.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("alice")
                .password(passwordEncoder().encode("pass"))
                .roles(Role.READER.getValue(), Role.WRITER.getValue())
                .and()
                .withUser("bob")
                .password(passwordEncoder().encode("pass"))
                .roles(Role.READER.getValue(), Role.WRITER.getValue())
                .and()
                .withUser("carol")
                .password(passwordEncoder().encode("pass"))
                .roles(Role.READER.getValue())
                .and()
                .withUser("david")
                .password(passwordEncoder().encode("pass"))
                .roles(Role.WRITER.getValue())
                .and()
                .withUser("jane")
                .password(passwordEncoder().encode("pass"))
                .roles(Role.DEFAULT.getValue());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .accessDecisionManager(accessDecisionManager())
                .and()
                .formLogin().disable()
                .csrf().disable()
                .httpBasic();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = List.of(
                new RoleVoter(), new AuthenticatedVoter(), new OpaVoter()
        );
        return new UnanimousBased(decisionVoters);
    }
}
