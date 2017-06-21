package com.yumingchuan.rsqmonthcalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yumingchuan.rsqmonthcalendar.R;
import com.yumingchuan.rsqmonthcalendar.adapter.MonthViewFragmentAdapter;
import com.yumingchuan.rsqmonthcalendar.utils.TimestampTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.vp_monthView)
    ViewPager vp_monthView;

    @BindView(R.id.date)
    TextView date;

    private List<Fragment> monthViewFragments;
    private MonthViewFragmentAdapter monthViewFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

       // vp_monthView = (ViewPager) findViewById(R.id.vp_monthView);

        monthViewFragments = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            monthViewFragments.add(MonthViewFragment.newInstance(i));
        }

        monthViewFragmentAdapter = new MonthViewFragmentAdapter(getSupportFragmentManager(), monthViewFragments);
        vp_monthView.setAdapter(monthViewFragmentAdapter);
        vp_monthView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                MonthViewFragment monthFragment = (MonthViewFragment) monthViewFragments.get(position);

                monthFragment.getCalendar();

                if (monthFragment.isAdded()) {
                    //monthFragment.setWeekFragments();
                    date.setText(TimestampTool.sdf_all.format(monthFragment.getCalendar().getTime()));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_monthView.setCurrentItem(24);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        //        MonthViewFragment monthFragment = (MonthViewFragment) monthViewFragmentAdapter.getItem(vp_monthView.getCurrentItem() % 5);
        //        monthEditAreaWidth = monthFragment.getMonthCalendarArea().getWidth();
        //        monthEditAreaHeight = monthFragment.getMonthCalendarArea().getHeight();



    }
}
