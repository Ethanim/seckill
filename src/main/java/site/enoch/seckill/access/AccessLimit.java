package site.enoch.seckill.access;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 限流方法注解
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
	int seconds(); //限制秒数
	int maxCount(); //限制次数，限制多少秒内访问多少次
	boolean needLogin() default true; //是否需要登录
}
