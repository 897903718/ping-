package chanlytech.paydemo.model;

import android.content.Context;
import android.util.Log;

import com.arialyy.frame.http.HttpUtil;

import chanlytech.paydemo.activity.MainActivity;
import chanlytech.paydemo.base.BaseModule;
import chanlytech.paydemo.http.ServerUtil;

/**
 * Created by Lyy on 2016/5/26.
 */
public class HttpMoldel extends BaseModule {
    public HttpMoldel(Context context) {
        super(context);
    }

    public void getData(String url){
        ServerUtil.getChanger(url,new HttpUtil.Response(){
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                callback(MainActivity.NOHTTP_WHAT_TEST,data);
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                Log.i("error",error.toString());
            }
        });
    }
}
