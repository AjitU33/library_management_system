package com.edu.library_management_system.dto;

import lombok.Getter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class BookRequestDto {

	@NotBlank(message="titile cannot be blank")
	private String title;
	 
	 private String author;
	 @Min(value = 1,message = "Price must be greater than 0")
	 
	 private Double price;
	 private Integer year;

}
