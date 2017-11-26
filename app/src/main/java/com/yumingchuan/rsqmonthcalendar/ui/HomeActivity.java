package com.yumingchuan.rsqmonthcalendar.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yumingchuan.rsqmonthcalendar.R;
import com.yumingchuan.rsqmonthcalendar.adapter.MonthViewFragmentAdapter;
import com.yumingchuan.rsqmonthcalendar.listener.MyOnPageChangeListener;
import com.yumingchuan.rsqmonthcalendar.utils.TimestampTool;
import com.yumingchuan.rsqmonthcalendar.view.CustomViewPager;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.vp_monthView)
    CustomViewPager vp_monthView;


    private MonthViewFragmentAdapter monthViewFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        monthViewFragmentAdapter = new MonthViewFragmentAdapter(getSupportFragmentManager());
        vp_monthView.setCustomViewPagerParam(1, true, monthViewFragmentAdapter, monthViewOnPageChangeListener, 24);

    }

    MyOnPageChangeListener monthViewOnPageChangeListener = new MyOnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(TimestampTool.sdf_all.parse(TimestampTool.getCurrentDateToWeb()));
                calendar.add(Calendar.MONTH, position - 24);
                String yMp = TimestampTool.sdf_yMp.format(calendar.getTime());

//                if (!RSQApplication.getInstance().getCurrentSelectDate().contains(yMp)) {
//                    setCurrentDateState(yMp + "-01 00:00:00");
//                }
//
//                if (getMonthViewFragment() != null && getMonthViewFragment().getCustomMonthView() != null) {
//                    getMonthViewFragment().getCustomMonthView().renderMonthCalendarBackground();
//                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}
