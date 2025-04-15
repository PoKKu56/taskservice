package ru.cinimex.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Limit;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinimex.taskservice.controller.FeignClientContoller;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.domain.TaskStatus;
import ru.cinimex.taskservice.dto.GetUserInfoResponse;
import ru.cinimex.taskservice.dto.SendMessageKafkaResponse;
import ru.cinimex.taskservice.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaMessageScheduler {

    @Value("${scheduler.batch-size}")
    private int batchSize;

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, SendMessageKafkaResponse> kafkaTemplate;
    private final FeignClientContoller feignClientContoller;

    @Scheduled(cron = "${scheduler.cron}")
    @Transactional
    public void sendMessages() {
        System.out.println("i am here");
        List<TaskEntity> tasks = taskRepository.findAndLockTasks(Limit.of(batchSize));
        GetUserInfoResponse getUserInfoResponse = feignClientContoller.getUsers();
        for (TaskEntity task : tasks) {
            try {
                kafkaTemplate.send("tasks-topic" , new SendMessageKafkaResponse(
                        task.getTitle(),
                        task.getDescription(),
                        getUserInfoResponse.getEmail()
                ));
                task.setStatus(TaskStatus.DONE);
            } catch (Exception e) {
                task.setStatus(TaskStatus.ERROR);
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        taskRepository.saveAll(tasks);
    }
}
