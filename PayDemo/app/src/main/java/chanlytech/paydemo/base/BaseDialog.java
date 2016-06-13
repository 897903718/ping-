package chanlytech.paydemo.base;

import android.content.Context;

import com.arialyy.frame.core.AbsDialog;

/**
 * Created by Lyy on 2015/7/6.
 */
public abstract class BaseDialog<M extends BaseModule> extends AbsDialog<M> {
    public BaseDialog(Context context, Object obj) {
        super(context, obj);
    }

    public BaseDialog(Context context, int theme, Object obj) {
        super(context, theme, obj);
    }

    @Override
    protected void init() {

    }

    @Override
    protected M initModule() {
        return null;
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
