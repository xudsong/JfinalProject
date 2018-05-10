package hisense.code.model.dto;

import hisense.code.config.constant.field.Constant;
import hisense.code.interceptor.BusinessException;

/**
 * 接口返回工具类
 * Created by zhanghaichao on 2018/4/27.
 */
public class ServiceResult<T> {

    /**
     * 返回给客户端的结果
     */
    private T result;

    /**
     * 返回给客户端的消息描述
     */
    private String message = "";

    /**
     * 返回给客户端的消息代码
     */
    private String code = "";

    /**
     * 执行成功标志
     */
    private boolean success = false;

    public ServiceResult() {
    }

    /**
     * ServiceResult的构造函数
     * @param result 结果
     */
    public ServiceResult(final T result) {
        this.result = result;
    }


    /**
     * 服务执行过程发生异常时设置错误信息。
     * <p>该方法会将 success设置为false（服务执行失败）</p>
     * @param e 异常
     */
    public void setError(final Exception e) {
        this.code = "500";
        this.success = false;
        if (e instanceof BusinessException) {
            this.message = e.getMessage();
        } else {
            //若不是BusinessException，则设置message为“系统错误”
            this.message = Constant.serviceErr;
        }
    }


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
