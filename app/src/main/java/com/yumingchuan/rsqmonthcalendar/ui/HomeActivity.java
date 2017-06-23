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
            if (i >= 22 && i <= 26) {
                monthViewFragments.add(i, MonthViewFragment.newInstance(i));
            } else {
                monthViewFragments.add(null);
            }

        }

        monthViewFragmentAdapter = new MonthViewFragmentAdapter(getSupportFragmentManager(), monthViewFragments);
        vp_monthView.setCurrentItem(2);
        vp_monthView.setAdapter(monthViewFragmentAdapter);
        vp_monthView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    MonthViewFragment monthFragment = (MonthViewFragment) monthViewFragmentAdapter.getItem(vp_monthView.getCurrentItem());

                    if (monthFragment.isAdded()) {

                        date.setText(TimestampTool.sdf_all.format(monthFragment.getCalendar().getTime()));
                    }

                    monthFragment.parseData();


                }
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
