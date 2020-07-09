package com.zhny.library.presenter.playback.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemPlaybackListBinding;
import com.zhny.library.presenter.playback.listener.OnItemClickListener;
import com.zhny.library.presenter.playback.model.PlaybackInfoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-07 10:03
 */
public class PlaybackListAdapter extends RecyclerView.Adapter<PlaybackListAdapter.RecyclerHolder> {
    private List<PlaybackInfoDto> playbackInfoDtos;
    private OnItemClickListener onItemClickListener;

    public PlaybackListAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        playbackInfoDtos = new ArrayList<>();
    }

    public void updateData(List<PlaybackInfoDto> data) {
        playbackInfoDtos.clear();
        playbackInfoDtos.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        ItemPlaybackListBinding binding = DataBindingUtil.inflate(inflater,R.layout.item_playback_list,parent,false);
        return new RecyclerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.bind(playbackInfoDtos.get(position));
    }

    @Override
    public int getItemCount() {
        return playbackInfoDtos.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        private ItemPlaybackListBinding binding;

        RecyclerHolder(ItemPlaybackListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlaybackInfoDto dto) {
            binding.setPlaybackInfoDto(dto);
            if (!TextUtils.isEmpty(dto.productBrand) && !TextUtils.isEmpty(dto.productModel)) {
                dto.brand_model = dto.productBrandMeaning + "-" + dto.productModel;
            }
            binding.setItemClick(onItemClickListener);
            binding.executePendingBindings();
        }
    }


}
