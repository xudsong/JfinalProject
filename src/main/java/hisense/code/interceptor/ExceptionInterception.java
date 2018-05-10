package hisense.code.interceptor;

import hisense.code.config.constant.field.Constant;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.apache.log4j.Logger;

public class ExceptionInterception implements Interceptor {
    Logger logg = Logger.getLogger(ExceptionInterception.class);

    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        try {
            ai.invoke();
        } catch (Exception e) {
            logg.error(e);
            controller.renderJson(Constant.serviceError);
        }

    }
}
