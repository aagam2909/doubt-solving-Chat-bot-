package com.doubtsolver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubtsolver.enums.TrainingStatus;
import com.doubtsolver.model.Bot;
import com.doubtsolver.model.KnowledgeBase;
import com.doubtsolver.model.helper.FileDetails;
import com.doubtsolver.repository.BotRepository;
import com.doubtsolver.repository.KbRepository;

@Service
public class KbService {
	
	@Autowired
	private KbRepository kbRepository;
	
	@Autowired
	private BotRepository botRepository;
	
	public String addNewFileStatus(String botId, String fileName) throws Exception {
		Optional<Bot> botOptional = botRepository.findById(botId);
		if(botOptional.isEmpty()) throw new Exception("No such botId found, hence we cannot train on this file Id");
		Optional<KnowledgeBase> kbOptional = kbRepository.findById(botId);
		UUID uuid = UUID.randomUUID();
		FileDetails fileDetail = new FileDetails();
		
		fileDetail.setFileName(fileName);
		fileDetail.setTrainingStatus(TrainingStatus.INPROCESS);
		if(kbOptional.isEmpty()) {
			KnowledgeBase kBase  = new KnowledgeBase();
			kBase.setBotId(botId);
			Map<String, FileDetails> fileDetails = new HashMap<String, FileDetails>();
			
			fileDetails.put(uuid.toString(), fileDetail);
			kBase.setFilesDetails(fileDetails);
			kbRepository.save(kBase);
			
		}else {
			KnowledgeBase kBase = kbOptional.get();
			kBase.getFilesDetails().put(uuid.toString(),fileDetail);
			kbRepository.save(kBase);
		}
		
		return "started file training";
		
		
		
	}
	
	
	public String untrainFile(String botId, String fileId) throws Exception {
		Optional<Bot> botOptional = botRepository.findById(botId);
		if(botOptional.isEmpty()) throw new Exception("No such botId found, hence we cannot train on this file Id");
		Optional<KnowledgeBase> kbOptional = kbRepository.findById(botId);
		if(kbOptional.isEmpty()) throw new Exception("No such bot Id exists");
		KnowledgeBase kBase = kbOptional.get();
		Map<String,FileDetails> fileDetails = kBase.getFilesDetails();
		 
		if(!fileDetails.containsKey(fileId)) throw new Exception("invalid file id");
		fileDetails.remove(fileId);
		kBase.setFilesDetails(fileDetails);
		kbRepository.save(kBase);
		return "file removed and untrained successfully";
	}

	
	

}
