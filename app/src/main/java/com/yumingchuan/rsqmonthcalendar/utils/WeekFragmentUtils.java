package com.yumingchuan.rsqmonthcalendar.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.yumingchuan.rsqmonthcalendar.adapter.WeekViewFragmentAdapter;
import com.yumingchuan.rsqmonthcalendar.ui.WeekViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yumingchuan on 2017/6/22.
 */

public class WeekFragmentUtils {


    private static WeekFragmentUtils weekFragmentUtils;
    private List<Fragment> weekFragments;
    private List<Fragment> tempFragments;
    private WeekViewFragmentAdapter weekViewFragmentAdapter;

    public static WeekFragmentUtils getInstance() {
        if (weekFragmentUtils == null) {//双重校验DCL单例模式
            synchronized (WeekFragmentUtils.class) {//同步代码块
                if (weekFragmentUtils == null) {
                    weekFragmentUtils = new WeekFragmentUtils();
                }
            }
        }
        return weekFragmentUtils;
    }

    /**
     * @return 获取WeekFragment实例
     */
    public List<Fragment> getWeekFragmentInstance() {
        if (weekFragments == null || weekFragments.size() == 0) {
            weekFragments = new ArrayList<>();
            tempFragments = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                weekFragments.add(WeekViewFragment.newInstance());
            }
            tempFragments.addAll(weekFragments);
        }
        return weekFragments;
    }

    /**
     * @return 获取WeekViewFragmentAdapter实例
     */
    public WeekViewFragmentAdapter getWeekViewFragmentAdapterInstance(FragmentManager fm) {
        if (weekViewFragmentAdapter == null){
            weekViewFragmentAdapter = new WeekViewFragmentAdapter(fm, getWeekFragmentInstance());
        }
        return weekViewFragmentAdapter;
    }

    public List<Fragment> getTempFragments() {
        return tempFragments;
    }
}
