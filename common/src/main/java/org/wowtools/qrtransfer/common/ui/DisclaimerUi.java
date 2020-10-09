package org.wowtools.qrtransfer.common.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author liuyu
 * @date 2020/9/30
 */
public class DisclaimerUi extends JFrame {

    private final String text =
            "------免责声明-------\n" +
                    "本软件使用读取屏幕图像的方式从远程桌面拷贝文件，" +
                    "其运作原理与您用ByteEdit、UltraEdit等软件从远程桌面查看并抄写字节到本地文件是一样的，并不具备泄密特性。\n" +
                    "但是，本软件仅供拷贝运行命令、分析参数等非敏感信息，作紧急排查问题等用途。\n" +
                    "使用本软件时，请严格准守相关法律法规及贵公司各项规定，不要拷被任何涉密文件。\n" +
                    "如您擅自违反上述法律法规及规定，造成的任何责任将完全由您自行承担。\n" +
                    "\n\n" +
                    "本软件源码公开在github(https://github.com/codingmiao/qrtransfer)上，请先阅读源码以确认是否有泄密可能后再谨慎使用。";


    public DisclaimerUi(String title, ActionListener onAgree) throws HeadlessException {
        super(title);
        this.setSize(700, 400);
        DisclaimerUi ui = this;
        JButton agreeButton = new JButton("我已完全知晓并同意上述内容");
        this.add(agreeButton, BorderLayout.SOUTH);
        agreeButton.addActionListener((e) -> {
            ui.setVisible(false);
            onAgree.actionPerformed(e);
        });

        TextArea textArea = new TextArea();
        this.add(textArea, BorderLayout.CENTER);
        textArea.setText(text);

        this.setVisible(true);

        //关闭按钮
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
