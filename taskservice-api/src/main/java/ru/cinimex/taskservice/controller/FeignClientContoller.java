package ru.cinimex.taskservice.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskservice.dto.GetUserInfoResponse;

@RestController
@FeignClient(value = "getEmail", url = "http://localhost:8080")
public interface FeignClientContoller {

    @GetMapping("/users")
    GetUserInfoResponse getUsers();
}
