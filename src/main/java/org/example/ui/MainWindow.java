package org.example.ui;

import org.example.Extractor.DataExtractor;
import org.example.Extractor.RegexExtractor;
import org.example.TextProcessor;
import org.example.googleapi.writer.DataWriter;
import javax.swing.*;
public class MainWindow {
    private String inputText;

    public void makeWindow(TextProcessor processor, DataExtractor extractor,
                           DataWriter writer) {
        JFrame frame = new JFrame("Write to a SpreadSheet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(630, 650);
        frame.setLocation(1250, 100);

        // panel
        JPanel panel = new JPanel();

        // layout

        // textarea
        JTextArea textArea = new JTextArea(30, 50);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // execute button
        JButton executeButton = new JButton("書き込み");

        // 書き込みボタンのアクション
        executeButton.addActionListener(e -> {
            inputText = textArea.getText();
            processor.processText(inputText, new RegexExtractor(), writer);
        });

        panel.add(scrollPane);
        panel.add(executeButton);

        frame.add(panel);
        frame.setVisible(true);


    }
}
