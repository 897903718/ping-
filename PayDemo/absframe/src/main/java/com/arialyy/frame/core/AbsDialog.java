package com.arialyy.frame.core;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.util.Injector;
import com.arialyy.frame.util.StringUtil;

import butterknife.ButterKnife;

/**
 * Created by AriaLyy on 2015/2/6.
 * Dialog的module的回调函数是回调寄主的函数
 *
 * 当对话框只是需要做一些简单的操作时（如：消息的确认，列表的点击）,可以直接使用框架提供的DialogSimpleModule.
 * 该module的使用和其他module的使用方法一致,获取module的地方是使用getSimpleModule
 * 但是如果需要使用该module回调数据，寄住需要实现onDialog(Bundle b)方法.
 *
 */
public abstract class AbsDialog<M extends AbsModule> extends Dialog {
    public static final int ENTER = 0;    //确认标志位
    public static final int CANCEL = 1;    //取消标志位

    protected static String TAG = "";
    protected int mLayoutId = -1;
    private M mModule;
    private Object mObj;
    private DialogSimpleModule mSimpleModule;


    /**
     * @param context
     * @param obj     被观察的对象
     */
    public AbsDialog(Context context, Object obj) {
        super(context);
        mObj = obj;
    }

    /**
     * @param context
     * @param theme   Dialog风格
     * @param obj     被观察的对象
     */
    public AbsDialog(Context context, int theme, Object obj) {
        super(context, theme);
        mObj = obj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    private void initDialog() {
        mLayoutId = setDialogView();
        setContentView(mLayoutId);
        ButterKnife.inject(this);
        TAG = StringUtil.getClassName(this);
        mModule = initModule();
        //这里设置的被代理者是Dialog的寄主
        if(mModule == null ){
            mSimpleModule = new DialogSimpleModule(getContext());
            IOCProxy.newInstance(mObj, mSimpleModule);
        }else{
            IOCProxy.newInstance(mObj, mModule);
        }
        init();
    }

    /**
     * 获取自定义的module
     * @return
     */
    public M getModule() {
        return mModule;
    }

    /**
     * 获取默认的module
     * @return
     */
    public DialogSimpleModule getSimpleModule(){
        return mSimpleModule;
    }


    /**
     * 子类在这里进行初始化操作
     */
    protected abstract void init();

    /**
     * 设置Dialog布局
     *
     * @return
     */
    protected abstract int setDialogView();

    /**
     * 初始化module
     */
    protected abstract M initModule();

    /**
     * 统一的回调接口
     * @param result  返回码，用来判断是哪个接口进行回调
     * @param data     回调数据
     */
    protected abstract void dataCallback(int result, Object data);

}
