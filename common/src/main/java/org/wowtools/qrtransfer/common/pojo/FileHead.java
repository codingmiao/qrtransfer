package org.wowtools.qrtransfer.common.pojo;


import org.wowtools.qrtransfer.common.util.Constant;

/**
 * 头信息，描述分页数、md5码等
 *
 * @author liuyu
 * @date 2020/9/28
 */
public class FileHead {
    private String md5;
    private long fileSize;

    public byte[] toByte() {
        String res = md5 + "\n" + fileSize;
        return res.getBytes(Constant.charset);
    }

    public FileHead() {

    }

    public FileHead(byte[] bts) {
        String res = new String(bts, Constant.charset);
        String[] strs = res.split("\n");
        md5 = strs[0];
        fileSize = Integer.valueOf(strs[1]);
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileSizeStr() {
        if (fileSize > 1024 * 1024 * 1024) {
            float f = fileSize / (1024f * 1024 * 1024);
            return String.format("%.3f", f) + "gb";
        } else if (fileSize > 1024 * 1024) {
            float f = fileSize / (1024f * 1024);
            return String.format("%.3f", f) + "mb";
        } else if (fileSize > 1024) {
            float f = fileSize / (1024f);
            return String.format("%.3f", f) + "kb";
        } else {
            return fileSize + "b";
        }
    }

    @Override
    public String toString() {
        return "md5='" + md5 + '\'' +
                ", fileSize=" + getFileSizeStr();
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
