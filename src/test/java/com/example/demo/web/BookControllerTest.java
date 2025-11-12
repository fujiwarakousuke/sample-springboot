package com.example.demo.web;

import com.example.demo.domain.Book;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/books で一覧が取得できること")
    void testGetBooks() throws Exception {
        Book b = new Book();
        b.setId(1L);
        b.setTitle("Effective Java");
        b.setAuthor("Joshua Bloch");
        b.setPrice(5500);

        Page<Book> page = new PageImpl<>(List.of(b), PageRequest.of(0,1), 1);
        Mockito.when(service.list(Mockito.any(), Mockito.any())).thenReturn(page);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Effective Java"));
    }

    @Test
    @DisplayName("POST /api/books でBookが登録されること")
    void testCreateBook() throws Exception {
        Book newBook = new Book();
        newBook.setId(2L);
        newBook.setTitle("JUnit入門");
        newBook.setAuthor("山田太郎");
        newBook.setPrice(1800);

        Mockito.when(service.create(Mockito.any())).thenReturn(newBook);

        String json = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("JUnit入門"))
                .andExpect(jsonPath("$.price").value(1800));
    }
}
