package com.yumingchuan.rsqmonthcalendar;

import java.io.Serializable;


public class ScheduleToDo implements Comparable<ScheduleToDo>, Serializable {


    public String id;
    public String pUserId;
    public String username;
    public String pTitle;
    public String pNote;
    public boolean pIsDone;
    public String pPlanedTime;
    public String pDisplayOrder;
    public String pContainer;
    public String isFrom;
    public String dateCreated;
    public String receiverIds;
    public String clockAlert;
    public boolean isCloseRepeat;
    public String startDate;
    public String endDate;
    public String dates;
    public String delayDays;
    public String currentDay;


    public String repeatBaseTime;
    public String repeatType;
    public boolean isLastDate;
    public boolean isCurrentUserFocus;
    public String checkAuthority;
    public String editAuthority;
    public boolean alwaysRepeat;
    public String repeatOverDate;//yyyyMMdd

    public From from;

    public class From implements Serializable {
        public String levelOneId;
        public String levelOneName;
        public String levelTwoId;
        public String levelTwoName;
        public String levelThreeId;
        public String levelThreeName;
        public boolean isFromKanban;
    }


    @Override
    public int compareTo(ScheduleToDo another) {
        return (int) (Double.parseDouble(pDisplayOrder) - Double.parseDouble(another.pDisplayOrder));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScheduleToDo) {
            ScheduleToDo todo = (ScheduleToDo) o;
            if ((id == todo.id)) {
                return true;
            } else
                return false;
        } else {
            return false;
        }
    }
}

