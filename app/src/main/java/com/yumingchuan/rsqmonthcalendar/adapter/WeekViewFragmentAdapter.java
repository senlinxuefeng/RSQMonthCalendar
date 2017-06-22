package com.yumingchuan.rsqmonthcalendar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class WeekViewFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager fm;


    public WeekViewFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }


    /**
     * 只需要实现下面两个方法即可。
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
