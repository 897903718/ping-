package com.arialyy.frame.core;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.util.StringUtil;

/**
 * Created by AriaLyy on 2015/5/1.
 */
public abstract class AbsAlertDialog<M extends AbsModule> extends DialogFragment{
    public static final String RESULT_DATA = "result_data";
    public static final int ENTER = 0;
    public static final int CANCEL = 1;
    protected static String TAG = "";
    private M mModule;
    private Object mObj;
    private DialogSimpleModule mSimpleModule;
    private Dialog mDialog;

    public AbsAlertDialog(Object obj){
        mObj = obj;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        TAG = StringUtil.getClassName(this);
        mModule = initModule();
        mDialog = initDialog();
        //这里设置的被代理者是Dialog的寄主
        if(mModule == null ){
            mSimpleModule = new DialogSimpleModule(getActivity());
            IOCProxy.newInstance(mObj, mSimpleModule);
        }else{
            IOCProxy.newInstance(mObj, mModule);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }

    /**
     * 创建AlertDialog
     * 建议使用AppCompatDialog，该Dialog具有5.0的效果
     */
    public abstract Dialog initDialog();

    /**
     * 获取自定义的module
     */
    public M getModule() {
        return mModule;
    }

    /**
     * 获取默认的module
     */
    public DialogSimpleModule getSimpleModule(){
        return mSimpleModule;
    }

    /**
     * 统一的回调接口
     * @param result  返回码，用来判断是哪个接口进行回调
     * @param data     回调数据
     */
    protected abstract void dataCallback(int result, Object data);

    /**
     * 初始化module
     */
    public abstract M initModule();

    protected <T extends View> T getView(View view, int id){
        T widget = (T) view.findViewById(id);
        if (widget == null)
            throw new IllegalArgumentException("view 0x"
                    + Integer.toHexString(id) + " doesn't exist");
        return widget;
    }

}
