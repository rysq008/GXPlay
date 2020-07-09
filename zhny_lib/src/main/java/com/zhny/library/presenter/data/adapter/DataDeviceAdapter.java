package com.zhny.library.presenter.data.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemDataDeviceContentBinding;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class DataDeviceAdapter extends RecyclerView.Adapter<DataDeviceAdapter.DataDeviceViewHolder> {

    private List<DataDeviceDto> data;
    private OnDataMachineListener listener;

    public DataDeviceAdapter(OnDataMachineListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }

    public void refresh(List<DataDeviceDto> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemDataDeviceContentBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_data_device_content, parent, false);
            return new DataDeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataDeviceViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class DataDeviceViewHolder extends RecyclerView.ViewHolder {

        private ItemDataDeviceContentBinding binding;

        DataDeviceViewHolder(@NonNull ItemDataDeviceContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DataDeviceDto dto) {
            binding.setDevice(dto);
            binding.setOnDataMachineListener(listener);
            binding.executePendingBindings();
        }
    }

    public interface OnDataMachineListener {
        void onCheckMachineListener(DataDeviceDto dataDeviceDto);
    }

}
