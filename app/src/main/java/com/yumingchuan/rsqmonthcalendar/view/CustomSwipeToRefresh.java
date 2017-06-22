package com.yumingchuan.rsqmonthcalendar.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by yumingchuan on 2016/10/28.
 */

public class CustomSwipeToRefresh extends SwipeRefreshLayout {

    private int mTouchSlop;
    private float mPrevX;
    private OnCanRefreshListener onCanRefreshListener;


    public CustomSwipeToRefresh(Context context) {
        super(context);
    }

    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        //判断用户在进行滑动操作的最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                //获取水平移动距离
                float xDiff = Math.abs(eventX - mPrevX);
                //当水平移动距离大于滑动操作的最小距离的时候就认为进行了横向滑动
                //不进行事件拦截,并将这个事件交给子View处理
                try {
                    if (xDiff > mTouchSlop) {//|| !onCanRefreshListener.isCanRefresh()
                        return false;
                    }
                } catch (Exception e) {

                }
        }

        return super.onInterceptTouchEvent(event);
    }


    public interface OnCanRefreshListener {
        boolean isCanRefresh();//需要回调的方法
    }

    public void setOnCanRefreshListener(OnCanRefreshListener onCanRefreshListener) {
        this.onCanRefreshListener = onCanRefreshListener;
    }
}
