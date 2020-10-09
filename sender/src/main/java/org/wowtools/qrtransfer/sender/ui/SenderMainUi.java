package org.wowtools.qrtransfer.sender.ui;

import org.wowtools.qrtransfer.common.pojo.FileHead;
import org.wowtools.qrtransfer.common.protobuf.QrPageProto;
import org.wowtools.qrtransfer.common.ui.LogTextArea;
import org.wowtools.qrtransfer.common.util.QRCodeUtil;
import org.wowtools.qrtransfer.sender.logic.Config;
import org.wowtools.qrtransfer.sender.logic.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

/**
 * 主界面
 *
 * @author liuyu
 * @date 2020/9/27
 */
public class SenderMainUi extends JFrame {

    public static final QrCodeCanvas qrCodeCanvas;

    public static final PageTurner pageTurner;

    public static final LogTextArea logTextArea;

    static {
        SenderMainUi ui = new SenderMainUi("sender");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ui.setSize(screenSize.width, screenSize.height);    //设置Frame的大小
//        ui.setBackground(Color.yellow);      //设置Frame的背景色

//        // 全屏设置
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        gd.setFullScreenWindow(ui);

        int w = ui.getWidth();
        int h = ui.getHeight();

        //确定水平布局还是垂直布局
        BoxLayout layout = w > h ?
                new BoxLayout(ui.getContentPane(), BoxLayout.X_AXIS) :
                new BoxLayout(ui.getContentPane(), BoxLayout.Y_AXIS);
        ui.setLayout(layout);

        //二维码框
        int qrCodeWidth = w > h ? h : w;
        qrCodeCanvas = new QrCodeCanvas(256);
        ui.add(qrCodeCanvas);


        //日志
        logTextArea = new LogTextArea(35);
        ui.add(logTextArea);

        //翻页
        pageTurner = new PageTurner();
        logTextArea.addKeyListener(pageTurner);

        //关闭按钮
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        ui.setVisible(true);         //设置Frame为可见
        ui.setResizable(false);

    }


    public static void start() throws Exception {
        FileHead head = FileReader.readHead(Config.tar);
        QRCodeUtil.generateQRCodeImage(head.toByte(), qrCodeCanvas.img);
        SenderMainUi.qrCodeCanvas.paint(SenderMainUi.qrCodeCanvas.getGraphics());
        Iterator<QrPageProto.QrPagePb> pages = FileReader.readPage(Config.tar);
        pageTurner.setPages(pages);
        logTextArea.log("文件载入完成, md5:" + head.getMd5() + ", size:" + head.getFileSizeStr());
    }

    public SenderMainUi(String str) {
        super(str);     //调用父类的构造方法
    }
}
