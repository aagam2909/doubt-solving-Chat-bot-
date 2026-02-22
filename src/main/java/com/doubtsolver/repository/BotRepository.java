package com.doubtsolver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.doubtsolver.model.Bot;

@Repository
public interface BotRepository extends MongoRepository<Bot, String>{

}
