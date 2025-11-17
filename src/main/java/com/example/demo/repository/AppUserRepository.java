package com.example.demo.repository;

import com.example.demo.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// ----------------------------------------------
// AppUser（ユーザー情報）を扱うリポジトリ（DAO）
// Spring Data JPA が自動で実装を作ってくれる
// ----------------------------------------------
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // -------------------------------------------------------------
    // findByUsername:
    //   メソッド名からクエリを自動生成する「クエリメソッド」
    //
    //   SELECT * FROM users WHERE username = ?
    //   というクエリが自動で実行される。
    //
    //   Spring Security はログイン時に
    //   「ユーザー名に一致するユーザー情報を返して」と呼び出すため、必須。
    //
    //   Optional<AppUser>:
    //     ・ユーザーが存在する → Optional.of(AppUser)
    //     ・存在しない → Optional.empty()
    //     という安全な戻り値になる。
    // -------------------------------------------------------------
    Optional<AppUser> findByUsername(String username);
}
