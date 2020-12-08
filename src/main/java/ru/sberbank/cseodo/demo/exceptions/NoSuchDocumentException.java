package ru.sberbank.cseodo.demo.exceptions;

public class NoSuchDocumentException extends RuntimeException {

    public NoSuchDocumentException(String message) {
        super(message);
    }

}
