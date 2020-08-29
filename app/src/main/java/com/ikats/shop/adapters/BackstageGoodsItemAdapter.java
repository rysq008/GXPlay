package com.ikats.shop.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikats.shop.R;
import com.ikats.shop.model.BackstageGoodsItemBean;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class BackstageGoodsItemAdapter extends SimpleRecAdapter<BackstageGoodsItemBean, BackstageGoodsItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public BackstageGoodsItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_backstage_goods_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BackstageGoodsItemBean item = data.get(position);
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


        public void setDisplay(int type, BackstageGoodsItemBean itemType) {
            getView(R.id.backstage_goods_search_item_index_tv).setText(itemType.index+"");
            getView(R.id.backstage_goods_search_item_name_tv).setText(itemType.name);
            getView(R.id.backstage_goods_search_item_barcode_tv).setText(itemType.barcode);
            getView(R.id.backstage_goods_search_item_code_tv).setText(itemType.code);
            getView(R.id.backstage_goods_search_item_unit_tv).setText(itemType.unit);
            getView(R.id.backstage_goods_search_item_price_tv).setText(itemType.price);
            getView(R.id.backstage_goods_search_item_place_tv).setText(itemType.place);
            getView(R.id.backstage_goods_search_item_brand_tv).setText(itemType.brank);
            getView(R.id.backstage_goods_search_item_spec_tv).setText(itemType.spec);
            getView(R.id.backstage_goods_search_item_rough_weight_tv).setText(itemType.rough_weight);
            getView(R.id.backstage_goods_search_item_net_weight_tv).setText(itemType.net_weight);
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
