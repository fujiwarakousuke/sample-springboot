package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // ← このクラスがDBテーブルと対応する「エンティティ」であることを示す
public class Book {

    @Id // ← 主キーを表す（必須）
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ← 主キー値をデータベース側で自動採番（AUTO_INCREMENT など）
    private Long id;

    @Column(nullable = false, length = 120)
    // ← このカラムは「NOT NULL」、最大120文字という制約を付ける
    private String title;

    @Column(nullable = false, length = 80)
    // ← 同じく「NOT NULL」、最大80文字の制約
    private String author;

    // price は制約がないため null も OK
    private Integer price;

    @Column(updatable = false)
    // ← 一度登録されると UPDATE では変更されない（createdAt は固定）
    private LocalDateTime createdAt;

    // 更新時に毎回上書きされる
    private LocalDateTime updatedAt;

    @PrePersist
        // ← Entity が「新規保存される直前」に自動的に実行されるメソッド
    void onCreate() {
        createdAt = LocalDateTime.now(); // 作成日時を設定
        updatedAt = createdAt;           // 初回は作成日時と同じ
    }

    @PreUpdate
        // ← Entity が「更新される直前」に自動的に実行されるメソッド
    void onUpdate() {
        updatedAt = LocalDateTime.now(); // 更新日時を毎回更新
    }

    // ---- ここから getter/setter ----
    // JPA ではプロパティへのアクセスに getter/setter が必要
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
