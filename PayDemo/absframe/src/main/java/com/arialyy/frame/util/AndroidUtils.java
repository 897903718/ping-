package com.arialyy.frame.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.arialyy.frame.util.show.FL;
import com.arialyy.frame.util.show.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * android系统工具类
 */
public class AndroidUtils {
    private static final String TAG = "AndroidUtils";

    private AndroidUtils() {

    }

    /**
     * 获取设备的唯一ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 返回版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 返回版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 打开一个隐藏了图标的APP<br/>
     *
     * @param context
     */
    public static void openAppWithAction(Context context, String packageName, String activity) {
        ComponentName componentName = new ComponentName(packageName, activity);
        try {
            Intent intent = new Intent();
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            FL.e(TAG, "没有找到应用程序:packageName:" + packageName + "  activity:" + activity);

        }
    }

    /**
     * 应用是否被安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstall(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            //            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 安装APP
     *
     * @param file
     */
    public static void install(Context context, File file) {
        FL.e(TAG, "install Apk:" + file.getName());
        context.startActivity(getInstallIntent(file));
    }

    /**
     * 获取安装应用的Intent
     *
     * @param file
     * @return
     */
    public static Intent getInstallIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 拷贝Assets中的文件到指定目录
     *
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean copyFileFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = FileUtils.createFile(path);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     * 获取版本信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pkg = null;
        if (context == null) {
            return null;
        }
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkg;
    }

    /**
     * 获取设备的显示属性
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取电话号码
     *
     * @param context
     * @return
     */
    public static String getLocalPhoneNumber(Context context) {
        String line1Number = getTelephonyManager(context).getLine1Number();
        return line1Number == null ? "" : line1Number;
    }

    /**
     * 获取设备型号(Nexus5)
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取电话通讯管理（可以通过这个对象获取手机号码等）
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取版本号
     */
    public static String getSystemVersion() {
        // 获取android版本号
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 返回ApplicationInfo（可以通过这个读取meta-data等等）
     */
    public static ApplicationInfo getApplicationInfo(Context context) {
        if (context == null) {
            return null;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationInfo;
    }

    /**
     * 获取MetaData的Bundle
     */
    public static Bundle getMetaData(Context context) {
        ApplicationInfo applicationInfo = getApplicationInfo(context);
        if (applicationInfo == null) {
            return new Bundle();
        }
        return applicationInfo.metaData;
    }

    /**
     * 应用是否启动
     */
    public static boolean appIsRunning(Context context) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        String packageName = getPackageInfo(context).packageName;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                //find it, break
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 检查系统是否有这个Intent，在启动Intent的时候需要检查，因为启动一个没有的Intent程序会Crash
     */
    public static boolean isIntentSafe(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }

    /**
     * 获取android系统缓存文件夹路径
     */
    public static String getDiskCacheDirName(Context context) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        File cacheDirPath = getExternalCacheDir(context);
        if (cacheDirPath == null) {
            cacheDirPath = context.getCacheDir();
        }
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? cacheDirPath.getPath() : context.getCacheDir().getPath();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        return !AndroidVersionUtil.hasGingerbread() || Environment.isExternalStorageRemovable();
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (AndroidVersionUtil.hasFroyo()) {
            File cacheDir;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                // /sdcard/Android/data/<application package>/cache
                if(context == null){
                    NullPointerException ex = new NullPointerException("context == null");
                    FL.e(TAG, FL.getPrintException(ex));
                    throw ex;
                }
                cacheDir = context.getExternalCacheDir();
            } else {
                // /data/data/<application package>/cache
                cacheDir = context.getCacheDir();
            }
            return cacheDir;
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (AndroidVersionUtil.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
