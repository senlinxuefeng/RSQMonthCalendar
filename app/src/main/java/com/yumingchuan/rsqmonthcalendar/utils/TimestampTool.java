package com.yumingchuan.rsqmonthcalendar.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimestampTool {

    //斜杠（slash）:s，平行杠的约定（parallel):p
    public static SimpleDateFormat sdf_all = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf_ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat sdf_hm = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat sdf_all_z = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.CHINA);

    public static SimpleDateFormat sdf_yMd = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat sdf_yM = new SimpleDateFormat("yyyyMM");
    public static SimpleDateFormat sdf_YM = new SimpleDateFormat("yyyy年M月");
    public static SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyyy年M月d日");
    public static SimpleDateFormat sdf_YMMDD = new SimpleDateFormat("yyyy年MM月dd日");


    public static SimpleDateFormat sdf_Md = new SimpleDateFormat("M月d");
    public static SimpleDateFormat sdf_MdE = new SimpleDateFormat("M月d EEEE");
    public static SimpleDateFormat sdf_MD = new SimpleDateFormat("M月d日");
    public static SimpleDateFormat sdf_MMDD = new SimpleDateFormat("MM月dd日");

    public static SimpleDateFormat sdf_yMds = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat sdf_yMdp = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdf_yMp = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat sdf_yMd_dot = new SimpleDateFormat("yyyy.MM.dd");
    public static SimpleDateFormat sdf_yM_dot = new SimpleDateFormat("yyyy.MM");
    public static SimpleDateFormat sdf_Mdp = new SimpleDateFormat("MM.dd");
    public static SimpleDateFormat sdf_d = new SimpleDateFormat("d");
    public static SimpleDateFormat sdf_M = new SimpleDateFormat("M月");
    public static SimpleDateFormat sdf_m = new SimpleDateFormat("M");
    public static SimpleDateFormat sdf_y = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat sdf_Y = new SimpleDateFormat("yyyy年");

    public static Calendar mCalendar = Calendar.getInstance();//创建一个日期实例





    /**
     * 获取某天本周第一天是星期日的日期，例如2016-12-28 00:00:00 输出：2016-12-25
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getSomeWeekFirstDay(String dateStr, boolean isLast) {

        String tempStr = "";
        try {
            Calendar mCalendar = Calendar.getInstance();
            Date date = sdf_all.parse(dateStr);
            mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
            mCalendar.setTime(date);
            mCalendar.add(Calendar.DATE, -mCalendar.get(Calendar.DAY_OF_WEEK) + 1);
            if (isLast) {
                mCalendar.add(Calendar.DATE, 6);
            }
            tempStr = sdf_yMdp.format(mCalendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempStr;
    }


    /**
     * 获取某天本周第一天是星期日的日期，例如2016-12-28 00:00:00 输出：2016-12-25
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getSomeMonthFirstOrLastDay(String dateStr, boolean isLast) {

        String tempStr = "";
        try {
            Calendar mCalendar = Calendar.getInstance();
            Date date = sdf_all.parse(dateStr);
            mCalendar.setTime(date);

            mCalendar.set(Calendar.DAY_OF_MONTH, 1);//设置为本月1日
            int week = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
            mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.get(Calendar.DAY_OF_MONTH) - (week == 0 ? 7 : week));

            if (isLast) {
                mCalendar.add(Calendar.DATE, 41);
            }
            tempStr = sdf_yMdp.format(mCalendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempStr;
    }


    /**
     * @param type
     * @param dateStr
     * @return true 表示不延期
     */
    public static boolean compareToToday(int type, String dateStr) {
        boolean flag = false;
        try {
            if (type == 0) {///"yyyy-MM-dd"
                flag = getSomedayTime(dateStr + " 00:00:00") - getSomedayTime(getCurrentDateToWeb()) >= 0;
            } else if (type == 1) {//yyyyMMdd
                flag = getSomedayTime(sdf_yMdp.format(sdf_yMd.parse(dateStr)) + " 00:00:00") - getSomedayTime(getCurrentDateToWeb()) >= 0;
            } else if (type == 2) {///"yyyy-MM-dd  00:00:00"
                flag = getSomedayTime(dateStr) - getSomedayTime(getCurrentDateToWeb()) >= 0;
            }
        } catch (Exception e) {

        }
        return flag;
    }


    /**
     * 获取当前日期的date
     *
     * @return
     * @throws ParseException
     */
    public static Date getDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获得某日前后的某一天
     *
     * @param date java.util.Date
     * @param day  int
     * @return java.util.Date
     */
    public static Date getDay(Date date, int day) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(GregorianCalendar.DATE, day);
        return c.getTime();
    }

    /**
     * @param timeStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long getSomedayTime(String timeStr) {

        Date date = null;
        try {
            date = sdf_all.parse(timeStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    /**
     * 获取当前时间的字符串 用来提交给web端
     *
     * @return String ex:2013-11-21 00:00:00
     */
    public static String getCurrentDateToWeb() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
    }

    /**
     * 获取当前时间的字符串 用来提交给web端
     *
     * @return String ex:2013-11-21
     */
    public static String getCurrentDateToWebYMD() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }


    /**
     * 得到年月
     *
     * @return String ex:20131102
     */
    public static String getCurrentYearMonthDay() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }


    /*
     *20150924->2015-09-24 00:00:00
	 */
    public static String dateToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(date)) + " 00:00:00";
    }

    /**
     * 获取当前时间的字符串
     *
     * @return String ex:2006-07-07
     */
    public static String getCurrentDate() {
        Timestamp d = new Timestamp(System.currentTimeMillis());
        return d.toString().substring(0, 10);
    }

    /**
     * 获取当前时间的字符串
     *
     * @return String ex:2006-07-07 22:10:10
     */
    public static String getCurrentDateTime() {
        return sdf_all.format(new Date());
    }


    /**
     * 字符串的日期格式的计算 yyyy-MM-dd
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf_yMdp.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf_yMdp.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }


    /**
     * @param tempDate yyyy-MM-dd HH:mm:ss
     * @return 获取多少周
     */
    public static int getWhichWeek(String tempDate) throws ParseException {
        mCalendar.setTime(TimestampTool.sdf_all.parse(tempDate));
        return mCalendar.get(Calendar.WEEK_OF_YEAR);
    }


    /**
     * 获取周一周末的日期
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return 【yyyy-MM-dd】
     */
    public static String[] getMondaySunday(String dateStr) throws ParseException {
        String[] range = new String[2];
        Calendar mCalendar = Calendar.getInstance();
        Date date = sdf_all.parse(dateStr);
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        mCalendar.setTime(date);
        mCalendar.add(Calendar.DATE, -mCalendar.get(Calendar.DAY_OF_WEEK) + 2);
        range[0] = sdf_yMdp.format(mCalendar.getTime());
        mCalendar.add(Calendar.DATE, 6);
        range[1] = sdf_yMdp.format(mCalendar.getTime());

        return range;
    }


    /**
     * @param dateStr1
     * @param dateStr2
     * @return 比较两天时间的大小
     */
    public static int compareTwoDays(String dateStr1, String dateStr2) {
        return getSomedayTime(dateStr1) - getSomedayTime(dateStr2) > 0 ? 1 : getSomedayTime(dateStr1) - getSomedayTime(dateStr2) == 0 ? 0 : -1;
    }


    public static String ymdToMd(String createTaskDate) {
        try {
            return sdf_MD.format(TimestampTool.sdf_yMd.parse(createTaskDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return createTaskDate;
    }

    public static String allToHm(Date all) {
        return sdf_hm.format(all);
    }
    public static String allToMd(Date all) {
        return sdf_MD.format(all);
    }
}
