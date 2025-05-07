package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void testGetAllBooks() {
        // Given
        List<Book> books = Arrays.asList(
            new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald"),
            new Book(2L, "1984", "George Orwell")
        );
        when(bookService.getAllBooks()).thenReturn(books);

        // When
        List<Book> result = bookController.getAllBooks();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("The Great Gatsby");
        assertThat(result.get(1).getTitle()).isEqualTo("1984");
    }

    @Test
    public void testGetBookById_Found() {
        // Given
        Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald");
        when(bookService.getBookById(1L)).thenReturn(book);

        // When
        ResponseEntity<Book> response = bookController.getBookById(1L);

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("The Great Gatsby");
        assertThat(response.getBody().getAuthor()).isEqualTo("F. Scott Fitzgerald");
    }

    @Test
    public void testGetBookById_NotFound() {
        // Given
        when(bookService.getBookById(3L)).thenReturn(null);

        // When
        ResponseEntity<Book> response = bookController.getBookById(3L);

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    public void testCreateBook() {
        // Given
        Book book = new Book(3L, "The Hobbit", "J.R.R. Tolkien");
        when(bookService.addBook(book)).thenReturn(book);

        // When
        ResponseEntity<Book> response = bookController.createBook(book);

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(3L);
        assertThat(response.getBody().getTitle()).isEqualTo("The Hobbit");
        assertThat(response.getBody().getAuthor()).isEqualTo("J.R.R. Tolkien");
    }
}