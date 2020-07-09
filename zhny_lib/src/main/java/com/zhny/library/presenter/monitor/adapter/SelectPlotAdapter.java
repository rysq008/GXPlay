package com.zhny.library.presenter.monitor.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.amap.api.maps.Projection;
import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemMonitorSelectPlotBinding;
import com.zhny.library.presenter.monitor.listener.SelectPlotListener;
import com.zhny.library.presenter.monitor.model.vo.MapPath;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class SelectPlotAdapter extends RecyclerView.Adapter<SelectPlotAdapter.SelectPlotViewHolder> {

    private List<SelectFarmDto.SelectPlotDto> plotDtoList;
    private SelectPlotListener selectPlotListener;

    SelectPlotAdapter(SelectPlotListener selectPlotListener) {
        this.plotDtoList = new ArrayList<>();
        this.selectPlotListener = selectPlotListener;
    }

    void refreshData(List<SelectFarmDto.SelectPlotDto> data) {
        plotDtoList.clear();
        plotDtoList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectPlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemMonitorSelectPlotBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.layout_item_monitor_select_plot, parent, false);
        return new SelectPlotViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPlotViewHolder holder, int position) {
        SelectFarmDto.SelectPlotDto dto = plotDtoList.get(position);
        holder.bind(dto);
    }

    @Override
    public int getItemCount() {
        return plotDtoList.size();
    }

    class SelectPlotViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemMonitorSelectPlotBinding binding;

        SelectPlotViewHolder(LayoutItemMonitorSelectPlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SelectFarmDto.SelectPlotDto plotDto) {
            binding.setPlot(plotDto);
            binding.setPlotListener(selectPlotListener);
            binding.executePendingBindings();
        }
    }


}

