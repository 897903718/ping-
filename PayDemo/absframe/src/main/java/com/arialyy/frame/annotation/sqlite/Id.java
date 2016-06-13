package com.arialyy.frame.annotation.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by AriaLyy on 2015/2/14.
 * 如果没有设置column,则会自动设置默认id字段为主键名
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    public String column() default "";
}
