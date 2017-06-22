package com.yumingchuan.rsqmonthcalendar.utils;

import android.content.Context;

/**
 * Created by love on 2015/4/29.
 */

/**
 * 工具类
 */
public class CommonUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
