package com.example.todoapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.todoapp.models.Distance;

public interface DistanceRepository extends MongoRepository<Distance,String> {

	@Query("{'$or':[ {'zip_code1':?0}, {'zip_code2':?0} ]}")
	public List<Distance> searchByName(String zip_code1);
}
