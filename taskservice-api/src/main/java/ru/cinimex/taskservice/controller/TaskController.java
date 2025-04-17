package ru.cinimex.taskservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.CreateTaskResponse;
import ru.cinimex.taskservice.dto.GetTasksRequest;
import ru.cinimex.taskservice.dto.PutTaskRequest;

import java.util.UUID;

@RequestMapping("/tasks")
@RestController
public interface TaskController {

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<?> createTask(@RequestBody CreateTaskRequest createTaskRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<?> getAllTasks(@RequestBody GetTasksRequest getTasksRequest);

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    ResponseEntity<?> getTaskById(@PathVariable("id") UUID id);

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTaskById(@PathVariable("id") UUID id);

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateTaskById(@PathVariable("id") UUID id,@RequestBody PutTaskRequest putTaskRequest);
}
