package com.arialyy.frame.util.show;

import android.util.Log;

import com.arialyy.frame.util.CalendarUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lyy on 2015/4/1.
 * 写入文件的log，由于使用到反射和文件流的操作，建议在需要的地方才去使用
 */
public class FL {
    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static String PATH = "AriaFrame";    //log路径

    // 下面四个是默认tag的函数
    public static void i(Object obj, String msg) {
        if (isDebug) {
            String TAG = getTag(obj);
            Log.i(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void d(Object obj, String msg) {
        if (isDebug) {
            String TAG = getTag(obj);
            Log.d(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void e(Object obj, String msg) {
        if (isDebug) {
            String TAG = getTag(obj);
            Log.e(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void v(Object obj, String msg) {
        if (isDebug) {
            String TAG = getTag(obj);
            Log.v(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }


    public static void i(String TAG, String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    /**
     * 获取类名
     */
    private static String getTag(Object object) {
        Class<?> cls = object.getClass();
        String tag = cls.getName();
        String arrays[] = tag.split("\\.");
        tag = arrays[arrays.length - 1];
        return tag;
    }

    /**
     * 返回日志路径
     */
    public static String getLogPath() {
        return android.os.Environment.getExternalStorageDirectory().getPath() + File.separator + PATH + ".log";
    }

    /**
     * 把日志记录到文件
     */
    private static int writeLogToFile(String tag, String message) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(CalendarUtils.getNowDataTime());
        stringBuffer.append("    ");
        stringBuffer.append(tag);
        stringBuffer.append("    ");
        stringBuffer.append(message);
        stringBuffer.append("\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(getLogPath(), true));
            writer.append(stringBuffer);
        } catch (Exception e) {
            //            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return 0;
    }

    /**
     * 设置打印的异常格式
     */
    public static String getPrintException(Throwable ex) {
        StringBuilder err = new StringBuilder();
        err.append("ExceptionDetailed:\n");
        err.append("====================Exception Info====================\n");
        err.append(ex.toString());
        err.append("\n");
        StackTraceElement[] stack = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : stack) {
            err.append(stackTraceElement.toString()).append("\n");
        }
        Throwable cause = ex.getCause();
        if (cause != null) {
            err.append("【Caused by】: ");
            err.append(cause.toString());
            err.append("\n");
            StackTraceElement[] stackTrace = cause.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                err.append(stackTraceElement.toString()).append("\n");
            }
        }
        err.append("===================================================");
        return err.toString();
    }

    /**
     * 打印Map
     */
    public static String getMapString(Map map) {
        Set set = map.keySet();
        if (set.size() < 1) {
            return "[]";
        }
        StringBuilder strBuilder = new StringBuilder();
        Object[] array = set.toArray();
        strBuilder.append("[").append(array[0]).append("=").append(map.get(array[0]));
        for (int i = 1; i < array.length; ++i) {
            strBuilder.append(", ");
            strBuilder.append(array[i]).append("=");
            strBuilder.append(map.get(array[i]));
        }
        strBuilder.append("]");
        return strBuilder.toString();
    }

}
