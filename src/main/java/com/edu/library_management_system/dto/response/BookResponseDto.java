package com.edu.library_management_system.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDto {

	private int id;
	private String title;
	
	private String author; 
	private double price;
	private int year;

}
