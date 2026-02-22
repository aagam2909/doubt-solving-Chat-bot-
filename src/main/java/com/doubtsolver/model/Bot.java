package com.doubtsolver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.doubtsolver.model.helper.KnowledgeBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("bots")
public class Bot {
	
	@Id
	private String name;
	private String description;
	private String prompt;
	private KnowledgeBase kb;
	
	
	

}
