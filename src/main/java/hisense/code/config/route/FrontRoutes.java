package hisense.code.config.route;

import hisense.code.controller.login.LoginController;
import com.jfinal.config.Routes;

/**
 * 前端路由
 *
 * @author durunguo
 */
public class FrontRoutes extends Routes {

    @Override
    public void config() {
        // 首页
        add("/", LoginController.class);
    }

}
