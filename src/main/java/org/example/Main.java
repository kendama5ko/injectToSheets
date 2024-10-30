package org.example;

import org.example.Extractor.RegexExtractor;
import org.example.googleapi.authentication.AuthenticationService;
import org.example.googleapi.authentication.GoogleAuthenticator;
import org.example.googleapi.service.GoogleSheetsService;
import org.example.googleapi.service.SheetsService;
import org.example.googleapi.writer.DataWriter;
import org.example.googleapi.writer.SheetsWriter;
import org.example.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        String inputText = """
                
                85.155点
                  ビブラートボーナス
                  0.423点
                
                
                  全国平均84.401点
                  音程安定性表現力リズムビブラート＆
                  ロングトーン音程安定性表現力リズムビブラート＆
                  ロングトーン
                  分析レポート
                  なかなかの盛り上げ上手ですね。次はアプローチも気を抜かずに最後まで張り切ってみて。
                  音程
                  正確率78%
                  表現力
                  76点　/　抑揚54点　しゃくり16回　こぶし10回　フォール2回
                  ロングトーン
                  上手さ
                  安定性
                  震えがちまっすぐ
                  リズム
                  タメ走り
                  ビブラート上手さ合計5.4秒11回
                  タイプボックス形(A-1)
                  声域
                
                """; // 入力テキスト

        // 利用するAuthenticatorを作成
        AuthenticationService authService = new GoogleAuthenticator();

        // 認証されたauthServiceを利用してSheetsServiceのインスタンスを作成
        SheetsService sheetsService = new GoogleSheetsService(authService);

        // sheetsServiceをDataWriterに渡す
        DataWriter writer = new SheetsWriter(sheetsService);

        MainWindow mainWindow = new MainWindow();
        mainWindow.makeWindow(new TextProcessor(), new RegexExtractor(), writer);

    }
}
