package com.ikats.shop.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikats.shop.R;
import com.ikats.shop.model.BaseModel.ItemType;
import com.ikats.shop.model.SearchItemBean;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class SearchItemAdapter extends SimpleRecAdapter<SearchItemBean, SearchItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public SearchItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SearchItemBean item = data.get(position);
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


        public void setDisplay(int type, SearchItemBean itemType) {
            getView(R.id.search_item_sort_tv).setText(itemType.sort);
            getView(R.id.search_item_order_tv).setText(itemType.order);
            getView(R.id.search_item_count_tv).setText(itemType.count);
            getView(R.id.search_item_amount_tv).setText(itemType.amount);
            getView(R.id.search_item_person_tv).setText(itemType.person);
            getView(R.id.search_item_phone_tv).setText(itemType.phone);
            getView(R.id.search_item_status_tv).setText(itemType.status);
            getView(R.id.search_item_status_tv).setText(itemType.createtime);
            getView(R.id.search_item_action_tv).setText(itemType.action);

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
