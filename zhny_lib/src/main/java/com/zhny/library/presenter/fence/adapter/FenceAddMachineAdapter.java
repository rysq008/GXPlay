package com.zhny.library.presenter.fence.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemFenceAddMachineContentBinding;
import com.zhny.library.databinding.ItemFenceAddMachineHeaderBinding;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class FenceAddMachineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_HEADER = 1, ITEM_CONTENT = 2;

    private List<Object> data;
    private OnFenceCheckMachineListener listener;

    public FenceAddMachineAdapter(OnFenceCheckMachineListener listener) {
        this.listener = listener;
        this.data = new ArrayList<>();
    }

    public void refresh(List<Object> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_HEADER) {
            ItemFenceAddMachineHeaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_fence_add_machine_header, parent, false);
            return new FenceAddMachineHeaderViewHolder(binding);
        } else {
            ItemFenceAddMachineContentBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_fence_add_machine_content, parent, false);
            return new FenceAddMachineContentViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_HEADER) {
            FenceAddMachineHeaderViewHolder headerHolder = (FenceAddMachineHeaderViewHolder) holder;
            String name = (String) data.get(position);
            headerHolder.bind(name);
        } else {
            FenceAddMachineContentViewHolder contentHolder = (FenceAddMachineContentViewHolder) holder;
            FenceMachine dto = (FenceMachine) data.get(position);
            contentHolder.bind(dto);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof String) {
            return ITEM_HEADER;
        } else {
            return ITEM_CONTENT;
        }
    }


    class FenceAddMachineContentViewHolder extends RecyclerView.ViewHolder {

        private ItemFenceAddMachineContentBinding binding;

        FenceAddMachineContentViewHolder(@NonNull ItemFenceAddMachineContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FenceMachine dto) {
            binding.setMachine(dto);
            binding.setOnFenceCheckMachine(listener);
            binding.executePendingBindings();
        }
    }


    class FenceAddMachineHeaderViewHolder extends RecyclerView.ViewHolder {

        private ItemFenceAddMachineHeaderBinding binding;

        FenceAddMachineHeaderViewHolder(@NonNull ItemFenceAddMachineHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String name) {
            binding.setName(name);
            binding.executePendingBindings();
        }
    }

    public interface OnFenceCheckMachineListener {
        void onCheckMachineListener(FenceMachine machine);
    }


}
