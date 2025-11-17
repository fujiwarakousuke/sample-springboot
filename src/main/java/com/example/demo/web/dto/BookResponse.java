package com.example.demo.web.dto;

import com.example.demo.domain.Book;
import java.time.LocalDateTime;

// 3. Book(Entity) に対応する「レスポンス専用 DTO」
// record を使うことで、
// - フィールドの自動生成（getter、equals、hashCode、toString）
// - 不変（immutable）なオブジェクト
// をシンプルに表現できる。
// API などの外部公開値を返すためのクラス。
public record BookResponse(
        Long id,            // Book の ID
        String title,       // タイトル
        String author,      // 著者
        Integer price,      // 価格
        LocalDateTime createdAt, // 作成日時
        LocalDateTime updatedAt  // 更新日時
) {

    // --- Entity(Book) から BookResponse を作成するためのユーティリティメソッド ---
    // サービス層やコントローラで簡単に DTO を生成するために使用する。
    // Book -> BookResponse の変換ロジックを 1 か所にまとめることで
    // コードの重複を防ぎ、メンテナンス性を高める。
    public static BookResponse of(Book b) {
        return new BookResponse(
                b.getId(),         // Book の id
                b.getTitle(),      // Book のタイトル
                b.getAuthor(),     // Book の著者
                b.getPrice(),      // Book の価格
                b.getCreatedAt(),  // 作成日時
                b.getUpdatedAt()   // 更新日時
        );
    }
}


