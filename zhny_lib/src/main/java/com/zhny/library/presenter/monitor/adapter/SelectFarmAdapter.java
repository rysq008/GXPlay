package com.zhny.library.presenter.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.Projection;
import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemMonitorSelectFarmBinding;
import com.zhny.library.presenter.monitor.listener.SelectFarmListener;
import com.zhny.library.presenter.monitor.listener.SelectPlotListener;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class SelectFarmAdapter extends RecyclerView.Adapter<SelectFarmAdapter.SelectFarmViewHolder> {

    private List<SelectFarmDto> farmList;
    private SelectPlotListener selectPlotListener;
    private Context context;

    public SelectFarmAdapter(SelectPlotListener selectPlotListener, Context context) {
        this.farmList = new ArrayList<>();
        this.selectPlotListener = selectPlotListener;
        this.context = context;
    }

    public void refreshData(List<SelectFarmDto> data) {
        farmList.clear();
        farmList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectFarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemMonitorSelectFarmBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.layout_item_monitor_select_farm, parent, false);
        return new SelectFarmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectFarmViewHolder holder, int position) {
        holder.bind(farmList.get(position));
    }

    @Override
    public int getItemCount() {
        return farmList.size();
    }

    class SelectFarmViewHolder extends RecyclerView.ViewHolder implements SelectFarmListener {

        private LayoutItemMonitorSelectFarmBinding binding;
        private SelectPlotAdapter selectPlotAdapter;

        SelectFarmViewHolder(LayoutItemMonitorSelectFarmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            selectPlotAdapter = new SelectPlotAdapter(selectPlotListener);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rvMonitorSelectPlot.setLayoutManager(manager);
            binding.rvMonitorSelectPlot.setAdapter(selectPlotAdapter);
        }

        void bind(SelectFarmDto farm) {
            selectPlotAdapter.refreshData(farm.fieldList);
            binding.setFarm(farm);
            binding.setFarmListener(this);
            binding.executePendingBindings();
        }

        @Override
        public void onFarmSelected(SelectFarmDto dto) {
            boolean isSelected = !binding.ivItemMonitorSelectFarmSwitch.isSelected();
            binding.ivItemMonitorSelectFarmSwitch.setSelected(isSelected);
            binding.rvMonitorSelectPlot.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        }
    }


}

