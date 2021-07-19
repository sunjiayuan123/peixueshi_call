package com.peixueshi.crm.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.MyViewPagerAdapter;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * 作者:zhaobaolei
 * 描述:
 */
public class MainRankingFragmentRight extends BaseFragment {

    @BindView(R.id.viewpager_ranking_right)
    ViewPager vp;
    @BindView(R.id.tabLayout_ranking_right)
    TabLayout tabLayout;

     MyViewPagerAdapter adapter;
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
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_ranking_right);
    }



    @Override
    public void initData(@Nullable Bundle savedInstanceState) {


        //设置ToolBar的title颜色
        //toolbar.setTitleTextColor(Color.WHITE);
        //getActivity().setSupportActionBar(toolbar);
        adapter=new MyViewPagerAdapter(getActivity().getSupportFragmentManager());

           /* adapter.addFragment(OneFragmentRanking.newInstance("今日",false,1),"今日");
            adapter.addFragment(OneFragmentRanking.newInstance("7天",false,7),"7天");
            adapter.addFragment(OneFragmentRanking.newInstance("30天",false,30),"30天");

            tabLayout.addTab(tabLayout.newTab().setText("今日"));
            tabLayout.addTab(tabLayout.newTab().setText("7天"));
            tabLayout.addTab(tabLayout.newTab().setText("30天"));
*/
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);// 此处可换为具体某一时间
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

        adapter.addFragment(OneFragmentRanking.newInstance("今日",false,1),"今日");
        adapter.addFragment(OneFragmentRanking.newInstance("本周",false,weekDay),"本周");
        adapter.addFragment(OneFragmentRanking.newInstance("本月",false,monthDay),"本月");

        tabLayout.addTab(tabLayout.newTab().setText("今日"));
        tabLayout.addTab(tabLayout.newTab().setText("本周"));
        tabLayout.addTab(tabLayout.newTab().setText("本月"));
            vp.setOffscreenPageLimit(4);


        TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
       // title.setTextSize(18);
        title.setAlpha(0.9f);
        title.setTextAppearance(getActivity(), R.style.TabLayoutTextStyle);

        vp.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vp);

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
                tabLayout.getTabAt(0).select();
            }

        }.execute();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选择时触发
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
              //  title.setTextSize(18);
                title.setAlpha(0.9f);
                title.setTextAppearance(getActivity(), R.style.TabLayoutTextStyle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选择是触发
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
              //  title.setTextSize(14);
                title.setAlpha(0.5f);
                title.setTextAppearance(getActivity(),Typeface.NORMAL);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setData(@Nullable Object data) {

    }



}
