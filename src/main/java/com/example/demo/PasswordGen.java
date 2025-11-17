package com.example.demo;

// Spring Security が提供する BCrypt アルゴリズムのパスワードハッシュ化クラスを読み込む
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGen {
    public static void main(String[] args) {

        // BCryptPasswordEncoder のインスタンスを作成
        // BCrypt はパスワードハッシュ化で最も一般的で安全な方式
        // Salt（ランダム値）を内部で自動生成し、毎回異なるハッシュ値を作る仕組み
        var encoder = new BCryptPasswordEncoder();

        // encode("password"):
        //   生のパスワード "password" を BCrypt でハッシュ化する
        //   ハッシュ値は復号できない一方向変換（非常に安全）
        //   同じ "password" を encode しても毎回違うハッシュ値になる
        //
        // System.out.println(...):
        //   ハッシュ化したパスワードをコンソールに出力する。
        //   出力結果をそのまま PostgreSQL の users テーブルに INSERT する。
        System.out.println(encoder.encode("password")); // "password" は任意のパスワードに変更
    }
}


