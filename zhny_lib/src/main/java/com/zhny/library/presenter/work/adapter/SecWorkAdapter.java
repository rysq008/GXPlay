package com.zhny.library.presenter.work.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemSecWorkBinding;
import com.zhny.library.presenter.work.dto.WorkDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class SecWorkAdapter extends RecyclerView.Adapter<SecWorkAdapter.RecyclerHolder> {
    private List<WorkDto> dataList;

    public SecWorkAdapter() {
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
        ItemSecWorkBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_sec_work, parent, false);
        return new RecyclerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        private ItemSecWorkBinding binding;

        RecyclerHolder(ItemSecWorkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WorkDto workDto) {
            binding.setWorkDto(workDto);
            binding.executePendingBindings();
        }
    }


}
