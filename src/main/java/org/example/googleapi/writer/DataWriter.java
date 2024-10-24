package org.example.googleapi.writer;

public interface DataWriter {

    /**
     * Google Sheets APIからスプレッドシートに 'data' を書き込む
     *
     * @param data RegexExtractor.extractDataメソッドで抽出したデータ
     */
    void write(String[] data);
}
