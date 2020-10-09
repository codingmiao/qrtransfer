package org.wowtools.qrtransfer.receiver;

import org.wowtools.qrtransfer.common.protobuf.QrPageProto;
import org.wowtools.qrtransfer.common.util.QRCodeUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author liuyu
 * @date 2020/9/29
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("D:\\_tmp\\7.jpg"));
        byte[] bts = QRCodeUtil.parseQRCodeImage(img);
        QrPageProto.QrPagePb pb = QrPageProto.QrPagePb.parseFrom(bts);
        System.out.println(pb.getPageNum());
    }
}
