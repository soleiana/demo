package ru.sberbank.cseodo.demo.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sberbank.cseodo.demo.dtos.CreateDocumentDTO;
import ru.sberbank.cseodo.demo.dtos.DocumentDTO;
import ru.sberbank.cseodo.demo.dtos.DocumentResponseDTO;
import ru.sberbank.cseodo.demo.exceptions.NoSuchDocumentException;
import ru.sberbank.cseodo.demo.services.DocumentService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("demo")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentController {

    DocumentService documentService;


    @PostMapping(path = "/document", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createDocument(@RequestBody CreateDocumentDTO createDocumentDTO) {
        val document = DocumentDTO.builder()
                .author(createDocumentDTO.getAuthor())
                .content(createDocumentDTO.getContent())
                .secret(createDocumentDTO.isSecret())
                .build();
        documentService.createDocument(document);
    }

    @PostMapping(path = "/documents", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createDocument(@RequestBody List<CreateDocumentDTO> createDocumentDTOs) {
        val documents = createDocumentDTOs.stream()
                .map(d -> DocumentDTO.builder()
                        .author(d.getAuthor())
                        .secret(d.isSecret())
                        .content(d.getContent())
                        .build())
                .collect(Collectors.toList());
        documentService.createDocuments(documents);
    }

    @GetMapping(path = "/document/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public DocumentResponseDTO getDocument(@PathVariable UUID id) {
        val document = documentService.getDocument(id);
        return DocumentResponseDTO.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .content(document.getContent())
                .build();
    }

    @GetMapping(path = "/documents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public List<DocumentResponseDTO> getDocuments() {
        return documentService.getDocuments().stream()
                .map(d -> DocumentResponseDTO.builder()
                        .id(d.getId())
                        .author(d.getAuthor())
                        .content(d.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @ExceptionHandler({NoSuchDocumentException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleException(NoSuchDocumentException ex) {
        log.error(ex.getMessage());
    }

}
