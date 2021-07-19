package com.peixueshi.crm.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.fragment.SearchOrderResult;
import com.peixueshi.crm.ui.adapter.PopPhoneHistoryAdapter;
import com.peixueshi.crm.utils.ActivityUtils;

import butterknife.BindView;

public class ShowSearchOrder extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.et_phoe_click)
    EditText et_phoe_click;
    @BindView(R.id.tv_search)
    TextView tv_search;

    SearchOrderResult orderFramelayout = new SearchOrderResult();
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopPhoneHistoryAdapter popPhoneHistoryAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.show_search_order;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        orderFramelayout.setView(et_phoe_click,tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderFramelayout.initData();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                orderFramelayout, R.id.frame_search_order);

    }



}
