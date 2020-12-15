package ru.sberbank.cseodo.demo.dtos;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class DocumentDTO {

    @NonNull
    String author;

    boolean secret;

    @NonNull
    String content;
}
