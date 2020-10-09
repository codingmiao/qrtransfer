package org.wowtools.qrtransfer.sender.logic;

import org.wowtools.common.utils.PropertiesReader;
import org.wowtools.qrtransfer.sender.StartSender;

import java.io.File;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class Config {

    /**
     * 目标文件
     */
    public static final File tar;

    /**
     * 二维码初次尝试容纳字节数,默认1500
     */
    public static final int initQrPageSize;

    /**
     * 二维码宽度(像素)，默认256，过大过小都会影响解析，需合理设置
     */
    public static final int qrCodeWidth;

    /**
     * 是否开启二维码测试，默认true，为true时，发送端生成验证码后会测试一下是否能识别，不能则尝试自动调整initQrPageSize的值
     */
    public static final boolean testQr;

    static {
        PropertiesReader p = new PropertiesReader(StartSender.class, "config.properties");
        int i;

        tar = new File(p.getString("tar"));

        try {
            i = p.getInteger("initQrPageSize");
        } catch (Exception e) {
            i = 1500;
        }
        initQrPageSize = i;

        try {
            i = p.getInteger("qrCodeWidth");
        } catch (Exception e) {
            i = 256;
        }
        qrCodeWidth = i;

        boolean b;
        try {
            b = p.getString("testQr").equals("true");
        } finally {
            b = true;
        }
        testQr = b;
    }
}
