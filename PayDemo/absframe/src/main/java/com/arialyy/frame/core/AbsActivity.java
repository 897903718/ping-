package com.arialyy.frame.core;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.arialyy.frame.application.AbsApplication;
import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.util.StringUtil;

import butterknife.ButterKnife;

/**
 * Created by AriaLyy on 2015/2/2.
 *
 * @param <M> Module类型
 */
public abstract class AbsActivity<M extends AbsModule> extends AppCompatActivity {
    protected static String TAG = "";
    private long mFirstClickTime = 0;
    protected int mLayoutId = -1;
    private M mModule;
    private DialogTask mDialog;
    private boolean useBindData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyApplication().getAppManager().addActivity(this);
        mModule = initModule();
        initActivity();
        init(savedInstanceState);
    }

    /**
     * 进行一些初始化操作
     */
    protected abstract void init(Bundle savedInstanceState);





    /**
     * 必须使用该方法设置布局
     *
     * @return
     */
    public abstract int setContentView();

    /**
     * 初始化module
     */
    public abstract M initModule();

    private void initActivity() {
        TAG = StringUtil.getClassName(this);
        //加载布局
        mLayoutId = setContentView();
        if (!useBindData) {
            setContentView(mLayoutId);
            ButterKnife.inject(this);
        }
        //设置Model代理
        IOCProxy.newInstance(this, mModule);
    }

    public AbsApplication getMyApplication() {
        return AbsApplication.getInstance();
    }

    /**
     * 获取module
     *
     * @return
     */
    public M getModule() {
        return mModule;
    }

//    /**
//     * 初始化注入器
//     */
//    private void initInjector() {
//        getMyApplication().getInjector().injectResource(this);
////        getMyApplication().getInjector().inject(this);
//    }

    @Override
    public void finish() {
        getMyApplication().getAppManager().removeActivity(this);
        super.finish();
    }

    /**
     * 双击退出
     */
    private boolean onDoubleClickExit(long timeSpace) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mFirstClickTime > timeSpace) {
            doubleExitCallBack();
            mFirstClickTime = currentTimeMillis;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 双击退出，间隔时间为2000ms
     *
     * @return
     */
    public boolean onDoubleClickExit() {
        return onDoubleClickExit(2000);
    }

    /**
     * 双击退出不成功的回调。 第一次点击后回调，直到第二次点击的时间超过了给定时间，每一个回合回调一次
     */
    public void doubleExitCallBack() {
        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行,如果为true则为后台运行
     */
    public void exitApp(Boolean isBackground) {
        getMyApplication().exitApp(isBackground);
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        getMyApplication().exitApp(false);
    }

    /**
     * 显示加载对话框
     *
     * @param text 对话框加载信息，如果不显示，设置为空
     */
    protected void showLoadDialog(CharSequence text) {
        mDialog = new DialogTask(text);
        mDialog.execute("");
    }

    /**
     * 停止显示加载对话框
     */
    @Deprecated
    protected void stopLoadDialog() {
        if (mDialog != null) {
            mDialog.onPostExecute(DialogTask.DISMISS);
        }
    }

    /**
     * 停止显示加载对话框
     */
    protected void dismissLoadDialog() {
        if (mDialog != null) {
            mDialog.onPostExecute(DialogTask.DISMISS);
        }
    }

    /**
     * 统一的回调接口
     *
     * @param result 返回码，用来判断是哪个接口进行回调
     * @param data   回调数据
     */
    protected abstract void dataCallback(int result, Object data);

    /**
     * 加载对话框
     */
    private class DialogTask extends AsyncTask<CharSequence, Integer, Integer> {
        private CharSequence text;
        ProgressDialog dialog;
        public static final int DISMISS = 1;

        public DialogTask(CharSequence text) {
            this.text = text;
        }

        public void setText(CharSequence text) {
            this.text = text;
        }

        @Override
        protected Integer doInBackground(CharSequence... params) {
            dialog = ProgressDialog.newInstance(text);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "progress");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);
            if (dialog != null) {
                dialog.setText(text);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer != null && integer == DISMISS) {
                dialog.dismiss();
            }
        }
    }



}
