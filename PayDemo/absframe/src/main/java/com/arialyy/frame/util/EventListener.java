package com.arialyy.frame.util;

import android.view.View;
import android.widget.AdapterView;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by AriaLyy on 2015/2/4.
 * 统一的事件类，利用反射调用指定的事件方法
 */
@Deprecated
public class EventListener implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {
    /**
     * 宿主类
     */
    private Object mObj;

    private String clickMethod;
    private String longClickMethod;
    private String itemClickMethod;
    private String itemSelectMethod;
    private String nothingSelectedMethod;
    private String itemLongClickMehtod;

    public EventListener(Object obj) {
        mObj = obj;
    }

    /**
     * 设置点击事件
     *
     * @param methodName 实现事件的方法名
     * @return 事件监听
     */
    public EventListener click(String methodName) {
        this.clickMethod = methodName;
        return this;
    }

    /**
     * 设置长按事件
     *
     * @param methodName
     * @return
     */
    public EventListener longClick(String methodName) {
        this.longClickMethod = methodName;
        return this;
    }

    /**
     * item事件
     *
     * @param methodName
     * @return
     */
    public EventListener itemClick(String methodName) {
        this.itemClickMethod = methodName;
        return this;
    }

    /**
     * 长按事件
     *
     * @param methodName
     * @return
     */
    public EventListener itemLongClick(String methodName) {
        this.itemLongClickMehtod = methodName;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mObj == null) {
            return;
        }
        try {
            Method method = null;
            method = mObj.getClass().getDeclaredMethod(clickMethod, View.class);
            if (method != null)
                method.invoke(mObj, v);
            else
                throw new RuntimeException("no such method:" + clickMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (RuntimeException viewException) {
            viewException.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mObj == null) return;
        try {
            Method method = mObj.getClass().getDeclaredMethod(itemClickMethod, AdapterView.class, View.class, int.class, long.class);
            if (method != null)
                method.invoke(mObj, parent, view, position, id);
            else
                throw new RuntimeException("no such method:" + itemClickMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mObj == null)
            throw new RuntimeException("invokeItemLongClickMethod: handler is null :");
        Method method = null;
        try {
            ///onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            method = mObj.getClass().getDeclaredMethod(itemLongClickMehtod, AdapterView.class, View.class, int.class, long.class);
            if (method != null) {
                Object obj = method.invoke(itemLongClickMehtod, AdapterView.class, View.class, int.class, long.class);
                return Boolean.valueOf(obj == null ? false : Boolean.valueOf(obj.toString()));
            } else
                throw new RuntimeException("no such method:" + itemLongClickMehtod);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onLongClick(View v) {
        if (mObj == null) return false;
        Method method = null;
        try {
            //public boolean onLongClick(View v)
            method = mObj.getClass().getDeclaredMethod(longClickMethod, View.class);
            if (method != null) {
                Object obj = method.invoke(longClickMethod, v);
                return obj == null ? false : Boolean.valueOf(obj.toString());
            } else
                throw new RuntimeException("no such method:" + longClickMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
