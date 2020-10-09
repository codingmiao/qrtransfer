package org.wowtools.qrtransfer.sender.ui;

import org.wowtools.qrtransfer.common.protobuf.QrPageProto;
import org.wowtools.qrtransfer.common.util.Constant;
import org.wowtools.qrtransfer.common.util.QRCodeUtil;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文件数据翻页
 *
 * @author liuyu
 * @date 2020/9/28
 */
public class PageTurner extends KeyAdapter {

    private static final int pageCacheSize = 100;

    private Iterator<QrPageProto.QrPagePb> pages;
    private ArrayList<QrPageProto.QrPagePb> pageCaches = new ArrayList<>(pageCacheSize);

    {
        for (int i = 0; i < pageCacheSize; i++) {
            pageCaches.add(null);
        }
    }

    private int pageCacheCursor = pageCacheSize;
    private QrPageProto.QrPagePb currentPagePb = null;
    private final AtomicBoolean busying = new AtomicBoolean(false);


    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("按下:"+e.getKeyChar());
        if (pages == null) {
            SenderMainUi.logTextArea.log("文件尚未载入完成");
            return;
        }
        if (busying.getAndSet(true)) {//忙碌中则跳过此次按键事件，避免二维码未及时刷新
            SenderMainUi.logTextArea.log("busying...");
            return;
        }

        char inputKey = e.getKeyChar();

        switch (inputKey) {
            case Constant.Key_NextPage:
                nextPage();
                break;
            case Constant.Key_BeforePage:
                beforePage();
                break;
        }

        busying.set(false);
    }

    /**
     * 翻到下一页
     */
    private void nextPage() {

        if (pageCacheCursor < pageCacheSize) {//从缓存页取数据
            QrPageProto.QrPagePb page = pageCaches.get(pageCacheCursor);
            if (null == page) {
                SenderMainUi.logTextArea.log("翻到下一页失败，缓存逻辑错误");
                return;
            }
            try {
                QRCodeUtil.generateQRCodeImage(page.toByteArray(), SenderMainUi.qrCodeCanvas.img);
                SenderMainUi.qrCodeCanvas.paint(SenderMainUi.qrCodeCanvas.getGraphics());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            currentPagePb = page;
            pageCacheCursor++;
            SenderMainUi.logTextArea.log("page " + currentPagePb.getPageNum() + ",size " + currentPagePb.getBytes().size());
        } else {//从文件取数据
            if (pages.hasNext()) {
                QrPageProto.QrPagePb page = pages.next();
                try {
                    QRCodeUtil.generateQRCodeImage(page.toByteArray(), SenderMainUi.qrCodeCanvas.img);
                    SenderMainUi.qrCodeCanvas.paint(SenderMainUi.qrCodeCanvas.getGraphics());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    throw new RuntimeException(exception);
                }
                System.out.println(page.getPageNum() + " " + page.getBytes().size());
                currentPagePb = page;
                pageCacheCursor = pageCacheSize;
                pageCaches.add(page);
                pageCaches.remove(0);
                SenderMainUi.logTextArea.log("page " + currentPagePb.getPageNum() + ",size " + currentPagePb.getBytes().size());
            } else {
                SenderMainUi.logTextArea.log("page load success,num:" + currentPagePb.getPageNum());
            }
        }
    }

    /**
     * 翻到上一页
     */
    private void beforePage() {
        if (currentPagePb == null) {
            SenderMainUi.logTextArea.log("尚未开始");
            return;
        }
        if (currentPagePb.getPageNum() == 0) {
            SenderMainUi.logTextArea.log("已是首页");
            return;
        }
        if (pageCacheCursor == 0) {
            SenderMainUi.logTextArea.log("翻到上一页失败，已达到最大缓存，请重新启动程序 0x1");
            return;
        }
        pageCacheCursor--;
        QrPageProto.QrPagePb page = pageCaches.get(pageCacheCursor);
        if (null == page) {
            pageCacheCursor++;
            SenderMainUi.logTextArea.log("已是首页 0x2");
            return;
        }
        try {
            QRCodeUtil.generateQRCodeImage(page.toByteArray(), SenderMainUi.qrCodeCanvas.img);
            SenderMainUi.qrCodeCanvas.paint(SenderMainUi.qrCodeCanvas.getGraphics());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        currentPagePb = page;
        SenderMainUi.logTextArea.log("page " + currentPagePb.getPageNum());

    }

    public synchronized void setPages(Iterator<QrPageProto.QrPagePb> pages) {
        this.pages = pages;
    }
}
