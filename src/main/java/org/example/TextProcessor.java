package org.example;

import org.example.Extractor.DataExtractor;
import org.example.googleapi.writer.DataWriter;

public class TextProcessor {

    /**
     * RegexExtractor.extractDataメソッドからデータを抽出し、SheetsWriter.writeメソッドでデータを書き込む
     *
     * @param text オリジナルのテキストデータ
     * @param extractor text からregexで指定されたデータを抽出するためのクラスのインスタンス
     * @param writer 抽出されたデータを書き込むためのクラスのインスタンス
     */
    public void processText(String text, DataExtractor extractor, DataWriter writer) {
        String[] extractedData = extractor.extractData(text);
        writer.write(extractedData);
    }
}
