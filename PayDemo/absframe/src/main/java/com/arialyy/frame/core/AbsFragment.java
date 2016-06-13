package com.arialyy.frame.core;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.FL;

import butterknife.ButterKnife;

/**
 * Created by AriaLyy on 2015/2/5.
 * 抽象的Fragment
 */
public abstract class AbsFragment<M extends AbsModule> extends Fragment {
    protected static String TAG = "";
    protected int mLayoutId = -1;
    private M mModule;
    /**
     * fragment需要加载的view
     */
    protected View mView;
    private DialogTask mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(mLayoutId, container, false);
        ButterKnife.inject(this, mView);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

    private void initFragment() {
        mModule = initModule();
        mLayoutId = setContentView();
        IOCProxy.newInstance(this, mModule);
        TAG = StringUtil.getClassName(this);
    }


    /**
     * 进行一些初始化操作
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 必须使用该方法设置布局
     */
    public abstract int setContentView();

    /**
     * 初始化module
     */
    public abstract M initModule();

    public M getModule() {
        return mModule;
    }


    /**
     * 设置加载对话框文字
     */
    protected void setLoadDialogText(CharSequence text) {
        if (mDialog != null) {
            mDialog.setText(text);
            mDialog.onProgressUpdate(0);
        } else {
            FL.e(TAG, "加载对话框为空，请初始化加载对话框再进行操作");
        }
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
            dialog.show(getActivity().getSupportFragmentManager(), "progress");
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
