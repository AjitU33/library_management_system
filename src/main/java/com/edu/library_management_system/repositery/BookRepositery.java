package com.edu.library_management_system.repositery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.library_management_system.entity.Book;

@Repository
public interface BookRepositery 
extends JpaRepository<Book,Integer>
{
	
	public Book findByTitleAndAuthor(String title, String author);
	//select b from Book b
	//where b.title=:title and b.author=:author
	
	@Query("select b from Book b where b.price=:price")
	public List<Book> getBookByPrice(double price);
	
}
