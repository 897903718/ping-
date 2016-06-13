package chanlytech.paydemo;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

import chanlytech.paydemo.http.ServerUtil;

/**
 * Created by Lyy on 2016/5/26.
 */
public class MyAppLoction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.init(this);
        ServerUtil.newInstance(getApplicationContext());//初始化网络请求
    }
}
