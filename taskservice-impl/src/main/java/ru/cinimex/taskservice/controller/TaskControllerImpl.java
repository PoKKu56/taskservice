package ru.cinimex.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskservice.dto.*;
import ru.cinimex.taskservice.service.TaskService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    @Override
    public ResponseEntity<CreateTaskResponse> createTask(CreateTaskRequest createTaskRequest) {
        return ResponseEntity.status(200).body(taskService.createTask(createTaskRequest));
    }

    @Override
    public ResponseEntity<GetTaskResponse> getAllTasks(GetTasksRequest getTasksRequest) {
        return ResponseEntity.status(200).body(taskService.getTasksOfCurrentUser(getTasksRequest));
    }

    @Override
    public ResponseEntity<GetTaskResponse> getTaskById(UUID id) {
        return ResponseEntity.status(200).body(taskService.getTaskById(id));
    }

    @Override
    public ResponseEntity<Void> deleteTaskById(UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<String> updateTaskById(UUID id, PutTaskRequest putTaskRequest) {
        return ResponseEntity.status(200).body(taskService.updateTask(id, putTaskRequest));
    }
}
