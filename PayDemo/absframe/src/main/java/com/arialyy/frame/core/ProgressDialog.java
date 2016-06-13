package com.arialyy.frame.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arialyy.frame.R;


/**
 * Created by Lyy on 2015/3/17.
 * 进度条对话框
 */
public class ProgressDialog extends DialogFragment {

    private CharSequence mHint;
    private TextView mText;

    public static ProgressDialog newInstance(CharSequence text){
        ProgressDialog dialog = new ProgressDialog();
        Bundle b = new Bundle();
        b.putCharSequence("text", text);
        dialog.setArguments(b);
        return dialog;
    }

    public ProgressDialog(){
        setStyle(DialogFragment.STYLE_NO_TITLE | DialogFragment.STYLE_NO_FRAME, R.style.MyDialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHint = getArguments().getCharSequence("text");
    }

    public void setText(CharSequence text){
        if(!TextUtils.isEmpty(text)){
            mHint = text;
            if (mText != null){
                mText.setVisibility(View.VISIBLE);
                mText.setText(mHint);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.dialog_progress, container, false);
        if(!TextUtils.isEmpty(mHint)){
            mText = (TextView) mView.findViewById(R.id.text);
            mText.setVisibility(View.VISIBLE);
            mText.setText(mHint);
        }
        return mView;
    }

}
