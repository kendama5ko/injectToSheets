package org.example.googleapi.writer;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.example.googleapi.service.SheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheetsWriter implements DataWriter {

    private final SheetsService sheetsService;
    private static final String APPLICATION_NAME = "InjectToSheets";
    private static final String FILEID =
            "1mVZ_s2KjmEATftnagsp33w7UUpigiwDc42d1lA2IxMM";
    private static final String RANGE = "精密採点!D4:O4";
    private static final int SHEETID = 76597951;
    private Sheets sheets;

    public SheetsWriter(SheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    /**
     * Google Sheets APIからスプレッドシートに 'data' を書き込む
     *
     * @param data RegexExtractor.extractDataメソッドで抽出したデータ
     */
    @Override
    public void write(String[] data) {
        try {
            this.sheets = sheetsService.createSheets();

            // D4:N4のデータを取得
            List<List<Object>> dataOfCells = getData(RANGE);

            // データがあれば行を挿入、なければそのままデータを書き込む
            insertRowIfDataExists(dataOfCells);

            // dataの中身をdoubleかStringに変換する
            List<Object> rowData = convertData(data);

            // 配列の各要素をセルにセットする(Arrays.asListよりもCollections.singletonListの方が軽量）
            ValueRange injectData =
                    new ValueRange().setValues(Collections.singletonList(rowData));

            // Sheetsからスプレッドシートに書き込む
            sheets.spreadsheets().values()
                    .update(FILEID, RANGE, injectData)
                    .setValueInputOption("RAW") // データの形式はRAWもしくはUSER_ENTERED
                    .execute();


        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

    }
    /**
     * 受け取ったデータの中身がnullであれば何もしない
     * データがnullでなければ、insertRowAboveメソッドを呼び出し新たに行を挿入する
     *
     * @param dataOfCells getDataメソッドで得た指定した範囲のデータ
     * @throws IOException
     */
    private void insertRowIfDataExists(List<List<Object>> dataOfCells) throws IOException {
        // データがない場合
        if (dataOfCells == null || dataOfCells.isEmpty()) {
            System.out.println("D4:N4には既にデータがありません。行は追加せずデータを書き込みます。");

            // データがある場合
        } else {
            // 新しい行を追加
            insertRowAbove(3, 4);
            System.out.println("D4:N4にはデータがあります。行を追加してデータを書き込みます。");
        }
    }

    /**
     * スプレッドシートに行を追加する。
     * startRowIndexとendRowIndexの間に行が追加される。
     * (endRowIndex - startRowIndex) の数値分の行が追加される
     * @param startRowIndex 指定したindexの下に行を追加
     * @param endRowIndex   指定したindexの上まで行を追加
     * @throws IOException
     */
    public void insertRowAbove(int startRowIndex, int endRowIndex) throws IOException {
        // Google Sheets APIのリクエストを作成し、行を追加する処理
        List<Request> requests = new ArrayList<>();

        // endRowIndexで指定した行の上に行を挿入する (新しいendRowIndexの行を作成)
        requests.add(new Request()
                .setInsertRange(new InsertRangeRequest()
                        .setRange(new GridRange()
                                // シートIDを指定
                                .setSheetId(SHEETID)

                                // どの行の下に追加したいかを指定 (今回は3行目の下に追加したいので3)
                                .setStartRowIndex(startRowIndex)

                                // どの行の上まで追加したいかを指定（今回は4）
                                .setEndRowIndex(endRowIndex))

                        .setShiftDimension("ROWS"))); // 行の挿入を指定

        // リクエストをバッチで送信
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        sheets.spreadsheets().batchUpdate(FILEID, body).execute();
    }

    /**
     * 抽出したテキストの中身をdoubleかStringに変換する
     *
     * @param data RegexExtractor.extractDataで抽出したテキスト
     * @return doubleかStringに変換されたdata
     */
    public List<Object> convertData(String[] data) {
        List<Object> rowData = new ArrayList<>();

        // dataの要素のnullチェック
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                data[i] = "no data";
            }
        }

        for (String item : data) {
            // 数値かどうかの判定を行い、数値であればDoubleに変換、そうでなければそのまま文字列
            try {
                double numericValue = Double.parseDouble(item);
                rowData.add(numericValue); // 数値として追加
            } catch (NumberFormatException e) {
                rowData.add(item); // 文字列として追加
            }
        }

        return rowData;
    }

    /**
     * sheetsインスタンスのスプレッドシートからrangeで指定された範囲のデータを取得する
     * @param range 取得したいデータの範囲を記述（例： "シート名!A1:Z1" ）
     * @return 指定された範囲のデータを List<List<Object>> で返す
     * @throws IOException
     */
    public List<List<Object>> getData(String range) throws IOException {
        // rangeのデータを取得
        ValueRange response = sheets.spreadsheets().values()
                .get(FILEID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        return values;
    }

}
