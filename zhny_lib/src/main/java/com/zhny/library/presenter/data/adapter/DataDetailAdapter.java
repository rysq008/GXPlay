package com.zhny.library.presenter.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemDataDeviceDetailBinding;
import com.zhny.library.presenter.data.custom.group.GroupRecyclerAdapter;
import com.zhny.library.presenter.data.model.vo.DataDetailVo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class DataDetailAdapter extends GroupRecyclerAdapter<String, DataDetailVo> {

    private List<DataDetailVo> data;

    public DataDetailAdapter(Context context) {
        super(context);
        data = new ArrayList<>();
    }

    public void refresh(List<DataDetailVo> dataDetails) {
        data.clear();
        data.addAll(dataDetails);
        LinkedHashMap<String, List<DataDetailVo>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("", data);
        titles.add("");
        resetGroups(map,titles);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDataDeviceDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_data_device_detail, parent, false);
        return new DataDetailViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder viewHolder, DataDetailVo item, int position) {
        DataDetailViewHolder holder = (DataDetailViewHolder) viewHolder;
        holder.bind(item);
    }

    class DataDetailViewHolder extends RecyclerView.ViewHolder {
        private ItemDataDeviceDetailBinding binding;

        DataDetailViewHolder(@NonNull ItemDataDeviceDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DataDetailVo dto) {
            binding.setDevice(dto);
            binding.executePendingBindings();
        }

    }

}
