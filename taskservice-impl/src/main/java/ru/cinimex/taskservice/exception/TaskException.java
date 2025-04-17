package ru.cinimex.taskservice.exception;

public class TaskException extends RuntimeException {
    public TaskException(String message) {
        super(message);
    }
}
