package com.peixueshi.crm.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.utils.ActivityUtils;

import butterknife.BindView;

/**
 * 作者:zhaobaolei
 * 描述:
 */
public class RankingFragment extends BaseFragment implements View.OnClickListener {

    MainRankingFragment mainHomeRanking = new MainRankingFragment();
    MainRankingFragmentRight mainHomeRanXiaozu = new MainRankingFragmentRight();
    @BindView(R.id.rank_main)
    FrameLayout rank_main;
    @BindView(R.id.tv_zhaosheng)
    TextView tv_zhaosheng;
    @BindView(R.id.tv_xiaozu)
    TextView tv_xiaozu;
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ArmsUtils.inflate(getActivity(), R.layout.frame_rank);
    }



    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tv_zhaosheng.setOnClickListener(this);
        tv_xiaozu.setOnClickListener(this);


        ActivityUtils.addOrShowFragmentToActivity(getChildFragmentManager(), mainHomeRanking, R.id.rank_main);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setData(@Nullable Object data) {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_zhaosheng:
                tv_zhaosheng.setBackgroundResource(R.drawable.shape_rank_select_left);
                tv_xiaozu.setBackgroundResource(R.drawable.shape_rank_common_right);
                tv_zhaosheng.setTextColor(getActivity().getResources().getColor(R.color.white));
                tv_xiaozu.setTextColor(getActivity().getResources().getColor(R.color.home_top_blue));
             ActivityUtils.addOrShowFragmentToActivity(getChildFragmentManager(),
                        mainHomeRanking, R.id.rank_main);

                break;
            case R.id.tv_xiaozu:
                tv_xiaozu.setBackgroundResource(R.drawable.shape_rank_select_right);
                tv_zhaosheng.setBackgroundResource(R.drawable.shape_rank_common_left);
                tv_xiaozu.setTextColor(getActivity().getResources().getColor(R.color.white));
                tv_zhaosheng.setTextColor(getActivity().getResources().getColor(R.color.home_top_blue));
                ActivityUtils.addOrShowFragmentToActivity(getChildFragmentManager(), mainHomeRanXiaozu, R.id.rank_main);
                break;
        }
    }
}
