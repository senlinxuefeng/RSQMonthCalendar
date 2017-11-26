# 高仿朝夕日历,滴答清单，当日历软件的月视图
由于项目的需求，参考了大量的日历相关资料，基本上实现了所需功能，加入了假数据，希望可以帮助需要的朋友，有什么不懂得可以发邮件联系我（1107963848@qq.com)
## 效果图：

![](https://github.com/senlinxuefeng/RSQMonthCalendar/raw/master/picture/monthcalendar2.gif)<br>

## 遇到坑：<br>

#### 坑 1：ViewPager setCurrentItem()后出现的问题，Can't change tag of fragment<br>
    参考代码：
    
    private List<PageFragment> pages;
    //...
    public PageFragment getItem(int position) {
        PageFragment page = null;
        if (pages.size() > position) {
            page = pages.get(position);
            if (page != null) {
                return page;
            }
        }

        while (position>=pages.size()) {
            pages.add(null);
        }
        page = PageFragment.newPage(pageList.get(position),position);
        pages.set(position, page);
        return page;
    }
    
#### 坑 2：ViewPager + fragment + viewpager + View 无线循环<br>
    
#### 坑 3：ChildViewPager：内部viewpager自定义，最重要的是android:layout_marginTop="-500dp"，防止强焦点<br>
    
       <com.yumingchuan.rsqmonthcalendar.view.ChildViewPager
           android:id="@+id/vp_weekSchedule"
           android:layout_width="match_parent"
           android:layout_height="1dp"
           android:layout_marginTop="-500dp"
           android:visibility="gone" />
    
 