package com.doubtsolver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doubtsolver.model.Bot;
import com.doubtsolver.service.BotService;

import jakarta.websocket.server.PathParam;

@RestController
public class BotController {

	@Autowired
	private BotService botService;
	
	
	@PostMapping
	public String addBot(@RequestBody Bot bot) {
		return botService.addBot(bot);
	}
	
	
	// ye API kaam nai karri h
	@GetMapping("/{name}")
	public Bot getBot(@PathVariable String name) {
		return botService.getBot(name);
	}
	
	
	@GetMapping
	public List<Bot> getAll(){
		return botService.getAll();
	}
	
	@DeleteMapping
	public String deleteBot(@RequestParam String id) {
		return botService.deleteBot(id);
	}
	
}
