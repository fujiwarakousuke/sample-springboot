package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // ------------------------------
    //  ログイン画面の表示
    // ------------------------------

    // HTTP GET /login に対して処理を行う
    @GetMapping("/login")
    public String login() {

        // 返却値 "login" は、Thymeleaf のテンプレート名を意味する
        // → src/main/resources/templates/login.html を表示する
        return "login";
    }


    // ------------------------------
    //  ログイン後のホーム画面（Book Manager 画面）
    // ------------------------------

    // HTTP GET /home でホーム画面を表示
    @GetMapping("/home")
    public String home() {

        // 返却値 "index" もテンプレート名
        // → src/main/resources/templates/index.html を表示する
        return "index";
    }


    // ------------------------------
    //  トップページ（/）にアクセスされた場合の処理
    // ------------------------------

    // HTTP GET / にアクセスされた時の処理
    @GetMapping("/")
    public String root() {

        // "redirect:/home" を返すと、ブラウザに対して
        // 「/home に 302 リダイレクトしなさい」という指示を返す。
        //
        // そのため、ユーザーが http://localhost:8080/ にアクセスすると
        // 自動的に /home へ移動する。
        return "redirect:/home";
    }
}

