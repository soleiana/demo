package ru.sberbank.cseodo.demo.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sberbank.cseodo.demo.dtos.CreateDocumentRequest;
import ru.sberbank.cseodo.demo.dtos.DocumentResponse;
import ru.sberbank.cseodo.demo.exceptions.NoSuchDocumentException;
import ru.sberbank.cseodo.demo.services.DocumentService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("demo")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DemoController {

    DocumentService documentService;


    @PostMapping(path = "/document", consumes = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createDocument(@RequestBody CreateDocumentRequest createDocumentRequest) {
        documentService.createDocument(createDocumentRequest);
        log.info("Document created {}", createDocumentRequest);
    }

    @GetMapping(path = "/document/{id}", produces = "application/json")
    @ResponseStatus(code = HttpStatus.OK)
    public DocumentResponse getDocument(@PathVariable UUID id) {
        val document = documentService.getDocument(id);
        return DocumentResponse.builder()
                .id(document.getId())
                .content(document.getContent())
                .build();
    }

    @ExceptionHandler({NoSuchDocumentException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleException(NoSuchDocumentException ex) {
        log.error(ex.getMessage());
    }

}
