package chanlytech.paydemo.base;

import android.os.Bundle;

import com.arialyy.frame.core.AbsFragment;

/**
 * Created by Lyy on 2015/7/6.
 */
public abstract class BaseFragment<M extends BaseModule> extends AbsFragment<M> {
    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public M initModule() {
        return null;
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
