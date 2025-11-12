package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository repo;
    public BookService(BookRepository repo) { this.repo = repo; }

    public Page<Book> list(String q, Pageable pageable) {
        return (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByTitleContainingIgnoreCase(q, pageable);
    }

    public Book get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }

    public Book create(Book b) { return repo.save(b); }

    public Book update(Long id, Book patch) {
        Book cur = get(id);
        if (patch.getTitle() != null)  cur.setTitle(patch.getTitle());
        if (patch.getAuthor() != null) cur.setAuthor(patch.getAuthor());
        if (patch.getPrice() != null)  cur.setPrice(patch.getPrice());
        return repo.save(cur);
    }

    public void delete(Long id) { repo.delete(get(id)); }
}
