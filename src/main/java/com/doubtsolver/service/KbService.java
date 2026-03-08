package com.doubtsolver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doubtsolver.enums.TrainingStatus;
import com.doubtsolver.model.Bot;
import com.doubtsolver.model.KnowledgeBase;
import com.doubtsolver.model.VectorDocuments;
import com.doubtsolver.model.helper.FileDetails;
import com.doubtsolver.repository.BotRepository;
import com.doubtsolver.repository.KbRepository;
import com.doubtsolver.repository.VectorDocumentsRepository;
import com.doubtsolver.utils.DocumentTextExtractor;

@Service
public class KbService {
	
	@Autowired
	private KbRepository kbRepository;
	
	@Autowired
	private BotRepository botRepository;
	
	@Autowired
	private OpenAIService openAIService;
	
	@Autowired
	private VectorDocumentsRepository vectorDocumentsRepository;
	
	public KnowledgeBase addNewFileStatus(String botId, String fileName,String fileId) throws Exception {
		Optional<Bot> botOptional = botRepository.findById(botId);
		if(botOptional.isEmpty()) throw new Exception("No such botId found, hence we cannot train on this file Id");
		Optional<KnowledgeBase> kbOptional = kbRepository.findById(botId);
		
		FileDetails fileDetail = new FileDetails();
		
		fileDetail.setFileName(fileName);
		fileDetail.setTrainingStatus(TrainingStatus.INPROCESS);
		
		if(kbOptional.isEmpty()) {
			KnowledgeBase kBase  = new KnowledgeBase();
			kBase.setBotId(botId);
			Map<String, FileDetails> fileDetails = new HashMap<String, FileDetails>();
			
			fileDetails.put(fileId, fileDetail);
			kBase.setFilesDetails(fileDetails);
			kBase=kbRepository.save(kBase);
			return kBase;
		}else {
			KnowledgeBase kBase = kbOptional.get();
			kBase.getFilesDetails().put(fileId,fileDetail);
			kBase=kbRepository.save(kBase);
			return kBase;
		}
		
		
		
		
		
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
		vectorDocumentsRepository.deleteByFileId(fileId);
		return "file removed and untrained successfully";
	}
	
	public void trainFile(String botId, String fileId, MultipartFile file,KnowledgeBase kBase) {
		try {
			Optional<KnowledgeBase> kbOptional = kbRepository.findById(botId);
			String textFromFile=DocumentTextExtractor.extractText(file);
			textFromFile = openAIService.preprocessText(textFromFile);
			List<String> chunks= openAIService.getChunks(textFromFile);
			List<VectorDocuments> vectorDocuments = new ArrayList<VectorDocuments>();
			for(String chunk : chunks ) {
				List<Double> embeddings = openAIService.createEmbedding(chunk);
				VectorDocuments vectorDocument = new VectorDocuments();
				vectorDocument.setEmbeddings(embeddings);
				vectorDocument.setFileContent(chunk);
				vectorDocument.setBotId(botId);
				vectorDocument.setFileId(fileId);
				vectorDocuments.add(vectorDocument);
			}
			vectorDocumentsRepository.saveAll(vectorDocuments);
			FileDetails fileDetails = kBase.getFilesDetails().get(fileId);
			fileDetails.setTrainingStatus(TrainingStatus.COMPLETED);
			kBase.getFilesDetails().put(fileId, fileDetails);
			kbRepository.save(kBase);
			
			
		} catch (Exception e) {
			FileDetails fileDetails = kBase.getFilesDetails().get(fileId);
			fileDetails.setTrainingStatus(TrainingStatus.FAILED);
			fileDetails.setErrorMessage(e.getMessage());
			kBase.getFilesDetails().put(fileId, fileDetails);
			kbRepository.save(kBase);
		}
	}
	
	

	
	

}
