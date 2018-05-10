package hisense.code.utils;

import hisense.code.model.dto.BusinessUserDto;

import java.util.UUID;

/**
 * session工具
 * Created by zhanghaichao on 2018/4/28.
 */
public class SessionUtil {

    /**
     * 保存用户信息并返回用户token
     *
     * @return
     */
    public static String setUserInfo(BusinessUserDto user) {
        String token = UUID.randomUUID().toString();
        RedisUtil.setex(token, user, 3600);
        return token;
    }

    /**
     * 根据token获取用户权限集合
     * @param token
     * @return
     */
    public static String[] getUserAuth(String token){
        BusinessUserDto user = RedisUtil.get(token);
        return user.getAuthCode();
    }

}
