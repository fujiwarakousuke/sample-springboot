package com.example.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// StringUtil クラスの trimToNull() メソッドをテストするクラス
// ・Spring は使用しない純粋な Java の単体テスト
// ・JUnit5 の @Test だけで実行できる
class StringUtilTest {

    @Test
    void trimToNull_string() {
        // -----------------------------------------------------
        // ① 前後に空白がある通常の文字列を渡したとき
        //    trim() がかかり、"hello" に変換されるかテストする
        // -----------------------------------------------------
        String result = StringUtil.trimToNull("  hello  ");

        // 結果が "hello" に正しくトリムされていることを検証
        assertEquals("hello", result);
    }

    @Test
    void trimToNull_space() {
        // -----------------------------------------------------
        // ② 空白だけの文字列を渡したとき
        //    trim() の結果が ""（空文字）になる
        //    → 空文字は null として返す仕様のため null を期待する
        // -----------------------------------------------------
        String result = StringUtil.trimToNull("   ");

        // 結果が null であることを検証
        assertNull(result);
    }

    @Test
    void trimToNull_null() {
        // -----------------------------------------------------
        // ③ 引数に null を渡したとき
        //    メソッド内で null チェックされ、そのまま null が返る仕様
        // -----------------------------------------------------
        String result = StringUtil.trimToNull(null);

        // 結果が null のまま返されることを検証
        assertNull(result);
    }
}
