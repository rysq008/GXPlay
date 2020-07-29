package com.ikats.shop.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikats.shop.R;
import com.ikats.shop.model.SearchItemBean;
import com.ikats.shop.model.StatisItemBean;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class StatisItemAdapter extends SimpleRecAdapter<StatisItemBean, StatisItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public StatisItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final StatisItemBean item = data.get(position);
        int type = item.itemType();
        holder.setDisplay(type, item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public void setDisplay(int type, StatisItemBean itemType) {
            getView(R.id.statis_item_sort_tv).setText(itemType.sort);
            getView(R.id.statis_item_goods_tv).setText(itemType.goods);
            getView(R.id.statis_item_code_tv).setText(itemType.qrcode);
            getView(R.id.statis_item_sell_tv).setText(itemType.sellCount);
            getView(R.id.statis_item_amount_tv).setText(itemType.sellAmount);
        }


        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }

        public <T extends View> TextView getView(int resid){
            return itemView.findViewById(resid);
        }
    }
}
