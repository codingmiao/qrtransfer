package org.wowtools.qrtransfer.receiver.logic;

import org.wowtools.qrtransfer.common.util.Md5Util;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liuyu
 * @date 2020/9/30
 */
public class FileWriter {
    private static final FileOutputStream os;
    private static long size;
    static {
        try {
            if (Config.dest.exists()) {
                Config.dest.delete();
            }
            os = new FileOutputStream(Config.dest, true);
        } catch (Exception e) {
            throw new RuntimeException("获取文件流异常", e);
        }
    }

    public static void write(byte[] bytes) {
        try {
            os.write(bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new RuntimeException("写入文件异常", e);
        }
        size += bytes.length;
//        System.out.println("file size " + size);
    }

    public static void close() {
        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("关闭文件里异常异常", e);
        }
        md5 = Md5Util.getFileMD5(Config.dest);
//        System.out.println("file closed,size:" + size);
    }

    private static String md5;

    public static String getMd5() {
        return md5;
    }

    public static long getSize() {
        return size;
    }
}
