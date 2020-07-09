package com.zhny.library.presenter.work.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.amap.api.maps.Projection;
import com.zhny.library.R;


import com.zhny.library.databinding.LayoutItemSelectPlotBinding;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.vo.MapPath;
import com.zhny.library.presenter.work.listener.SelectPlotListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PlotAdapter extends RecyclerView.Adapter<PlotAdapter.SelectPlotViewHolder> {

    private List<SelectFarmDto.SelectPlotDto> plotDtoList;
    private SelectPlotListener selectPlotListener;
    private Projection projection;

    PlotAdapter(SelectPlotListener selectPlotListener, Projection projection) {
        this.plotDtoList = new ArrayList<>();
        this.selectPlotListener = selectPlotListener;
        this.projection = projection;
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
        LayoutItemSelectPlotBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.layout_item_select_plot, parent, false);
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

        private LayoutItemSelectPlotBinding binding;

        SelectPlotViewHolder(LayoutItemSelectPlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SelectFarmDto.SelectPlotDto plotDto) {
            plotDto.mapPath = new MapPath(projection, plotDto.coordinates);

            binding.setPlot(plotDto);

            binding.setPlotListener(selectPlotListener);
            binding.executePendingBindings();
        }
    }


}

