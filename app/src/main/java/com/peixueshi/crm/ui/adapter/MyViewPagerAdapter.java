package com.peixueshi.crm.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaobaolei on 2020/4/11.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList=new ArrayList<>();
    private Context mContext;
    private List<String> mTitles=new ArrayList<String>();

    public int currentPosition = 0;
    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    //得到对应position的Fragment
    @Override
    public Fragment getItem(int position) {
        currentPosition = position;
        return mList.get(position);
    }

    @Override
    public int getCount() {
        //返回Fragment的数量
        return mList.size();
    }

    /**
     *
     * @param fragment 添加Fragment
     * @param fragmentTitle Fragment的标题，即TabLayout中对应Tab的标题
     */
    public void addFragment(Fragment fragment,String fragmentTitle)
    {
        mList.add(fragment);
        mTitles.add(fragmentTitle);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //得到对应position的Fragment的title
        currentPosition = position;
        return mTitles.get(position);
    }
}

