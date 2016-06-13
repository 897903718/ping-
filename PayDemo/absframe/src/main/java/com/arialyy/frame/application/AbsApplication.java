package com.arialyy.frame.application;

import com.arialyy.frame.sqlite.SQLiteDbHelper;
import com.arialyy.frame.sqlite.TableInfo;
import com.arialyy.frame.util.Injector;

import org.litepal.LitePalApplication;

import java.util.HashMap;

/**
 * Created by AriaLyy on 2015/2/2.
 */
public class AbsApplication extends LitePalApplication{
    private AppManager mAppManager;

    private Injector mInjector;
    private boolean sqlDebug = true;
    private static AbsApplication mApp;
    private static SQLiteDbHelper mDbHelper;
    /**
     * 数据库里面所有表的信息
     */
    private HashMap<String, TableInfo> mTablesInfo;

    /**
     * 程序崩溃问题异常处理
     */
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    public static AbsApplication getInstance() {
        return mApp == null ? new AbsApplication() : mApp;
    }

    /**
     * 获取Activity管理者
     *
     * @return
     */
    public AppManager getAppManager() {
        if (mAppManager == null) {
            mAppManager = AppManager.getAppManager();
        }
        return mAppManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 注册App异常崩溃处理器
        if (mUncaughtExceptionHandler == null) {
            mUncaughtExceptionHandler = CrashHandler.getInstance(this);
        }
//        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
        mApp = this;
    }

    /**
     * 获取数据库帮助类
     *
     */
    public static synchronized SQLiteDbHelper getDbHelper() {
        return mDbHelper;
    }

    /**
     * 获取数据库里面所有表的信息
     *
     */
    public HashMap<String, TableInfo> getTablesInfo() {
        return mTablesInfo;
    }


    public boolean isSqlDebug() {
        return sqlDebug;
    }

//    /**
//     * 创建数据库
//     */
//    private synchronized void initDb() {
//        try {
//            ApplicationInfo appInfo = this.getPackageManager()
//                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            if (appInfo.metaData == null || appInfo.metaData.isEmpty()) {
//                return;
//            }
//            String dbName = appInfo.metaData.getString("DATABASE");
//            int version = appInfo.metaData.getInt("VERSION", -1);
//            sqlDebug = appInfo.metaData.getBoolean("DEBUG");
//            mTablesInfo = new HashMap<>();
//            if (!TextUtils.isEmpty(dbName) && version != -1) {
//                mDbHelper = new SQLiteDbHelper(getApplicationContext(), dbName, version);
//                SQLiteDatabase db = mDbHelper.getWritableDatabase();
//                for (String tableName : DbUtil.getAllTableName(db)) {
//                    TableInfo tableInfo = new TableInfo();
//                    tableInfo.setExist(true);
//                    mTablesInfo.put(tableName, tableInfo);
//                }
//                db.close();
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取注入器
     *
     * @return
     */
    @Deprecated
    public Injector getInjector() {
        if (mInjector == null) {
            mInjector = Injector.getInstance();
        }
        return mInjector;
    }

    @Deprecated
    public void setInjector(Injector injector) {
        this.mInjector = injector;
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行,如果为true则为后台运行
     */
    public void exitApp(Boolean isBackground) {
        mAppManager.AppExit(this, isBackground);
    }

}
