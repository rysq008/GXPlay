package com.zhny.library.presenter.alarm.adapter;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemAlarmFenceListBinding;
import com.zhny.library.databinding.ItemAlarmJobListBinding;
import com.zhny.library.presenter.alarm.dto.AlarmDto;
import com.zhny.library.presenter.alarm.util.AlarmUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MsgHolder> {

    private static final int ALARM_FENCE = 0;
    private static final int ALARM_JOB = 1;

    private List<AlarmDto> dataList;
    private OnMsgDetailListener onMsgDetailListener;

    public AlarmAdapter(OnMsgDetailListener onMsgDetailListener) {
        dataList = new ArrayList<>();
        this.onMsgDetailListener = onMsgDetailListener;
    }

    public void updateData(List<AlarmDto> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ALARM_JOB) {
            ItemAlarmJobListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_alarm_job_list, parent, false);
            return new JobRecyclerHolder(binding);
        } else {
            ItemAlarmFenceListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_alarm_fence_list, parent, false);
            return new FenceRecyclerHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MsgHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return TextUtils.equals(dataList.get(position).typeCode, "JOB_INFO") ? ALARM_JOB : ALARM_FENCE;
    }


    //job
    class JobRecyclerHolder extends MsgHolder {
        private ItemAlarmJobListBinding binding;

        JobRecyclerHolder(ItemAlarmJobListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        void bind(AlarmDto alarmDto) {
            binding.setAlarmDto(alarmDto);
            binding.setOnMsgDetailListener(onMsgDetailListener);
            long timeStamp = AlarmUtil.date2TimeStamp(alarmDto.startDate, "yyyy-MM-dd HH:mm:ss");
            alarmDto.convertDate = AlarmUtil.formatTime(timeStamp);
            binding.executePendingBindings();
        }
    }

    //fence
    class FenceRecyclerHolder extends MsgHolder {
        private ItemAlarmFenceListBinding binding;

        FenceRecyclerHolder(ItemAlarmFenceListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        void bind(AlarmDto alarmDto) {
            binding.setAlarmDto(alarmDto);
            long timeStamp = AlarmUtil.date2TimeStamp(alarmDto.startDate, "yyyy-MM-dd HH:mm:ss");
            alarmDto.convertDate = AlarmUtil.formatTime(timeStamp);
            binding.executePendingBindings();
        }
    }


    // parent msg
    abstract class MsgHolder extends RecyclerView.ViewHolder {

        MsgHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bind(AlarmDto alarmDto);
    }


    public interface OnMsgDetailListener {
        void onMsgDetail(AlarmDto alarmDto);
    }

}
