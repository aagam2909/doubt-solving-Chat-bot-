package com.doubtsolver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doubtsolver.repository.BotRepository;
import com.doubtsolver.service.KbService;

@RestController
@RequestMapping("/kb")
public class KbController {
	
	@Autowired
	private KbService kbService;
	
	@Autowired
	private BotRepository botRepository;
	
	@PostMapping("/upload-train")
	public ResponseEntity<String> trainFile(@RequestParam String botId, @RequestBody MultipartFile file) throws Exception{
		return ResponseEntity.ok(kbService.addNewFileStatus(botId, file.getName()));
		
	}
	
	@DeleteMapping("/untrain-remove")
	public ResponseEntity<String> untrainFile(@RequestParam String botId, @RequestParam String fileId) throws Exception{
		return ResponseEntity.ok(kbService.untrainFile(botId, fileId));
	}

}
