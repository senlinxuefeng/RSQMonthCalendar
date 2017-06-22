package com.yumingchuan.rsqmonthcalendar.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.WindowManager;

import com.yumingchuan.rsqmonthcalendar.bean.ScheduleMonthDetail;
import com.yumingchuan.rsqmonthcalendar.bean.ScheduleToDo;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;


/**
 * 公共的常用方法
 */
public class PMUtils {

    private static PMUtils pmUtils;

    public static PMUtils getInstance() {
        if (pmUtils == null) {//双重校验DCL单例模式
            synchronized (PMUtils.class) {//同步代码块
                if (pmUtils == null) {
                    pmUtils = new PMUtils();
                }
            }
        }
        return pmUtils;
    }

    /**
     * 取消软键盘的遮挡
     *
     * @param activity
     */
    public void setSoftInputMode(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 返回当前程序版本名
     */
    //版本名
    public String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


    public String weekToDate(List<String> weekDays) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int week = c.get(Calendar.DAY_OF_WEEK) - 1;///即中国星期
        List<String> tempDates = new ArrayList<String>();
        String dateTime = "";
        for (int i = 0; i < weekDays.size(); i++) {
            switch (weekDays.get(i)) {
                case "周一":
                    dateTime = new Timestamp(addDateOneDay(1 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
                case "周二":
                    dateTime = new Timestamp(addDateOneDay(2 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
                case "周三":
                    dateTime = new Timestamp(addDateOneDay(3 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
                case "周四":
                    dateTime = new Timestamp(addDateOneDay(4 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
                case "周五":
                    dateTime = new Timestamp(addDateOneDay(5 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
                case "周六":
                    dateTime = new Timestamp(addDateOneDay(6 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
                case "周日":
                    dateTime = new Timestamp(addDateOneDay(0 - week).getTime()).toString().substring(0, 10).replace("-", "");
                    tempDates.add(dateTime);
                    break;
            }
        }

        String dateStr = "";
        for (int i = 0; i < tempDates.size(); i++) {
            if (i == tempDates.size() - 1) {
                dateStr += tempDates.get(i);
            } else {
                dateStr += tempDates.get(i) + ",";
            }
        }
        return dateStr;
    }


    public List<String> dateToWeeks(String[] days) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        List<String> tempWeeks = new ArrayList<String>();
        for (int i = 0; i < days.length; i++) {
            try {
                c.setTimeInMillis(TimestampTool.getSomedayTime(TimestampTool.dateToDate(days[i])));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int week = c.get(Calendar.DAY_OF_WEEK) - 1;///即中国星期
            switch (week) {
                case 0:
                    tempWeeks.add("周日");
                    break;
                case 1:
                    tempWeeks.add("周一");
                    break;
                case 2:
                    tempWeeks.add("周二");
                    break;
                case 3:
                    tempWeeks.add("周三");
                    break;
                case 4:
                    tempWeeks.add("周四");
                    break;
                case 5:
                    tempWeeks.add("周五");
                    break;
                case 6:
                    tempWeeks.add("周六");
                    break;
            }
        }
        return tempWeeks;
    }


    SimpleDateFormat simpleDateFormat_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat simpleDateFormat_d = new SimpleDateFormat("d");

    public List<String> datesToPerMonth(String[] days) {
        List<String> tempDays = new ArrayList<String>();
        try {
            for (int i = 0; i < days.length; i++) {
                tempDays.add(simpleDateFormat_d.format(simpleDateFormat_yyyyMMdd.parse(days[i])));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempDays;
    }


    /**
     * 返回当前天数的前后几天的时间和日期的对象,,,返回20150925
     *
     * @param delayOrAfter
     * @return
     */
    public Date addDateOneDay(int delayOrAfter) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.DATE, delayOrAfter); //日期加1天
        date = c.getTime();
        return date;
    }

    public String monthToDate(List<String> monthDays) {
        String dateTime = "";
        for (int i = 0; i < monthDays.size(); i++) {
            if (i == monthDays.size() - 1) {
                dateTime += "201601" + String.format("%02d", Integer.parseInt(monthDays.get(i)));
            } else {
                dateTime += "201601" + String.format("%02d", Integer.parseInt(monthDays.get(i))) + ",";
            }
        }
        return dateTime;
    }

    /**
     * 回收bitmap对象
     *
     * @param bitmap
     * @return
     */
    public void recycleBitmap(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    List<Object> todoTempObject = new ArrayList<Object>();



    /**
     * 判断应用是否已经启动
     *
     * @param mContext    一个mContext
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public boolean isAppAlive(Context mContext, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
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


    public double getMaxDisplayOrder(String pContainer, List<ScheduleToDo> tempTodos) {
        double pDisplayOrder = 65535;
        List<ScheduleToDo> tempMaxTodos = new ArrayList<ScheduleToDo>();
        if (tempTodos != null && pContainer != null) {
            for (int i = 0; i < tempTodos.size(); i++) {
                if (tempTodos.get(i).pContainer.equals(pContainer)) {
                    tempMaxTodos.add(tempTodos.get(i));
                }
            }
            if (tempMaxTodos.size() > 0) {
                Collections.sort(tempMaxTodos);
                Collections.reverse(tempMaxTodos);
                pDisplayOrder = Double.parseDouble(tempMaxTodos.get(0).pDisplayOrder) + 65536;
            }
        }
        return pDisplayOrder;
    }


    public int totalSchedules = 0;
    public int needDoSchedules = 0;
    public int haveDoneSchedules = 0;

    /**
     * 对日程的排序
     *
     * @param scheduleMonthDetail
     * @return
     */
    public List<ScheduleToDo> sortScheduleTodo(ScheduleMonthDetail scheduleMonthDetail) {

        totalSchedules = 0;
        needDoSchedules = 0;
        haveDoneSchedules = 0;
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
            needDoSchedules = totalTodos.size();

            totalTodos.addAll(doneIE);
            totalTodos.addAll(doneIU);
            totalTodos.addAll(doneUE);
            totalTodos.addAll(doneUU);

            haveDoneSchedules = totalTodos.size() - needDoSchedules;
            totalSchedules = totalTodos.size();
        }
        return totalTodos;
    }


    public List<ScheduleToDo> sortTempScheduleTodo(List<ScheduleToDo> tempTodos) {
        totalTodos.clear();
        IETodos.clear();
        IUTodos.clear();
        UETodos.clear();
        UUTodos.clear();

        noDoneIE.clear();
        doneIE.clear();
        noDoneIU.clear();
        doneIU.clear();
        noDoneUE.clear();
        doneUE.clear();
        noDoneUU.clear();
        doneUU.clear();

        if (tempTodos != null) {
            for (int i = 0; i < tempTodos.size(); i++) {
                if (tempTodos.get(i).pContainer.equals("IE")) {
                    IETodos.add(tempTodos.get(i));
                } else if (tempTodos.get(i).pContainer.equals("IU")) {
                    IUTodos.add(tempTodos.get(i));
                } else if (tempTodos.get(i).pContainer.equals("UE")) {
                    UETodos.add(tempTodos.get(i));
                } else {
                    UUTodos.add(tempTodos.get(i));
                }
            }
        }

        sortDoOrDone(IETodos, noDoneIE, doneIE);
        sortDoOrDone(IUTodos, noDoneIU, doneIU);
        sortDoOrDone(UETodos, noDoneUE, doneUE);
        sortDoOrDone(UUTodos, noDoneUU, doneUU);

        totalTodos.addAll(noDoneIE);
        totalTodos.addAll(noDoneIU);
        totalTodos.addAll(noDoneUE);
        totalTodos.addAll(noDoneUU);

        totalTodos.addAll(doneIE);
        totalTodos.addAll(doneIU);
        totalTodos.addAll(doneUE);
        totalTodos.addAll(doneUU);

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


    private List<ScheduleToDo> schedules = new ArrayList<ScheduleToDo>();
    private List<ScheduleToDo> doneSchedules = new ArrayList<ScheduleToDo>();


    /**
     * 对单象限日程排序
     *
     * @param tempTodos
     */
    public List<ScheduleToDo> sortSingleViewSchedule(List<ScheduleToDo> tempTodos) {
        schedules.clear();
        doneSchedules.clear();
        if (tempTodos != null && 0 != tempTodos.size()) {
            Collections.sort(tempTodos);
            Collections.reverse(tempTodos);
            for (int i = 0; i < tempTodos.size(); i++) {
                if (tempTodos.get(i).pIsDone) {
                    doneSchedules.add(tempTodos.get(i));
                } else {
                    schedules.add(tempTodos.get(i));
                }
            }
            tempTodos.clear();
            tempTodos.addAll(schedules);
            tempTodos.addAll(doneSchedules);
        }
        return tempTodos;
    }




    /**
     * 获取ip地址
     *
     * @return
     */
    public String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }


}
