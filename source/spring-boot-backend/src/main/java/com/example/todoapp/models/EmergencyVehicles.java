package com.example.todoapp.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="EmergencyVehicles")
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class EmergencyVehicles {

	@Id
    private String id;
	
	private String vehicle_type;
	
	private String vehicle_name;
	
	private List<ZipCode> zip_code;

	
	public EmergencyVehicles(){
		super();
	}
	
	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getVehicle_name() {
		return vehicle_name;
	}

	public void setVehicle_name(String vehicle_name) {
		this.vehicle_name = vehicle_name;
	}

	public List<ZipCode> getZip_code() {
		return zip_code;
	}

	public void setZip_code(List<ZipCode> zip_code) {
		this.zip_code = zip_code;
	}
	
	@Override
    public String toString() {
        return String.format(
                "Todo[id=%s, vehicle_type='%s', vehicle_name='%s']",
                id, vehicle_type, vehicle_name);
    }
	
	public boolean equals(Object o){
	    if(o == null)
	    	return false;
	    if(!(o instanceof EmergencyVehicles)) 
	    	return false;

	    EmergencyVehicles other = (EmergencyVehicles) o;
	    if(!this.vehicle_type.equalsIgnoreCase(other.vehicle_type))      return false;
	    if(!this.vehicle_name.equalsIgnoreCase(other.vehicle_name))      return false;

	    return true;
	  }
	
	public int hashCode(){
	    return (int) 31 *
	    		vehicle_type.hashCode()*vehicle_name.hashCode();
	  }
	
}
