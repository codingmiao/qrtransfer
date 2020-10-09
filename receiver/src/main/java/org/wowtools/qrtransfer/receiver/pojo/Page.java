package org.wowtools.qrtransfer.receiver.pojo;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class Page {
    private final int pageNum;
    private final byte[] bytes;
    private final String msg;

    public Page(int pageNum, byte[] bytes, String msg) {
        this.pageNum = pageNum;
        this.bytes = bytes;
        this.msg = msg;
    }

    public int getPageNum() {
        return pageNum;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getMsg() {
        return msg;
    }
}
