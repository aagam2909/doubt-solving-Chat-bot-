package com.doubtsolver.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.doubtsolver.model.helper.Qna;
import com.fasterxml.jackson.core.io.schubfach.DoubleToDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("llmConfig")
public class LlmConfig {
	
	@Id
	private String botId;
	private String systemPrompt;
	private double temperature;
	private String model;
	

}
