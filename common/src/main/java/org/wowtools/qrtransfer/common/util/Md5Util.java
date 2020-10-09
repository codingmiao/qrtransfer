package org.wowtools.qrtransfer.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class Md5Util {

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        byte buffer[] = new byte[1024];
        int len;
        MessageDigest digest;
        try (FileInputStream in = new FileInputStream(file)) {
            digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
