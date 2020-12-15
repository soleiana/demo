package ru.sberbank.cseodo.demo.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;
import ru.sberbank.cseodo.demo.dtos.DocumentDTO;
import ru.sberbank.cseodo.demo.entities.DocumentEntity;
import ru.sberbank.cseodo.demo.exceptions.NoSuchDocumentException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentService {

    JpaRepository<DocumentEntity, UUID> documentRepository;


    @PreAuthorize("@opaClient.allow('write', T(java.util.Map).of('author', #document.author))")
    public void createDocument(DocumentDTO document) {
        val documentEntity = DocumentEntity.builder()
                .id(UUID.randomUUID())
                .author(document.getAuthor())
                .secret(document.isSecret())
                .content(document.getContent())
                .build();
        documentRepository.save(documentEntity);
        log.info("Document created {}", documentEntity);
    }

    @PreFilter("@opaClient.allow('write', T(java.util.Map).of('author', filterObject.author))")
    public void createDocuments(List<DocumentDTO> documents) {
        val documentEntities = documents.stream()
                .map(d -> DocumentEntity.builder()
                        .id(UUID.randomUUID())
                        .author(d.getAuthor())
                        .secret(d.isSecret())
                        .content(d.getContent())
                        .build()
                )
                .collect(Collectors.toList());
        documentRepository.saveAll(documentEntities);
        log.info("Documents created {}", documentEntities);
    }

    @PostAuthorize("@opaClient.allow('read', T(java.util.Map).of('author', returnObject.author, 'secret', returnObject.secret))")
    public DocumentEntity getDocument(UUID documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchDocumentException(String.format("Document not found %s", documentId)));
    }

   @PostFilter("@opaClient.allow('read', T(java.util.Map).of('author', filterObject.author, 'secret', filterObject.secret))")
    public List<DocumentEntity> getDocuments() {
        return documentRepository.findAll();
    }

}
