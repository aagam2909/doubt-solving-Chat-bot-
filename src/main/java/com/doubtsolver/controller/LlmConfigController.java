package com.doubtsolver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doubtsolver.model.LlmConfig;
import com.doubtsolver.service.LlmConfigService;

@RestController
@RequestMapping("/llm-config")
public class LlmConfigController {
	
	@Autowired
	private LlmConfigService llmConfigService;
	
	@PostMapping("/addOrUpdate")
	public ResponseEntity<LlmConfig> addOrUpdate(@RequestBody LlmConfig llmConfig) throws Exception{
		return ResponseEntity.ok(llmConfigService.addOrUpdateConfig(llmConfig));
	}
	
	
	@GetMapping("/getconfig")
	public ResponseEntity<LlmConfig> getConfig(@RequestParam String botId){
		return ResponseEntity.ok(llmConfigService.getConfig(botId));
	}
	
	


}
