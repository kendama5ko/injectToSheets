package org.example.googleapi.writer;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.example.googleapi.service.SheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheetsWriter implements DataWriter{

    private final SheetsService sheetsService;
    private static final String APPLICATION_NAME = "InjectToSheets";
    private static final String FILEID =
            "1mVZ_s2KjmEATftnagsp33w7UUpigiwDc42d1lA2IxMM";
    private static final String RANGE = "精密採点!D4:O4";
    private static final int SHEETID = 76597951;

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
            Sheets sheets = sheetsService.createSheets();

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

}
