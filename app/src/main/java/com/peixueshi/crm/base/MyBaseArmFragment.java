package com.peixueshi.crm.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.IView;
import com.mf.library.utils.ToastUtils;

/**
 * 基类
 */
public abstract class MyBaseArmFragment extends BaseFragment implements IView {
    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void showToast(String content) {
        ToastUtils.showShort(content);

    }


    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }

    protected void logD(String msg) {
        Log.d("123", msg);
    }


}
