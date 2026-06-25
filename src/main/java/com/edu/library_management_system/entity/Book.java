package com.edu.library_management_system.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Book 
{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

 private String title;
 
 private String author;
 private double price;
 
 private int year;
	
 @CreationTimestamp
 private LocalDateTime createdAt;
 
 @UpdateTimestamp
 private LocalDate updatedAt;
}
