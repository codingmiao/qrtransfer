package org.wowtools.qrtransfer.receiver;

import org.wowtools.qrtransfer.common.ui.DisclaimerUi;
import org.wowtools.qrtransfer.receiver.ui.ReceiverMainUi;

/**
 * @author liuyu
 * @date 2020/9/30111
 */
public class StartReceiver {
    public static void main(String[] args) throws Exception{
        new DisclaimerUi("receiver", (e) -> {
            try {
                ReceiverMainUi.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
