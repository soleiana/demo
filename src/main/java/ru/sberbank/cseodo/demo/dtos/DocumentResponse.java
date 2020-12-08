package ru.sberbank.cseodo.demo.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentResponse {

    @NonNull
    UUID id;

    @NonNull
    String content;
}
