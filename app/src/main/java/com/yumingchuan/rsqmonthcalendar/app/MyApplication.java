package com.yumingchuan.rsqmonthcalendar.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.yumingchuan.rsqmonthcalendar.utils.TimestampTool;

/**
 * Created by yumingchuan on 2017/11/26.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;

    private String currentSelectDate = TimestampTool.getCurrentDateToWeb();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * @return 返回当前应用选中的日期
     */
    public String getCurrentSelectDate() {
        return TextUtils.isEmpty(currentSelectDate) ? TimestampTool.getCurrentDateToWeb() : currentSelectDate;
    }

}
