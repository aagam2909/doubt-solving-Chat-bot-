package com.doubtsolver.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubtsolver.dto.BotDto;
import com.doubtsolver.model.Bot;
import com.doubtsolver.repository.BotRepository;

@Service
public class BotService {
	
	@Autowired
	private BotRepository botRepository;
	
	
	public Bot addBot(BotDto botDto) throws Exception {
		try {
			Bot bot = new Bot(botDto);
			botRepository.save(bot);
			return bot;
		} catch (Exception e) {
			throw new Exception("Error creating bot: " + e.getMessage());
		}
		
	}
	
	public Bot getBot(String botId) {
		Optional<Bot> botOptional = botRepository.findById(botId);
		if(botOptional.isPresent()) return botOptional.get();
		return null;
	}
	
	public List<Bot> getAll(){
		return botRepository.findAll();
	}
	
	public String deleteBot(String name) {
		botRepository.deleteById(name);
		return "bot deleted successfully";
	}
	
	
	public Bot updateBot(String botId, BotDto botDto) throws Exception {
		Optional<Bot> botOptional = botRepository.findById(botId);
		if(botOptional.isEmpty()) throw new Exception("Bot Id provided is incorrect");
		Bot bot = botOptional.get();
		bot.setDescription(botDto.getDescription());
		bot.setDisplayName(botDto.getDisplayName());
		bot.setName(botDto.getName());
		botRepository.save(bot);
		return bot;
		
	}
	
	
}
