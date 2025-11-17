package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// このクラスが「設定クラス（コンフィグ）」であることを示す
// Spring Boot 起動時に自動で読み込まれ、Bean が登録される
public class SecurityConfig {

    // --------------------------------------------
    //  認証に使うユーザー情報の定義（メモリ上）
    // --------------------------------------------

    @Bean
    // UserDetailsService:
    //  「ユーザー名からユーザー情報（パスワードや権限）を取得する」ためのインターフェース
    //  ここでは InMemoryUserDetailsManager を使い、DBなしでメモリ上にユーザーを用意する
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        // UserDetails:
        //  Spring Security が扱う「ユーザー情報」を表すクラス
        UserDetails user = User.withUsername("user")          // ログインID = "user"
                .password(passwordEncoder.encode("password")) // 生パスワード "password" をハッシュ化
                .roles("USER")                                // ロール（権限）= "ROLE_USER"
                .build();

        // InMemoryUserDetailsManager:
        //  メモリ上に UserDetails を保持する実装。
        //  開発・学習用に簡単なユーザー認証を作るときに便利。
        return new InMemoryUserDetailsManager(user);
    }

    // --------------------------------------------
    //  パスワードエンコーダー設定
    // --------------------------------------------

    @Bean
    // PasswordEncoder:
    //  パスワードをハッシュ化するためのインターフェース。
    //  生のパスワードをそのまま保存せず、必ずハッシュ化して保存するのが基本。
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder:
        //  BCrypt というアルゴリズムでパスワードをハッシュ化する実装。
        //  Spring Security でよく使われる安全な方式。
        return new BCryptPasswordEncoder();
    }

    // --------------------------------------------
    //  HTTPリクエストに対するセキュリティ設定
    // --------------------------------------------

    @Bean
    // SecurityFilterChain:
    //  どのURLにどんなセキュリティ（認証・認可・CSRFなど）をかけるか定義するクラス。
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ★CSRFは disable しない（= デフォルトで有効）
                //   → POST/PUT/DELETE などの「状態を変えるリクエスト」には
                //      CSRFトークンが必須になる（すでに login.html, index.html で対応済み）
                .authorizeHttpRequests(auth -> auth
                        // requestMatchers:
                        //   指定したパスに対してアクセス制御ルールを設定する。
                        //
                        // "/login"  … ログイン画面
                        // "/error"  … エラーページ
                        // "/css/**", "/js/**" … 静的ファイル（CSS/JavaScript）
                        // これらはログインしていなくてもアクセスできるように permitAll()
                        .requestMatchers("/login", "/error", "/css/**", "/js/**").permitAll()
                        // anyRequest().authenticated():
                        //   上記以外の全てのURLに対してログイン必須（認証済み）とする。
                        .anyRequest().authenticated()
                )
                // -----------------------
                //  フォームログインの設定
                // -----------------------
                .formLogin(form -> form
                        // loginPage("/login"):
                        //   自作のログイン画面のURLを指定。
                        //   標準の /login ページではなく templates/login.html を使う。
                        .loginPage("/login")

                        // loginProcessingUrl("/login"):
                        //   認証処理を受け付けるURL（フォームの action と一致させる）。
                        //   <form method="post" th:action="@{/login}"> からの POST がここに来る。
                        .loginProcessingUrl("/login")

                        // defaultSuccessUrl("/home", true):
                        //   ログイン成功後に必ず /home にリダイレクトする。
                        //   第2引数 true によって、「必ず /home に行く（直前のURLに戻らない）」動きになる。
                        .defaultSuccessUrl("/home", true)

                        // failureUrl("/login?error"):
                        //   ログイン失敗時に /login?error にリダイレクト。
                        //   login.html 側で th:if="${param.error}" でメッセージ表示に使える。
                        .failureUrl("/login?error")

                        // ログイン関連のURLは誰でもアクセス可
                        .permitAll()
                )
                // -----------------------
                //  ログアウトの設定
                // -----------------------
                .logout(logout -> logout
                        // ログアウト処理を行うURL（フォームの action と一致させる）
                        .logoutUrl("/logout")

                        // ログアウト成功後の遷移先
                        // /login?logout にリダイレクトし、login.html でメッセージ表示に利用できる。
                        .logoutSuccessUrl("/login?logout")

                        // ログアウトURLもログイン不要でアクセス可
                        .permitAll()
                );

        // 最後に SecurityFilterChain オブジェクトを生成して返す
        return http.build();
    }
}

