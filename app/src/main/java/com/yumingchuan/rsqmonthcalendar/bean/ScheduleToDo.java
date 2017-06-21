package com.yumingchuan.rsqmonthcalendar.bean;

import java.io.Serializable;
import java.util.List;


public class ScheduleToDo implements Comparable<ScheduleToDo>, Serializable {

    public int whichType;//是不是日期
    public String whichDate;//具体的日期
    public String donePercentage;//完成的比例

    public String id;
    public String pUserId;
    public String username;
    public String pTitle;
    public String pNote;
    public boolean pIsDone;
    public String pPlanedTime;
    public String pDisplayOrder;
    public String pContainer;
    public String inboxPContainer;
    public String isFrom;
    public String dateCreated;
    public String receiverIds;
    public String clockAlert;
    public boolean isCloseRepeat;
    public boolean isSchedueleDelay;
    public String startDate;
    public String endDate;
    public String dates;
    public String delayDays;
    public String pFinishedTime;

    public String currentDay;


    public String repeatBaseTime;
    public String repeatType;
    public boolean isLastDate;

    public boolean isCurrentUserFocus;

    public List<ReceiverUser> receiverUser;
    public String checkAuthority;
    public String editAuthority;
    public boolean alwaysRepeat;
    public String repeatOverDate;//yyyyMMdd

    public From from;


    public boolean isToday;

    public String todoLabelIds;
    public String itemLabelIds;


    public class From implements Serializable {
        public String levelOneId;
        public String levelOneName;
        public String levelTwoId;
        public String levelTwoName;
        public String levelThreeId;
        public String levelThreeName;
        public String levelFourId;
        public String levelFourName;
        public boolean isFromKanban;

    }


    public class ReceiverUser implements Serializable {
        public String id;


    }

    @Override
    public int compareTo(ScheduleToDo another) {
        return (int) (Double.parseDouble(pDisplayOrder) - Double.parseDouble(another.pDisplayOrder));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScheduleToDo) {
            ScheduleToDo todo = (ScheduleToDo) o;
            return (id == todo.id);
        } else {
            return false;
        }
    }
}

