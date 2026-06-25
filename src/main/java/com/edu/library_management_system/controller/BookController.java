package com.edu.library_management_system.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.edu.library_management_system.Service.BookService;
import com.edu.library_management_system.dto.BookRequestDto;
import com.edu.library_management_system.dto.UpdateBookPriceDto;
import com.edu.library_management_system.dto.response.ApiResponseDto;
import com.edu.library_management_system.dto.response.BookResponseDto;

//make changes as 

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")

public class BookController {

	private BookService bookservice;

	public BookController(BookService bookservice) {
		this.bookservice = bookservice;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<BookResponseDto>> getBook(@PathVariable int id)

	{
		BookResponseDto book = bookservice.getBookById(id);

		return ResponseEntity.ok(ApiResponseDto.success(book, "Booked Fetched Successfully"));
	}

	// Get allBook

	@GetMapping
	public ResponseEntity<ApiResponseDto<List<BookResponseDto>>> getAllBooks() {

		List<BookResponseDto> books = bookservice.getAllBooks();
		return ResponseEntity.ok(ApiResponseDto.success(books, "Books Fetched Successfully"));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponseDto<BookResponseDto>> getBookByTitleAndAuthor(
			@RequestParam @NotBlank(message = "title is requred") String title,
			@RequestParam @NotBlank(message = "author is requred") String author) {
			BookResponseDto book
			=bookservice.getBookByTitleAndAuthor(title, author);
			return ResponseEntity.ok(
						ApiResponseDto.success(book, "Book Fetch Successfully")
					);
					
	}
	
	@GetMapping("/price")
	public ResponseEntity<ApiResponseDto<List<BookResponseDto>>>
	getBookByPrice(
			@RequestParam @Positive(message = "price must be greater than 0") double price){
		List<BookResponseDto> books
		=bookservice.getBookByPrice(price);
		return ResponseEntity.ok(
				ApiResponseDto.success(books, "Books with given price fetched successfully")
				);
	}
	
	//pagination
	@GetMapping("/books")
	public ResponseEntity<ApiResponseDto<Page<BookResponseDto>>> getBooks(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String sortDir) {

	    Sort sort = sortDir.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<BookResponseDto> books = bookservice.getBooks(pageable);

	    return ResponseEntity.ok(
	            ApiResponseDto.success(books, "Books fetched successfully with pagination")
	    );
	}

	// AddBook
	@PostMapping
	public ResponseEntity<ApiResponseDto<BookResponseDto>> addBook(@Valid @RequestBody BookRequestDto dto) {

		BookResponseDto book = bookservice.addBook(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.created(book, "Book Added Successfully"));

	}

	// Update Book
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDto<BookResponseDto>> updateBook(@PathVariable int id,
			@Valid @RequestBody BookRequestDto dto) {
		BookResponseDto book = bookservice.updateBook(id, dto);

		return ResponseEntity.ok(ApiResponseDto.success(book, "Updated Book Successfully"));

	}

	// Update Book PRICE
	@PatchMapping("/{id}/price")
	public ResponseEntity<ApiResponseDto<BookResponseDto>> updateBookPrice(@PathVariable int id,
			@Valid @RequestBody UpdateBookPriceDto dto,
			@RequestHeader(value = "Prefer", required = false) String prefer) {

		BookResponseDto book = bookservice.updateBookPrice(id, dto);

		if ("return=representation".equals(prefer)) {
			return ResponseEntity.ok(ApiResponseDto.success(book, "Price Updated Successfully"));
		}
		return ResponseEntity.noContent().build();

	}

//delete book

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable int id) {
		bookservice.deleteBook(id);

		return ResponseEntity.noContent().build();
	}

}
