package com.yumingchuan.rsqmonthcalendar.ui;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yumingchuan.rsqmonthcalendar.R;
import com.yumingchuan.rsqmonthcalendar.adapter.WeekViewFragmentAdapter;
import com.yumingchuan.rsqmonthcalendar.base.BaseFragment;
import com.yumingchuan.rsqmonthcalendar.bean.CurrentMonthInfo;
import com.yumingchuan.rsqmonthcalendar.bean.DayInfo;
import com.yumingchuan.rsqmonthcalendar.bean.DayType;
import com.yumingchuan.rsqmonthcalendar.bean.ScheduleMonthDetail;
import com.yumingchuan.rsqmonthcalendar.bean.ScheduleToDo;
import com.yumingchuan.rsqmonthcalendar.utils.CommonUtils;
import com.yumingchuan.rsqmonthcalendar.utils.JsonUtils;
import com.yumingchuan.rsqmonthcalendar.utils.LogUtils;
import com.yumingchuan.rsqmonthcalendar.utils.MonthViewCalendarUtil;
import com.yumingchuan.rsqmonthcalendar.utils.PMUtils;
import com.yumingchuan.rsqmonthcalendar.utils.TimestampTool;
import com.yumingchuan.rsqmonthcalendar.view.ChildViewPager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class MonthViewFragment extends BaseFragment {

    private boolean isNeedReAddFragment;
    public DayInfo currentDayInfo;
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    setWeekFragmentData(currentDayInfo);
                    break;
            }
        }
    };

    private LinearLayout[] linearLayouts;
    private List<DayInfo> listDayInfos;
    private Calendar calendar;
    private final int currentPosition;

    public MonthViewFragment(int position) {
        currentPosition = position;
    }


    public static MonthViewFragment newInstance(int position) {
        return new MonthViewFragment(position);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_month_opt;
    }

    @Override
    protected void initView() {
        super.initView();

        weekFragments = new ArrayList<>();
        listDayInfos = new ArrayList<DayInfo>();//一定要new 对象不能直接引用，气死了
        initCurrentCalendar();
        listDayInfos.addAll(MonthViewCalendarUtil.getInstance(mContext).getDayInfo(calendar));

//        mrl_monthView.setDispatchView(vp_weekSchedule, ll_monthCalendarArea);

        linearLayouts = new LinearLayout[]{ll_1, ll_2, ll_3, ll_4, ll_5, ll_6};

        initCurrentMonthInfo();

        renderMonthCalendarData(false);

        //获取网络数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                    refreshMonthViewData();
            }
        }, 30);

    }

    /**
     * 初始化当前月份的日历的信息
     */
    private void initCurrentCalendar() {
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(TimestampTool.sdf_all.parse(TimestampTool.getCurrentDateToWeb()));
            calendar.add(Calendar.MONTH, currentPosition - 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化当前月份的信息
     */
    public void initCurrentMonthInfo() {
        currentMonthInfo = new CurrentMonthInfo();
        currentMonthInfo.weeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        currentMonthInfo.currentY_M = TimestampTool.sdf_yMp.format(calendar.getTime());
        currentMonthInfo.currentYM = TimestampTool.sdf_yM.format(calendar.getTime());
        currentMonthInfo.currentYMD = TimestampTool.sdf_yMd.format(calendar.getTime());

        LogUtils.i("MonthView", currentMonthInfo.weeks + " : " + currentMonthInfo.monthAreaWidth + " : " + currentMonthInfo.monthAreaHeight + " : " + currentMonthInfo.weekAreaHeight);

        ViewTreeObserver vto = ll_monthCalendarArea.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                currentMonthInfo.monthAreaWidth = ll_monthCalendarArea.getWidth();
                currentMonthInfo.monthAreaHeight = ll_monthCalendarArea.getHeight();

                currentMonthInfo.weekAreaHeight = (int) currentMonthInfo.monthAreaHeight / currentMonthInfo.weeks;

                setLayoutWidthHeight(vp_weekSchedule, currentMonthInfo.monthAreaWidth, currentMonthInfo.weeks == 5 ? (currentMonthInfo.weekAreaHeight * 3) : (currentMonthInfo.weekAreaHeight * 4));

                // 成功调用一次后，移除 Hook 方法，防止被反复调用
                // removeGlobalOnLayoutListener() 方法在 API 16 后不再使用
                // 使用新方法 removeOnGlobalLayoutListener() 代替
                ll_monthCalendarArea.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        ll_6.setVisibility(currentMonthInfo.weeks == 5 ? View.GONE : View.VISIBLE);

    }


    @Override
    protected void initData() {
        super.initData();

        loadData();

        parseData();

    }


    public void parseData() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    LogUtils.i("InputStream", System.currentTimeMillis() + "");

                    InputStream is = mContext.getAssets().open("testdata.json");
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i;
                    while ((i = is.read()) != -1) {
                        baos.write(i);
                    }

                    LogUtils.i("InputStream", System.currentTimeMillis() + "");

                    final List<ScheduleMonthDetail> scheduleMonthDetails = JsonUtils.stringToArray(baos.toString(), ScheduleMonthDetail[].class);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //防止快速切换月造成的数据不对问题
                                if (scheduleMonthDetails != null && scheduleMonthDetails.get(20).date.contains(currentMonthInfo.currentY_M)) {
                                    for (int j = 0; j < scheduleMonthDetails.size(); j++) {
                                        listDayInfos.get(j).todos.clear();
                                        listDayInfos.get(j).todos.addAll(PMUtils.getInstance().sortScheduleTodo(scheduleMonthDetails.get(j)));
                                    }
                                    renderMonthCalendarData(true);
                                }
                            }
                        });
                    }

                    is.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public void loadData() {

        for (int i = 0; i < 7; i++) {
            weekFragments.add(WeekViewFragment.newInstance());
        }

        vpStatePagerAdapter = new WeekViewFragmentAdapter(getChildFragmentManager(), weekFragments);
        vp_weekSchedule.setAdapter(vpStatePagerAdapter);
        vp_weekSchedule.setOnPageChangeListener(weekOnPageChangeListener);

//        srl_monthCalender.setOnRefreshListener(onRefreshListener);
//        srl_monthCalender.setOnCanRefreshListener(onCanRefreshListener);
    }

    //////////////////////////////////////////月视图start//////////////////////////////////////////////////////


    public LinearLayout getMonthCalendarArea() {
        return ll_monthCalendarArea;
    }

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

    @BindView(R.id.vp_weekSchedule)
    ChildViewPager vp_weekSchedule;


//    @BindView(R.id.srl_monthCalender)
//    public CustomSwipeToRefresh srl_monthCalender;

    public CurrentMonthInfo currentMonthInfo;
    private ViewGroup currentViewGroup;
    List<Fragment> weekFragments;
    private WeekViewFragmentAdapter vpStatePagerAdapter;


    private void stopRefresh() {
//        if (srl_monthCalender != null && srl_monthCalender.isRefreshing()) {
//            srl_monthCalender.setRefreshing(false);
//        }
    }


    public void refreshData(List<ScheduleToDo> tempTodo) {
        listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).todos.clear();
        listDayInfos.get(currentMonthInfo.currentOpenDayOfMonth).todos.addAll(tempTodo);
        renderMonthCalendarData(true);
    }


    private void addScheduleOrFestival(ViewGroup viewGroup, ScheduleToDo todo) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(10);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(mContext.getResources().getColor(todo.pIsDone ? R.color.black_a3 : R.color.black_33));
        textView.getPaint().setFlags(todo.pIsDone ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
        textView.getPaint().setAntiAlias(true);
        if (todo.pContainer.equals("IE")) {
            textView.setBackgroundResource(R.color.IE);
        } else if (todo.pContainer.equals("IU")) {
            textView.setBackgroundResource(R.color.IU);
        } else if (todo.pContainer.equals("UE")) {
            textView.setBackgroundResource(R.color.UE);
        } else if (todo.pContainer.equals("UU")) {
            textView.setBackgroundResource(R.color.UU);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(mContext, 14));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = CommonUtils.dip2px(mContext, 1);
        textView.setLayoutParams(lp);

        textView.setText(todo.pTitle);
        viewGroup.addView(textView);
    }

    private float getEditAreaTranslateHeight(int week) {
        return currentMonthInfo.weeks == 5 ? ((week + 1) == 5 ? (currentMonthInfo.initY + currentMonthInfo.weekAreaHeight * 2) : (currentMonthInfo.weekAreaHeight + currentMonthInfo.initY)) : ((week + 1) == 6 ? (currentMonthInfo.weekAreaHeight * 2 + currentMonthInfo.initY) : (currentMonthInfo.weekAreaHeight + currentMonthInfo.initY));
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
                translateView(ll_1, ll_1.getY(), (ll_1.getY() + currentMonthInfo.weekAreaHeight * transDistance51), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() + currentMonthInfo.weekAreaHeight * transDistance52), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() + currentMonthInfo.weekAreaHeight * transDistance53), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() + currentMonthInfo.weekAreaHeight * transDistance54), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() + currentMonthInfo.weekAreaHeight * transDistance55), transAnim);

                vp_weekSchedule.setVisibility(View.VISIBLE);
                translateView(vp_weekSchedule, (int) vp_weekSchedule.getY(), getEditAreaTranslateHeight(currentOpenWeek), 10);
            } else {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() + currentMonthInfo.weekAreaHeight * transDistance61), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() + currentMonthInfo.weekAreaHeight * transDistance62), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() + currentMonthInfo.weekAreaHeight * transDistance63), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() + currentMonthInfo.weekAreaHeight * transDistance64), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() + currentMonthInfo.weekAreaHeight * transDistance65), transAnim);
                translateView(ll_6, ll_6.getY(), (ll_6.getY() + currentMonthInfo.weekAreaHeight * transDistance66), transAnim);

                vp_weekSchedule.setVisibility(View.VISIBLE);
                translateView(vp_weekSchedule, (int) vp_weekSchedule.getY(), getEditAreaTranslateHeight(currentOpenWeek), 10);
            }
        } else {
            if (is5Week) {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() - currentMonthInfo.weekAreaHeight * transDistance51), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() - currentMonthInfo.weekAreaHeight * transDistance52), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() - currentMonthInfo.weekAreaHeight * transDistance53), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() - currentMonthInfo.weekAreaHeight * transDistance54), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() - currentMonthInfo.weekAreaHeight * transDistance55), transAnim);

                mMonthHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp_weekSchedule.setVisibility(View.GONE);
                    }
                }, transAnim);

            } else {
                translateView(ll_1, ll_1.getY(), (ll_1.getY() - currentMonthInfo.weekAreaHeight * transDistance61), transAnim);
                translateView(ll_2, ll_2.getY(), (ll_2.getY() - currentMonthInfo.weekAreaHeight * transDistance62), transAnim);
                translateView(ll_3, ll_3.getY(), (ll_3.getY() - currentMonthInfo.weekAreaHeight * transDistance63), transAnim);
                translateView(ll_4, ll_4.getY(), (ll_4.getY() - currentMonthInfo.weekAreaHeight * transDistance64), transAnim);
                translateView(ll_5, ll_5.getY(), (ll_5.getY() - currentMonthInfo.weekAreaHeight * transDistance65), transAnim);
                translateView(ll_6, ll_6.getY(), (ll_6.getY() - currentMonthInfo.weekAreaHeight * transDistance66), transAnim);

                mMonthHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp_weekSchedule.setVisibility(View.GONE);
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
     * 动态设置布局的高和宽
     *
     * @param viewGroup
     * @param screenWidth
     * @param screenHeight
     */
    private void setLayoutWidthHeight(ViewGroup viewGroup, float screenWidth, float screenHeight) {
        ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams(); //取控件textView当前的布局参数
        layoutParams.width = (int) screenWidth;
        layoutParams.height = (int) screenHeight;
        viewGroup.setLayoutParams(layoutParams);
    }

    ////////////////////////////////////日历部分////////////////////////////////////////////////////

    /**
     * @param addContent 是否添加内容
     */
    public void renderMonthCalendarData(boolean addContent) {
        //展開的時候刷新數據
//        int j = 0;
//        for (int i = currentMonthInfo.startPosition; i < currentMonthInfo.endPosition; i++) {
//            ((WeekViewFragment) tempFragments.get(j++)).addAndRefreshData(listDayInfos.get(i).todos);
//        }

        for (int i = 0; i < 42; i++) {
            final ViewGroup view = (ViewGroup) ll_monthCalendarArea.getChildAt(i / 7);
            final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(i % 7);
            LinearLayout ll_schedule = (LinearLayout) dayOfWeek.findViewById(R.id.ll_schedule);
            TextView tv_scheduleNum = (TextView) dayOfWeek.findViewById(R.id.tv_scheduleNum);

            if (addContent) {
                if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                    for (int k = 0; k < listDayInfos.get(i).todos.size() && k < 2; k++) {
                        addScheduleOrFestival(ll_schedule, listDayInfos.get(i).todos.get(k));
                    }
                    if (listDayInfos.get(i).todos.size() > 2) {
                        tv_scheduleNum.setText("+" + (listDayInfos.get(i).todos.size() - 2));
                    } else {
                        tv_scheduleNum.setText("");
                    }
                } else {
                    tv_scheduleNum.setText("");
                    ((TextView) dayOfWeek.findViewById(R.id.tv_gongli)).setText("");
                }
            } else {
                ll_schedule.removeAllViews();
                tv_scheduleNum.setText("");
                if (listDayInfos.get(i).dayType == DayType.DAY_TYPE_NOW) {
                    ((TextView) dayOfWeek.findViewById(R.id.tv_gongli)).setText(listDayInfos.get(i).day + "");
                    ((TextView) dayOfWeek.findViewById(R.id.tv_lunar)).setText(listDayInfos.get(i).lunarStr + "");
                    if (listDayInfos.get(i).holidays == 1) {
                        dayOfWeek.findViewById(R.id.tv_holidays).setVisibility(View.VISIBLE);
                        ((TextView) dayOfWeek.findViewById(R.id.tv_holidays)).setText("休");
                        dayOfWeek.findViewById(R.id.tv_holidays).setBackgroundColor(getResources().getColor(R.color.UU_border));
                    } else if (listDayInfos.get(i).holidays == 2) {
                        dayOfWeek.findViewById(R.id.tv_holidays).setVisibility(View.VISIBLE);
                        ((TextView) dayOfWeek.findViewById(R.id.tv_holidays)).setText("班");
                        dayOfWeek.findViewById(R.id.tv_holidays).setBackgroundColor(getResources().getColor(R.color.IE_border));
                    } else {
                        dayOfWeek.findViewById(R.id.tv_holidays).setVisibility(View.GONE);
                    }


                    LogUtils.i("renderMonthCalendarData", (i) + "::::" + listDayInfos.get(i).holidays + "::::::" + listDayInfos.get(i).date);


                    //设置背景
                    setMonthCalendarItemBackground(dayOfWeek, listDayInfos.get(i));

                    dayOfWeek.setTag(i);

                    dayOfWeek.setOnClickListener(onClickListener);

                } else {
                    ((TextView) dayOfWeek.findViewById(R.id.tv_gongli)).setText("");
                    ((TextView) dayOfWeek.findViewById(R.id.tv_lunar)).setText("");
                    dayOfWeek.findViewById(R.id.tv_holidays).setVisibility(View.GONE);

                    dayOfWeek.setTag(i);

                    dayOfWeek.setOnClickListener(null);
                }
            }
        }

    }


    /**
     * 渲染月视图的背景色
     */
    private void renderMonthCalendarBackground() {
        for (int i = 0; i < 42; i++) {
            ViewGroup view = (ViewGroup) ll_monthCalendarArea.getChildAt(i / 7);
            ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(i % 7);
            DayInfo dayInfo = listDayInfos.get((Integer) dayOfWeek.getTag());

            //设置背景
            setMonthCalendarItemBackground(dayOfWeek, dayInfo);
        }
    }

    /**
     * 设置月视图的item的背景色
     *
     * @param dayOfWeek
     * @param dayInfo
     */
    private void setMonthCalendarItemBackground(ViewGroup dayOfWeek, DayInfo dayInfo) {
        if (dayInfo.dayType == DayType.DAY_TYPE_NOW && (currentMonthInfo.currentY_M + "-" + String.format("%02d", dayInfo.day) + " 00:00:00").equals("2017-06-19 00:00:00")) {
            dayOfWeek.setBackgroundResource(R.drawable.shape_rect_border_da_bg_f5);
        } else if (dayInfo.dayType == DayType.DAY_TYPE_NOW && TimestampTool.getCurrentYearMonthDay().equals(currentMonthInfo.currentYM + String.format("%02d", dayInfo.day))) {
            dayOfWeek.setBackgroundResource(R.drawable.shape_rect_border_da_bg_bg);
        } else {
            dayOfWeek.setBackgroundResource(R.drawable.shape_rect_border_da_bg_white);
        }
    }


    ViewPager.OnPageChangeListener weekOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if (vp_weekSchedule.getVisibility() == View.VISIBLE && currentMonthInfo.currentOpenDayOfMonth != -1 && listDayInfos != null && listDayInfos.size() == 42) {
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

                WeekViewFragment weekScheduleFragment = (WeekViewFragment) vpStatePagerAdapter.getItem(position);
                weekScheduleFragment.addAndRefreshData(listDayInfos.get(currentDayInfo.position).todos);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void setItemBackground(int position) {
        for (int i = 0; i < 7; i++) {
            final DayInfo dayInfo = listDayInfos.get((Integer) ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).getTag());
            if (i == position) {
                ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundResource(R.drawable.shape_rect_border_da_bg_f5);
                //RSQApplication.getInstance().setCurrentSelectDate(currentMonthInfo.currentY_M + "-" + String.format("%02d", dayInfo.day) + " 00:00:00");
                //((HomeActivity) mContext).setIsToday();
            } else if (TimestampTool.getCurrentYearMonthDay().equals(currentMonthInfo.currentYM + String.format("%02d", dayInfo.day))) {
                ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundResource(R.drawable.shape_rect_border_da_bg_bg);
            } else {
                ((ViewGroup) currentViewGroup.getParent()).getChildAt(i).setBackgroundResource(R.drawable.shape_rect_border_da_bg_white);
            }

        }
    }


    private ViewGroup getCurrentClickView(int position) {
        return (ViewGroup) ((ViewGroup) ll_monthCalendarArea.getChildAt(position / 7)).getChildAt(position % 7);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && vp_weekSchedule != null && vp_weekSchedule.getVisibility() == View.VISIBLE
                && currentMonthInfo.isExpand && currentMonthInfo.currentOpenDayOfMonth != -1) {
            clickSomeDate(getCurrentClickView(currentMonthInfo.currentOpenDayOfMonth));
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickSomeDate(v);
        }
    };


    /**
     * 点击某一天
     *
     * @param v
     */
    private void clickSomeDate(View v) {

        if (listDayInfos == null){
            return;
        }

        currentDayInfo = listDayInfos.get((Integer) v.getTag());
        if (listDayInfos.get(currentDayInfo.position).dayType == DayType.DAY_TYPE_NOW) {

            isNeedReAddFragment = (-1 == currentMonthInfo.currentOpenDayOfMonth) || (currentMonthInfo.currentOpenDayOfMonth / 7 != currentDayInfo.position / 7);

            boolean isNeedChange = (currentDayInfo.position / 7 != currentMonthInfo.currentOpenWeek) ||
                    ((currentDayInfo.position / 7 == currentMonthInfo.currentOpenWeek) && currentMonthInfo.currentOpenDayOfWeek == (listDayInfos.get(currentDayInfo.position).whichWeek == 1 ? (currentDayInfo.position) % 7 - (7 - currentDayInfo.daysOfWeek) : currentDayInfo.position % 7));

            if (!isNeedReAddFragment && isCanTranslate(160) && !isNeedChange) {
                startClickTime = System.currentTimeMillis();
                if (listDayInfos.get(currentDayInfo.position).dayType == DayType.DAY_TYPE_NOW) {
                    currentViewGroup = (ViewGroup) v;
//                        RSQApplication.getInstance().setCurrentSelectDate(currentMonthInfo.currentY_M + "-" + String.format("%02d", currentDayInfo.day) + " 00:00:00");
//                        ((HomeActivity) mContext).setIsToday();
                    renderMonthCalendarBackground();
                    currentMonthInfo.currentOpenDayOfMonth = currentDayInfo.position;
                    mHandler.sendEmptyMessage(1000);
                    v.setBackgroundResource(R.drawable.shape_rect_border_da_bg_f5);
                }
            } else {
                if (isCanTranslate(400)) {
                    startClickTime = System.currentTimeMillis();
                    currentViewGroup = (ViewGroup) v;
//                        RSQApplication.getInstance().setCurrentSelectDate(currentMonthInfo.currentY_M + "-" + String.format("%02d", currentDayInfo.day) + " 00:00:00");
//                        ((HomeActivity) mContext).setIsToday();
                    renderMonthCalendarBackground();

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

    private void setWeekFragmentData(final DayInfo dayInfo) {
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

//////////////////////////////////////////月视图end//////////////////////////////////////////////////////


    public void parseData(String response) {

        stopRefresh();
        List<ScheduleMonthDetail> scheduleMonthDetails = JsonUtils.stringToArray(response, ScheduleMonthDetail[].class);
        //防止快速切换月造成的数据不对问题
        if (scheduleMonthDetails != null && scheduleMonthDetails.get(20).date.contains(currentMonthInfo.currentY_M)) {
            //数据处理
            if (currentMonthInfo.isNeedRemoveData) {
                for (int i = 0; i < 7; i++) {
                    scheduleMonthDetails.add(scheduleMonthDetails.remove(0));
                }
            }
            //数据赋值
            for (int i = 0; i < scheduleMonthDetails.size(); i++) {
                listDayInfos.get(i).todos.clear();
                listDayInfos.get(i).todos.addAll(PMUtils.getInstance().sortScheduleTodo(scheduleMonthDetails.get(i)));
            }
            renderMonthCalendarData(true);
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy", "onDestroy");
        listDayInfos = null;
        weekFragments = null;
    }


}
