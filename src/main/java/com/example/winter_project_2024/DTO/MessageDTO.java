package com.example.winter_project_2024.DTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDTO {
    private String type;
    private int value;
    private String message;
}
