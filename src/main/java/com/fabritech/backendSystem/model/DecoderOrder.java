package com.fabritech.backendSystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class DecoderOrder {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String decoderType;
	    private String installationType;
	    private String name;
	    private String phoneNumber;
	    private String email;
	    private String location;
	    
	    @Column(nullable = false)
	    private String status = "Pending"; // Default value
	    
	    @Column(nullable = false)
	    private LocalDateTime createdAt = LocalDateTime.now();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDecoderType() {
		return decoderType;
	}
	public void setDecoderType(String decoderType) {
		this.decoderType = decoderType;
	}
	public String getInstallationType() {
		return installationType;
	}
	public void setInstallationType(String installationType) {
		this.installationType = installationType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
    
    
}

