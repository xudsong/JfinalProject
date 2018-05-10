package hisense.code.config.constant.field;

/**
 * 通用字段
 * Created by zhanghaichao on 2018/4/27.
 */
public class Constant {

    /**
     * 网络请求方式
     */
    public static String POST = "post";
    public static String GET = "get";
    public static String PUT = "put";
    public static String DELETE = "delete";
    public static String PATH = "path";

    /**
     * Content-Type
     */
    public static String ContentTypeJson = "application/json";
    public static String ContentTypeXml = "application/xml";
    public static String ContentTypeForm = "application/x-www-form-urlencoded";


    public static String serviceErr = "系统错误";

    public static String method404 = "{\"result\":false,\"code\":\"404\",\"success\":false,\"message\":\"该方法未被实现\"}";
    public static String noLogin = "{\"result\":false,\"code\":\"401\",\"success\":false,\"message\":\"用户未登录\"}";
    public static String noAuth = "{\"result\":false,\"code\":\"403\",\"success\":false,\"message\":\"用户权限不足\"}";
    public static String serviceError = "{\"result\":false,\"code\":\"500\",\"success\":false,\"message\":\"系统错误\"}";
}
