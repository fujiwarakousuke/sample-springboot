package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // パスワードエンコーダー（BCrypt）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // セキュリティ設定（CSRFはデフォルトで有効）
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // ログイン画面、エラー、静的ファイルは誰でもOK
                        .requestMatchers("/login", "/error", "/css/**", "/js/**").permitAll()
                        // それ以外はログイン必須
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                // 自作ログイン画面
                        .loginProcessingUrl("/login")       // 認証処理用URL（POST先）
                        .defaultSuccessUrl("/home", true)   // 成功時は /home に固定遷移
                        .failureUrl("/login?error")         // 失敗時
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}


