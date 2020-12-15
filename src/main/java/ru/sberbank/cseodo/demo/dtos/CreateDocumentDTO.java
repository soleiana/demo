package ru.sberbank.cseodo.demo.dtos;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDocumentDTO {

    @NotBlank
    String author;

    boolean secret;

    @NotBlank
    String content;
}
