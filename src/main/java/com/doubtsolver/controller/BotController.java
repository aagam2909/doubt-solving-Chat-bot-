package com.doubtsolver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doubtsolver.dto.BotDto;
import com.doubtsolver.model.Bot;
import com.doubtsolver.service.BotService;

@RestController
@RequestMapping("/bot")
public class BotController {

	@Autowired
	private BotService botService;
	
	
	@PostMapping("/addbot")
	public ResponseEntity<Bot> addBot(@RequestBody BotDto botDto) {
		try {
			return ResponseEntity.ok(botService.addBot(botDto)); 
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PutMapping("/updatebot")
	public ResponseEntity<?> updateBot(@RequestParam String botId,@RequestBody BotDto botDto){
		try {
			return ResponseEntity.ok(botService.updateBot(botId, botDto));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	
	// ye API kaam nai karri h
	@GetMapping("/getBot/{botId}")
	public ResponseEntity<Bot> getBot(@PathVariable String botId) {
		Bot bot= botService.getBot(botId);
		if(bot==null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(bot);
	}
	
	
	@GetMapping("/getall")
	public ResponseEntity<List<Bot>> getAll(){
		return ResponseEntity.ok(botService.getAll());
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<String> deleteBot(@RequestParam String botId) {
		return ResponseEntity.ok(botService.deleteBot(botId));
	}
	
}
