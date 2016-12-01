package com.yumingchuan.rsqmonthcalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by love on 2015/4/29.
 */
public class WeekScheduleFragment extends Fragment {

    @BindView(R.id.lv_monthSchedule)
    public ListView lv_monthSchedule;



    private Context mContext;

    public ArrayList<ScheduleToDo> totalTodo = new ArrayList<ScheduleToDo>();

    private MonthScheduleAdapter monthScheduleAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_week_schedule, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initListener();
        initData();

        return view;
    }

    private void initView(View view) {
        monthScheduleAdapter = new MonthScheduleAdapter(mContext, totalTodo);
        lv_monthSchedule.setAdapter(monthScheduleAdapter);
    }


    private void initListener() {
        lv_monthSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initData() {

    }


    public void addAndRefreshData(List<ScheduleToDo> temoTodos) {

        totalTodo.clear();
        totalTodo.addAll(temoTodos);
        monthScheduleAdapter.notifyDataSetChanged();
    }

}