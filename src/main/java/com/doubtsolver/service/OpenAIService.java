package com.doubtsolver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.doubtsolver.dto.openaidto.ChatCompletionDto;
import com.doubtsolver.dto.openaidto.Message;
import com.doubtsolver.dto.openaidto.ResponseFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.completion.url}")
    private String openAiUrl;
    
    @Value("${openai.embedding.model}")
    private String embeddingModel;

    public String preprocessText(String inputText) {
        Pattern nonAlphanumericPattern = Pattern.compile("[^a-zA-Z0-9\\s]");
        Pattern extraSpacesPattern = Pattern.compile("\\s{2,}");
        Pattern newlinePattern = Pattern.compile("\\r?\\n");
        Matcher matcher = nonAlphanumericPattern.matcher(inputText);
        String cleanText = matcher.replaceAll(" ");
        matcher = newlinePattern.matcher(cleanText);
        cleanText = matcher.replaceAll(" ");
        matcher = extraSpacesPattern.matcher(cleanText);
        cleanText = matcher.replaceAll(" ");
        return cleanText.trim();
    }

    public List<String> getChunks(String input){
        List<String> dividedString= new ArrayList<>();
        int maxLength =4000;
        int length= input.length();
        int startIndex= 0;
        while(startIndex<length){
            int endIndex= Math.min(startIndex+maxLength,length);
            String subString= input.substring(startIndex,endIndex);
            dividedString.add(subString);
            startIndex=endIndex;
        }
        return dividedString;
    }

    public List<Double> createEmbedding(String text) throws IOException {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("input", text);
        requestBody.put("model", embeddingModel);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                openAiUrl+"embeddings",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IOException("Server-side error: " + response.getStatusCodeValue());
        }


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode embeddingNode = root.path("data").get(0).path("embedding");

        List<Double> embeddings = new ArrayList<>();
        if (embeddingNode.isArray()) {
            for (JsonNode node : embeddingNode) {
                embeddings.add(node.asDouble());
            }
        }

        return embeddings;
    }


    public String jsonResponseString(String modelName, double temperature, List<Message> messages,String responseFormat) throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ChatCompletionDto chatCompletionDto = new ChatCompletionDto();
        chatCompletionDto.setModel(modelName);
        chatCompletionDto.setResponse_format(new ResponseFormat(responseFormat));
        chatCompletionDto.setTemperature(temperature);
        chatCompletionDto.setMessages(messages);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<ChatCompletionDto> request= new HttpEntity<>(chatCompletionDto,headers);
        ResponseEntity<String> response = restTemplate.exchange(openAiUrl+"chat/completions",HttpMethod.POST, request,String.class);
        String responseBody= response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode= objectMapper.readTree(responseBody);
        JsonNode choicesNode= rootNode.path("choices");
        String finalResponseString="";
        if(choicesNode.isArray() && choicesNode.has(0)) {
            JsonNode firstChoiceNode = choicesNode.get(0);
            JsonNode messageNode = firstChoiceNode.path("message");
            String content2 = messageNode.path("content").asText();
            finalResponseString= content2;
        }
        return finalResponseString;

    }


}
