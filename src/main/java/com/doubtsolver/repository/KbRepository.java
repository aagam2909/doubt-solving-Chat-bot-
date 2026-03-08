package com.doubtsolver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.doubtsolver.model.KnowledgeBase;

@Repository
public interface KbRepository extends MongoRepository<KnowledgeBase, String>{

}
