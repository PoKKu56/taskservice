package ru.cinimex.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageKafkaResponse {

    private String title;
    private String description;
    private String email;

}
