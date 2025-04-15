package ru.cinimex.taskservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.domain.TaskStatus;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.PutTaskRequest;
import ru.cinimex.taskservice.service.TaskService;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    CreateTaskRequest taskEntityToDto(TaskEntity taskEntity);
    void updateTaskDtoToEntity(@MappingTarget TaskEntity taskEntity, PutTaskRequest putTaskRequest);
    TaskEntity taskDtoToEntity(CreateTaskRequest createTaskRequest);

    @AfterMapping
    default void fillAdditionalFields(@MappingTarget TaskEntity taskEntity) {
        if (taskEntity.getStatus() == null) {
            taskEntity.setStatus(TaskStatus.CREATED);
        }
        if (taskEntity.getCreatedAt() == null){
            taskEntity.setCreatedAt(OffsetDateTime.now());
        }
        taskEntity.setUpdatedAt(OffsetDateTime.now());
    }

}
