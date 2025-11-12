package com.example.demo.web.dto;

import com.example.demo.domain.Book;
import java.time.LocalDateTime;

public record BookResponse(
        Long id, String title, String author, Integer price,
        LocalDateTime createdAt, LocalDateTime updatedAt
) {
    public static BookResponse of(Book b) {
        return new BookResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getPrice(), b.getCreatedAt(), b.getUpdatedAt());
    }
}

