package com.zhny.library.presenter.machine.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemMachineContentBinding;
import com.zhny.library.databinding.ItemMachineHeaderBinding;
import com.zhny.library.presenter.machine.listener.ItemClickListener;
import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class MachineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int ITEM_HEADER = 1, ITEM_CONTENT = 2;
    private List<Object> dataList;
    private ItemClickListener clickListener;

    public final void updateData(List<Object> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public MachineAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
        this.dataList = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEADER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemMachineHeaderBinding binding = DataBindingUtil.inflate
                    (inflater, R.layout.item_machine_header, parent, false);
            return new HeadViewHolder(binding);
        } else if (viewType == ITEM_CONTENT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemMachineContentBinding binding = DataBindingUtil.inflate
                    (inflater, R.layout.item_machine_content, parent, false);
            return new ContentViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        /**头部*/
        if (getItemViewType(position) == ITEM_HEADER) {
            final HeadViewHolder headerHolder = (HeadViewHolder) holder;
            String headerInfo = (String) dataList.get(position);
            headerHolder.bind(headerInfo);
        }
        /**机具信息*/
        else if (getItemViewType(position) == ITEM_CONTENT) {
            final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            MachineDto machineDto = (MachineDto) dataList.get(position);
            contentViewHolder.bind(machineDto);
        }

    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof String) {
            return ITEM_HEADER;
        } else if (dataList.get(position) instanceof MachineDto) {
            return ITEM_CONTENT;
        }
        return ITEM_CONTENT;
    }


    class HeadViewHolder extends RecyclerView.ViewHolder {
        private ItemMachineHeaderBinding binding;

        HeadViewHolder(ItemMachineHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String headerInfo) {
            binding.type.setText(headerInfo);
            binding.executePendingBindings();
        }
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        private ItemMachineContentBinding binding;

        ContentViewHolder(ItemMachineContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MachineDto machineDto) {
            binding.setMachineDto(machineDto);
            Context context = binding.icon.getContext();
            machineDto.productBrandMeaning = TextUtils.isEmpty(machineDto.productBrandMeaning) ?
                    context.getString(R.string.unknown) : machineDto.productBrandMeaning;
            machineDto.productModel = TextUtils.isEmpty(machineDto.productModel) ?
                    context.getString(R.string.unknown) : machineDto.productModel;
            machineDto.brand_model = machineDto.productBrandMeaning + "-" + machineDto.productModel;


            if (TextUtils.isEmpty(machineDto.name)) {
                machineDto.name = context.getString(R.string.unknown);
            }
//            ImageLoaderUtil.loadImage(context, machineDto.imgUrl
//                    , R.drawable.default_machine_icon, R.drawable.default_machine_icon, binding.icon);
            binding.setItemClick(clickListener);
            binding.executePendingBindings();
        }
    }


}