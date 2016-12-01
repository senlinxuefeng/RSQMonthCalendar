package com.yumingchuan.rsqmonthcalendar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yumingchuan.rsqmonthcalendar.TimestampTool.sdf_yMd;

public class MonthScheduleActivity extends AppCompatActivity {


    private Context mContext;
    private DayInfo currentDayInfo;
    private boolean isNeedReAddFragment;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000://打开月试图
                    setWeekFragmentData(currentDayInfo, isNeedReAddFragment);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_schedule);
        ButterKnife.bind(this);

        mContext = this;

        intMonthCalendar();

    }

    @OnClick({R.id.widgetCalendar_imgForeMonth, R.id.widgetCalendar_imgNextMonth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.widgetCalendar_imgForeMonth://///月试图
                calendar.add(Calendar.MONTH, -1);
                initMonthCalendarLayout(calendar);
                break;
            case R.id.widgetCalendar_imgNextMonth://///月试图
                calendar.add(Calendar.MONTH, 1);
                initMonthCalendarLayout(calendar);
                break;

        }
    }


    //////////////////////////////////////////月视图start//////////////////////////////////////////////////////

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

    @BindView(R.id.srl_monthCalender)
    public CustomSwipeToRefresh srl_monthCalender;

    @BindView(R.id.ll_titleTotalHeight)
    public LinearLayout ll_titleTotalHeight;


    public CurrentMonthInfo currentMonthInfo;
    private ViewGroup currentViewGroup;
    List<Fragment> weekFragments = new ArrayList<>();
    List<Fragment> tempFragments = new ArrayList<>();
    private VPStatePagerAdapter2 vpStatePagerAdapter;
    private WeekScheduleFragment weekScheduleFragment0;
    private WeekScheduleFragment weekScheduleFragment1;
    private WeekScheduleFragment weekScheduleFragment2;
    private WeekScheduleFragment weekScheduleFragment3;
    private WeekScheduleFragment weekScheduleFragment4;
    private WeekScheduleFragment weekScheduleFragment5;
    private WeekScheduleFragment weekScheduleFragment6;


    private void intMonthCalendar() {

        weekScheduleFragment0 = new WeekScheduleFragment();
        weekScheduleFragment1 = new WeekScheduleFragment();
        weekScheduleFragment2 = new WeekScheduleFragment();
        weekScheduleFragment3 = new WeekScheduleFragment();
        weekScheduleFragment4 = new WeekScheduleFragment();
        weekScheduleFragment5 = new WeekScheduleFragment();
        weekScheduleFragment6 = new WeekScheduleFragment();

        ViewTreeObserver vto = ll_monthCalendarArea.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_monthCalendarArea.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                monthEditAreaWidth = ll_monthCalendarArea.getWidth();
                monthEditAreaHeight = ll_monthCalendarArea.getHeight();
                titleTotalHeight = ll_titleTotalHeight.getHeight();
                initMonthCalendarLayout(calendar);
            }
        });
    }

    //全部高度变量
    private float monthEditAreaWidth;
    private float monthEditAreaHeight;
    private float titleTotalHeight;


    /**
     * 初始化布局
     */
    public void initMonthCalendarLayout(Calendar someCalendar) {
        currentMonthInfo = new CurrentMonthInfo();
        currentMonthInfo.weeks = someCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        currentMonthInfo.monthEditAreaWidth = monthEditAreaWidth;
        currentMonthInfo.monthEditAreaHeight = monthEditAreaHeight;
        currentMonthInfo.weekEditAreaHeight = (int) currentMonthInfo.monthEditAreaHeight / currentMonthInfo.weeks;

        //重新调整高度(误差在6dp的范围内)
        setEditAreaHeightAndWidth(ll_titleHeight, currentMonthInfo.monthEditAreaWidth, titleTotalHeight - currentMonthInfo.weekEditAreaHeight * currentMonthInfo.weeks);
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

        weekFragments.clear();
        tempFragments.clear();
        weekScheduleFragment0.totalTodo.clear();
        weekScheduleFragment1.totalTodo.clear();
        weekScheduleFragment2.totalTodo.clear();
        weekScheduleFragment3.totalTodo.clear();
        weekScheduleFragment4.totalTodo.clear();
        weekScheduleFragment5.totalTodo.clear();
        weekScheduleFragment6.totalTodo.clear();
        tempFragments.add(weekScheduleFragment0);
        tempFragments.add(weekScheduleFragment1);
        tempFragments.add(weekScheduleFragment2);
        tempFragments.add(weekScheduleFragment3);
        tempFragments.add(weekScheduleFragment4);
        tempFragments.add(weekScheduleFragment5);
        tempFragments.add(weekScheduleFragment6);


        for (int i = 0; i < 7; i++) {
            weekFragments.add(tempFragments.get(i));
        }

        vpStatePagerAdapter = new VPStatePagerAdapter2(getSupportFragmentManager(), weekFragments);
        vp_weekSchedule.setAdapter(vpStatePagerAdapter);
        vp_weekSchedule.setOffscreenPageLimit(7);


        vp_weekSchedule.setOnPageChangeListener(onMonthPageChangeListener);
        srl_monthCalender.setOnRefreshListener(onRefreshListener);
        srl_monthCalender.setOnCanRefreshListener(onCanRefreshListener);

        initBackground();
        refreshMonthDataRequest(false);
    }

    /**
     * @param noLoadLocalData 手动刷新不需要加载本地数据
     */
    public void refreshMonthDataRequest(boolean noLoadLocalData) {

        try {
            if (!noLoadLocalData) {
                parseData(APIUtils.testData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseData(String response) {

        stopRefresh();
        List<ScheduleMonthDetail> scheduleMonthDetails = new Gson().fromJson(response, new TypeToken<List<ScheduleMonthDetail>>() {
        }.getType());

        //防止快速切换月造成的数据不对问题
        if (scheduleMonthDetails.get(20).date.contains(currentMonthInfo.currentY_M)) {
            //数据处理
            if (currentMonthInfo.isNeedRemoveData) {
                for (int i = 0; i < 6; i++) {
                    scheduleMonthDetails.add(scheduleMonthDetails.remove(0));
                }
            }
            //数据赋值
            for (int i = 0; i < scheduleMonthDetails.size(); i++) {
                listDayInfos.get(i).todos.clear();
                listDayInfos.get(i).todos.addAll(sortScheduleTodo(scheduleMonthDetails.get(i)));
            }
            refreshData();
        }
    }


    private List<ScheduleToDo> IETodos = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> IUTodos = new ArrayList<ScheduleToDo>();

    private List<ScheduleToDo> UETodos = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> UUTodos = new ArrayList<ScheduleToDo>();

    private List<ScheduleToDo> noDoneIE = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> doneIE = new ArrayList<ScheduleToDo>();

    private List<ScheduleToDo> noDoneIU = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> doneIU = new ArrayList<ScheduleToDo>();

    private List<ScheduleToDo> noDoneUE = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> doneUE = new ArrayList<ScheduleToDo>();

    private List<ScheduleToDo> noDoneUU = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> doneUU = new ArrayList<ScheduleToDo>();

    private List<ScheduleToDo> totalTodos = new ArrayList<ScheduleToDo>();


    /**
     * 排序
     *
     * @param scheduleMonthDetail
     * @return
     */
    public List<ScheduleToDo> sortScheduleTodo(ScheduleMonthDetail scheduleMonthDetail) {

        totalTodos.clear();
        if (scheduleMonthDetail != null && scheduleMonthDetail.data != null) {
            noDoneIE.clear();
            doneIE.clear();
            noDoneIU.clear();
            doneIU.clear();
            noDoneUE.clear();
            doneUE.clear();
            noDoneUU.clear();
            doneUU.clear();

            sortDoOrDone(scheduleMonthDetail.data.IETodos, noDoneIE, doneIE);
            sortDoOrDone(scheduleMonthDetail.data.IUTodos, noDoneIU, doneIU);
            sortDoOrDone(scheduleMonthDetail.data.UETodos, noDoneUE, doneUE);
            sortDoOrDone(scheduleMonthDetail.data.UUTodos, noDoneUU, doneUU);

            totalTodos.addAll(noDoneIE);
            totalTodos.addAll(noDoneIU);
            totalTodos.addAll(noDoneUE);
            totalTodos.addAll(noDoneUU);

            totalTodos.addAll(doneIE);
            totalTodos.addAll(doneIU);
            totalTodos.addAll(doneUE);
            totalTodos.addAll(doneUU);
        }
        return totalTodos;
    }

    /**
     * @param tempTodos
     * @param tempTodos_do
     * @param tempTodos_done
     */
    private void sortDoOrDone(List<ScheduleToDo> tempTodos, List<ScheduleToDo> tempTodos_do, List<ScheduleToDo> tempTodos_done) {
        if (tempTodos != null && 0 != tempTodos.size()) {
            Collections.sort(tempTodos);
            Collections.reverse(tempTodos);
            for (int i = 0; i < tempTodos.size(); i++) {
                if (tempTodos.get(i).pIsDone) {
                    tempTodos_done.add(tempTodos.get(i));
                } else {
                    tempTodos_do.add(tempTodos.get(i));
                }
            }
        }
    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshMonthDataRequest(true);
        }
    };

    CustomSwipeToRefresh.OnCanRefreshListener onCanRefreshListener = new CustomSwipeToRefresh.OnCanRefreshListener() {
        @Override
        public boolean isCanRefresh() {
            try {
                if ((!currentMonthInfo.isExpand) ||
                        ((WeekScheduleFragment) weekFragments.get(currentMonthInfo.currentOpenDayOfWeek)).totalTodo.size() == 0 ||
                        (((WeekScheduleFragment) weekFragments.get(currentMonthInfo.currentOpenDayOfWeek)).lv_monthSchedule.getChildCount() > 0 &&
                                ((WeekScheduleFragment) weekFragments.get(currentMonthInfo.currentOpenDayOfWeek)).lv_monthSchedule.getFirstVisiblePosition() == 0 &&
                                ((WeekScheduleFragment) weekFragments.get(currentMonthInfo.currentOpenDayOfWeek)).lv_monthSchedule.getChildAt(0).getTop() >= ((WeekScheduleFragment) weekFragments.get(currentMonthInfo.currentOpenDayOfWeek)).lv_monthSchedule.getPaddingTop())) {
                    return true;
                }
            } catch (Exception e) {

            }
            return false;
        }
    };

    private void stopRefresh() {
        if (srl_monthCalender != null && srl_monthCalender.isRefreshing()) {
            srl_monthCalender.setRefreshing(false);
        }
    }


    public void refreshData(List<ScheduleToDo> tempTodo) {
        listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).todos.clear();
        listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).todos.addAll(tempTodo);
        refreshData();
    }

    public void refreshData() {

        //展開的時候刷新數據
        int j = 0;
        for (int i = currentMonthInfo.startPosition; i < currentMonthInfo.endPosition; i++) {
            ((WeekScheduleFragment) tempFragments.get(j++)).addAndRefreshData(listDayInfos.get(i).todos);
        }

        for (int i = 0; i < 42; i++) {
            final ViewGroup view = (ViewGroup) ll_monthCalendarArea.getChildAt(i / 7);
            final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(i % 7);
            TextView tv_scheduleNum = (TextView) dayOfWeek.findViewById(R.id.tv_scheduleNum);
            if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                LinearLayout ll_schedule = (LinearLayout) dayOfWeek.findViewById(R.id.ll_schedule);
                ll_schedule.removeAllViews();
                for (int k = 0; k < listDayInfos.get(i).todos.size() && k < 3; k++) {
                    addScheduleOrFestival(ll_schedule, listDayInfos.get(i).todos.get(k));
                }

                if (listDayInfos.get(i).todos.size() > 3) {
                    tv_scheduleNum.setText("+" + (listDayInfos.get(i).todos.size() - 3));
                } else {
                    tv_scheduleNum.setText("");
                }
            } else {
                LinearLayout ll_schedule = (LinearLayout) dayOfWeek.findViewById(R.id.ll_schedule);
                ll_schedule.removeAllViews();
                tv_scheduleNum.setText("");
                ((TextView) dayOfWeek.findViewById(R.id.tv_gongli)).setText("");
            }

            dayOfWeek.setTag(listDayInfos.get(i));
        }
    }


    private void addScheduleOrFestival(ViewGroup viewGroup, ScheduleToDo todo) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(10);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.getPaint().setFlags(todo.pIsDone ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
        textView.getPaint().setAntiAlias(true);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(mContext, 14));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = CommonUtils.dip2px(mContext, 1);
        textView.setLayoutParams(lp);

        textView.setText(todo.pTitle);
        viewGroup.addView(textView);
    }

    private float getEditAreaTranslateHeight(int week) {
        return currentMonthInfo.weeks == 5 ? ((week + 1) == 5 ? (currentMonthInfo.initY + currentMonthInfo.weekEditAreaHeight * 2) : (currentMonthInfo.weekEditAreaHeight + currentMonthInfo.initY)) : ((week + 1) == 6 ? (currentMonthInfo.weekEditAreaHeight * 2 + currentMonthInfo.initY) : (currentMonthInfo.weekEditAreaHeight + currentMonthInfo.initY));
    }

    private int transAnim = 200;

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
                translateView(ll_1, ll_1.getY(), (ll_1.getY() + currentMonthInfo.weekEditAreaHeight * transDistance51), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() + currentMonthInfo.weekEditAreaHeight * transDistance52), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() + currentMonthInfo.weekEditAreaHeight * transDistance53), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() + currentMonthInfo.weekEditAreaHeight * transDistance54), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() + currentMonthInfo.weekEditAreaHeight * transDistance55), transAnim);

                ll_week.setVisibility(View.VISIBLE);
                translateView(ll_week, (int) ll_week.getY(), getEditAreaTranslateHeight(currentOpenWeek), 10);
            } else {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() + currentMonthInfo.weekEditAreaHeight * transDistance61), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() + currentMonthInfo.weekEditAreaHeight * transDistance62), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() + currentMonthInfo.weekEditAreaHeight * transDistance63), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() + currentMonthInfo.weekEditAreaHeight * transDistance64), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() + currentMonthInfo.weekEditAreaHeight * transDistance65), transAnim);
                translateView(ll_6, ll_6.getY(), (ll_6.getY() + currentMonthInfo.weekEditAreaHeight * transDistance66), transAnim);

                ll_week.setVisibility(View.VISIBLE);
                translateView(ll_week, (int) ll_week.getY(), getEditAreaTranslateHeight(currentOpenWeek), 10);
            }
        } else {
            if (is5Week) {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() - currentMonthInfo.weekEditAreaHeight * transDistance51), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() - currentMonthInfo.weekEditAreaHeight * transDistance52), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() - currentMonthInfo.weekEditAreaHeight * transDistance53), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() - currentMonthInfo.weekEditAreaHeight * transDistance54), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() - currentMonthInfo.weekEditAreaHeight * transDistance55), transAnim);

                mMonthHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_week.setVisibility(View.INVISIBLE);
                    }
                }, transAnim);

            } else {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() - currentMonthInfo.weekEditAreaHeight * transDistance61), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() - currentMonthInfo.weekEditAreaHeight * transDistance62), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() - currentMonthInfo.weekEditAreaHeight * transDistance63), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() - currentMonthInfo.weekEditAreaHeight * transDistance64), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() - currentMonthInfo.weekEditAreaHeight * transDistance65), transAnim);
                translateView(ll_6, ll_6.getY(), (ll_6.getY() - currentMonthInfo.weekEditAreaHeight * transDistance66), transAnim);

                mMonthHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_week.setVisibility(View.INVISIBLE);
                    }
                }, transAnim);

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


    Handler mMonthHandler = new Handler();

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

            startClickTime = System.currentTimeMillis();

            mMonthHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1000);
                }
            }, 220);

        } else {
            if (currentMonthInfo.currentOpenWeek == currentOpenWeek) {
                //打开到关闭
                openOrCloseWeek(!currentMonthInfo.isExpand, currentMonthInfo.weeks == 5, currentOpenWeek, transDistance51, transDistance52, transDistance53, transDistance54, transDistance55,
                        transDistance61, transDistance62, transDistance63, transDistance64, transDistance65, transDistance66);
                currentMonthInfo.isExpand = false;
                currentMonthInfo.currentOpenWeek = -1;

                startClickTime = System.currentTimeMillis();

                mMonthHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1000);
                    }
                }, 220);

            } else {
                //先关闭
                onlyCloseWeek(currentMonthInfo.currentOpenWeek);

                mMonthHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onlyOpenWeek(currentOpenWeek);
                        currentMonthInfo.isExpand = true;
                        currentMonthInfo.currentOpenWeek = currentOpenWeek;
                        startClickTime = System.currentTimeMillis();

                        mMonthHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(1000);
                            }
                        }, 220);
                    }
                }, 220);

            }
        }
    }

    private long startClickTime = 0;

    /**
     * 判断是否可以移动
     *
     * @return
     */
    private boolean isCanTranslate(int time) {
        for (int i = 0; i < objectAnimatorList.size(); i++) {
            if (objectAnimatorList.get(i).isRunning() || System.currentTimeMillis() - startClickTime < time) {
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
    public Calendar calendar = Calendar.getInstance();
    public List<DayInfo> listDayInfos = new ArrayList<DayInfo>();


    /**
     * 日期信息实体类
     **/
    public class DayInfo {
        public int position;
        public int day;
        public DayType dayType;
        public int daysOfWeek;
        public int whichWeek;
        public List<ScheduleToDo> todos = new ArrayList<ScheduleToDo>();

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

        this.calendar = calendar;

        int year = calendar.get(Calendar.YEAR);//获得年份
        int month = calendar.get(Calendar.MONTH) + 1;//获取月份
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        int centry = Integer.valueOf(String.valueOf(year).substring(0, 2));//取年份前两位作为世纪数,世纪数-1
        int tmpYear = Integer.valueOf(String.valueOf(year).substring(2, 4));//取年份后两位
        ///方法1计算该月的第一天是星期几
        if (month == 1 || month == 2) {//该年的1、2月看作为前一年的13月，14月
            tmpYear -= 1;
            month += 12;
        }
        //计算该月的第一天是星期几
        int firstOfWeek = (tmpYear + (tmpYear / 4) + centry / 4 - 2 * centry + 26 * (month + 1) / 10) % 7;

        if (firstOfWeek <= 0) firstOfWeek = 7 + firstOfWeek;//处理星期的显示

        //计算第一天所在的索引值,如果该天为星期一，则做换行处理
        final int firstDayIndex = firstOfWeek == 7 ? 7 : firstOfWeek;

        final int dayCount = getDayCount(year, month);//获取该月的天数
        currentMonthInfo.currentYM = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
        currentMonthInfo.currentY_M = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
        currentMonthInfo.currentYMD = sdf_yMd.format(calendar.getTime());

        //处理本月的数据
        for (int i = firstDayIndex; i < firstDayIndex + dayCount; i++) {
            if (dayInfos[i] == null)
                dayInfos[i] = new DayInfo();
            dayInfos[i].day = i - firstDayIndex + 1;
            dayInfos[i].dayType = DayType.DAY_TYPE_NOW;
            dayInfos[i].todos.clear();
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
            dayInfos[i].todos.clear();

        }
        //处理下一个月的数据
        for (int i = 0; i < MAX_DAY_COUNT - dayCount - firstDayIndex; i++) {
            if (dayInfos[firstDayIndex + dayCount + i] == null)
                dayInfos[firstDayIndex + dayCount + i] = new DayInfo();
            dayInfos[firstDayIndex + dayCount + i].day = i + 1;
            dayInfos[firstDayIndex + dayCount + i].dayType = DayType.DAY_TYPE_NEXT;
            dayInfos[i].todos.clear();
        }

        if (firstOfWeek == 7) {
            currentMonthInfo.isNeedRemoveData = true;
            //应为星期一是第二周的第一天,所以做数据处理
            listDayInfos.clear();
            listDayInfos.addAll(Arrays.asList(dayInfos));
            for (int i = 0; i < 6; i++) {
                listDayInfos.add(listDayInfos.remove(0));
            }
        } else {
            currentMonthInfo.isNeedRemoveData = false;
            listDayInfos.clear();
            listDayInfos.addAll(Arrays.asList(dayInfos));
        }

        int[] weekDays = {0, 0, 0, 0, 0, 0, 0};

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
    }


    /**
     * 渲染page中的view：7天
     */
    private void render(final ViewGroup view1, List<DayInfo> listDayInfos) {

        for (int i = 0; i < 42; i++) {
            final ViewGroup view = (ViewGroup) view1.getChildAt(i / 7);
            final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(i % 7);
            LinearLayout ll_schedule = (LinearLayout) dayOfWeek.findViewById(R.id.ll_schedule);
            ((TextView) dayOfWeek.findViewById(R.id.tv_scheduleNum)).setText("");
            ll_schedule.removeAllViews();
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
            DayInfo dayInfo = (DayInfo) dayOfWeek.getTag();
            if (dayInfo.dayType == DayType.DAY_TYPE_NOW && TimestampTool.getCurrentYearMonthDay().equals(currentMonthInfo.currentYM + String.format("%02d", dayInfo.day))) {
                dayOfWeek.setBackgroundResource(R.drawable.shape_rect_border_da_bg_bg);
            } else {
                dayOfWeek.setBackgroundResource(R.drawable.shape_rect_border_da_bg_white);
            }
        }
    }


    ViewPager.OnPageChangeListener onMonthPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).daysOfWeek == 7) {
                setItemBackground(position);
                currentMonthInfo.currentOpenDayOfWeek = position;
            } else {
                if (listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).whichWeek == 1) {
                    setItemBackground(position + (7 - listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).daysOfWeek));
                    currentMonthInfo.currentOpenDayOfWeek = position;
                } else {
                    setItemBackground(position);
                    currentMonthInfo.currentOpenDayOfWeek = position;
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void setItemBackground(int position) {
        for (int i = 0; i < 7; i++) {
            final DayInfo dayInfo = (DayInfo) ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).getTag();
            if (i == position) {
                ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundResource(R.drawable.shape_rect_border_da_bg_f5);
            } else if (TimestampTool.getCurrentYearMonthDay().equals(currentMonthInfo.currentYM + String.format("%02d", dayInfo.day))) {
                ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundResource(R.drawable.shape_rect_border_da_bg_bg);
            } else {
                ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundResource(R.drawable.shape_rect_border_da_bg_white);
            }

        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            currentDayInfo = (DayInfo) v.getTag();
            if (listDayInfos.get(currentDayInfo.position).dayType == DayType.DAY_TYPE_NOW) {

                isNeedReAddFragment = (-1 == currentMonthInfo.currentOpenDayOfMonth) || (currentMonthInfo.currentOpenDayOfMonth / 7 != currentDayInfo.position / 7);
                boolean isNeedChange = (currentDayInfo.position / 7 != currentMonthInfo.currentOpenWeek) ||
                        ((currentDayInfo.position / 7 == currentMonthInfo.currentOpenWeek) && currentMonthInfo.currentOpenDayOfWeek == (listDayInfos.get(currentDayInfo.position).whichWeek == 1 ? (currentDayInfo.position) % 7 - (7 - currentDayInfo.daysOfWeek) : currentDayInfo.position % 7));

                //isNeedReAddFragment = true;
                if (!isNeedReAddFragment && isCanTranslate(160) && !isNeedChange) {
                    startClickTime = System.currentTimeMillis();
                    if (listDayInfos.get(currentDayInfo.position).dayType == DayType.DAY_TYPE_NOW) {
                        currentViewGroup = (ViewGroup) v;
                        initBackground();
                        currentMonthInfo.currentOpenDayOfMonth = currentDayInfo.position;
                        mHandler.sendEmptyMessage(1000);
                        v.setBackgroundResource(R.drawable.shape_rect_border_da_bg_f5);

                    }
                } else {
                    if (isCanTranslate(400)) {
                        startClickTime = System.currentTimeMillis();
                        currentViewGroup = (ViewGroup) v;
                        initBackground();

                        if (isNeedChange) {
                            switch (currentDayInfo.position / 7) {
                                case 0:
                                    objectAnimatorList.clear();
                                    dealWeek(0, 0, 3, 3, 3, 3, 0, 4, 4, 4, 4, 4);
                                    break;
                                case 1:
                                    objectAnimatorList.clear();
                                    dealWeek(1, -1, -1, 2, 2, 2, -1, -1, 3, 3, 3, 3);
                                    break;
                                case 2:
                                    objectAnimatorList.clear();
                                    dealWeek(2, -2, -2, -2, 1, 1, -2, -2, -2, 2, 2, 2);
                                    break;
                                case 3:
                                    objectAnimatorList.clear();
                                    dealWeek(3, -3, -3, -3, -3, 0, -3, -3, -3, -3, 1, 1);
                                    break;
                                case 4:
                                    objectAnimatorList.clear();
                                    dealWeek(4, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, 0);
                                    break;
                                case 5:
                                    objectAnimatorList.clear();
                                    dealWeek(5, -3, -3, -3, -3, -3, -4, -4, -4, -4, -4, -4);
                                    break;
                                default:
                                    startClickTime = System.currentTimeMillis();
                                    break;
                            }

                        }
                        currentMonthInfo.currentOpenDayOfMonth = currentDayInfo.position;
                        v.setBackgroundResource(R.drawable.shape_rect_border_da_bg_f5);
                    }
                }
            }

        }
    };

    private void setWeekFragmentData(final DayInfo dayInfo, boolean isNeedReAddFragment) {
        if (isNeedReAddFragment && currentMonthInfo.isExpand) {
            //造成数据的错乱的主要原因
            weekFragments.clear();
            for (int i = 0; i < listDayInfos.get(dayInfo.position).daysOfWeek; i++) {
                weekFragments.add(tempFragments.get(i));
            }
            vpStatePagerAdapter.notifyDataSetChanged();

            //刷新数据
            mMonthHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int j = 0;
                    currentMonthInfo.startPosition = (dayInfo.position - currentMonthInfo.currentOpenDayOfWeek);
                    currentMonthInfo.endPosition = (dayInfo.position - currentMonthInfo.currentOpenDayOfWeek) + listDayInfos.get(dayInfo.position).daysOfWeek;
                    for (int i = currentMonthInfo.startPosition; i < currentMonthInfo.endPosition; i++) {
                        ((WeekScheduleFragment) tempFragments.get(j++)).addAndRefreshData(listDayInfos.get(i).todos);
                    }
                }
            }, 40);
        }

        if (listDayInfos.get(dayInfo.position).daysOfWeek == 7) {
            currentMonthInfo.currentOpenDayOfWeek = (dayInfo.position) % 7;
            vp_weekSchedule.setCurrentItem((dayInfo.position) % listDayInfos.get(dayInfo.position).daysOfWeek);
        } else {
            if (listDayInfos.get(dayInfo.position).whichWeek == 1) {
                currentMonthInfo.currentOpenDayOfWeek = (dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek);
                vp_weekSchedule.setCurrentItem((dayInfo.position) % 7 - (7 - dayInfo.daysOfWeek));
            } else {
                currentMonthInfo.currentOpenDayOfWeek = (dayInfo.position) % 7;
                vp_weekSchedule.setCurrentItem((dayInfo.position) % 7);
            }
        }
    }


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

//////////////////////////////////////////月视图end//////////////////////////////////////////////////////


