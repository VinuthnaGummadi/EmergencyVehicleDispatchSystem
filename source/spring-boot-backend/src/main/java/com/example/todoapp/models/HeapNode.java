package com.example.todoapp.models;

public class HeapNode {

	private String zipCode;
	
	private Double distance;
	
	private String path;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean equals(Object o){
	    if(o == null)
	    	return false;
	    if(!(o instanceof HeapNode)) 
	    	return false;

	    HeapNode other = (HeapNode) o;
	    if(!this.zipCode.equalsIgnoreCase(other.zipCode))      return false;

	    return true;
	  }
	
	public int hashCode(){
	    return (int) 31 *
	    		zipCode.hashCode();
	  }
}
