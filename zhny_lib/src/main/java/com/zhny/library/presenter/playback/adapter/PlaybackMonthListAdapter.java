package com.zhny.library.presenter.playback.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhny.library.R;
import com.zhny.library.presenter.playback.listener.OnItemClickListener;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.utils.DataUtil;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-07 1:03
 */
public class PlaybackMonthListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> groups;
    private ArrayList<ArrayList<PlaybackMonthDetailDto>> childs;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private String today;

    public PlaybackMonthListAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        groups = new ArrayList<>();
        childs = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        today = TimeUtils.getDateStr(new Date(), 0);
    }

    public void updateData(List<String> groupData, ArrayList<ArrayList<PlaybackMonthDetailDto>> chindData) {
        groups.clear();
        groups.addAll(groupData);
        childs.clear();
        childs.addAll(chindData);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_goup, parent, false);
        }
        String group = groups.get(groupPosition);
        ImageView expandedImage = convertView.findViewById(R.id.expandedImage);
        TextView tvGroup = convertView.findViewById(R.id.month);
        tvGroup.setText(group);

        expandedImage.setBackgroundResource(isExpanded ? R.drawable.icon_up : R.drawable.icon_down);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        PlaybackMonthDetailDto dto = childs.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_child, parent, false);
        }
        RelativeLayout childLayout = convertView.findViewById(R.id.childLayout);
        TextView date = convertView.findViewById(R.id.date);
        TextView workArea = convertView.findViewById(R.id.workArea);
        TextView workTime = convertView.findViewById(R.id.workTime);
        TextView unlineTime = convertView.findViewById(R.id.unlineTime);
        TextView mileage = convertView.findViewById(R.id.mileage);
        TextView runningTime = convertView.findViewById(R.id.runningTime);
        TextView workType = convertView.findViewById(R.id.workType);

        childLayout.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(dto);
            }
        });

        date.setText(dto.date == null ? "" : dto.date);

        boolean isToday = TextUtils.equals(today,  dto.workDetail.rptDate);
        workTime.setText(context.getString(R.string.workTime).concat(TimeUtils.timeStamp2Hm(dto.workDetail.jobDuration)));
        unlineTime.setText(context.getString(R.string.unlineTime).concat(isToday ? "-" : TimeUtils.timeStamp2Hm(dto.workDetail.offlineDuration)));
        runningTime.setText(context.getString(R.string.runningTime).concat(isToday ? "-" : TimeUtils.timeStamp2Hm(dto.workDetail.runningDuration)));
        workArea.setText(context.getString(R.string.workArea)
                .concat(DataUtil.get2Point(dto.workDetail.reportArea))
                .concat(context.getString(R.string.unitOfArea)));
        mileage.setText(context.getString(R.string.mileage)
                .concat(DataUtil.get2Point(dto.workDetail.mileage))
                .concat(context.getString(R.string.km)));
        workType.setText(context.getString(R.string.workType).concat(TextUtils.isEmpty(dto.workDetail.jobType) ? "" : dto.workDetail.jobType));



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
