package com.doubtsolver.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.doubtsolver.model.helper.FileDetails;
import com.doubtsolver.model.helper.Qna;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("kb")
public class KnowledgeBase {
	
	@Id
	private String botId;
	private Map<String,FileDetails> filesDetails;
	private Map<String,Qna> qnas;
	
	
	
}
