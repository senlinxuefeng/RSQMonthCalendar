package com.yumingchuan.rsqmonthcalendar;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ll_week)
    LinearLayout ll_week;
    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.ll_4)
    LinearLayout ll_4;
    @BindView(R.id.ll_5)
    LinearLayout ll_5;

    @BindView(R.id.ll_6)
    LinearLayout ll_6;

    @BindView(R.id.ll_monthCalendarArea)
    LinearLayout ll_monthCalendarArea;

    @BindView(R.id.ll_titleHeight)
    LinearLayout ll_titleHeight;


    @BindView(R.id.widgetCalendar_imgForeMonth)
    RelativeLayout widgetCalendar_imgForeMonth;

    @BindView(R.id.widgetCalendar_imgNextMonth)
    RelativeLayout widgetCalendar_imgNextMonth;


    @BindView(R.id.widgetCalendar_txtTitle)
    TextView widgetCalendar_txtTitle;

    @BindView(R.id.vp_weekSchedule)
    ViewPager vp_weekSchedule;


    private CurrentMonthInfo currentMonthInfo;
    private ViewGroup currentViewGroup;
    List<Fragment> fragments = new ArrayList<>();
    List<Fragment> tempFragments = new ArrayList<>();
    private VPStatePagerAdapter vpStatePagerAdapter;

    WeekScheduleFragment weekScheduleFragment0 = new WeekScheduleFragment();
    WeekScheduleFragment weekScheduleFragment1 = new WeekScheduleFragment();
    WeekScheduleFragment weekScheduleFragment2 = new WeekScheduleFragment();
    WeekScheduleFragment weekScheduleFragment3 = new WeekScheduleFragment();
    WeekScheduleFragment weekScheduleFragment4 = new WeekScheduleFragment();
    WeekScheduleFragment weekScheduleFragment5 = new WeekScheduleFragment();
    WeekScheduleFragment weekScheduleFragment6 = new WeekScheduleFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ViewTreeObserver vto = ll_monthCalendarArea.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_monthCalendarArea.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initLayout(calendar);
            }
        });

    }

    /**
     * 初始化布局
     */
    private void initLayout(Calendar someCalendar) {
        currentMonthInfo = new CurrentMonthInfo();
        currentMonthInfo.weeks = someCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        currentMonthInfo.monthEditAreaWidth = ll_monthCalendarArea.getWidth();
        currentMonthInfo.monthEditAreaHeight = ll_monthCalendarArea.getHeight();
        currentMonthInfo.weekEditAreaHeight = (int) currentMonthInfo.monthEditAreaHeight / currentMonthInfo.weeks;
        setEditAreaHeightAndWidth(ll_monthCalendarArea, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight * currentMonthInfo.weeks);


        setEditAreaHeightAndWidth(ll_1, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight);
        setEditAreaHeightAndWidth(ll_2, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight);
        setEditAreaHeightAndWidth(ll_3, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight);
        setEditAreaHeightAndWidth(ll_4, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight);
        setEditAreaHeightAndWidth(ll_5, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight);
        setEditAreaHeightAndWidth(ll_6, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weekEditAreaHeight);

        setEditAreaHeightAndWidth(ll_week, currentMonthInfo.monthEditAreaWidth, currentMonthInfo.weeks == 5 ? (currentMonthInfo.weekEditAreaHeight * 3) : (currentMonthInfo.weekEditAreaHeight * 4));

        currentMonthInfo.initY = ll_titleHeight.getHeight();


        showCalendar(someCalendar);

        //还原到初始化的布局
        translateView(ll_1, ll_1.getY(), (currentMonthInfo.weekEditAreaHeight * 0), 20);
        translateView(ll_2, ll_2.getY(), (currentMonthInfo.weekEditAreaHeight * 1), 20);
        translateView(ll_3, ll_3.getY(), (currentMonthInfo.weekEditAreaHeight * 2), 20);
        translateView(ll_4, ll_4.getY(), (currentMonthInfo.weekEditAreaHeight * 3), 20);
        translateView(ll_5, ll_5.getY(), (currentMonthInfo.weekEditAreaHeight * 4), 20);
        translateView(ll_6, ll_6.getY(), (currentMonthInfo.weekEditAreaHeight * 5), 20);

        //ll_week.setVisibility(View.GONE);

        tempFragments.add(weekScheduleFragment0);
        tempFragments.add(weekScheduleFragment1);
        tempFragments.add(weekScheduleFragment2);
        tempFragments.add(weekScheduleFragment3);
        tempFragments.add(weekScheduleFragment4);
        tempFragments.add(weekScheduleFragment5);
        tempFragments.add(weekScheduleFragment6);


        vpStatePagerAdapter = new VPStatePagerAdapter(getSupportFragmentManager(), fragments);
        vp_weekSchedule.setAdapter(vpStatePagerAdapter);

        vp_weekSchedule.setOffscreenPageLimit(7);

        vp_weekSchedule.setOnPageChangeListener(onPageChangeListener);

        initBackground();
    }

    private float getEditAreaTranslateHeight(int week) {
        return currentMonthInfo.weeks == 5 ? ((week + 1) == 5 ? (currentMonthInfo.initY + currentMonthInfo.weekEditAreaHeight * 2) : (currentMonthInfo.weekEditAreaHeight + currentMonthInfo.initY)) : ((week + 1) == 6 ? (currentMonthInfo.weekEditAreaHeight * 2 + currentMonthInfo.initY) : (currentMonthInfo.weekEditAreaHeight + currentMonthInfo.initY));
    }

    /**
     * 打开和关闭
     *
     * @param isExpand
     * @param is5Week
     * @param currentOpenWeek
     * @param transDistance51
     * @param transDistance52
     * @param transDistance53
     * @param transDistance54
     * @param transDistance55
     * @param transDistance61
     * @param transDistance62
     * @param transDistance63
     * @param transDistance64
     * @param transDistance65
     * @param transDistance66
     */
    private void openOrCloseWeek(boolean isExpand, boolean is5Week, int currentOpenWeek, int transDistance51, int transDistance52, int transDistance53
            , int transDistance54, int transDistance55, int transDistance61, int transDistance62, int transDistance63
            , int transDistance64, int transDistance65, int transDistance66) {
        if (isExpand) {
            if (is5Week) {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() + currentMonthInfo.weekEditAreaHeight * transDistance51), 200);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() + currentMonthInfo.weekEditAreaHeight * transDistance52), 200);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() + currentMonthInfo.weekEditAreaHeight * transDistance53), 200);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() + currentMonthInfo.weekEditAreaHeight * transDistance54), 200);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() + currentMonthInfo.weekEditAreaHeight * transDistance55), 200);

                ll_week.setVisibility(View.VISIBLE);
                translateView(ll_week, (int) ll_week.getY(), getEditAreaTranslateHeight(currentOpenWeek), 10);
            } else {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() + currentMonthInfo.weekEditAreaHeight * transDistance61), 200);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() + currentMonthInfo.weekEditAreaHeight * transDistance62), 200);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() + currentMonthInfo.weekEditAreaHeight * transDistance63), 200);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() + currentMonthInfo.weekEditAreaHeight * transDistance64), 200);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() + currentMonthInfo.weekEditAreaHeight * transDistance65), 200);
                translateView(ll_6, ll_6.getY(), (ll_6.getY() + currentMonthInfo.weekEditAreaHeight * transDistance66), 200);

                ll_week.setVisibility(View.VISIBLE);
                translateView(ll_week, (int) ll_week.getY(), getEditAreaTranslateHeight(currentOpenWeek), 10);
            }
        } else {
            if (is5Week) {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() - currentMonthInfo.weekEditAreaHeight * transDistance51), 200);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() - currentMonthInfo.weekEditAreaHeight * transDistance52), 200);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() - currentMonthInfo.weekEditAreaHeight * transDistance53), 200);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() - currentMonthInfo.weekEditAreaHeight * transDistance54), 200);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() - currentMonthInfo.weekEditAreaHeight * transDistance55), 200);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_week.setVisibility(View.INVISIBLE);
                    }
                }, 180);

            } else {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() - currentMonthInfo.weekEditAreaHeight * transDistance61), 200);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() - currentMonthInfo.weekEditAreaHeight * transDistance62), 200);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() - currentMonthInfo.weekEditAreaHeight * transDistance63), 200);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() - currentMonthInfo.weekEditAreaHeight * transDistance64), 200);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() - currentMonthInfo.weekEditAreaHeight * transDistance65), 200);
                translateView(ll_6, ll_6.getY(), (ll_6.getY() - currentMonthInfo.weekEditAreaHeight * transDistance66), 200);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_week.setVisibility(View.INVISIBLE);
                    }
                }, 180);

            }
        }

    }

    /**
     * 只是简单的关闭
     *
     * @param week
     */
    public void onlyCloseWeek(int week) {
        switch (week) {
            case 0:
                openOrCloseWeek(false, currentMonthInfo.weeks == 5, week, 0, 3, 3, 3, 3, 0, 4, 4, 4, 4, 4);
                break;
            case 1:
                openOrCloseWeek(false, currentMonthInfo.weeks == 5, week, -1, -1, 2, 2, 2, -1, -1, 3, 3, 3, 3);
                break;
            case 2:
                openOrCloseWeek(false, currentMonthInfo.weeks == 5, week, -2, -2, -2, 1, 1, -2, -2, -2, 2, 2, 2);
                break;
            case 3:
                openOrCloseWeek(false, currentMonthInfo.weeks == 5, week, -3, -3, -3, -3, 0, -3, -3, -3, -3, 1, 1);
                break;
            case 4:
                openOrCloseWeek(false, currentMonthInfo.weeks == 5, week, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, 0);
                break;
            case 5:
                openOrCloseWeek(false, currentMonthInfo.weeks == 5, week, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, -4);
                break;
        }
    }

    /**
     * 只是简单的打开
     *
     * @param week
     */
    public void onlyOpenWeek(int week) {
        switch (week) {
            case 0:
                openOrCloseWeek(true, currentMonthInfo.weeks == 5, week, 0, 3, 3, 3, 3, 0, 4, 4, 4, 4, 4);
                break;
            case 1:
                openOrCloseWeek(true, currentMonthInfo.weeks == 5, week, -1, -1, 2, 2, 2, -1, -1, 3, 3, 3, 3);
                break;
            case 2:
                openOrCloseWeek(true, currentMonthInfo.weeks == 5, week, -2, -2, -2, 1, 1, -2, -2, -2, 2, 2, 2);
                break;
            case 3:
                openOrCloseWeek(true, currentMonthInfo.weeks == 5, week, -3, -3, -3, -3, 0, -3, -3, -3, -3, 1, 1);
                break;
            case 4:
                openOrCloseWeek(true, currentMonthInfo.weeks == 5, week, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, 0);
                break;
            case 5:
                openOrCloseWeek(true, currentMonthInfo.weeks == 5, week, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, -4);
                break;
        }
    }


    Handler mHandler = new Handler();

    /**
     * 具体的移动方案
     *
     * @param currentOpenWeek
     * @param transDistance51
     * @param transDistance52
     * @param transDistance53
     * @param transDistance54
     * @param transDistance55
     * @param transDistance61
     * @param transDistance62
     * @param transDistance63
     * @param transDistance64
     * @param transDistance65
     * @param transDistance66
     */
    private void dealWeek(final int currentOpenWeek, final int transDistance51, final int transDistance52, final int transDistance53
            , final int transDistance54, final int transDistance55, final int transDistance61, final int transDistance62, final int transDistance63
            , final int transDistance64, final int transDistance65, final int transDistance66) {
        if (currentMonthInfo.currentOpenWeek == -1 && !currentMonthInfo.isExpand) {
            //关闭到打开
            openOrCloseWeek(!currentMonthInfo.isExpand, currentMonthInfo.weeks == 5, currentOpenWeek, transDistance51, transDistance52, transDistance53, transDistance54, transDistance55,
                    transDistance61, transDistance62, transDistance63, transDistance64, transDistance65, transDistance66);
            currentMonthInfo.isExpand = true;
            currentMonthInfo.currentOpenWeek = currentOpenWeek;
        } else {
            if (currentMonthInfo.currentOpenWeek == currentOpenWeek) {
                //打开到关闭
                openOrCloseWeek(!currentMonthInfo.isExpand, currentMonthInfo.weeks == 5, currentOpenWeek, transDistance51, transDistance52, transDistance53, transDistance54, transDistance55,
                        transDistance61, transDistance62, transDistance63, transDistance64, transDistance65, transDistance66);
                currentMonthInfo.isExpand = false;
                currentMonthInfo.currentOpenWeek = -1;
            } else {
                //先关闭
                onlyCloseWeek(currentMonthInfo.currentOpenWeek);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onlyOpenWeek(currentOpenWeek);
                        currentMonthInfo.isExpand = true;
                        currentMonthInfo.currentOpenWeek = currentOpenWeek;
                    }
                }, 300);
            }
        }
    }


    /**
     * 点击事件的处理
     *
     * @param view
     */
    @OnClick({R.id.ll_week, R.id.widgetCalendar_imgForeMonth, R.id.widgetCalendar_imgNextMonth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_week:
                break;

            case R.id.widgetCalendar_imgForeMonth:
                calendar.add(Calendar.MONTH, -1);
                initLayout(calendar);
                break;
            case R.id.widgetCalendar_imgNextMonth:
                calendar.add(Calendar.MONTH, 1);
                initLayout(calendar);
                break;
        }
    }


    private long startClickTime = 0;

    /**
     * 判断是否可以移动
     *
     * @return
     */
    private boolean isCanTranslate() {
        for (int i = 0; i < objectAnimatorList.size(); i++) {
            if (objectAnimatorList.get(i).isRunning() || System.currentTimeMillis() - startClickTime < 380) {
                return false;
            }
        }
        return true;
    }

    List<ObjectAnimator> objectAnimatorList = new ArrayList<ObjectAnimator>();

    /**
     * 平移属性动画
     *
     * @param view
     * @param from
     * @param to
     */
    public void translateView(View view, float from, float to, int animTime) {
        ObjectAnimator translationUp = ObjectAnimator.ofFloat(view, "Y", from, to);
        translationUp.setInterpolator(new DecelerateInterpolator());
        translationUp.setDuration(animTime);
        translationUp.start();
        objectAnimatorList.add(translationUp);
    }

    /**
     * 设置高和宽
     *
     * @param viewGroup
     * @param screenWidth
     * @param screenHeight
     */
    private void setEditAreaHeightAndWidth(ViewGroup viewGroup, float screenWidth, float screenHeight) {
        ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams(); //取控件textView当前的布局参数
        layoutParams.width = (int) screenWidth;
        layoutParams.height = (int) screenHeight;
        viewGroup.setLayoutParams(layoutParams);
    }


    ////////////////////////////////////日历部分////////////////////////////////////////////////////
    private static final int MAX_DAY_COUNT = 42;//最大格子数量
    private DayInfo[] dayInfos = new DayInfo[MAX_DAY_COUNT];//每月应该有的天数，36为最大格子数
    private final Calendar calendar = Calendar.getInstance();
    private int needNum = 0;
    private boolean isNeed = true;
    private List<DayInfo> listDayInfos = new ArrayList<DayInfo>();
    private String currentYearAndMonth;


    /**
     * 日期信息实体类
     **/
    public class DayInfo {
        public int position;
        public int day;
        public DayType dayType;
        public int daysOfWeek;
        public int whichWeek;
        List<ScheduleData.Todo> todos = new ArrayList<ScheduleData.Todo>();

        @Override
        public String toString() {
            return String.valueOf(day);
        }
    }


    /**
     * 日期类型
     **/
    public enum DayType {
        DAY_TYPE_NONE(0),
        DAY_TYPE_FORE(1),
        DAY_TYPE_NOW(2),
        DAY_TYPE_NEXT(3);
        private int value;

        DayType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * 显示日历数据
     **/
    private void showCalendar(final Calendar calendar) {
        needNum = 0;
        isNeed = true;

        int year = calendar.get(Calendar.YEAR);//获得年份
        int month = calendar.get(Calendar.MONTH) + 1;//获取月份
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

//        int day = calendar.get(Calendar.DATE);//获取天数
        int centry = Integer.valueOf(String.valueOf(year).substring(0, 2));//取年份前两位作为世纪数,世纪数-1
        int tmpYear = Integer.valueOf(String.valueOf(year).substring(2, 4));//取年份后两位
        ///方法1计算该月的第一天是星期几
        if (month == 1 || month == 2) {//该年的1、2月看作为前一年的13月，14月
            tmpYear -= 1;
            month += 12;
        }
        //计算该月的第一天是星期几
        int firstOfWeek = (tmpYear + (tmpYear / 4) + centry / 4 - 2 * centry + 26 * (month + 1) / 10) % 7;


        //LogUtil.i("showCalendarshowCalendar",firstOfWeek+":"+month);
        if (firstOfWeek <= 0) firstOfWeek = 7 + firstOfWeek;//处理星期的显示

        //计算第一天所在的索引值,如果该天为星期一，则做换行处理
//        final int firstDayIndex = firstOfWeek == 1 ? 7 : firstOfWeek;
        final int firstDayIndex = firstOfWeek == 7 ? 7 : firstOfWeek;

        final int dayCount = getDayCount(year, month);//获取该月的天数
        //处理本月的数据
        for (int i = firstDayIndex; i < firstDayIndex + dayCount; i++) {
            if (dayInfos[i] == null)
                dayInfos[i] = new DayInfo();
            dayInfos[i].day = i - firstDayIndex + 1;
            dayInfos[i].dayType = DayType.DAY_TYPE_NOW;

        }
        //处理前一个月的数据
        calendar.add(Calendar.MONTH, -1);//前一个月
        year = calendar.get(Calendar.YEAR);//获得年份
        month = calendar.get(Calendar.MONTH) + 1;//获取月份
        final int foreDayCount = getDayCount(year, month);//获得前一个月的天数
        for (int i = 0; i < firstDayIndex; i++) {
            if (dayInfos[i] == null)
                dayInfos[i] = new DayInfo();
            dayInfos[i].day = foreDayCount - firstDayIndex + i + 1;
            dayInfos[i].dayType = DayType.DAY_TYPE_FORE;
        }
        //处理下一个月的数据
        for (int i = 0; i < MAX_DAY_COUNT - dayCount - firstDayIndex; i++) {
            if (dayInfos[firstDayIndex + dayCount + i] == null)
                dayInfos[firstDayIndex + dayCount + i] = new DayInfo();
            dayInfos[firstDayIndex + dayCount + i].day = i + 1;
            dayInfos[firstDayIndex + dayCount + i].dayType = DayType.DAY_TYPE_NEXT;
        }


        if (firstOfWeek == 7) {
            //应为星期一是第二周的第一天,所以做数据处理
            listDayInfos.clear();
            listDayInfos.addAll(Arrays.asList(dayInfos));
            for (int i = 0; i < 6; i++) {
                listDayInfos.add(listDayInfos.remove(0));
            }
        } else {
            listDayInfos.clear();
            listDayInfos.addAll(Arrays.asList(dayInfos));
        }

        int[] weekDays = {0, 0, 0, 0, 0, 0, 0};
        List<ScheduleData.Todo> tempTodos = new ArrayList<ScheduleData.Todo>();


        for (int i = 0; i < listDayInfos.size(); i++) {
            switch (i / 7) {
                case 0:
                    if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                        weekDays[0]++;
                        listDayInfos.get(i).whichWeek = 1;
                    }
                    break;
                case 1:
                    if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                        weekDays[1]++;
                        listDayInfos.get(i).whichWeek = 2;
                    }
                    break;
                case 2:
                    if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                        weekDays[2]++;
                        listDayInfos.get(i).whichWeek = 3;
                    }
                    break;
                case 3:
                    if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                        weekDays[3]++;
                        listDayInfos.get(i).whichWeek = 4;
                    }
                    break;
                case 4:
                    if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                        weekDays[4]++;
                        listDayInfos.get(i).whichWeek = 5;
                    }
                    break;
                case 5:
                    if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                        weekDays[5]++;
                        listDayInfos.get(i).whichWeek = 6;
                    }
                    break;
            }
        }


        for (int i = 0; i < listDayInfos.size(); i++) {
            listDayInfos.get(i).position = i;
            listDayInfos.get(i).daysOfWeek = weekDays[i / 7];///一周有几天是今天
        }


        render(ll_monthCalendarArea, listDayInfos);


        calendar.add(Calendar.MONTH, 1);//还原月份数据
        widgetCalendar_txtTitle.setText(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));//设置日历显示的标题
        // "2015-11-30 17:11:01",
        currentYearAndMonth = (new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        currentYearAndMonth = currentYearAndMonth.substring(0, currentYearAndMonth.length() - 2);


    }


    /**
     * 渲染page中的view：7天
     */
    private void render(final ViewGroup view1, List<DayInfo> listDayInfos) {

        for (int i = 0; i < 42; i++) {
            final ViewGroup view = (ViewGroup) view1.getChildAt(i / 7);
            final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(i % 7);
            if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                ((TextView) dayOfWeek.findViewById(R.id.tv_gongli)).setText(listDayInfos.get(i).day + "");
            } else {
                ((TextView) dayOfWeek.findViewById(R.id.tv_gongli)).setText("");
            }

            dayOfWeek.setTag(listDayInfos.get(i));
            dayOfWeek.setOnClickListener(onClickListener);
        }

    }

    private void initBackground() {
        for (int i = 0; i < 42; i++) {
            ViewGroup view = (ViewGroup) ll_monthCalendarArea.getChildAt(i / 7);
            ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(i % 7);
            dayOfWeek.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }


    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).daysOfWeek == 7) {
                for (int i = 0; i < 7; i++) {
                    if (i == position) {
                        ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundColor(getResources().getColor(R.color.weekSchedule));
                    } else {
                        ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
                currentMonthInfo.currentOpenDayOfWeek = position;
            } else {
                if (listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).whichWeek == 1) {
                    for (int i = 0; i < 7; i++) {
                        if (i == position + (7 - listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).daysOfWeek)) {
                            ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundColor(getResources().getColor(R.color.weekSchedule));
                        } else {
                            ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                    Log.i("222", "eeee"+position );
                    currentMonthInfo.currentOpenDayOfWeek = position ;
                } else {
                    for (int i = 0; i < 7; i++) {
                        if (i == position) {
                            ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundColor(getResources().getColor(R.color.weekSchedule));
                        } else {
                            ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                    currentMonthInfo.currentOpenDayOfWeek = position;
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DayInfo dayInfo = (DayInfo) v.getTag();
            if (listDayInfos.get(dayInfo.position).dayType == DayType.DAY_TYPE_NOW) {
                currentViewGroup = (ViewGroup) v;
                initBackground();
                v.setBackgroundColor(getResources().getColor(R.color.weekSchedule));
                Log.i("44",""+listDayInfos.get(dayInfo.position).whichWeek+":"+((dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek))+":"+(currentMonthInfo.currentOpenDayOfWeek));

                if ((dayInfo.position / 7 != currentMonthInfo.currentOpenWeek) || ((dayInfo.position / 7 == currentMonthInfo.currentOpenWeek) && currentMonthInfo.currentOpenDayOfWeek == (listDayInfos.get(dayInfo.position).whichWeek == 1 ? (dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek) : dayInfo.position % 7))) {
                    switch (dayInfo.position / 7) {

                        case 0:
                            if (isCanTranslate()) {
                                objectAnimatorList.clear();
                                dealWeek(0, 0, 3, 3, 3, 3, 0, 4, 4, 4, 4, 4);
                                startClickTime = System.currentTimeMillis();
                            }
                            break;
                        case 1:
                            if (isCanTranslate()) {
                                objectAnimatorList.clear();
                                dealWeek(1, -1, -1, 2, 2, 2, -1, -1, 3, 3, 3, 3);
                                startClickTime = System.currentTimeMillis();
                            }
                            break;
                        case 2:
                            if (isCanTranslate()) {
                                objectAnimatorList.clear();
                                dealWeek(2, -2, -2, -2, 1, 1, -2, -2, -2, 2, 2, 2);
                                startClickTime = System.currentTimeMillis();
                            }
                            break;

                        case 3:
                            if (isCanTranslate()) {

                                objectAnimatorList.clear();
                                dealWeek(3, -3, -3, -3, -3, 0, -3, -3, -3, -3, 1, 1);
                                startClickTime = System.currentTimeMillis();
                            }
                            break;

                        case 4:
                            if (isCanTranslate()) {
                                objectAnimatorList.clear();
                                dealWeek(4, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, 0);
                                startClickTime = System.currentTimeMillis();
                            }
                            break;

                        case 5:
                            if (isCanTranslate()) {
                                objectAnimatorList.clear();
                                dealWeek(5, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, -4);
                                startClickTime = System.currentTimeMillis();
                            }
                            break;
                    }
                }

                fragments.clear();
                for (int i = 0; i < listDayInfos.get(dayInfo.position).daysOfWeek; i++) {
                    fragments.add(tempFragments.get(i));
                }
                vpStatePagerAdapter.notifyDataSetChanged();
                currentMonthInfo.currentOpenDayOfMonth = dayInfo.position;
                if (listDayInfos.get(dayInfo.position).daysOfWeek == 7) {
                    currentMonthInfo.currentOpenDayOfWeek = (dayInfo.position) % 7;
                    vp_weekSchedule.setCurrentItem((dayInfo.position) % listDayInfos.get(dayInfo.position).daysOfWeek);
                } else {
                    if (listDayInfos.get(dayInfo.position).whichWeek == 1) {
                        Log.i("dayInfo", "" + ((dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek)));
                        Log.i("222", "wwwww"+((dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek)));
                        currentMonthInfo.currentOpenDayOfWeek = (dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek);
                        vp_weekSchedule.setCurrentItem((dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek));
                    } else {
                        currentMonthInfo.currentOpenDayOfWeek = (dayInfo.position) % 7;
                        vp_weekSchedule.setCurrentItem((dayInfo.position) % 7);
                    }
                }


            }
        }
    };


    /**
     * 是否是平年
     **/
    private boolean isLeapYear(int year) {
        return !((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }

    /**
     * 获取某年的某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    private int getDayCount(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            case 13://其实是1月，当作上一年的13月看待
                return 31;
            case 2:
            case 14://其实是2月，当作上一年的14月看
                return isLeapYear(year) ? 28 : 29;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return 0;
    }


}
