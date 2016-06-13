package com.arialyy.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by AriaLyy on 2015/2/2.
 * 控件注解器
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
    /** View的ID */
    public int id() default -1;

    /** View的单击事件 */
    public String click() default "";

    /** View的长按键事件 */
    public String longClick() default "";

    /** View的焦点改变事件 */
    public String focuschange() default "";

    /** View的手机键盘事件 */
    public String key() default "";

    /** View的触摸事件 */
    public String touch() default "";
    /** ListView 的Item点击事件*/
    public String itemClick() default "";
    /** ListView 的Item长按事件*/
    public String itemLongClick() default "";
}
