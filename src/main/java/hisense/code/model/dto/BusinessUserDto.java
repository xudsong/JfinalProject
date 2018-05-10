package hisense.code.model.dto;

import hisense.code.model.TUser;

import java.io.Serializable;

/**
 * 用户完全信息
 * Created by zhanghaichao on 2018/4/28.
 */
public class BusinessUserDto implements Serializable {

    private static final long serialVersionUID = 3268987629373386058L;
    private String[] authCode;

    private TUser tUser;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String[] authCode) {
        this.authCode = authCode;
    }

    public TUser gettUser() {
        return tUser;
    }

    public void settUser(TUser tUser) {
        this.tUser = tUser;
    }
}
