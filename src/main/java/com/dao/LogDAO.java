package com.dao;

import java.util.HashSet;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.log.LightLog;

public interface LogDAO extends MongoRepository<LightLog, String> {
	// On peut ajouter des querys facilement, Mongo gere les requetes � partir du nom de la m�thode

	@Query("{ 'Content' : { $regex: ?0 } }")
	HashSet<LightLog> findLightLogByRegexpContent(String regexp);
}
