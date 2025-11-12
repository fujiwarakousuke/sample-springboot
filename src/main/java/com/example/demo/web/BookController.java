package com.example.demo.web;

import com.example.demo.domain.Book;
import com.example.demo.service.BookService;
import com.example.demo.web.dto.BookRequest;
import com.example.demo.web.dto.BookResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;
    public BookController(BookService service) { this.service = service; }

    // 一覧（検索 + ページング）
    @GetMapping
    public Page<BookResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return service.list(q, pageable).map(BookResponse::of);
    }

    // 取得
    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return BookResponse.of(service.get(id));
    }

    // 作成
    @PostMapping
    public BookResponse create(@RequestBody @Valid BookRequest req) {
        Book b = new Book();
        b.setTitle(req.title());
        b.setAuthor(req.author());
        b.setPrice(req.price());
        return BookResponse.of(service.create(b));
    }

    // 更新（部分更新）
    @PatchMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @RequestBody BookRequest req) {
        Book patch = new Book();
        patch.setTitle(req.title());
        patch.setAuthor(req.author());
        patch.setPrice(req.price());
        return BookResponse.of(service.update(id, patch));
    }

    // 削除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
