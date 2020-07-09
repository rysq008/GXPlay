package com.zhny.library.presenter.monitor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemMonitorExpandParentBinding;
import com.zhny.library.databinding.LayoutItemMonitorExpandPlotBinding;
import com.zhny.library.presenter.monitor.model.dto.ExpandData;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;

/**
 * created by liming
 */
public class PlotExpandAdapter extends BaseExpandableListAdapter {

    public static final Integer DEVICE_DATA = 1;
    public static final Integer PLOT_DATA = 2;

    private List<ExpandData> data;

    public PlotExpandAdapter() {
        this.data = new ArrayList<>();
    }

    public void refreshPlot(List<ExpandData> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

//    public void refreshMachine(ExpandData deviceExpandData) {
//        if (data.size() == 0) {
//            data.add(deviceExpandData);
//        } else {
//            if (data.get(0).dataType == DEVICE_DATA) {
//                data.remove(0);
//            }
//            data.add(0, deviceExpandData);
//        }
//        notifyDataSetChanged();
//    }

    public List<ExpandData> getData() {
        return data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).content == null ? 0 : data.get(groupPosition).content.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).content.get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LayoutItemMonitorExpandParentBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_monitor_expand_parent, parent, false);
            parentViewHolder = new ParentViewHolder(binding);
            convertView = binding.getRoot();
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        parentViewHolder.bind(isExpanded, data.get(groupPosition).name, data.get(groupPosition).size);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (data.get(groupPosition).dataType == PLOT_DATA) {
        PlotChildViewHolder plotChildViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LayoutItemMonitorExpandPlotBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_monitor_expand_plot, parent, false);
            plotChildViewHolder = new PlotChildViewHolder(binding);
            convertView = binding.getRoot();
            convertView.setTag(plotChildViewHolder);
        } else {
            plotChildViewHolder = (PlotChildViewHolder) convertView.getTag();
        }
        SelectFarmDto.SelectPlotDto plot = (SelectFarmDto.SelectPlotDto) data.get(groupPosition).content.get(childPosition);
        plotChildViewHolder.bind(plot);
        return convertView;

//        } else if (data.get(groupPosition).dataType == DEVICE_DATA) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            LayoutItemMonitorExpandDeviceBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_monitor_expand_device, parent, false);
//            DeviceChildViewHolder deviceChildViewHolder = new DeviceChildViewHolder(binding);
//            convertView = binding.getRoot();
//
//            SelectMachineDto device = (SelectMachineDto) data.get(groupPosition).content.get(childPosition);
//            deviceChildViewHolder.bind(device);
//            return convertView;
//        }
//        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

    }


    class ParentViewHolder {
        private LayoutItemMonitorExpandParentBinding binding;

        ParentViewHolder(LayoutItemMonitorExpandParentBinding binding) {
            this.binding = binding;
        }

        public void bind(boolean isExpanded, String name, int size) {
            binding.setName(name + "(" + size + ")");
            binding.ivItemMonitorSelectFarmSwitch.setSelected(isExpanded);
            binding.executePendingBindings();
        }
    }


//    class DeviceChildViewHolder {
//        private LayoutItemMonitorExpandDeviceBinding binding;
//
//        DeviceChildViewHolder(LayoutItemMonitorExpandDeviceBinding binding) {
//            this.binding = binding;
//        }
//
//        public void bind(SelectMachineDto machineDto) {
//            binding.setMachineDto(machineDto);
//            binding.executePendingBindings();
//        }
//    }

    class PlotChildViewHolder {
        private LayoutItemMonitorExpandPlotBinding binding;

        PlotChildViewHolder(LayoutItemMonitorExpandPlotBinding binding) {
            this.binding = binding;
        }

        public void bind(SelectFarmDto.SelectPlotDto plotDto) {
            binding.setPlot(plotDto);
            binding.executePendingBindings();
        }
    }


}
