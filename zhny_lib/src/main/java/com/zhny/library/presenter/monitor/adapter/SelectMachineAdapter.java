package com.zhny.library.presenter.monitor.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemMonitorSelectMachineBinding;
import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;
import com.zhny.library.presenter.monitor.listener.SelectMachineListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class SelectMachineAdapter extends RecyclerView.Adapter<SelectMachineAdapter.SelectMachineViewHolder> {

    private List<SelectMachineDto> machineDtoList;
    private SelectMachineListener selectMachineListener;

    public SelectMachineAdapter(SelectMachineListener selectMachineListener) {
        this.machineDtoList = new ArrayList<>();
        this.selectMachineListener = selectMachineListener;
    }

    public void refreshData(List<SelectMachineDto> data) {
        machineDtoList.clear();
        machineDtoList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectMachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemMonitorSelectMachineBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.layout_item_monitor_select_machine, parent, false);
        return new SelectMachineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMachineViewHolder holder, int position) {
        holder.bind(machineDtoList.get(position));
    }

    @Override
    public int getItemCount() {
        return machineDtoList.size();
    }

    class SelectMachineViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemMonitorSelectMachineBinding binding;

        SelectMachineViewHolder(LayoutItemMonitorSelectMachineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SelectMachineDto machineDto) {
            binding.setMachineDto(machineDto);
            binding.setSelectMachineListener(selectMachineListener);
            binding.executePendingBindings();
        }
    }


}

