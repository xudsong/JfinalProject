package hisense.code.config.handler;

import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * Created by zhanghaichao on 2018/5/2.
 */
public class WebSocketHandler extends Handler {

    private Pattern filterUrlRegxPattern;

    public WebSocketHandler(String filterUrlRegx) {
        if (StrKit.isBlank(filterUrlRegx))
            throw new IllegalArgumentException("The para filterUrlRegx can not be blank.");
        filterUrlRegxPattern = Pattern.compile(filterUrlRegx);
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (filterUrlRegxPattern.matcher(target).find()) {
            return;
        }else
            next.handle(target, request, response, isHandled);

    }


}
