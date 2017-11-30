package com.example.todoapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.todoapp.models.EmergencyVehicles;


public interface EmergencyVehiclesRepository extends MongoRepository<EmergencyVehicles,String>{

	@Query("{'vehicle_type' : ?0}")
	public EmergencyVehicles searchByName(String vehicle_type);

}
