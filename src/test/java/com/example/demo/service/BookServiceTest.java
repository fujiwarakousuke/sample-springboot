package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Service層の単体テストを行うクラス
// ・Repositoryはモック化して、DBには接続しない純粋なロジックのテストだけを行う
class BookServiceTest {

    // Repositoryをモック化する（偽物のBookRepository）
    // ・DBアクセスを行わず、好きな戻り値を自由に設定できる
    @Mock
    private BookRepository repo;

    // BookServiceの中にあるBookRepositoryフィールドへ
    // 上で作ったモックを自動注入する
    @InjectMocks
    private BookService service;

    // テストで使う共通のBookデータ
    private Book sample;

    // 各テストの前に実行される初期化処理
    @BeforeEach
    void setUp() {
        // @Mockや@InjectMocksを有効化する
        MockitoAnnotations.openMocks(this);

        // テスト用のBookエンティティを準備する
        sample = new Book();
        sample.setId(1L);
        sample.setTitle("JUnit入門");
        sample.setAuthor("山田太郎");
        sample.setPrice(1800);
    }

    // ---------------------------
    // 【正常系】IDでBookが取得できる
    // ---------------------------

    @DisplayName("ID指定でBookを取得できること")
    @Test
    void testGetBookById() {

        // モックの動作を定義：
        // repo.findById(1L) が呼ばれたら "sample" を返すように設定
        when(repo.findById(1L)).thenReturn(Optional.of(sample));

        // テスト対象のメソッドを呼び出し
        Book result = service.get(1L);

        // -------- 検証（アサーション） --------
        assertNotNull(result);  // nullではない
        assertEquals("JUnit入門", result.getTitle());  // タイトルが一致

        // -------- モックの呼び出しを検証 --------
        // findById(1L) が 1回だけ実行されたことを確認
        verify(repo, times(1)).findById(1L);
    }

    // ---------------------------
    // 【異常系】IDが存在しない場合
    // ---------------------------

    @DisplayName("存在しないIDの場合、例外が投げられること")
    @Test
    void testGetBook_NotFound() {

        // repo.findById(999L) を呼ぶと Optional.empty() を返すように設定
        when(repo.findById(999L)).thenReturn(Optional.empty());

        // service.get(999L) が IllegalArgumentException を投げることを確認
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.get(999L)
        );

        // 例外メッセージに "Book not found" が含まれているかチェック
        assertTrue(ex.getMessage().contains("Book not found"));
    }
}
