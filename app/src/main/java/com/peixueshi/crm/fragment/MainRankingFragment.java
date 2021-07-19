package com.peixueshi.crm.fragment;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
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
import com.peixueshi.crm.service.CommonModel;
import com.peixueshi.crm.ui.adapter.MyViewPagerAdapter;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 作者:zhaobaolei
 * 描述:
 */
public class MainRankingFragment extends BaseFragment {

    @BindView(R.id.viewpager_ranking)
    ViewPager vp;
    @BindView(R.id.tabLayout_ranking)
    TabLayout tabLayout;

    @Inject
    CommonModel commonModel;

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
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_ranking);
    }


    /**

     * 通过反射机制 修改TabLayout 的下划线长度

     */

    public void setIndicator (TabLayout tabs,int leftDip,int rightDip) {
        //通过反射获取到
         Class tabLayout = tabs.getClass();
        Field tabStrip =null;
        try {
                    tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
        }catch (NoSuchFieldException e) {
                    e.printStackTrace();
        }
        //设置模式
         tabStrip.setAccessible(true);
         //获得tabview
        LinearLayout llTab =null;
        try {
                    llTab = (LinearLayout) tabStrip.get(tabs);
        }catch (IllegalAccessException e) {

        e.printStackTrace();

        }
        //设置tabView的padding为0，并且设置了margin
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        for (int i =0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
             params.leftMargin = left;
             params.rightMargin = right;
             child.setLayoutParams(params);
             child.invalidate();
        }
    }



    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        /*tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout,20,20);
            }
        });*/
        //设置ToolBar的title颜色
        //toolbar.setTitleTextColor(Color.WHITE);
        //getActivity().setSupportActionBar(toolbar);
        adapter=new MyViewPagerAdapter(getActivity().getSupportFragmentManager());

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);// 此处可换为具体某一时间
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

            adapter.addFragment(OneFragmentRanking.newInstance("今日",true,1),"今日");
            adapter.addFragment(OneFragmentRanking.newInstance("本周",true,weekDay),"本周");
            adapter.addFragment(OneFragmentRanking.newInstance("本月",true,monthDay),"本月");

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
