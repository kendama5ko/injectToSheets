package org.example.googleapi.authentication;

import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class GoogleAuthenticator implements AuthenticationService{
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/dataextractor-2024-10-17-3311967df236.json";

    /**
     * サービスアカウントの認証情報を取得し、Google Sheets APIに対するスプレッドシートの読み書き権限を持つ認証情報を作成する。
     * <p>
     * [メソッドの流れ]
     * FileInputStream serviceAccountStream
     *  └─ 作成したGCPサービスアカウントのキー（このキーは同じものを再DL不可のため注意）を読み込みます。
     * <p>
     * ServiceAccountCredentials.fromStream(serviceAccountStream)
     *  └─ キーからサービスアカウントの認証情報を作成します。
     * <p>
     *  createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS))
     *  └─ 認証情報にスコープを適用します。今回はSheetsScopes.SPREADSHEETSでスプレッドシートへのアクセス権限を設定。
     *
     * @return Google Sheets APIのスコープにアクセスできる認証情報
     * @throws IOException サービスアカウント認証情報のファイルを読み込む際にエラーが発生した場合
     */
    @Override
    public GoogleCredentials getCredentials() throws IOException {
        FileInputStream serviceAccountStream =
                new FileInputStream(CREDENTIALS_FILE_PATH);

        return ServiceAccountCredentials.fromStream(serviceAccountStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }
}
