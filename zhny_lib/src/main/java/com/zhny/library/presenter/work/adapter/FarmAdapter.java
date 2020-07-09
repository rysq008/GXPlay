package com.zhny.library.presenter.work.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.Projection;
import com.zhny.library.R;


import com.zhny.library.databinding.LayoutItemSelectFarmBinding;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.work.listener.SelectFarmListener;
import com.zhny.library.presenter.work.listener.SelectPlotListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.SelectFarmViewHolder> {

    private List<SelectFarmDto> farmList;
    private SelectPlotListener selectPlotListener;
    private Context context;
    private Projection projection;
    private boolean isSearch;

    public FarmAdapter(SelectPlotListener selectPlotListener, Context context, Projection projection) {
        this.farmList = new ArrayList<>();
        this.selectPlotListener = selectPlotListener;
        this.context = context;
        this.projection = projection;


    }

    public void refreshData(List<SelectFarmDto> data, boolean isSearch) {
        this.isSearch = isSearch;
        farmList.clear();
        farmList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectFarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemSelectFarmBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.layout_item_select_farm, parent, false);
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

        private LayoutItemSelectFarmBinding binding;
        private PlotAdapter selectPlotAdapter;

        SelectFarmViewHolder(LayoutItemSelectFarmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            selectPlotAdapter = new PlotAdapter(selectPlotListener, projection);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rvSelectPlot.setLayoutManager(manager);
            binding.rvSelectPlot.setAdapter(selectPlotAdapter);
        }

        void bind(SelectFarmDto farm) {
            selectPlotAdapter.refreshData(farm.fieldList);

            //默认折叠
            binding.ivItemSelectFarmSwitch.setSelected(isSearch);
            binding.rvSelectPlot.setVisibility(isSearch ? View.VISIBLE : View.GONE);

            binding.setFarm(farm);
            binding.setFarmListener(this);
            binding.executePendingBindings();
        }

        @Override
        public void onFarmSelected(SelectFarmDto dto) {
            boolean isSelected = !binding.ivItemSelectFarmSwitch.isSelected();
            binding.ivItemSelectFarmSwitch.setSelected(isSelected);
            binding.rvSelectPlot.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        }
    }


}

