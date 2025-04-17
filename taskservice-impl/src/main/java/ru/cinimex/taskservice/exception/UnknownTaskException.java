package ru.cinimex.taskservice.exception;

public class UnknownTaskException extends RuntimeException {
    public UnknownTaskException(String message) {
        super(message);
    }
}
