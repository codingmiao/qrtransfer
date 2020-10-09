package org.wowtools.qrtransfer.sender;

import org.wowtools.qrtransfer.common.ui.DisclaimerUi;
import org.wowtools.qrtransfer.sender.ui.SenderMainUi;

/**
 * @author liuyu
 * @date 2020/9/30
 */
public class StartSender {
    public static void main(String[] args) {
        new DisclaimerUi("sender",(e)->{
            try {
                SenderMainUi.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
