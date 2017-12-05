package com.example.todoapp.controllers;

import javax.validation.Valid;

import com.example.todoapp.models.Distance;
import com.example.todoapp.models.EmergencyVehicles;
import com.example.todoapp.models.HeapNode;
import com.example.todoapp.models.Todo;
import com.example.todoapp.models.VehicleIDs;
import com.example.todoapp.models.ZipCode;
import com.example.todoapp.repositories.DistanceRepository;
import com.example.todoapp.repositories.EmergencyVehiclesRepository;
import com.example.todoapp.repositories.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utils.MinHeap;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TodoController {

	static int incrementor = 0;
    @Autowired
    TodoRepository todoRepository;
    
    @Autowired
    EmergencyVehiclesRepository vehiclesRepository;
    
    @Autowired
    DistanceRepository distanceRepository;
    
    int counter = 0;

    /**
     * Get all Requests available from table.
     * @return
     */
    @GetMapping("/todos")
    public List<Todo> getAllTodos() {
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
       List<Todo> todos = todoRepository.findAll(sortByCreatedAtDesc);
       for(Todo todo:todos){
    	   if(todo.getVehicleType().equalsIgnoreCase("1"))
    		   todo.setVehicleType("Ambulance");
    	   else if(todo.getVehicleType().equalsIgnoreCase("2"))
    		   todo.setVehicleType("Fire Truck");
    	   else if(todo.getVehicleType().equalsIgnoreCase("3"))
    		   todo.setVehicleType("Police Car");
       }
       return todos;
    }

    /**
     * Create Request based on zip code and vehicle Type
     * @param todo
     * @return
     */
    @PostMapping("/todos")
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        todo.setCompleted(false);
        
        //Set unique requestId based on old request
        incrementor = incrementor+1;
        todo.setRequestId(Integer.toString(incrementor));
        
        /**Get values from vehicle Repository which contains 
         * vehicle_type and vehicle ID's present in all zipcodes
         */
       String vehicle_type = todo.getVehicleType();
       
       //Get all the vehicles in all zipCodes where vehicle type is available.
      EmergencyVehicles vehicles=  vehiclesRepository.searchByName(vehicle_type);
     
      if(vehicles!=null){
    	  //Get all destination zip codes
    	  List<ZipCode> destinationZipCodes = vehicles.getZip_code();
    	  // Heap List which contains all nodes that should be inserted into min heap.
    	  List<HeapNode> heapList = null;
    	  // check if available vehicle is in same zip
    	  boolean vehicleInsameZip = false;
    	  // Min heap
    	  MinHeap minHeap = null;
    	  
    	  heapList = new ArrayList<HeapNode>();
    	  
    	  // initialise minimum distance to max value.
    	  Double minDistance = 100000.00;
    	  Double pathMinDistance = 100000.00;
    	  
    	  String mindestinationZipCode = "";
    	  
    	  String minsourceZipCode ="";
    	  
    	  for(ZipCode zip:destinationZipCodes){
    		  /**Check if vehicle is present in the same zip location then set the
    		   * distance as 0 and retrieve availabe vehicle ID and the source. Else
    		   * fill the create nodes with the zip and maximum distance.
    		   */
    		  if(Integer.parseInt(todo.getZipCode())==Integer.parseInt(zip.getZip())
    				  && zip.getVehicle_ids()!=null && !zip.getVehicle_ids().isEmpty()){
    			  todo.setDistance(Double.toString(0.0));
    			  vehicleInsameZip = true;
    			  todo.setVehicleId(zip.getVehicle_ids().get(0).getVehicle_id());
    			  todo.setSource(zip.getZip());
    			  todo.setPath(zip.getZip()+"--"+zip.getZip());
    			  heapList = null;
    			  break;
    		  }else{
    			  HeapNode node = new HeapNode();
	    		  node.setZipCode(zip.getZip());
	    		  node.setDistance(100000.00);
	    		  node.setPath("");
	    		  if(zip.getVehicle_ids()!=null && !zip.getVehicle_ids().isEmpty())
	    			  node.setVehicleAvailable(true);
	    		  else
	    			  node.setVehicleAvailable(false);
	    		  heapList.add(node);
    		  }
    	  }
    	  
    	  if(!vehicleInsameZip && !heapList.isEmpty()){
    		  
    		  //Add the rootNode that is the sourceZipCode with distance as 0
    		  HeapNode sourceNode = new HeapNode();
    		  sourceNode.setZipCode(todo.getZipCode());
    		  sourceNode.setPath(todo.getZipCode());
    		  sourceNode.setDistance(0.0);
    		  heapList.add(sourceNode);
    		  
    		  // Create min heap which heapifies.
    		  minHeap = new MinHeap(heapList);
    		  
    		  int heapSize = heapList.size();
    		  
    		  List<String> finisedNodes = new ArrayList<String>();
    		  String path = "";
    		  
    		  // Iterating the heap list, calculate the minimum distance and the minimum path.
    		  for(int i =1; i<heapSize;i++){
    			  
    			  if(!minHeap.isEmpty()){
    				  /**After heapify, extract the minimum node. Minumum node will be the node
    				   * with minimum distance.
    				   */
	    			  HeapNode minHeapNode = minHeap.extractMin();
	        		  
			    	  String sourceZipCode = minHeapNode.getZipCode();
			    	  
			    	  Double sourceMinDistance = minHeapNode.getDistance();
			    	  
			    	  String path1 = path+"--";
			    	  
			    	  // Get the distances from source to other destination zips from the table.
			    	  List<Distance> distanceList = distanceRepository.searchByName(sourceZipCode);
			    	  
			    	  if(distanceList!=null){
			    		  //Iterate all the destinations available
			    		  for(Distance distanceObj:distanceList){
			    			  if( !finisedNodes.contains(distanceObj.getZip_code1()) &&
			    					  !finisedNodes.contains(distanceObj.getZip_code2())){
				    			  String destinationZipCode = null;
				    			  
				    			  
				    			  
				    			  // Get the destination zip Code, as it is bi directional, source can be at zipcode1 or zipcode2
				    			  if(Integer.parseInt(distanceObj.getZip_code2())!=Integer.parseInt(sourceZipCode)){
				    				  destinationZipCode = distanceObj.getZip_code2();
				    				  path1 = distanceObj.getZip_code2();
				    			  }
				    			  else{
				    				  destinationZipCode = distanceObj.getZip_code1();
				    				  path1 = distanceObj.getZip_code1();
				    			  }
				    			  
				    			  // Increment the distance based on the previous edge distance.
					    		  double distance = sourceMinDistance + Double.parseDouble(distanceObj.getDistance());
					    		 
					    		  
					    		  HeapNode node = new HeapNode();
					    		  node.setZipCode( destinationZipCode);
					    		  
					    		  //Get the index of the destination heap node from the heap list
					    		  int index =  heapList.indexOf(node);
					    		  
					    		  if(index!=-1){
					    			  
					    			  //Get availability
					    			  boolean sourceVehicleAvailable = heapList.get(index).isVehicleAvailable();
					    			  
					    			  //Modify the heap node in the min heap and re-heapify
					    			  node.setDistance(distance);
					    			  node.setPath(path);
					    			  heapList.set(index, node);
					    			  minHeap = new MinHeap(heapList);
					    			  
					    			  if(!sourceVehicleAvailable && pathMinDistance>distance){
					    				  pathMinDistance = distance;
					    				  path = path1;
					    			  }
					    			  // If the edge distance is minimum then get the vehicle Ids and remove the min heap node from heap
					    			  if( sourceVehicleAvailable && minDistance>distance){
					    				  minDistance = distance;
					    				  minsourceZipCode = sourceZipCode;
					    				  mindestinationZipCode = destinationZipCode;
					    				  path = path1+"--"+path;
					    				 
					    			  }
					    		  }
					    		  counter = counter+1;
			    			  }  
			    		  }
			    		  // remove the min heap node from heap
			    		  HeapNode node = new HeapNode();
			    		  node.setZipCode( sourceZipCode);
			    		  heapList.remove(node);
			    		  minHeap = new MinHeap(heapList);
			    		  finisedNodes.add(sourceZipCode);
			    		 
			    	  }
	    		  }
    		  }
    		  ZipCode zipCode = new ZipCode();
			  zipCode.setZip(mindestinationZipCode);
			  int zipIndex = destinationZipCodes.indexOf(zipCode);
			  List<VehicleIDs> vehicleIds = destinationZipCodes.get(zipIndex).getVehicle_ids();
			  //Set the first vehicle available
			  todo.setVehicleId(vehicleIds.get(0).getVehicle_id());
			  
			  // Add the vehicle details at the source zip code.
			  ZipCode zipCode1 = new ZipCode();
			  zipCode1.setZip(minsourceZipCode);
			  VehicleIDs vehicle = new VehicleIDs();
			  vehicle.setVehicle_id(vehicleIds.get(0).getVehicle_id());
			  vehicle.setAt_source("N");
			  vehicle.setBelongs_to(vehicleIds.get(0).getAt_source());
			  List<VehicleIDs> sourceVehicleIds = new ArrayList<VehicleIDs>();
			  sourceVehicleIds.add(vehicle);
			  
			  //Delete the vehicle from the destination.
			  int sourcezipIndex = destinationZipCodes.indexOf(zipCode1);
			  destinationZipCodes.get(sourcezipIndex).setVehicle_ids(sourceVehicleIds);
			  
			  vehicleIds.remove(0);
			  destinationZipCodes.get(zipIndex).setVehicle_ids(vehicleIds);
			  
			  vehicles.setZip_code(destinationZipCodes);
			  
			  todo.setSource(mindestinationZipCode);
			  
			  vehiclesRepository.save(vehicles);
    		  // Set the min distance
    		  todo.setDistance(Double.toString(minDistance));
    	
			// Set path
    		  todo.setPath(path+"--"+todo.getZipCode());
    	  }
      }
       
       System.out.println("Counter:"+counter);
        return todoRepository.save(todo);
    }
}