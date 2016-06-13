package chanlytech.paydemo.base;

import android.content.Context;



/**
 * Created by Lyy on 2015/7/6.
 */
public class AppManager {
    private static Context mContext;

    public static void newInstance(Context context) {
        new AppManager(context);
    }

    private AppManager(Context context) {
        mContext = context;
    }


}
