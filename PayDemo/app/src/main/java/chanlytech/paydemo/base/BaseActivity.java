package chanlytech.paydemo.base;

import android.os.Bundle;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;


/**
 * Created by Lyy on 2015/7/6.
 * 基础的Activity
 */
public abstract class BaseActivity<M extends BaseModule> extends AbsActivity<M> {
//    @InjectView(R.id.back)
//    ImageView mBack;
    @Override
    protected void init(Bundle savedInstanceState) {

    }

    /**
     * 注意，使用该方法，需要判断点击的控件id
     * @param view
     */
    public void onClick(View view){
//        if (view.getId() == R.id.back){
//            finish();
//        }
    }

    @Override
    public M initModule() {
        return null;
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
