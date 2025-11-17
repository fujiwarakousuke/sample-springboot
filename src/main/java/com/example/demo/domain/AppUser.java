package com.example.demo.domain;

import jakarta.persistence.*;

// -----------------------------------------------------
// AppUser エンティティ
//   ・このクラスは "users" テーブルと対応する
//   ・Spring Security のログイン情報として利用される
// -----------------------------------------------------
@Entity
@Table(name = "users") // DB のテーブル名（users テーブルにマッピング）
public class AppUser {

    // -----------------------------------------------------
    // 主キー（ID）
    // -----------------------------------------------------
    @Id // 主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //  IDENTITY: PostgreSQL の SERIAL / BIGSERIAL のように自動採番
    private Long id;

    // -----------------------------------------------------
    // ログインID（ユーザー名）
    //   ・ログインフォームの username に対応
    //   ・ユニーク制約（unique = true）で同名ユーザーの重複登録を防ぐ
    // -----------------------------------------------------
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // -----------------------------------------------------
    // パスワード（ハッシュ値のみ保存する）
    //   ・生パスワードは絶対に保存してはいけない
    //   ・BCrypt のハッシュ値を保存するため長めに確保（200文字）
    // -----------------------------------------------------
    @Column(nullable = false, length = 200)
    private String password;

    // -----------------------------------------------------
    // ロール（権限）
    //   ・例: "ROLE_USER", "ROLE_ADMIN"
    //   ・Spring Security の権限チェックで使用される
    //   ・複数ロールを管理したい場合は別テーブルで管理する方法もある
    // -----------------------------------------------------
    @Column(nullable = false, length = 50)
    private String role;

    // -----------------------------------------------------
    // 有効フラグ
    //   ・true のレコードだけログイン可能
    //   ・アカウント凍結機能などに利用できる
    // -----------------------------------------------------
    @Column(nullable = false)
    private boolean enabled = true;

    // -----------------------------------------------------
    // getter / setter（プロパティアクセス用）
    // -----------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

