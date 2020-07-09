package com.zhny.library.presenter.fence.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemFenceInfoMachineBinding;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class FenceInfoMachineAdapter extends RecyclerView.Adapter<FenceInfoMachineAdapter.FenceInfoMachineViewHolder> {

    private List<FenceMachine> data;

    public FenceInfoMachineAdapter() {
        this.data = new ArrayList<>();
    }

    public void refresh(List<FenceMachine> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FenceInfoMachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemFenceInfoMachineBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_fence_info_machine, parent, false);
        return new FenceInfoMachineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FenceInfoMachineViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FenceInfoMachineViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemFenceInfoMachineBinding binding;

        FenceInfoMachineViewHolder(@NonNull LayoutItemFenceInfoMachineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FenceMachine machine) {
            binding.setMachine(machine);
            binding.executePendingBindings();
        }

    }

}
