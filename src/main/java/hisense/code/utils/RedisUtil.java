package hisense.code.utils;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

/**
 * redis操作工具类
 * Created by zhanghaichao on 2018/4/20.
 */
public class RedisUtil {

    private static Cache bbsCache;

    static {
        bbsCache = Redis.use();
    }

    public static void set(String key, String value) {
        bbsCache.set(key, value);
    }

    public static void setex(Object key, Object value, int seconds) {
        bbsCache.setex(key, seconds, value);
    }

    public static <T> T get(Object key) {
        return bbsCache.get(key);
    }

    public static Long del(Object key) {
        return bbsCache.del(key);
    }

}
