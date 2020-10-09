package org.wowtools.qrtransfer.sender.logic;

import com.google.protobuf.ByteString;
import org.wowtools.qrtransfer.common.pojo.FileHead;
import org.wowtools.qrtransfer.common.protobuf.QrPageProto;
import org.wowtools.qrtransfer.common.util.ByteDeque;
import org.wowtools.qrtransfer.common.util.Constant;
import org.wowtools.qrtransfer.common.util.Md5Util;
import org.wowtools.qrtransfer.common.util.QRCodeUtil;
import org.wowtools.qrtransfer.sender.ui.SenderMainUi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * 读取文件工具
 *
 * @author liuyu
 * @date 2020/9/28
 */
public class FileReader {

    /**
     * 读取文件头信息
     *
     * @param file
     * @return
     */
    public static FileHead readHead(File file) {
        FileHead fileHead = new FileHead();
        fileHead.setMd5(Md5Util.getFileMD5(file));
        fileHead.setFileSize(file.length());
        return fileHead;
    }

    private static final int bufferSize = 10240;//读文件的缓冲区大小
    private static final int maxMemSize = bufferSize * 10;//内存中存byte的最大数量

    /**
     * 读取文件字节并分页为Iterator返回
     *
     * @param file
     * @return
     */
    public static Iterator<QrPageProto.QrPagePb> readPage(File file) {
        FileInputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Iterator<QrPageProto.QrPagePb> iterator = new Iterator<>() {
            int len;
            int currentPage = 0;
            byte[] buffer = new byte[bufferSize];
            ByteDeque mem = new ByteDeque();

            @Override
            public synchronized boolean hasNext() {
                if (len != -1) {
                    return true;
                }
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return mem.size() > 0;
            }

            @Override
            public synchronized QrPageProto.QrPagePb next() {
                if (len != -1 && mem.size() < maxMemSize) {
                    try {
                        len = in.read(buffer, 0, buffer.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < len; i++) {
                        mem.addLast(buffer[i]);
                    }
                }
                int size = Config.initQrPageSize;
                if (mem.size() < size) {
                    size = mem.size();
                }
                byte[] bytes;
                if (Config.testQr) {//测试二维码是否有效并尝试调整二维码字节数
                    do {
                        bytes = readFromMem(size);
                        if (testQrCode(bytes)) {
                            break;
                        }
                        resetToMem(bytes);
                        if (size < 32) {
                            throw new RuntimeException("无法通过二维码测试,size:" + size);
                        }
                        size = size / 2;
                    } while (true);
                } else {//直接返回二维码
                    bytes = readFromMem(size);
                }

                return createPb(bytes);
            }

            private byte[] readFromMem(int size) {
                byte[] bytes = new byte[size];
                for (int i = 0; i < size; i++) {
                    bytes[i] = mem.removeFirst();
                }
                return bytes;
            }

            private void resetToMem(byte[] bytes) {
                for (int i = bytes.length - 1; i >= 0; i--) {
                    mem.addFirst(bytes[i]);
                }
            }


            private QrPageProto.QrPagePb createPb(byte[] bytes) {
                QrPageProto.QrPagePb.Builder builder = QrPageProto.QrPagePb.newBuilder();
                builder.setBytes(ByteString.copyFrom(bytes));
                builder.setPageNum(currentPage);
                if (len == -1 && mem.size() == 0) {
                    System.out.println("end ,pag:" + currentPage);
                    builder.setMessage(Constant.Flag_End);
                }
                currentPage++;
                return builder.build();
            }
        };
        return iterator;
    }


    private static boolean testQrCode(byte[] bytes) {
        try {
            QRCodeUtil.generateQRCodeImage(bytes, SenderMainUi.qrCodeCanvas.img);
            byte[] qrBytes = QRCodeUtil.parseQRCodeImage(SenderMainUi.qrCodeCanvas.img);
            if (null == qrBytes) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
