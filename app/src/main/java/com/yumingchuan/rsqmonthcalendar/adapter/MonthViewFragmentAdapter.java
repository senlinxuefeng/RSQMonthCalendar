package com.yumingchuan.rsqmonthcalendar.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yumingchuan.rsqmonthcalendar.ui.MonthViewFragment;

import java.util.List;
import java.util.Stack;

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

//    private Stack<Fragment> stack = new Stack<Fragment>();

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
//        Fragment fragment;
//        if (stack.isEmpty()){
//            fragment = MonthViewFragment.newInstance(position);
//        }else {
//             fragment = stack.pop();
//        }
//        return fragment;
//        return MonthViewFragment.newInstance(position);


//        Fragment page;
//        if (fragmentList.size() > position) {
//            page = fragmentList.get(position);
//            if (page != null) {
//                return page;
//            }
//        }
//
//        while (position >= fragmentList.size()) {
//            fragmentList.add(null);
//        }
//        page = MonthViewFragment.newInstance(position);
//        fragmentList.set(position, page);
//
//        return page;

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
//        Fragment fragment = (Fragment) object;
//        stack.add(fragment);

//    }
}
