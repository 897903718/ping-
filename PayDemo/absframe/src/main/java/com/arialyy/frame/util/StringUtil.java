package com.arialyy.frame.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;


/**
 * 字符串工具类
 *
 * @author Administrator
 */
public class StringUtil {

    /**
     * 用AES算法解密加密的密码
     *
     * @param seed     密钥
     * @param password 加密的密码
     * @return 解密后的密码, 默认返回""
     */
    public static String decryptPassword(String seed, String password) {
        try {
            String password1 = AESEncryption.decrypt(seed, password);
            return password1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从XML读取字符串
     *
     * @param context
     * @param id      字符串id
     * @return
     */
    public static String getStringFromXML(Context context, int id) {
        return context.getResources().getString(id);
    }

    /**
     * 从xml读取字符串数组
     *
     * @param context
     * @param id
     * @return
     */
    public static String[] getStringArrayFromXML(Context context, int id) {
        return context.getResources().getStringArray(id);
    }

    /**
     * 将字符串数组转换为list
     *
     * @param strArray
     * @return
     */
    public static List<String> stringArray2List(String[] strArray) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < strArray.length; i++) {
            list.add(strArray[i]);
        }
        return list;
    }

    /**
     * 高亮整段字符串
     */
    public static SpannableStringBuilder highLightStr(String str, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 高亮代码片段
     *
     * @param str          整段字符串
     * @param highLightStr 要高亮的代码段
     * @param color        高亮颜色
     * @return
     */
    public static SpannableStringBuilder highLightStr(String str, String highLightStr, int color) {
        int start = str.indexOf(highLightStr);
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        // new BackgroundColorSpan(Color.RED)背景高亮
        // ForegroundColorSpan(Color.RED) 字体高亮
        style.setSpan(new ForegroundColorSpan(color), start, start
                + highLightStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 字符串转dbouble
     *
     * @param str
     * @return
     */
    public static double strToDouble(String str) {
        // double d = Double.parseDouble(str);

		/* 以下代码处理精度问题 */
        BigDecimal bDeci = new BigDecimal(str);
        // BigDecimal chushu =new BigDecimal(100000000);
        // BigDecimal result =bDeci.divide(chushu,new
        // MathContext(4));//MathConText(4)表示结果精确4位！
        // return result.doubleValue() * 100000000;
        return bDeci.doubleValue();
    }

    /**
     * 将普通字符串转换为16位进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    /**
     * 把字符串长度加满16位
     *
     * @param str
     * @return 16位长度的字符串
     */
    public static String addStrLenTo16(String str) {
        //由于汉字的特殊性，长度要用byte来判断
        for (int i = str.getBytes().length; i < 16; i++) {
            str += '\0';
        }
        return str;
    }

    /**
     * 获取对象名
     * @param obj 对象
     * @return    对象名
     */
    public static String getClassName(Object obj){
        String arrays[] = obj.getClass().getName().split("\\.");
        return arrays[arrays.length - 1];
    }
}
