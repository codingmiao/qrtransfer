package org.wowtools.qrtransfer.receiver.ui;

import org.wowtools.qrtransfer.receiver.logic.Receiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class StartButton extends JButton {

    public StartButton() throws HeadlessException {
        super("start");
        this.setLayout(null);
        this.setBounds(0, 0, 100, 30);
    }

    private final StartButton ts = this;

    private final ActionListener onclick = e -> {
        ts.setEnabled(false);
        new Thread(() -> Receiver.start()).start();//新起一个线程，否则关闭按钮无效
    };

    {
        this.addActionListener(onclick);
    }

}
