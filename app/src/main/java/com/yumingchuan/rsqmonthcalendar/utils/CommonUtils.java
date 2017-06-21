package com.yumingchuan.rsqmonthcalendar.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
     * Andorid获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 求出图片的宽高比
     *
     * @param picPath
     * @return
     */
    public static double getPicWidth2Height(String picPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        LogUtils.i("addPicToLogContentFile",options.outWidth+"jjjj"+options.outHeight);
        return (double) options.outWidth / (double)options.outHeight;
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
