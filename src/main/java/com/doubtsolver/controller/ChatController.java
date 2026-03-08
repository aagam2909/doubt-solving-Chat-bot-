package com.doubtsolver.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doubtsolver.dto.ChatRequestDto;
import com.doubtsolver.dto.ChatResponseDto;
import com.doubtsolver.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {
	@Autowired
	private ChatService chatService;
	
	@PostMapping
	public ChatResponseDto chat(@RequestBody ChatRequestDto chatRequestDto) throws IOException {
		return chatService.chat(chatRequestDto);
	}
}
