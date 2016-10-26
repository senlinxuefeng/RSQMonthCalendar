package com.yumingchuan.rsqmonthcalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by love on 2015/4/29.
 */
public class WeekScheduleFragment extends Fragment {

//    @BindView(R.id.iv_scrollStartLocation)
//    TextView iv_scrollStartLocation;

    private Context mContext;
    private ArrayList<ScheduleData.Todo> totalTodo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalTodo = new ArrayList<ScheduleData.Todo>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_week_schedule, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initLitenser();
        initData();

        return view;
    }

    private void initView(View view) {

    }

    private void initData() {

    }

    private void initLitenser() {

    }
}