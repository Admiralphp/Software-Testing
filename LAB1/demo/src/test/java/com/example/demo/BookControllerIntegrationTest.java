package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        bookRepository.save(new Book(null, "The Great Gatsby", "F. Scott Fitzgerald"));
        bookRepository.save(new Book(null, "1984", "George Orwell"));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", is("The Great Gatsby")))
            .andExpect(jsonPath("$[1].title", is("1984")));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = bookRepository.findAll().get(0);

        mockMvc.perform(get("/api/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void testCreateBook() throws Exception {
        Book newBook = new Book(null, "To Kill a Mockingbird", "Harper Lee");

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("To Kill a Mockingbird")))
            .andExpect(jsonPath("$.author", is("Harper Lee")));
    }

    @Test
    public void testUpdateBook() throws Exception {
        // First, save a book to update
        Book existingBook = bookRepository.findAll().get(0);
        Book updatedBook = new Book(null, "Updated Title", "Updated Author");

        mockMvc.perform(put("/api/books/" + existingBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("Updated Title")))
            .andExpect(jsonPath("$.author", is("Updated Author")));

        // Verify the book was updated in the database
        Book bookInDb = bookRepository.findById(existingBook.getId()).orElse(null);
        assertEquals("Updated Title", bookInDb.getTitle());
        assertEquals("Updated Author", bookInDb.getAuthor());
    }

    @Test
    public void testUpdateBook_NotFound() throws Exception {
        Book updatedBook = new Book(null, "Updated Title", "Updated Author");

        mockMvc.perform(put("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(status().isNotFound());
    }
}