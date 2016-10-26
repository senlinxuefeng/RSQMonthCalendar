package com.yumingchuan.rsqmonthcalendar;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by love on 2015/4/29.
 */

/**
 * 工具类
 */
public class CommonUtils {
    /**
     * 吐司
     *
     * @param activity
     * @param data
     */
    public static void toast(Activity activity, String data) {
        Toast.makeText(activity, data, Toast.LENGTH_SHORT).show();
    }

    /**
     * 计算屏幕的分辨率
     *
     * @param activity 当前的activity
     * @return 屏幕的宽度
     */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 计算屏幕的分辨率
     *
     * @param activity 当前的activity
     * @return 屏幕的高度
     */
    public final static int getWindowsHeigh(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
