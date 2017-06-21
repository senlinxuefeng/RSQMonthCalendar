package com.yumingchuan.rsqmonthcalendar.bean;

import java.util.List;

public class ScheduleData {
    public List<ScheduleToDo> IETodos;// 紧急重要
    public List<ScheduleToDo> IUTodos;// 重要不紧急
    public List<ScheduleToDo> UETodos;// 紧急不重要
    public List<ScheduleToDo> UUTodos;// 不重要不紧急
}
