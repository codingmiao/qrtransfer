package org.wowtools.qrtransfer.receiver.logic;

import org.wowtools.qrtransfer.common.util.QRCodeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * 屏幕截图及识别屏幕截图中的二维码
 *
 * @author liuyu
 * @date 2020/9/28
 */
public class CaptureScreen {

    private static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage getCaptureScreenImage() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        return image;
    }

    public static byte[] encode() {
        BufferedImage img = getCaptureScreenImage();
        byte[] res = QRCodeUtil.parseQRCodeImage(img);
        return res;
    }


}
