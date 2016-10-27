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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by love on 2015/4/29.
 */
public class WeekScheduleFragment extends Fragment {

    @BindView(R.id.lv_monthSchedule)
    ListView lv_monthSchedule;

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
        initListener();
        initData();

        return view;
    }

    private void initView(View view) {
        //ScheduleAdapter scheduleAdapter = new ScheduleAdapter(mContext, totalTodo);
        //lv_monthSchedule.setAdapter(scheduleAdapter);
    }


    private void initListener() {
        lv_monthSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (totalTodo.size() >= position && position != 0) {
//                    Intent intent = new Intent(mContext, ScheduleDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("scheduleId", totalTodo.get(position - 1).id);
//                    bundle.putSerializable("scheduleTodo", totalTodo.get(position - 1));
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {

    }

}