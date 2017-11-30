package com.example.todoapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="Distance")
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Distance {

	@Id
    private String id;
	
	private String zip_code1;
	
	private String zip_code2;
	
	private String distance;
	
	public Distance(){
		super();
	}

	public String getZip_code1() {
		return zip_code1;
	}

	public void setZip_code1(String zip_code1) {
		this.zip_code1 = zip_code1;
	}

	public String getZip_code2() {
		return zip_code2;
	}

	public void setZip_code2(String zip_code2) {
		this.zip_code2 = zip_code2;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	@Override
    public String toString() {
        return String.format(
                "Todo[id=%s, zip_code1='%s', zip_code2='%s', distance='%s']",
                id, zip_code1, zip_code2,distance);
    }
}
