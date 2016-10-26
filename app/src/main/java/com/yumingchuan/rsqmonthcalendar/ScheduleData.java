package com.yumingchuan.rsqmonthcalendar;

import java.io.Serializable;
import java.util.List;

public class ScheduleData {
    public List<Todo> IETodos;// 紧急重要
    public List<Todo> IUTodos;// 重要不紧急
    public List<Todo> UETodos;// 紧急不重要
    public List<Todo> UUTodos;// 不重要不紧急
    

    public class Todo implements Comparable<Todo>, Serializable {
        public String id;
        public String pUserId;
        public String username;
        public String pTitle;
        public String pNote;
        public List<CheckPoint> checkPoints;
        public boolean pIsDone;
        public String pPlanedTime;
        public String pFinishedTime;
        public String pDisplayOrder;
        public String pContainer;
        public String pParentId;
        public String isFrom;
        public String dateCreated;
        public String lastUpdated;
        public String expired;
        public String hide;
        public String createdByClient;
        public String receiverIds;
        public String receiverNames;
        public String senderId;
        public String senderName;
        public String isDeleted;
        public String cid;
        public String hasRepeatTag;
        public String notePage;
        public String clockAlert;
        public String isNonCreate;
        public String monthDisplayOrder;
        public String repeatTagId;
        //public ScheduleDetailContainChildTask.KanbanItem kanbanItem;
        public String isRevoke;
        public String isCloseRepeat;
        public String startDate;
        public String endDate;
        public String dates;
        public String delayDays;

        public boolean isSchedueleDelay;

        public String currentDay;

        //public EditControlSchedule editControl;




        public int cardBackground;//1:123,2:13,3:134,4:1234
        public boolean isNoNeedShowLine;//1:123,2:13,3:134,4:1234

        @Override
        public int compareTo(Todo another) {
            return (int) (Double.parseDouble(pDisplayOrder) - Double.parseDouble(another.pDisplayOrder));
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Todo) {
                Todo todo = (Todo) o;
                if ((id == todo.id)) {
                    return true;
                } else
                    return false;
            } else {
                return false;
            }
        }
    }
    class CheckPoint implements Serializable{
        public String id;
        public String title;
        public String note;
        public String isDone;
        public String finishedTime;
        public String displayOrder;
        public String todoId;
    }

}
