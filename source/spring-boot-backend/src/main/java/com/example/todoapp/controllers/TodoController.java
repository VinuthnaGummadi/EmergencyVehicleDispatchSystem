package com.example.todoapp.controllers;

import javax.validation.Valid;

import com.example.todoapp.models.Distance;
import com.example.todoapp.models.EmergencyVehicles;
import com.example.todoapp.models.HeapNode;
import com.example.todoapp.models.Todo;
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

    @GetMapping("/todos")
    public List<Todo> getAllTodos() {
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
        return todoRepository.findAll(sortByCreatedAtDesc);
    }

    @PostMapping("/todos")
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        todo.setCompleted(false);
        
        //Set unique requestId based on old request
        incrementor = incrementor+1;
        todo.setRequestId(Integer.toString(incrementor));
        
        //Get values from vehicle Repository
       String vehicle_type = todo.getVehicleType();
       
      EmergencyVehicles vehicles=  vehiclesRepository.searchByName(vehicle_type);
     
      if(vehicles!=null){
    	  
    	  List<ZipCode> destinationZipCodes = vehicles.getZip_code();
    	  List<HeapNode> heapList = null;
    	  boolean vehicleInsameZip = false;
    	  MinHeap minHeap = null;
    	  heapList = new ArrayList<HeapNode>();
    	  Double minDistance = 100000.00;
    	  
    	  for(ZipCode zip:destinationZipCodes){
    		  if(Integer.parseInt(todo.getZipCode())==Integer.parseInt(zip.getZip())
    				  && Integer.parseInt(zip.getAvailableCount())!=0){
    			  todo.setDistance(Double.toString(0.0));
    			  vehicleInsameZip = true;
    			  heapList = null;
    			  break;
    		  }else{
    			  HeapNode node = new HeapNode();
	    		  node.setZipCode(zip.getZip());
	    		  node.setDistance(100000.00);
	    		  node.setPath("");
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
    		  
    		  minHeap = new MinHeap(heapList);
    		  
    		  int heapSize = heapList.size();
    		  
    		  for(int i =1; i<heapSize;i++){
    			  
    			  if(!minHeap.isEmpty()){
	    			  HeapNode minHeapNode = minHeap.extractMin();
	        		  
			    	  String sourceZipCode = minHeapNode.getZipCode();
			    	  
			    	  Double sourceMinDistance = minHeapNode.getDistance();
			    	  
			    	  
			    	  List<Distance> distanceList = distanceRepository.searchByName(sourceZipCode);
			    	  
			    	  if(distanceList!=null){
			    		  for(Distance distanceObj:distanceList){
				    		  String destinationZipCode = distanceObj.getZip_code2();
				    		  double distance = sourceMinDistance + Double.parseDouble(distanceObj.getDistance());
				    		  String path = distanceObj.getZip_code1();
				    		  
				    		  HeapNode node = new HeapNode();
				    		  node.setZipCode( destinationZipCode);
				    		  
				    		  int index =  heapList.indexOf(node);
				    		  
				    		  if(index!=-1){
				    			  node.setDistance(distance);
				    			  node.setPath(path);
				    			  heapList.set(index, node);
				    			  minHeap = new MinHeap(heapList);
				    			  if(minDistance>distance)
				    				  minDistance = distance;
				    		  }
				    		  
			    		  }
			    		  HeapNode node = new HeapNode();
			    		  node.setZipCode( sourceZipCode);
			    		  heapList.remove(node);
			    		  minHeap = new MinHeap(heapList);
			    	  }
	    		  }
    		  }
    		  todo.setDistance(Double.toString(minDistance));
    	  }
      }
       
       
        return todoRepository.save(todo);
    }

    @GetMapping(value="/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id) {
        Todo todo = todoRepository.findOne(id);
        if(todo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(todo, HttpStatus.OK);
        }
    }

    @PutMapping(value="/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id,
                                           @Valid @RequestBody Todo todo) {
        Todo todoData = todoRepository.findOne(id);
        if(todoData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        todoData.setZipCode(todo.getZipCode());
        todoData.setCompleted(todo.getCompleted());
        Todo updatedTodo = todoRepository.save(todoData);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @DeleteMapping(value="/todos/{id}")
    public void deleteTodo(@PathVariable("id") String id) {
        todoRepository.delete(id);
    }
}