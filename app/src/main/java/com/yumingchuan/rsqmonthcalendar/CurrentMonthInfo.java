package com.yumingchuan.rsqmonthcalendar;

/**
 * Created by yumingchuan on 16/10/21.
 */

public class CurrentMonthInfo {

    public boolean isNeedRemoveData;
    public boolean isExpand;//是否是展开的
    public int weeks = 6;//一共多少周
    public int currentOpenWeek = -1;//打开的行
    public int currentOpenDayOfWeek = -1;//打开的天在本周的位置
    public int currentOpenDayOfMonth = -1;//打开的位置在月中的位置
    public float monthEditAreaHeight;
    public float monthEditAreaWidth;
    public int weekEditAreaHeight;

    public float initY ;

    public String currentYM;//yyyyMM
    public String currentY_M; //yyyy-mm
    public String currentYMD;//yyyyMMdd


    public int startPosition = -1;//周日曆的開始位置
    public int endPosition = -1;//周日曆的結束位置

}
