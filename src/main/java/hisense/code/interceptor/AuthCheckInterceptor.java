package hisense.code.interceptor;

import hisense.code.config.constant.field.Constant;
import hisense.code.interceptor.annotation.AuthCode;
import hisense.code.utils.SessionUtil;
import hisense.code.utils.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import java.util.Arrays;

/**
 * 用户权限控制
 * Created by zhanghaichao on 2018/4/28.
 */
public class AuthCheckInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        //检查是否有注解
        AuthCode authCode = inv.getMethod().getAnnotation(AuthCode.class);
        if (authCode != null) {
            //获取用户token
            String token = controller.getCookie("token");
            if(!StringUtils.isNotNull(token)){
                controller.renderJson(Constant.noLogin);
            }else{
                String[] code = authCode.code();
                String[] codeUser = SessionUtil.getUserAuth(token);
                for(String c : code){
                    if(Arrays.binarySearch(codeUser, c)<0){
                        controller.renderJson(Constant.noAuth);
                        break;
                    }
                }
            }
        }else{
            inv.invoke();
        }
    }
}
