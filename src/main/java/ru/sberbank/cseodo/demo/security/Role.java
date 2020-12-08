package ru.sberbank.cseodo.demo.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Role {

    READER("READER"),
    WRITER("WRITER"),
    DEFAULT("DEFAULT")
    ;

    String value;
}
