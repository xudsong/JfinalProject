package hisense.code.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class LoginInterceptor implements Interceptor {

    private static int flag = 0;

    /**
     * 用户登录的全局拦截器
     */
    @SuppressWarnings("unused")
    public void intercept(Invocation inv) {
//        Controller controller = inv.getController();
//        String actKey = inv.getActionKey(); // 默认就是ActionKey
//        // 获取菜单名
//        if (actKey != null && !"".equals(actKey)) {
////            String menuname = getMenuName(actKey);
//            controller.setAttr("navigationmenuname", "213");
//        }
//        String error = controller.getSessionAttr("errormessage");
//        UserModel user = controller.getSessionAttr("userInfo");
//        if (actKey.equals("/login")) {
//            if (error != null) {
//                flag++;
//                if (flag > 1) {
//                    removeSession(controller);
//                    flag = 0;
//                }
//            }
//            inv.invoke();
//        } else if (actKey.equals("/dologin")) {
//            removeSession(controller);
//            inv.invoke();
//        } else if (actKey.equals("/jumpPage")) {
//            inv.invoke();
//        } else if (user == null) {
//            if (controller.getRequest().getHeader("x-requested-with") != null
//                    && controller.getRequest().getHeader("x-requested-with")
//                    .equalsIgnoreCase("XMLHttpRequest")) {
//                controller.getResponse().addHeader("sessionstatus", "timeout");
//                return;
//            }
//            if (actKey != null) {
//                extractedLogin(controller);
//            } else {
//                extracted(controller);
//            }
//        } else {
//            inv.invoke();
//        }
    }

}
