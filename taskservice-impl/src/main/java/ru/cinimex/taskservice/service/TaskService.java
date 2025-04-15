package ru.cinimex.taskservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.domain.TaskEntity_;
import ru.cinimex.taskservice.dto.*;
import ru.cinimex.taskservice.exception.AuntethicationException;
import ru.cinimex.taskservice.exception.DateException;
import ru.cinimex.taskservice.exception.TaskException;
import ru.cinimex.taskservice.exception.UnknownTaskException;
import ru.cinimex.taskservice.mapper.TaskMapper;
import ru.cinimex.taskservice.repository.TaskRepository;
import java.time.OffsetDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public static Specification<TaskEntity> titleAndStatusAndNotificateAtStartAndNotificateAtEnd(
            final GetTasksRequest getTasksRequest){
        return new Specification<>(){

            @Override
            public Predicate toPredicate(Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Predicate titlePredicate = criteriaBuilder.like(root.get(TaskEntity_.TITLE), "%" +
                        getTasksRequest.getTitle() + "%");
                Predicate statusPredicate = criteriaBuilder.like(root.get(TaskEntity_.STATUS), "%" +
                        getTasksRequest.getStatus() + "%");
                Predicate notificateAtStartPredicate = criteriaBuilder.greaterThanOrEqualTo(
                        root.get(TaskEntity_.NOTIFICATE_AT), getTasksRequest.getNotificateAtStart());
                Predicate notificateAtEndPredicate = criteriaBuilder.lessThanOrEqualTo(
                        root.get(TaskEntity_.NOTIFICATE_AT), getTasksRequest.getNotificateAtEnd());
                Predicate assigneePredicate = criteriaBuilder.equal(root.get(TaskEntity_.ASSIGNEE),
                        SecurityContextHolder.getContext().getAuthentication().getName());

                return criteriaBuilder.and(titlePredicate, statusPredicate,
                        notificateAtStartPredicate, notificateAtEndPredicate, assigneePredicate);
            }
        };
    }

    public CreateTaskResponse createTask(CreateTaskRequest createTaskRequest) {

        if (createTaskRequest.getNotificateAt().isBefore(OffsetDateTime.now())){
            throw new DateException("Неверная дата срока выполнения.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null){
            throw new AuntethicationException("Произошла ошибка аунтефикации.");
        }
        TaskEntity taskEntity = taskMapper.taskDtoToEntity(createTaskRequest);
        taskEntity.setAssignee(username);
        taskRepository.save(taskEntity);

        return new CreateTaskResponse(taskEntity.getId());
    }

    public GetTaskResponse getTasksOfCurrentUser(GetTasksRequest getTasksRequest) {

            Specification<TaskEntity> specification = Specification.where(
                titleAndStatusAndNotificateAtStartAndNotificateAtEnd(
                getTasksRequest));

            List<TaskEntity> tasks = taskRepository.findAll(specification);

            return (GetTaskResponse) tasks.stream().map(taskEntity ->
                            new GetTaskResponse(
                                    taskEntity.getId(),
                                    taskEntity.getTitle(),
                                    taskEntity.getDescription(),
                                    taskEntity.getNotificateAt()
                            ));
        }

    public GetTaskResponse getTaskById(UUID taskId) {

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() ->
                new UnknownTaskException("Неизвестная ошибка"));

        return new GetTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getNotificateAt()
        );
    }

    public String updateTask(UUID taskId, PutTaskRequest putTaskRequest) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()
                -> new UnknownTaskException("Неизвестная задача"));

        if (!task.getStatus().equals("CREATED")){
            throw new TaskException("Это задача уже в запущена");
        }
        if (!task.getAssignee().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new TaskException("Задача не принадлежит вам");
        }

        taskMapper.updateTaskDtoToEntity(task, putTaskRequest);

        taskRepository.save(task);

        return "Выполнено";
    }

    public void deleteTask(UUID taskId) {

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() ->
                new UnknownTaskException("Неизвестная задача"));

        taskRepository.delete(task);

    }
}
