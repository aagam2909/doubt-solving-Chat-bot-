package com.doubtsolver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.doubtsolver.model.LlmConfig;

@Repository
public interface LlmConfigRepository extends MongoRepository<LlmConfig, String>{

}
