package com.arialyy.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Wsx on 2015/7/20.
 * sharedpreference 数据存储工具类
 */
public class SharedPreferData {
    private static SharedPreferences mSharedPreferences;

    /**存入String型的数据格式
     * Context 上下文对象
     * KEY 存入的建
     * value 存入的值
     */
    public static void writeStringdata(Context context, String KEY, String value) {
        mSharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY, value);
        editor.commit();
    }

    /**读取String 型的数据
     *Context 上下文对象
     * KEY 存入的建（根据存入的建来取值）
     * 返回存入的值
     * */
    public static String readString(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        String value = mSharedPreferences.getString(key, "");
        return value;
    }
}
