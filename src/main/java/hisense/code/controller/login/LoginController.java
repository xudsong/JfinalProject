package hisense.code.controller.login;

import hisense.code.config.constant.field.Constant;
import hisense.code.controller.ApiController;
import hisense.code.model.TUser;
import hisense.code.mq.base.ActiveMQ;
import hisense.code.mq.base.JmsBaseSender;
import hisense.code.service.sys.UserService;
import com.jfinal.core.ActionKey;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.List;

public class LoginController extends ApiController {
    Logger logger = Logger.getLogger(LoginController.class);

    static UserService service = new UserService();




    public void activeMqSend() {
        JmsBaseSender sq1 = ActiveMQ.getSender("testSender1", "RestFul");
        TextMessage msg = null;
        try {
            msg = sq1.getSession().createTextMessage("测试" + new Date());
            sq1.sendMessage(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        renderJson("111111111111");
    }

    public void restful() {
        renderJson("111111111111");
    }

    @ActionKey("/user/delete")
    public void getOneUser() {
        String id = getPara("id");
        renderJson(id);
    }


    public void testAction() {

        String data = HttpKit.readData(getRequest());
        TUser tUser = FastJson.getJson().parse(data, TUser.class);
//        TUser tUser = getModel(TUser.class);
        renderJson(tUser);
    }


    //根据传入的 Id 来实现对一个对象的获取
    @ActionKey("/getUser/:id")
    public void getUserInfo() {
        String username = service.getOne();
        renderJson(username);
    }


    //获取对象集合的方法 返回类型为List<T>

    public void getUserList() {
        List<Record> userList = service.getList();
        renderJson(userList);

    }

    //分页查询
    public void getUserByPage() {
        int pg = 1;
        Page<TUser> userList = service.getListByPage(pg);
        renderJson(userList);

    }

    //分页查询 ( Mysql 分离式 sql ) 方式1：
    //需要两个参数 1.当前页 2.每页显示条数
    public void getUserListByPage() {
        Page<Record> userList = service.getUserListByPage(1, 4);
        renderJson(userList);
    }

    //分页查询 ( Mysql 分离式 sql ) 方式2：自己写 sql
    //需要两个参数 1.当前页 2.每页显示条数
    public void getUserListByPageAnother() {
        List<Record> userList = service.getUserListByPageAnother(1, 4);
        renderJson(userList);

    }


    //添加方法
    public void postUser() {
        //从页面获取user的属性
        TUser user = getModel(TUser.class);
        Boolean flag = service.postUser(user);
        if (flag) {
            //save()方法放返回一个布尔值，如果添加成功返回true，
            // 在这里我们添加成功的话就显示成功,失败的话就渲染一个纯文本页面，renderText
            //redirect("/user/");
            renderText("插入成功");
        } else {
            renderText("Sorry,有异常，插入失败");
        }

    }

    //删除方法(根据 id 删除)
    @ActionKey("/deleteUser/:id")
    public void deleteUser() {
        int id = 3;
        Boolean flag = service.deleteUser(id);
        if (flag) {
            //redirect("/user/");
            renderText("删除成功");
        } else {
            renderText("Sorry,有异常，删除失败");
        }

    }

    //修改方法
    @ActionKey(value = "/putUser/:id")
    public void putUser() {
        int flag = service.putUser();
        if (1 == flag) {
            //redirect("/user/");
            renderText("修改成功");
        } else {
            renderText("Sorry,有异常，修改失败");
        }

    }


    //高级用法测试
    public void find() {
        List<Record> userList = service.findTest();
        renderJson(userList);

    }

    public void testRestFul() {

        String method = getRequest().getMethod();

        if (Constant.GET.equals(method)) {

        }else if(Constant.POST.equals(method)){

        }else{
            renderJson("");
        }

    }

}
