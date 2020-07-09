package com.zhny.library.presenter.work.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhny.library.R;
import com.zhny.library.databinding.ItemYearBinding;
import com.zhny.library.presenter.work.helper.YearInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class YearAdapter extends RecyclerView.Adapter<YearAdapter.RecyclerHolder> {
    private List<YearInfo> dataList;
    private YearItemClickListener clickListener;


    public YearAdapter(YearItemClickListener clickListener) {
        this.clickListener = clickListener;
        dataList = new ArrayList<>();
    }

    public void updateData(List<YearInfo> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemYearBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_year, parent, false);
        return new RecyclerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.bind(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        private ItemYearBinding binding;

        RecyclerHolder(ItemYearBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(YearInfo yearInfo, int position) {
            Context context = binding.yearName.getContext();
            String text = yearInfo.year + context.getString(R.string.year);
            binding.yearName.setText(text);
            binding.setYearInfo(yearInfo);
            binding.yearName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onYearItemClick(yearInfo, position);
                }
            });
            binding.executePendingBindings();
        }
    }

    public interface YearItemClickListener {
        void onYearItemClick(YearInfo yearInfo, int position);
    }
}
