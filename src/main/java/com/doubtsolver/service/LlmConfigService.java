package com.doubtsolver.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubtsolver.model.Bot;
import com.doubtsolver.model.LlmConfig;
import com.doubtsolver.repository.BotRepository;
import com.doubtsolver.repository.LlmConfigRepository;

@Service
public class LlmConfigService {
	
	@Autowired
	private LlmConfigRepository llmConfigRepo;
	
	@Autowired 
	private BotRepository botRepository;
	
	public LlmConfig addOrUpdateConfig(LlmConfig llmConfig) throws Exception {
		Optional<Bot> botOptional = botRepository.findById(llmConfig.getBotId());
		if(botOptional.isEmpty()) throw new Exception("No bot exists with this id");
		Optional<LlmConfig> llmConfigOptional = llmConfigRepo.findById(llmConfig.getBotId());
		
		return llmConfigRepo.save(llmConfig);
		
		
	}
	
	
	
	public String deleteConfig(String botId) {
		llmConfigRepo.deleteById(botId);
		return "LLM config deleted";
	}
	
	public LlmConfig getConfig(String botId) {
		Optional<LlmConfig> llmConfigOptional = llmConfigRepo.findById(botId);
		return llmConfigOptional.orElseGet(null);
	}

}
