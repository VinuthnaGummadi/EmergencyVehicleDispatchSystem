package com.example.todoapp.models;

import java.util.List;

public class ZipCode {

	private String zip;
	
	private List<VehicleIDs> vehicle_ids;

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<VehicleIDs> getVehicle_ids() {
		return vehicle_ids;
	}

	public void setVehicle_ids(List<VehicleIDs> vehicle_ids) {
		this.vehicle_ids = vehicle_ids;
	}
	
	public boolean equals(Object o){
	    if(o == null)
	    	return false;
	    if(!(o instanceof ZipCode)) 
	    	return false;

	    ZipCode other = (ZipCode) o;
	    if(!this.zip.equalsIgnoreCase(other.zip))      return false;

	    return true;
	  }
	
	public int hashCode(){
	    return (int) 31 *
	    		zip.hashCode();
	  }
	
}
