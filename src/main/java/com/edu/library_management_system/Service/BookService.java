package com.edu.library_management_system.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.edu.library_management_system.dto.BookRequestDto;
import com.edu.library_management_system.dto.UpdateBookPriceDto;
import com.edu.library_management_system.dto.response.BookResponseDto;
import com.edu.library_management_system.entity.Book;

public interface BookService  
{

	public BookResponseDto getBookById(int id) ;
	 
	public List<BookResponseDto> getAllBooks();
	
	public BookResponseDto getBookByTitleAndAuthor (String title, String author);
	
	public List<BookResponseDto> getBookByPrice(double price);
	
	//pagination
	public Page<BookResponseDto> getBooks(Pageable p);
	
	public BookResponseDto addBook(BookRequestDto dto);
	
	public BookResponseDto updateBook(int id,BookRequestDto dto);
	
	public  BookResponseDto updateBookPrice(int id,UpdateBookPriceDto dto);
	
	public void deleteBook(int id);
	
	
	
}
