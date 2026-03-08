package com.doubtsolver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.record.VCenterRecord;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.stereotype.Service;

import com.doubtsolver.dto.ChatRequestDto;
import com.doubtsolver.dto.ChatResponseDto;
import com.doubtsolver.dto.openaidto.Message;
import com.doubtsolver.model.Bot;
import com.doubtsolver.model.LlmConfig;
import com.doubtsolver.model.VectorDocuments;
import com.doubtsolver.repository.BotRepository;
import com.doubtsolver.repository.LlmConfigRepository;
import com.doubtsolver.repository.VectorDocumentsRepository;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


@Service
public class ChatService {
	
	@Autowired
	private BotRepository botRepository;
	
	@Autowired 
	private LlmConfigRepository llmConfigRepository;
	
//	@Autowired
//	private KbRepository kbRepository;
	
	@Autowired
	private VectorDocumentsRepository vectorDocumentsRepository;
	
	@Autowired
	private OpenAIService openAIService;
	
	@Autowired
	private MongoClient mongoClient;
	
	@Value("${spring.data.mongodb.database}")
	private String db;
	
	public ChatResponseDto chat(ChatRequestDto chatRequestDto) throws IOException {
		
		Optional<Bot> botOptional = botRepository.findById(chatRequestDto.getBotId());
		if(botOptional.isEmpty()) return new ChatResponseDto("Hey there, I think the botId you entered is wrong, please correct it.");
		
		Optional<LlmConfig> llmComfigOptional  = llmConfigRepository.findById(chatRequestDto.getBotId());
		if(llmComfigOptional.isEmpty()) return new ChatResponseDto("Hey there, I am not configured, please add config for me.");
		LlmConfig llmConfig = llmComfigOptional.get();
		List<VectorDocuments> chunksDocuments = performVectorSearch(chatRequestDto);
		String systemPrompt=llmConfig.getSystemPrompt();
		StringBuilder sb = new StringBuilder();
		sb.append("Always use this as a knowledgebase and do not expand beyond this");
		for(VectorDocuments vc : chunksDocuments) {
			sb.append("Source: " + vc.getFileContent());
		}
		systemPrompt+=sb.toString();
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system",systemPrompt));
		messages.add(new Message("user",chatRequestDto.getQuestion()));
		
		String responseString = openAIService.jsonResponseString(llmConfig.getModel(), llmConfig.getTemperature(), messages, "text");
		return new ChatResponseDto(responseString);
		
	}
	
	
	private List<VectorDocuments> performVectorSearch(ChatRequestDto chatRequest) throws IOException {
        List<Double> queryEmbedding = openAIService.createEmbedding(chatRequest.getQuestion());
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("VectorDocuments");
        int numCandidates = 100;
        int limit = 3;
        Document filter = new Document("$and", Arrays.asList(
                new Document("botId", new Document("$in", Collections.singletonList(chatRequest.getBotId())))
        ));
        Bson vectorSearchStage = new Document("$vectorSearch", new Document().append("index", "vector_index")
                .append("path", "embeddings")
                .append("filter", filter)
                .append("queryVector", queryEmbedding)
                .append("numCandidates", numCandidates)
                .append("limit", limit)
        );
        List<Bson> aggregationPipeline = Collections.singletonList(vectorSearchStage);
        AggregateIterable<Document> result = collection.aggregate(aggregationPipeline);
        List<VectorDocuments> documents = new ArrayList<>();
        for (var doc : result) {
            JSONObject jsonObject = new JSONObject(doc.toJson());
            VectorDocuments vectorDocuments = new VectorDocuments();
            vectorDocuments.setFileId(jsonObject.optString("fileId", null));
            vectorDocuments.setFileContent(jsonObject.optString("fileContent", null));
           
           
            JSONObject idObject = jsonObject.optJSONObject("_id");
            if (idObject != null) {
                vectorDocuments.setId(idObject.optString("$oid", null));
            }
            documents.add(vectorDocuments);
        }
        return documents;
    }
	

}
