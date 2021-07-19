package com.peixueshi.crm.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.mvp.IView;
import com.mf.library.utils.ToastUtils;
import com.peixueshi.crm.R;

/**
 * 基类
 */
public abstract class MyBaseArmActivity extends BaseActivity implements IView {
    public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.white),0);
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void showToast(String content) {
        ToastUtils.showShort(content);
    }


    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    protected void logD(String msg) {
        Log.d("123",msg);
    }



    protected void setToolbarTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

//    protected void hideToolbarLeftImage() {
//        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
//        if (ivBack != null) {
//            DisPlayUtils.setViewGone(ivBack);
//        }
//        View ivBack2 = findViewById(R.id.toolbar_back);
//        if (ivBack2 != null) {
//            DisPlayUtils.setViewGone(ivBack);
//        }
//    }

    protected void setToolbarRightText(String content, View.OnClickListener onClickListener) {
        TextView tvRight = (TextView) findViewById(R.id.rightTitle);
        if (tvRight != null) {
            tvRight.setTextColor(getResources().getColor(R.color.font_6882fd));
            tvRight.setText(content);
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setOnClickListener(onClickListener);
        }
    }

    protected void setToolbarRightConfirmText(String content, View.OnClickListener onClickListener) {
        TextView tvRight = (TextView) findViewById(R.id.rightConfirm);
        if (tvRight != null) {
            tvRight.setText(content);
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setOnClickListener(onClickListener);
        }
    }

    /**
     * 隐藏键盘
     * @param view
     */
    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

}
