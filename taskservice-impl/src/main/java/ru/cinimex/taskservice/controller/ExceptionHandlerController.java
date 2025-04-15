package ru.cinimex.taskservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cinimex.taskservice.exception.AuntethicationException;
import ru.cinimex.taskservice.exception.DateException;
import ru.cinimex.taskservice.exception.TaskException;
import ru.cinimex.taskservice.exception.UnknownTaskException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DateException.class)
    public ResponseEntity<?> dateHandleException(RuntimeException e){
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage());
    }

    @ExceptionHandler({AuntethicationException.class, UnknownTaskException.class})
    public ResponseEntity<?> handleCommonExceptions(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<?> taskStatusHandleException(RuntimeException e){
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage());
    }
}
