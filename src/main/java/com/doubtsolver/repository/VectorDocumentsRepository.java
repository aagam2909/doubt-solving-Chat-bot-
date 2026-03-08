package com.doubtsolver.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.doubtsolver.model.VectorDocuments;

public interface VectorDocumentsRepository extends MongoRepository<VectorDocuments, String> {

	void deleteByFileId(String fileId);
	
	void deleteByBotId(String siteId);
	
	
}
