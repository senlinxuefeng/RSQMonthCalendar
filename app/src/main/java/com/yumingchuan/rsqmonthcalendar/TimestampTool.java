package com.yumingchuan.rsqmonthcalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampTool {

    public static SimpleDateFormat sdf_yMd = new SimpleDateFormat("yyyyMMdd");

    /**
     * 得到年月
     *
     * @return String ex:201311
     */
    public static String getCurrentYearMonthDay() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

}
