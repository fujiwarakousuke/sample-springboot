package com.example.demo.repository;

import com.example.demo.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

// JPA用のテストを行うためのアノテーション
// ・Springのコンテキストを起動し、Repository周り（EntityManager / DataSource など）だけを対象にする
// ・通常はテスト用の組み込みDB（H2など）を使ってテストする
@DataJpaTest
class BookRepositoryTest {

    // BookRepository をDI（依存性注入）で受け取る
    // ・@DataJpaTest によって自動的にテスト用のBeanが用意される
    @Autowired
    BookRepository repo;

    // テストメソッドであることを示すアノテーション
    // ・このメソッドが1つのテストケースとして実行される
    @Test
    void SavedAndSearched_Test() {

        // ---- ① テストデータを作成 ----
        Book b = new Book();           // 空のBookエンティティを生成
        b.setTitle("JPA入門");          // タイトルをセット
        b.setAuthor("Taro");           // 著者名をセット
        // ※ price や createdAt など、必須でない項目は省略

        // ---- ② テスト対象の処理：保存処理 ----
        repo.save(b);                  // Repository経由でDBにBookを1件保存する

        // ---- ③ テスト対象の処理：検索処理 ----
        // タイトルに "JPA" を含む本を、最大10件までページング検索する
        // ・Containing：部分一致検索
        // ・IgnoreCase：大文字・小文字を区別しない
        Page<Book> page = repo.findByTitleContainingIgnoreCase("JPA", Pageable.ofSize(10));

        // ---- ④ 検証（アサーション） ----
        // 検索結果の件数が1件であることを確認
        // ・さきほど保存した「JPA入門」だけがヒットするはず、という仕様をテストしている
        assertEquals(1, page.getTotalElements());
    }
}
