package com.arialyy.frame.util;

import android.app.Activity;

import java.lang.reflect.Field;

import com.arialyy.frame.annotation.InjectResource;
import com.arialyy.frame.annotation.InjectView;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by AriaLyy on 2015/2/2.
 * View控件，事件的注解类，注解的原理：通过注解获取需要注解的资源id和和类型，通过反射获取对象，通过field来使用对象的方法
 */
@Deprecated
public class Injector {
    private static Injector instance;

    public enum Method {
        Click, LongClick, ItemClick, itemLongClick
    }

    private Injector() {

    }

    /**
     * 获取注解工具
     *
     * @return
     */
    public static Injector getInstance() {
        if (instance == null) {
            instance = new Injector();
        }
        return instance;
    }

//    private void inject(Object obj, Field field) {
//        try {
//            field.setAccessible(true);
//            field.set(obj, field.getType().newInstance());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void injectView(Object injectedSource, View view, Field field) {
        //返回指定类型的注解
        InjectView viewInject = field.getAnnotation(InjectView.class);
        try {
            if (viewInject != null) {
                int viewId = viewInject.id();
                //setAccessible(true)可以访问private域
                field.setAccessible(true);
                field.set(injectedSource, view.findViewById(viewId));
                if (!TextUtils.isEmpty(viewInject.click()))
                    setListener(injectedSource, field, viewInject.click(), Method.Click);
                if (!TextUtils.isEmpty(viewInject.longClick()))
                    setListener(injectedSource, field, viewInject.longClick(), Method.LongClick);
                if (!TextUtils.isEmpty(viewInject.itemClick()))
                    setListener(injectedSource, field, viewInject.itemClick(), Method.ItemClick);
                if (!TextUtils.isEmpty(viewInject.itemLongClick()))
                    setListener(injectedSource, field, viewInject.itemLongClick(), Method.itemLongClick);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置事件
     *
     * @param injectedSource
     * @param field
     * @param methodName
     * @param method
     * @throws IllegalAccessException
     */
    private void setListener(Object injectedSource, Field field, String methodName, Method method) throws IllegalAccessException {
        if (methodName == null || methodName.trim().length() == 0)
            return;
        Object obj = field.get(injectedSource);
        switch (method) {
            case Click:
                if (obj instanceof View) {
                    ((View) obj).setOnClickListener(new EventListener(injectedSource).click(methodName));
                }
                break;
            case ItemClick:
                if (obj instanceof AbsListView) {
                    ((AbsListView) obj).setOnItemClickListener(new EventListener(injectedSource).itemClick(methodName));
                }
                break;
            case LongClick:
                if (obj instanceof View) {
                    ((View) obj).setOnLongClickListener(new EventListener(injectedSource).longClick(methodName));
                }
                break;
            case itemLongClick:
                if (obj instanceof AbsListView) {
                    ((AbsListView) obj).setOnItemLongClickListener(new EventListener(injectedSource).itemLongClick(methodName));
                }
                break;
            default:
                break;
        }
    }


    private void injectResource(Activity activity, Field field) {
        if (field.isAnnotationPresent(InjectResource.class)) {
            InjectResource resourceJect = field
                    .getAnnotation(InjectResource.class);
            int resourceID = resourceJect.id();
            try {
                field.setAccessible(true);
                Resources resources = activity.getResources();
                String type = resources.getResourceTypeName(resourceID);
                if (type.equalsIgnoreCase("string")) {
                    field.set(activity,
                            activity.getResources().getString(resourceID));
                } else if (type.equalsIgnoreCase("drawable")) {
                    field.set(activity,
                            activity.getResources().getDrawable(resourceID));
                } else if (type.equalsIgnoreCase("layout")) {
                    field.set(activity,
                            activity.getResources().getLayout(resourceID));
                } else if (type.equalsIgnoreCase("array")) {
                    if (field.getType().equals(int[].class)) {
                        field.set(activity, activity.getResources()
                                .getIntArray(resourceID));
                    } else if (field.getType().equals(String[].class)) {
                        field.set(activity, activity.getResources()
                                .getStringArray(resourceID));
                    } else {
                        field.set(activity, activity.getResources()
                                .getStringArray(resourceID));
                    }

                } else if (type.equalsIgnoreCase("color")) {
                    if (field.getType().equals(Integer.TYPE)) {
                        field.set(activity,
                                activity.getResources().getColor(resourceID));
                    } else {
                        field.set(activity, activity.getResources()
                                .getColorStateList(resourceID));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注解控件和事件
     *
     * @param injectedSource 被注解对象
     * @param sourceView     需要注解的视图View
     */
    public void injectView(Object injectedSource, View sourceView) {
        Field[] fields = injectedSource.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(InjectView.class)) {
                    injectView(injectedSource, sourceView, field);
                }
            }
        }
    }

    /**
     * 注解资源文件
     *
     * @param activity
     */
    public void injectResource(Activity activity) {
        Field[] fields = activity.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(InjectResource.class)) {
                    injectResource(activity, field);
                }
            }
        }
    }

//    /**
//     * 注解配置
//     *
//     * @param activity
//     */
//    public void inject(Object obj) {
//        Field[] fields = obj.getClass().getDeclaredFields();
//        if (fields != null && fields.length > 0) {
//            for (Field field : fields) {
//                if (field.isAnnotationPresent(Inject.class)) {
//                    inject(obj, field);
//                }
//            }
//        }
//    }

}
