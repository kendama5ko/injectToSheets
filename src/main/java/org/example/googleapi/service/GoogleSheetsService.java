package org.example.googleapi.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import org.example.googleapi.authentication.AuthenticationService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleSheetsService implements SheetsService{
    private static final String APPLICATION_NAME = "InjectToSheets";
    private final AuthenticationService authService;

    /**
     * 指定されたAuthenticationServiceを使用して、新しいGoogleSheetsServiceを構築します。
     *
     * @param authService Google Sheets APIにアクセスするための認証の手続きを行うクラス
     */
    public GoogleSheetsService(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Google Sheets APIのインスタンスを作成して返します。
     * 認証情報を用いてGoogle Sheets APIにアクセスし、スプレッドシートの読み書き等が利用可能になります。
     * <p>
     * [メソッドの流れ]
     * Sheets.Builder
     *  └─Google Sheets APIを操作するためのビルダーのインスタンスを作成
     * <p>
     * HttpCredentialsAdapter
     *  └─ GoogleのHttpRequestに認証情報を適用するためのクラスです。
     * <p>
     * GoogleNetHttpTransport.newTrustedTransport()
     *  └─ 安全なHTTP通信を行うために、TLS（SSL）通信をサポートする信頼できるトランスポート層を作成します。
     * <p>
     * GsonFactory
     *  └─ JSON形式のデータをJavaオブジェクトに変換したり、その逆を行うためのGoogleのライブラリです。
     * <p>
     * GsonFactory.getDefaultInstance()
     *  └─ JSONデータのシリアライズ/デシリアライズを行うためのGsonライブラリのファクトリインスタンスを作成します。
     * <p>
     * build()
     *  └─ 設定したHTTPトランスポート、JSONファクトリ、認証情報を使ってSheetsオブジェクトを構築します。
     *
     * @return Google Sheets APIインスタンス
     * @throws IOException 認証情報の読み込みに失敗した場合
     * @throws GeneralSecurityException セキュリティ設定に関連するエラーが発生した場合
     */
    @Override
    public Sheets createSheets() throws IOException, GeneralSecurityException {
        HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(authService.getCredentials());

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credentialsAdapter)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
