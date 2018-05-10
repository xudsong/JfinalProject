package hisense.code.interceptor.annotation;

import java.lang.annotation.*;

/**
 * 权限控制注解
 * Created by zhanghaichao on 2018/4/25.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AuthCode {
    String[] code();
}