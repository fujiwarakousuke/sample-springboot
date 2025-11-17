package com.example.demo.web;

import com.example.demo.domain.Book;
import com.example.demo.service.BookService;
import com.example.demo.web.dto.BookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Controller層だけをテストするためのアノテーション
// ・Spring MVC を最小構成で起動
// ・Service や Repository などはロードしない
// ・Controllerの動作とHTTPレスポンスのテストに特化
@WebMvcTest(BookController.class)
class BookControllerTest {

    // MockMvc は Spring MVC を模した「疑似HTTPクライアント」
    // ・実際にサーバーを起動せずに HTTP リクエストを送ってテストできる
    @Autowired
    private MockMvc mockMvc;

    // @MockBean を付けると Spring が BookService のモックを自動生成し、
    // Controller に注入してくれる
    // ・Service は実際の処理を行わず、テスト用の動作だけを返す
    @MockBean
    private BookService service;

    // JSON変換用 (Java ⇄ JSON)
    // ・MockMvc でPOSTする際に JSON を作る必要がある
    @Autowired
    private ObjectMapper objectMapper;

    // --------------------------------------------------------------
    // GET /api/books のテスト（一覧取得）
    // --------------------------------------------------------------
    @Test
    @DisplayName("GET /api/books で一覧が取得できること")
    void testGetBooks() throws Exception {

        // ---- ① テスト用の Book を1件準備 ----
        Book b = new Book();
        b.setId(1L);
        b.setTitle("Effective Java");
        b.setAuthor("Joshua Bloch");
        b.setPrice(5500);

        // ---- ② Page<Book> をテスト用に作成 ----
        // PageImpl を使って 1 件だけ入ったページを生成する
        Page<Book> page = new PageImpl<>(List.of(b), PageRequest.of(0,1), 1);

        // ---- ③ Service の戻り値をモック化 ----
        // BookService.list(...) が呼ばれたら、上で作った page を返す
        Mockito.when(service.list(Mockito.any(), Mockito.any())).thenReturn(page);

        // ---- ④ MockMvcでGETリクエストを実行 ----
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk()) // ステータスコード200
                // JSONの中身を検証 → $.content[0].title が "Effective Java" か？
                .andExpect(jsonPath("$.content[0].title").value("Effective Java"));
    }

    // --------------------------------------------------------------
    // POST /api/books のテスト（新規登録）
    // --------------------------------------------------------------
    @Test
    @DisplayName("POST /api/books でBookが登録されること")
    void testCreateBook() throws Exception {

        // ---- ① Serviceが返すテスト用Bookを作成 ----
        Book newBook = new Book();
        newBook.setId(2L);
        newBook.setTitle("JUnit入門");
        newBook.setAuthor("山田太郎");
        newBook.setPrice(1800);

        // ---- ② create() が呼ばれたら newBook を返すように設定 ----
        Mockito.when(service.create(Mockito.any())).thenReturn(newBook);

        // ---- ③ リクエストボディに送る JSON を作成 ----
        // ObjectMapper により Javaオブジェクト → JSON文字列 に変換
        String json = objectMapper.writeValueAsString(newBook);

        // ---- ④ POSTリクエストを実行 ----
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON) // JSONを送る宣言
                        .content(json))                         // ボディ部分にJSONを渡す
                .andExpect(status().isOk())                    // ステータス200
                .andExpect(jsonPath("$.title").value("JUnit入門")) // JSON結果の検証
                .andExpect(jsonPath("$.price").value(1800));
    }

    // --------------------------------------------------------------
    // POST /api/books のテスト（バリデーションチェック）
    // --------------------------------------------------------------
    @Test
    @DisplayName("title が空文字の場合、400 Bad Request になること")
    void testCreateBook_TitleBlank_BadRequest() throws Exception {

        // ---- ① バリデーションに引っかかる入力データを作成する ----
        // BookRequest の title が空（空白のみ）なので、@NotBlank に違反する
        // → Controller メソッドの @Valid によってバリデーションエラーとなる
        BookRequest invalidReq = new BookRequest(
                " ",          // ← @NotBlank によって「空白のみ」は無効と判断される
                "山田太郎",
                1800
        );

        // ---- ② リクエストボディに使う JSON を作成（Java → JSON） ----
        // MockMvc で POST するために JSON 形式の文字列に変換する
        String json = objectMapper.writeValueAsString(invalidReq);

        // ---- ③ /api/books に対して POST リクエストを送る ----
        // title が空白のため、Controller 側の @Valid によって自動的に 400 (Bad Request) が返る
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)  // JSON を送ると宣言
                        .content(json))                           // JSONデータ本体
                .andExpect(status().isBadRequest());              // 期待値：400 Bad Request

        // ----------------------------------------------------------
        // ※補足
        // JSONのエラー内容を細かく確認したい場合は jsonPath を使ってチェック可能
        // 例:
        // .andExpect(jsonPath("$.errors[0].field").value("title"));
        // .andExpect(jsonPath("$.errors[0].message").exists());
        // ----------------------------------------------------------
    }

}
