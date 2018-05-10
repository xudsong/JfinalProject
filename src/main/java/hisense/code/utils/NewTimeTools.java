package hisense.code.utils;

import java.util.Date;

/**
 * 新的时间工具类。之前的timetools时间工具类是基于mysql数据库的。很多操作不适用。保留之前的类，添加方法时进行参照。
 * @author mark
 *
 */
public class NewTimeTools {
    /**
     * 获取当前时间
     * @return
     */
    public static Long getNowDate() {
        Date rightNow = new Date();
        return rightNow.getTime();
    }
}
