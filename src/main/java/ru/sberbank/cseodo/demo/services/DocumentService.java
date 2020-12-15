package ru.sberbank.cseodo.demo.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import ru.sberbank.cseodo.demo.dtos.CreateDocumentRequest;
import ru.sberbank.cseodo.demo.entities.Document;
import ru.sberbank.cseodo.demo.exceptions.NoSuchDocumentException;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Transactional
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentService {

    JpaRepository<Document, UUID> documentRepository;


    @PreAuthorize("@opaClient.allow('write', T(java.util.Map).of('author', #request.author))")
    public void createDocument(CreateDocumentRequest request) {
        val document = Document.builder()
                .id(UUID.randomUUID())
                .author(request.getAuthor())
                .secret(request.isSecret())
                .content(request.getContent())
                .build();
        documentRepository.save(document);
        log.info("Document created {}", document);
    }

    @PostAuthorize("@opaClient.allow('read', T(java.util.Map).of('author', returnObject.author, 'secret', returnObject.secret))")
    public Document getDocument(UUID documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchDocumentException(String.format("Document not found %s", documentId)));
    }
}
