package com.yumingchuan.rsqmonthcalendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class VPStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager fm;


    public VPStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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

}
