package org.wowtools.qrtransfer.common.ui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志框
 *
 * @author liuyu
 * @date 2020/9/28
 */
public class LogTextArea extends TextArea {
    private final int columns;


    private final String[] logs;


    private int cursor = 0;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public LogTextArea(int columns) throws HeadlessException {
        this.columns = columns;
        this.setColumns(columns);
        this.setEditable(false); // 设置输入框不允许编辑
        logs = new String[columns];
        for (int i = 0; i < columns; i++) {
            logs[i] = "";
        }
    }


    public synchronized void log(String log) {
        log = sdf.format(new Date()) + "\t" + log;
        logs[cursor] = log;
        StringBuilder sb = new StringBuilder();
        for (int i = cursor + 1; i < columns; i++) {
            sb.append(logs[i]).append("\n");
        }
        for (int i = 0; i <= cursor; i++) {
            sb.append(logs[i]).append("\n");
        }
        this.setText(sb.toString());
        cursor++;
        if (cursor == columns) {
            cursor = 0;
        }
    }
}
