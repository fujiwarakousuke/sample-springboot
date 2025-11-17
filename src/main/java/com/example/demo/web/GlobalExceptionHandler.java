package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// ---------------------------------------------
// 全ての @RestController に共通の例外処理を適用するアノテーション
// これを付けたクラスは「REST API 全体のエラーハンドリング担当」になる
// ---------------------------------------------
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================================================
    // バリデーション（入力チェック）エラー用のハンドラ
    // =========================================================

    // @Valid のチェックに失敗したとき、Spring が投げる例外を捕まえる
    // 例えば title や author が空文字のときなどに発生する
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        // フィールド名 → エラーメッセージ を格納するための Map
        Map<String, String> errors = new HashMap<>();

        // ex.getBindingResult().getFieldErrors()
        // → どの項目がどんな理由でエラーになったかを全件取得できる
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            // fe.getField() → エラーになった項目名（例：title）
            // fe.getDefaultMessage() → @NotBlank や @Size のメッセージ
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        // HTTP 400 Bad Request を返して、エラー内容を JSON で返す
        // JSON例：
        // {
        //   "message": "validation error",
        //   "errors": { "title": "must not be blank" }
        // }
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message", "validation error",
                        "errors", errors
                ));
    }

    // =========================================================
    // IllegalArgumentException をキャッチするハンドラ
    // =========================================================

    // これは主に「対象のデータが見つからない」などの時に利用
    // BookService.get(id) などで throw new IllegalArgumentException("Book not found") とするとここに来る
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex) {

        // HTTP 404 Not Found を返す
        // body には "message": "Book not found: 1" などが入る
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }
}
