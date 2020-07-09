package com.zhny.library.presenter.fence.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemFenceSelectFenceBinding;
import com.zhny.library.presenter.fence.model.dto.Fence;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class FenceAdapter extends RecyclerView.Adapter<FenceAdapter.FenceViewHolder> {

    private List<Fence> data;
    private OnFenceItemClickListener listener;

    public FenceAdapter(OnFenceItemClickListener listener) {
        this.data = new ArrayList<>();
        this.listener = listener;
    }

    public void refresh(List<Fence> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemFenceSelectFenceBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_fence_select_fence, parent, false);
        return new FenceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FenceViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FenceViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemFenceSelectFenceBinding binding;

        FenceViewHolder(@NonNull LayoutItemFenceSelectFenceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Fence fence) {
            binding.setFence(fence);
            binding.setFenceItemClick(listener);
            binding.executePendingBindings();
        }
    }

    public interface OnFenceItemClickListener{
        void onFenceItemClick(Fence fence);
    }

}
