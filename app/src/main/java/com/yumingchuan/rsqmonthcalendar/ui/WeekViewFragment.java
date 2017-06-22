package com.yumingchuan.rsqmonthcalendar.ui;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yumingchuan.rsqmonthcalendar.R;
import com.yumingchuan.rsqmonthcalendar.adapter.MonthScheduleAdapter;
import com.yumingchuan.rsqmonthcalendar.base.BaseFragment;
import com.yumingchuan.rsqmonthcalendar.bean.ScheduleToDo;
import com.yumingchuan.rsqmonthcalendar.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by love on 2015/4/29.
 */
public class WeekViewFragment extends BaseFragment {

    @BindView(R.id.lv_monthSchedule)
    public ListView lv_monthSchedule;

    @BindView(R.id.tv_createSchedule)
    TextView tv_createSchedule;

    public List<ScheduleToDo> totalTodo;

    private MonthScheduleAdapter monthScheduleAdapter;


    public static WeekViewFragment newInstance() {
        return new WeekViewFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_week_schedule;
    }


    @Override
    protected void initView() {
        super.initView();
        initAdapter();
    }

    /**
     * 初始化adapter的信息
     */
    private void initAdapter() {
        totalTodo = new ArrayList<ScheduleToDo>();
        monthScheduleAdapter = new MonthScheduleAdapter(mContext, totalTodo);
        lv_monthSchedule.setAdapter(monthScheduleAdapter);
    }

    public void addAndRefreshData(List<ScheduleToDo> tempTodos) {
        totalTodo.clear();
        totalTodo.addAll(tempTodos);
        tv_createSchedule.setVisibility(totalTodo.size() != 0 ? View.GONE : View.VISIBLE);
        lv_monthSchedule.setVisibility(totalTodo.size() != 0 ? View.VISIBLE : View.GONE);
        monthScheduleAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_createSchedule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_createSchedule:
                //((HomeActivity) mContext).addContent();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy", "onDestroy");
        totalTodo = null;
    }
}