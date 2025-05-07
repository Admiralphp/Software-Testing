package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
    public List<Book> getAllBooks() {
        return Arrays.asList(
            new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald"),
            new Book(2L, "1984", "George Orwell")
        );
    }

    public Book getBookById(Long id) {
        return getAllBooks().stream()
            .filter(book -> book.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public Book addBook(Book book) {
        // In a real application, this would save to a database
        // For now, we'll just return the book with the ID set
        return book;
    }
}