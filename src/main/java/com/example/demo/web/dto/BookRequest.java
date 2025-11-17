package com.example.demo.web.dto;

import jakarta.validation.constraints.*;

// ------- Book を作成・更新するときに使用する「リクエスト DTO」 -------
// クライアント（HTMLフォーム・JavaScript・APIクライアント等）から
// 送られてくるデータを受け取るためのクラス。
// バリデーション（入力チェック）用のアノテーションで、
// 不正な値が送られてこないようにする。
public record BookRequest(

        // ------- title のバリデーション -------
        // @NotBlank : null・空文字("")・空白だけ("   ") を禁止する（文字列の必須入力）
        // @Size(max=120) : 文字数を 120 文字以内に制限
        @NotBlank
        @Size(max = 120)
        String title,

        // ------- author のバリデーション -------
        // @NotBlank : 著者名の必須チェック（空文字・空白のみは禁止）
        // @Size(max=80) : 文字数を 80 文字以内に制限
        @NotBlank
        @Size(max = 80)
        String author,

        // ------- price のバリデーション -------
        // @PositiveOrZero : 0以上の数値のみ許可（0、1、100 など）
        //   ※ null は許可される（必須にしたければ @NotNull を追加）
        @PositiveOrZero
        Integer price

) {}
