package com.arialyy.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

/**
 * Created by AriaLyy on 2015/2/2.
 * 资源文件注解器
 */
public @interface InjectResource {
    int id() default -1;
}
