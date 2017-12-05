package com.example.todoapp.models;

import java.util.Date;
import java.util.Random;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Request")
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Todo {
	
	@Id
    private String id;

    @Size(max=100)
    private String vehicleType="";
    
    @Size(max=100)
    private String zipCode="";
    
    @Size(max=100)
    private String vehicleId="";
    
    @Size(max=100)
    private String distance="";
    
    @Size(max=100)
    private String requestId="";
    
    private String source = "";
    
    private String path = "";

    private Boolean completed = false;

    private Date createdAt = new Date();

    public Todo() {
        super();
    }

    public Todo(String vehicleType,String zipCode,String vehicleId,String distance) {
        this.id = Integer.toString((int )(Math. random() * 50 + 1));
    	this.vehicleType = vehicleType;
        this.zipCode = zipCode;
        this.vehicleId = vehicleId;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    } 

    public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
    public String toString() {
        return String.format(
                "Todo[id=%s, zipCode='%s', completed='%s']",
                id, zipCode, completed);
    }
}