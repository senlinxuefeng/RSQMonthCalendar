#高仿朝夕日历的月视图(求关注加星:blush: :blush: :blush:)
由于项目的需求，要做类似朝夕日历月视图效果，参考了好多资料，基本上实现了所需功能，加入了假数据，点击事件做了大量的判断，视图展开更加流畅，解决了周视图切换可能出现bug，求关注，希望可以帮助需要的朋友，有什么不懂得可以发邮件联系我（1107963848@qq.com)

遇到坑：<br>

   坑 1：textView.setSingleLine();textView.setSingleLine(true);<br>
   会影响抢焦点，导致viewpager不能正常滑动<br>

   坑 2：ViewPager setCurrentItem()后出现的问题，Can't change tag of fragment<br>
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
    
    坑 3：ViewPager + fragment + viewpager + fragment 无线循环<br>
    
    
    坑 4：ChildViewPager：内部viewpager自定义，最重要的是android:layout_marginTop="-500dp"，防止强焦点<br>
    
    
       <com.yumingchuan.rsqmonthcalendar.view.ChildViewPager
           android:id="@+id/vp_weekSchedule"
           android:layout_width="match_parent"
           android:layout_height="1dp"
           android:layout_marginTop="-500dp"
           android:visibility="gone" />
    
    
    坑 5：SwipeRefreshLayout 刷新直接导致子viewpager不能滑动了，目前没有解决，😭😭😭😭😭😭😭😭😭😭<br><br>
    
    
    
    坑 6：


![](https://github.com/senlinxuefeng/RSQMonthCalendar/raw/master/picture/monthcalendar2.gif)<br>


###Developed By ywmingchuan@gmail.com


:blush:  :innocent:  :sunny:  :sunflower:  :blush:  :innocent:  :sunny:  :sunflower:  :blush:  :innocent:  :sunny:  :sunflower:  :blush:  :innocent:  :sunny:  :sunflower:  :blush:  :innocent:  :sunny:  :sunflower:  :blush:  :innocent:  :sunny:  :sunflower:  :blush: 
