package com.yumingchuan.rsqmonthcalendar.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by yumingchuan on 2017/6/15.
 * 无限循环viewpager adapter
 */

public class MonthViewFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public MonthViewFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 48;
    }


}
