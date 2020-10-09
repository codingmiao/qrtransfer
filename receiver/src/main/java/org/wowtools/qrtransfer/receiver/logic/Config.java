package org.wowtools.qrtransfer.receiver.logic;

import org.wowtools.common.utils.PropertiesReader;
import org.wowtools.qrtransfer.receiver.StartReceiver;

import java.io.File;

/**
 * @author liuyu
 * @date 2020/9/28
 */
public class Config {
    /**
     * 存储文件名(绝对路径)
     */
    public static final File dest;

    /**
     * 每刷新一页后延迟多少毫秒，默认0，如果图像有延迟可适当设置此参数
     */
    public static final long pageDelay;

    static {
        PropertiesReader p = new PropertiesReader(StartReceiver.class, "config.properties");

        dest = new File(p.getString("dest"));

        long l;
        try {
            String str = p.getString("pageDelay");
            l = Long.parseLong(str);
        } catch (Exception e) {
            l = 0;
        }
        pageDelay = l;
    }
}
