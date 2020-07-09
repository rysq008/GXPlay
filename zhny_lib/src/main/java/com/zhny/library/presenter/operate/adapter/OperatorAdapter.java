package com.zhny.library.presenter.operate.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemOperateBinding;
import com.zhny.library.presenter.operate.helper.OperatorEnum;
import com.zhny.library.presenter.operate.listener.OnOperatorItemListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.OperatorViewHolder> {

    private List<OperatorEnum> data;
    private OnOperatorItemListener listener;

    public OperatorAdapter(OnOperatorItemListener listener) {
        this.listener = listener;
        data = new ArrayList<>();
    }

    public void refreshData(List<OperatorEnum> operatorItems) {
        data.clear();
        data.addAll(operatorItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OperatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOperateBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_operate, parent, false);
        return new OperatorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OperatorViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class OperatorViewHolder extends RecyclerView.ViewHolder {
        private ItemOperateBinding binding;

        OperatorViewHolder(@NonNull ItemOperateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OperatorEnum item) {
            binding.setItem(item);
            binding.setItemClick(listener);
            binding.executePendingBindings();
        }

    }

}
