package chanlytech.paydemo.base;

import android.content.Context;

import com.arialyy.frame.module.AbsModule;


/**
 * Created by Lyy on 2015/7/6.
 * 基础的Module类
 */
public class BaseModule extends AbsModule {

    public BaseModule(Context context) {
        super(context);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {

//        if (AppManager.getUser() != null) {
//            AppManager.saveUser(AppManager.getUser());
//            callback(ResultCode.GET_USER_INFO, AppManager.getUser());
//        }else {
//            UserEntity newuserEntity = new UserEntity();
//            newuserEntity.setUserId("0");
//            AppManager.saveUser(newuserEntity);
//            callback(ResultCode.GET_USER_INFO, newuserEntity);
//        }

    }

    /**
     * 定位
     */
    public void location() {
//        if (AppManager.getCity() != null){
//
//        }
//        CityEntity cityEntity = new CityEntity();
//        AppManager.saveCity(cityEntity);
//        callback(ResultCode.GET_CITY_INFO, cityEntity);
    }
}
