package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository repo;

    @InjectMocks
    private BookService service;

    private Book sample;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sample = new Book();
        sample.setId(1L);
        sample.setTitle("JUnit入門");
        sample.setAuthor("山田太郎");
        sample.setPrice(1800);
    }

    @Test
    @DisplayName("ID指定でBookを取得できること")
    void testGetBookById() {
        when(repo.findById(1L)).thenReturn(Optional.of(sample));

        Book result = service.get(1L);

        assertNotNull(result);
        assertEquals("JUnit入門", result.getTitle());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    @DisplayName("存在しないIDの場合、例外が投げられること")
    void testGetBook_NotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.get(999L));
        assertTrue(ex.getMessage().contains("Book not found"));
    }
}
