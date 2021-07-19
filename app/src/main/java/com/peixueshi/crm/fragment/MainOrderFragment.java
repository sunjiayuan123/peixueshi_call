package com.peixueshi.crm.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.ShowSearchOrder;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.MyViewPagerAdapterOrder;

import butterknife.BindView;

/**
 * 作者:zhaobaolei
 * 描述:
 */
public class MainOrderFragment extends BaseFragment {

    @BindView(R.id.viewpager_order)
    ViewPager viewpager_order;
    @BindView(R.id.tabLayout_order)
    TabLayout tabLayout_order;
    @BindView(R.id.iv_search_order)
    ImageView iv_search_order;
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
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_find);
    }

    MyViewPagerAdapterOrder adapter;
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        adapter=new MyViewPagerAdapterOrder(manager);


        OneFragmentOrder oneFragment1 = OneFragmentOrder.newInstance("未支付订单",true);
        OneFragmentOrder oneFragment2 = OneFragmentOrder.newInstance("已支付订单",false);

        adapter.addFragment(oneFragment1,"未支付订单");
        adapter.addFragment(oneFragment2,"已支付订单");
        // adapter.addFragment(OneFragment.newInstance("制作方式"),"制作方式");
        viewpager_order.setAdapter(adapter);
        tabLayout_order.setTabMode(TabLayout.MODE_FIXED);
        tabLayout_order.addTab(tabLayout_order.newTab().setText("未支付订单"));
        tabLayout_order.addTab(tabLayout_order.newTab().setText("已支付订单"));
        // tabLayout.addTab(tabLayout.newTab().setText("制作方式"));
        tabLayout_order.setupWithViewPager(viewpager_order);

        iv_search_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowSearchOrder.class);
                startActivity(intent);
            }
        });

       new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                tabLayout_order.getTabAt(0).select();
            }

        }.execute();

        //adapter.getItem(0);
//        viewpager_order.setOffscreenPageLimit(2);
      //  adapter.notifyDataSetChanged();

        tabLayout_order.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选择时触发
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout_order.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                //  title.setTextSize(18);
                title.setAlpha(0.9f);
                title.setTextAppearance(getActivity(), R.style.TabLayoutTextStyle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选择是触发
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout_order.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                //  title.setTextSize(14);
                title.setAlpha(0.5f);
                title.setTextAppearance(getActivity(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //选中之后再次点击即复选时触发
             /*   if (tab.getText().equals("产品分类")) {
                    mPopupWindow.showAsDropDown(mTabLayout);
                }*/
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
