package org.example.ui;

import org.example.Extractor.DataExtractor;
import org.example.Extractor.RegexExtractor;
import org.example.TextProcessor;
import org.example.googleapi.writer.DataWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // paste button
        JButton pasteButton = new JButton("貼り付け");

        // ボタンにアクションリスナーを追加
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // クリップボードからデータを取得
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable transferable = clipboard.getContents(null);

                try {
                    // クリップボードからテキストを取得
                    String dataFromClipBoard = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    // JTextAreaに貼り付け
                    textArea.append(dataFromClipBoard);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "クリップボードからデータを取得できませんでした。");
                }
            }
        });

        // execute button
        JButton executeButton = new JButton("書き込み");

        // 書き込みボタンのアクション
        executeButton.addActionListener(e -> {
            inputText = textArea.getText();
            processor.processText(inputText, new RegexExtractor(), writer);
        });

        panel.add(scrollPane);
        panel.add(pasteButton);
        panel.add(executeButton);

        frame.add(panel);
        frame.setVisible(true);


    }
}
