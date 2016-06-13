package com.arialyy.frame.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.util.Injector;
import com.arialyy.frame.util.StringUtil;

import butterknife.ButterKnife;

/**
 * Created by AriaLyy on 2015/3/8.
 * Fragment里面的Dialog,生命周期随着Fragment注销而被注销
 * 可以直接重载onCreateDialog创建AlertDialog或Dialog而不需要createView
 * http://blog.csdn.net/lmj623565791/article/details/37815413
 */
public abstract class AbsDialogFragment<M extends AbsModule> extends DialogFragment{
    public static final int ENTER = 0;    //确认标志位
    public static final int CANCEL = 1;    //取消标志位

    protected static String TAG = "";
    protected int mLayoutId = -1;
    private M mModule;
    private Object mObj;
    private DialogSimpleModule mSimpleModule;
    protected View mView;


    /**
     * @param obj     被观察的对象
     */
    public AbsDialogFragment(Object obj) {
        mObj = obj;
    }

    /**
     * @param style   DialogFragment.STYLE_NO_TITLE , STYLE_NO_FRAME; STYLE_NO_FRAME | STYLE_NO_TITLE
     * @param theme   Dialog风格
     * @param obj     被观察的对象
     */
    public AbsDialogFragment(int style, int theme, Object obj) {
        setStyle(style, theme);
        mObj = obj;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(mLayoutId, container, false);
        ButterKnife.inject(this, mView);
        init();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 初始化Dialog的信息
     */
    private void initDialog() {
        mLayoutId = setDialogView();
        TAG = StringUtil.getClassName(this);
        mModule = initModule();
        //这里设置的被代理者是Dialog的寄主
        if(mModule == null ){
            mSimpleModule = new DialogSimpleModule(getActivity());
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
