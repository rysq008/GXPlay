package com.zhny.library.presenter.driver.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemDriverListBinding;
import com.zhny.library.presenter.driver.model.dto.DriverDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.RecyclerHolder> {
    private List<DriverDto> dataList;
    private DriverItemClickListener clickListener;


    public DriverListAdapter(DriverItemClickListener clickListener) {
        this.clickListener = clickListener;
        dataList = new ArrayList<>();
    }

    public void refresh(List<DriverDto> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDriverListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_driver_list, parent, false);
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
        private ItemDriverListBinding binding;

        RecyclerHolder(ItemDriverListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DriverDto driverDto) {
            binding.setDriverDto(driverDto);
            try {
                driverDto.firstName = driverDto.workerName.substring(0, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            binding.setItemClick(clickListener);
            binding.executePendingBindings();
        }

    }

    public interface DriverItemClickListener {
        void onDriverItemClick(DriverDto driverDto);
    }
}
