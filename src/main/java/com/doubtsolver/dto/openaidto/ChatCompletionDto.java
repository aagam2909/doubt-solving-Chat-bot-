package com.doubtsolver.dto.openaidto;

import java.util.List;

import lombok.Data;

@Data
public class ChatCompletionDto {

    private String model;
    private List<Message> messages;
    private ResponseFormat response_format;
    private double temperature;


}
