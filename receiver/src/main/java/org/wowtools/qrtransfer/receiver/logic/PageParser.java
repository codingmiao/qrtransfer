package org.wowtools.qrtransfer.receiver.logic;

import com.google.protobuf.InvalidProtocolBufferException;
import org.wowtools.qrtransfer.common.protobuf.QrPageProto;
import org.wowtools.qrtransfer.receiver.pojo.Page;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class PageParser {

    public static Page parse(byte[] bytes) {
        QrPageProto.QrPagePb pb;
        try {
            pb = QrPageProto.QrPagePb.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("解析proto异常", e);
        }
        return new Page(pb.getPageNum(), pb.getBytes().toByteArray(),pb.getMessage());
    }

}
