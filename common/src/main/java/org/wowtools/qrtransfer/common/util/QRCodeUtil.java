package org.wowtools.qrtransfer.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Map;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class QRCodeUtil {

    private static final Map<DecodeHintType, Object> hints = Map.of(
            DecodeHintType.CHARACTER_SET, "UTF-8",
            DecodeHintType.TRY_HARDER, false
    );

    public static void generateQRCodeImage(byte[] bts, BufferedImage img) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = img.getWidth();
        //TODO 先传BASE64,后续改成直接byte[]
        BitMatrix bitMatrix = qrCodeWriter.encode(Base64.getEncoder().encodeToString(bts), BarcodeFormat.QR_CODE, width, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                img.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
    }

    public static byte[] parseQRCodeImage(BufferedImage img) {
        String str = null;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(img);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
            str = result.getText();//TODO 先解析base64，后续改为直接解析byte
            byte[] res = Base64.getDecoder().decode(str);
            return res;
        } catch (NotFoundException e) {
            return null;
        } catch (Exception e) {
            System.out.println(str);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
