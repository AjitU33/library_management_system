package com.edu.library_management_system.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.library_management_system.dto.BookRequestDto;
import com.edu.library_management_system.dto.response.ApiResponseDto;
import com.edu.library_management_system.dto.response.BookResponseDto;
import com.edu.library_management_system.entity.Book;

@Component
public class BookMapper {

	@Autowired
	private ModelMapper mapper;
	
	
	public BookResponseDto toDto(Book book) {
		
		return mapper.map(book,BookResponseDto.class);
	}
	
	public Book toEntity(BookRequestDto dto) {
		return mapper.map(dto, Book.class);
	}
	
}
