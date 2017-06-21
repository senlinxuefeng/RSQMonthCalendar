package com.yumingchuan.rsqmonthcalendar.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yumingchuan.rsqmonthcalendar.R;
import com.yumingchuan.rsqmonthcalendar.bean.ScheduleToDo;

import java.util.List;


public class MonthScheduleAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<ScheduleToDo> totalTodo;
    private ViewHolder holder;

    public MonthScheduleAdapter(Context mContext, List<ScheduleToDo> totalTodo) {
        super();
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.totalTodo = totalTodo;
    }

    @Override
    public int getCount() {
        return totalTodo.size();
    }

    @Override
    public Object getItem(int position) {
        return totalTodo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.item_week_schedule_calendar, parent, false);
        }
        holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.cb_choose = (RelativeLayout) view.findViewById(R.id.cb_choose);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_delayDays = (TextView) view.findViewById(R.id.tv_delayDays);
            holder.iv_alarmOrSendOrReceive = (TextView) view.findViewById(R.id.iv_alarmOrSendOrReceive);
            //holder.iv_alarmOrSendOrReceive.setTypeface(((HomeActivity) mContext).iconfont);
            holder.view_quadrant = view.findViewById(R.id.view_quadrant);
            view.setTag(holder);
        }

        if (totalTodo.get(position).pContainer.equals("IE")) {
            holder.view_quadrant.setBackgroundResource(R.color.IE_1);
        } else if (totalTodo.get(position).pContainer.equals("IU")) {
            holder.view_quadrant.setBackgroundResource(R.color.IU_1);
        } else if (totalTodo.get(position).pContainer.equals("UE")) {
            holder.view_quadrant.setBackgroundResource(R.color.UE_1);
        } else if (totalTodo.get(position).pContainer.equals("UU")) {
            holder.view_quadrant.setBackgroundResource(R.color.UU_1);
        } else {
            holder.view_quadrant.setBackgroundResource(R.color.white);
        }

        if (totalTodo.get(position).isSchedueleDelay && !totalTodo.get(position).pIsDone) {
            holder.iv_alarmOrSendOrReceive.setVisibility(View.GONE);
            holder.tv_delayDays.setVisibility(View.VISIBLE);
            holder.tv_delayDays.setText("延期" + totalTodo.get(position).delayDays + "天");
        } else {
            holder.tv_delayDays.setVisibility(View.GONE);

            // 提醒是否显示
//            if (totalTodo.get(position).clock != null) {
//                if (totalTodo.get(position).clock.isAlert) {
//                    holder.iv_alarmOrSendOrReceive.setVisibility(View.VISIBLE);
//                    holder.iv_alarmOrSendOrReceive.setText(mContext.getResources().getString(R.string.icon_access_alarm));
//                } else {
//                    setComeFrom(position);
//                }
//            } else {
//                setComeFrom(position);
//            }
        }

        if (totalTodo.get(position).pIsDone) {
            holder.cb_choose.setSelected(true);
            holder.tv_content.setText(totalTodo.get(position).pTitle);
            holder.tv_content.setTextColor(mContext.getResources().getColor(R.color.black_a3));
            holder.tv_content.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_content.getPaint().setAntiAlias(true);
        } else {
            holder.cb_choose.setSelected(false);
            holder.tv_content.setTextColor(mContext.getResources().getColor(R.color.black_33));
            holder.tv_content.getPaint().setFlags(0);
            holder.tv_content.getPaint().setAntiAlias(true);
            holder.tv_content.setText(totalTodo.get(position).pTitle);
        }

        holder.cb_choose.setTag(position);


        return view;
    }



    private class ViewHolder {
        private RelativeLayout cb_choose;// 标题
        private TextView tv_content;// 日期
        private TextView iv_alarmOrSendOrReceive;// 文字提示语
        private TextView tv_delayDays;
        private View view_quadrant;
    }
}
