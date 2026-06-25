package com.edu.library_management_system.Service.imple;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.edu.library_management_system.Mapper.BookMapper;
import com.edu.library_management_system.Service.BookService;
import com.edu.library_management_system.dto.BookRequestDto;
import com.edu.library_management_system.dto.UpdateBookPriceDto;
import com.edu.library_management_system.dto.response.BookResponseDto;
import com.edu.library_management_system.entity.Book;
import com.edu.library_management_system.repositery.BookRepositery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepositery bookRepositery;

	private final BookMapper mapper;

	@Override
	public BookResponseDto getBookById(int id) {

		Book book = bookRepositery.findById(id).orElseThrow();

		return mapper.toDto(book);
	}

	@Override
	public List<BookResponseDto> getAllBooks() {
		List<Book> books = bookRepositery.findAll();

		return books.stream().map(mapper::toDto).toList();
	}

	@Override
	public BookResponseDto getBookByTitleAndAuthor(String title, String author) {
		Book book=bookRepositery.findByTitleAndAuthor(title, author);
		return mapper.toDto(book);
	}

	@Override
	public List<BookResponseDto> getBookByPrice(double price) {
		List<Book> books=bookRepositery.getBookByPrice(price);
		return books.stream()
				.map(mapper::toDto)
				.toList();
	}
	
	@Override
	public Page <BookResponseDto> getBooks(Pageable p) {
		Page<Book> books =bookRepositery.findAll(p);
		return books.map(mapper::toDto);
	}
	
	@Override
	public BookResponseDto  addBook(BookRequestDto dto) {

		Book book = mapper.toEntity(dto);
		Book newBook = bookRepositery.save(book);

		return mapper.toDto(newBook);
	}

	@Override
	public BookResponseDto  updateBook(int id, BookRequestDto dto) {

		Book book = bookRepositery.findById(id).orElseThrow();

		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setPrice(dto.getPrice());
		book.setYear(dto.getYear());

		Book updated = bookRepositery.save(book);

		return mapper.toDto(updated);
	}

	@Override
	public BookResponseDto updateBookPrice(int id, UpdateBookPriceDto dto) {

		Book book = bookRepositery.findById(id).orElseThrow();

		book.setPrice(dto.getPrice());
		Book b=bookRepositery.save(book);
		
		return mapper.toDto(b);

	}

	@Override
	public void deleteBook(int id) {
		Book book = bookRepositery.findById(id).orElseThrow();

		bookRepositery.delete(book);

		/*
		 * || Another way to delete book by id directly ||
		 * 
		 *** bookRepositery.deleteById(id)
		 * 
		 */

	}

}
