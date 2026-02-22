package com.doubtsolver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubtsolver.model.Bot;
import com.doubtsolver.repository.BotRepository;

@Service
public class BotService {
	
	@Autowired
	private BotRepository botRepository;
	
	
	public String addBot(Bot bot) {
		botRepository.save(bot);
		return "bot saved successfully";
	}
	
	public Bot getBot(String botName) {
		Bot bot = botRepository.findById(botName).get();
		return bot;
	}
	
	public List<Bot> getAll(){
		return botRepository.findAll();
	}
	
	public String deleteBot(String name) {
		botRepository.deleteById(name);
		return "bot deleted successfully";
	}
	
	
}
