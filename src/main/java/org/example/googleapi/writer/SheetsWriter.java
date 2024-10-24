package org.example.googleapi.writer;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.example.googleapi.service.SheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

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

            // 配列の各要素をセルにセットする
            ValueRange injectData =
                    new ValueRange().setValues(Collections.singletonList(Arrays.asList(data)));

            // Sheetsからスプレッドシートに書き込む
            sheets.spreadsheets().values()
                    .update(FILEID, RANGE, injectData)
                    .setValueInputOption("RAW") // データの形式はRAWもしくはUSER_ENTERED
                    .execute();


        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

}
