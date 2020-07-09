package com.zhny.library.presenter.work.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemFristWorkBinding;
import com.zhny.library.presenter.work.dto.WorkDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class FristWorkAdapter extends RecyclerView.Adapter<FristWorkAdapter.RecyclerHolder> {
    private List<WorkDto> dataList;
    private WorkItemClickListener clickListener;


    public FristWorkAdapter(WorkItemClickListener clickListener) {
        this.clickListener = clickListener;
        dataList = new ArrayList<>();
    }

    public void updateData(List<WorkDto> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFristWorkBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_frist_work, parent, false);
        return new RecyclerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.bind(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        private ItemFristWorkBinding binding;

        RecyclerHolder(ItemFristWorkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WorkDto workDto, int position) {
            binding.setWorkDto(workDto);
            binding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onWorkItemClick(workDto);
                }
            });
            binding.executePendingBindings();
        }
    }

    public interface WorkItemClickListener {
        void onWorkItemClick(WorkDto workDto);
    }
}
