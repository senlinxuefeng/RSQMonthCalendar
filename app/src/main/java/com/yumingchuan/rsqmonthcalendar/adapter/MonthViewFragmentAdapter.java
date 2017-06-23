package com.yumingchuan.rsqmonthcalendar.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yumingchuan.rsqmonthcalendar.ui.MonthViewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static android.media.CamcorderProfile.get;

/**
 * Created by yumingchuan on 2017/6/15.
 * 无限循环viewpager adapter
 */

public class MonthViewFragmentAdapter extends FragmentStatePagerAdapter {


    private Stack stack = new Stack();

    public Stack getStack() {
        return stack;
    }

    private List<Fragment> fragmentList;
    private Fragment mCurrentFragment;

    public MonthViewFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        fragment = MonthViewFragment.newInstance(position);
        stack.add(fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        stack.remove(object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }


    @Override
    public int getCount() {
        return 48;
    }


}
