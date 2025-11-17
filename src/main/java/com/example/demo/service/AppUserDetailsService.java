package com.example.demo.service;

import com.example.demo.domain.AppUser;
import com.example.demo.repository.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// ------------------------------------------------------------
// Spring Security がログイン処理のときに利用するサービス
// username（ログインID）からユーザー情報を取得して返す役割。
// 必ず "UserDetailsService" を実装する必要がある。
// ------------------------------------------------------------
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepo;

    // DI（依存性注入）：AppUserRepository を受け取る
    public AppUserDetailsService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ------------------------------------------------------------
    // username をもとにユーザーを検索し、Spring Security 用の
    // UserDetails オブジェクトに変換して返す。
    //
    // （ログイン時に Spring Security が自動で呼び出す）
    // ------------------------------------------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // ------------------------------------------------------------
        // DB から username に一致するユーザーを検索する
        // AppUserRepository の findByUsername を呼び出す
        //
        // 見つからない場合は UsernameNotFoundException を投げる
        // これによりログイン失敗（Bad Credentials）となる
        // ------------------------------------------------------------
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // ------------------------------------------------------------
        // 権限（ロール）のセット
        //
        // AppUser.role が "ROLE_USER" のような形式で保存されている前提。
        //
        // Spring Security では権限を GrantedAuthority として扱うので
        // SimpleGrantedAuthority に変換して List に詰める。
        // ------------------------------------------------------------
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        List<GrantedAuthority> authorities = List.of(authority);

        // ------------------------------------------------------------
        // Spring Security 標準の org.springframework.security.core.userdetails.User を生成して返す。
        //
        // コンストラクタの引数は以下の意味：
        //   username               → ログインID
        //   password               → BCrypt のハッシュ値
        //   enabled                → 有効フラグ（false ならログイン禁止）
        //   accountNonExpired      → アカウントが期限切れでない true=OK
        //   credentialsNonExpired  → パスワードが期限切れでない true=OK
        //   accountNonLocked       → アカウントロックされていない true=OK
        //   authorities            → 権限情報（ROLE_USER など）
        //
        // ※ AppUser.enabled のみ DB の値を使用し、それ以外は常に true としたシンプル仕様。
        // ------------------------------------------------------------
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),   // ここには BCrypt のハッシュ値が入る
                user.isEnabled(),     // enabled（ログイン可否）
                true,                 // accountNonExpired (アカウント期限切れチェックしない)
                true,                 // credentialsNonExpired (パスワード期限切れチェックしない)
                true,                 // accountNonLocked (ロック機能を使わない)
                authorities           // 権限リスト
        );
    }
}

