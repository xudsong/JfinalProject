package hisense.code.controller;

import hisense.code.config.constant.field.Constant;
import com.jfinal.core.Controller;

/**
 * api接口继承controller
 * 其中定义了Get,Post,Put,Delete,Options
 * Created by zhanghaichao on 2018/4/27.
 */
public class ApiController extends Controller {

    protected void get(){
        renderJson(Constant.method404);
    }
    protected void post(){
        renderJson(Constant.method404);
    }
    protected void delete(){
        renderJson(Constant.method404);
    }
    protected void put(){
        renderJson(Constant.method404);
    }

    public void index(){
        String method = getRequest().getMethod();
        if(Constant.POST.equalsIgnoreCase(method)) {
            post();
        } else if(Constant.GET.equalsIgnoreCase(method)){
            get();
        }else if(Constant.DELETE.equalsIgnoreCase(method)){
           delete();
        }else if(Constant.PUT.equalsIgnoreCase(method)){
            put();
        }else{
            renderJson(Constant.method404);
        }
    }

}
