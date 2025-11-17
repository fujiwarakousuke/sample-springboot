package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service  // ← このクラスが「ビジネスロジック（サービス層）」であることを示すアノテーション
public class BookService {

    // -------------------------
    // フィールド
    // -------------------------
    private final BookRepository repo;

    // コンストラクタ注入（DI）
    // Spring が BookRepository の実装クラスを自動で渡してくれる
    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    // -----------------------------------------------------
    // 一覧取得（検索 + ページング）
    // -----------------------------------------------------
    public Page<Book> list(String q, Pageable pageable) {
        // q が null または 空文字 の場合は全件検索
        // 検索ワードがある場合は title に部分一致する書籍を検索
        return (q == null || q.isBlank())
                ? repo.findAll(pageable)  // 全件取得（ページング付き）
                : repo.findByTitleContainingIgnoreCase(q, pageable); // 部分一致検索（ページング付き）
    }

    // -----------------------------------------------------
    // 1件取得（存在しない場合は例外）
    // -----------------------------------------------------
    public Book get(Long id) {
        return repo.findById(id)
                // 取得できなかった場合は例外（404 Not Found として扱われやすい）
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }

    // -----------------------------------------------------
    // 作成（保存）
    // -----------------------------------------------------
    public Book create(Book b) {
        return repo.save(b);  // save は INSERT を発行する
    }

    // -----------------------------------------------------
    // 更新（部分更新）
    // -----------------------------------------------------
    public Book update(Long id, Book patch) {
        // まず現在のデータを取得（存在しなければ例外）
        Book cur = get(id);

        // patch に入っている値だけ上書きする（null の場合は無視する）
        if (patch.getTitle() != null)
            cur.setTitle(patch.getTitle());

        if (patch.getAuthor() != null)
            cur.setAuthor(patch.getAuthor());

        if (patch.getPrice() != null)
            cur.setPrice(patch.getPrice());

        // 上書き後、DB に保存（UPDATE）
        return repo.save(cur);
    }

    // -----------------------------------------------------
    // 削除
    // -----------------------------------------------------
    public void delete(Long id) {
        // get(id) が NOT FOUND の場合は例外を投げて終了
        repo.delete(get(id)); // 取得した Book を削除
    }
}
