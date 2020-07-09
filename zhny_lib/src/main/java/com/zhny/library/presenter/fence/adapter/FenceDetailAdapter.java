package com.zhny.library.presenter.fence.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.LayoutItemPopWinFenceDetailBinding;
import com.zhny.library.presenter.fence.custom.FenceDetailPopWin;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by liming
 */
public class FenceDetailAdapter extends RecyclerView.Adapter<FenceDetailAdapter.FenceDetailViewHolder> {

    private List<FenceMachine> data;
    private FenceDetailPopWin.OnFenceDetailPopWinListener onMachineDeleteListener;

    public FenceDetailAdapter(FenceDetailPopWin.OnFenceDetailPopWinListener  listener) {
        this.data = new ArrayList<>();
        this.onMachineDeleteListener = listener;
    }

    public void refresh(List<FenceMachine> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FenceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemPopWinFenceDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_pop_win_fence_detail, parent, false);
        return new FenceDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FenceDetailViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FenceDetailViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemPopWinFenceDetailBinding binding;

        FenceDetailViewHolder(@NonNull LayoutItemPopWinFenceDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FenceMachine dto) {
            binding.setFenceMachine(dto);
            binding.setOnFenceDetailPopWinListener(onMachineDeleteListener);
            binding.executePendingBindings();
        }
    }
}
