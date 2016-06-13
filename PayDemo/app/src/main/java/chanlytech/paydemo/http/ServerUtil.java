package chanlytech.paydemo.http;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by Lyy on 2015/8/31.
 */
public class ServerUtil {
    private static HttpUtil mHttpUtil;
    private static Context mContext;

    public static void newInstance(Context context) {
        new ServerUtil(context);
    }

    private ServerUtil(Context context) {
        mHttpUtil = new HttpUtil(context);
        mContext = context;
    }

    /**
     * 是否请求成功
     *
     * @param jsonObject
     * @return
     */
    public static boolean isRequestScuess(JSONObject jsonObject) {
        try {
            return jsonObject.getString("status").equals("1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 组合通用参数
     *
     * @param map
     * @return
     */
//    public static Map<String, String> mixParams(Map<String, String> map) {
//        Map<String, String> params = new HashMap<>();
//        Set set = map.entrySet();
//        for (Object aSet : set) {
//            Map.Entry entry = (Map.Entry) aSet;
//            params.put(entry.getKey() + "", entry.getValue() + "");
//        }
//        String randomCode = Encryption.getRandomCode();
//        //添加通用参数
//        params.put("timestamp", randomCode);
//        params.put("sign", Encryption.getICityCode(randomCode));
//        params.put("userId", AppManager.getUser().getUserId());
//        params.put("version", AndroidUtils.getVersionName(mContext) + "");
//        params.put("versionCode", AndroidUtils.getVersionCode(mContext) + "");
//        params.put("device", "0");
//        params.put("cityId", AppManager.getUser().getCityId());
//        return params;
//    }
//
    public static void requestData(String url,HttpUtil.Response response) {
        mHttpUtil.get(url, null, response);
    }
//
//    public static void requestDataforCache(String url, Map<String, String> params, HttpUtil.Response response) {
//        mHttpUtil.postFromCanche(ServerConfig.BASE_URL + url, mixParams(params), response);
//    }

    /**
     * 获取首页数据
     */
    public static void getChanger(String url, HttpUtil.Response response) {
        requestData(url,response);
    }

    /**
     *
     * */
}
