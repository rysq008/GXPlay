package com.ikats.shop.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikats.shop.R;
import com.ikats.shop.model.VipBean;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class VipItemAdapter extends SimpleRecAdapter<VipBean, VipItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public VipItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_search_vip_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VipBean item = data.get(position);
        holder.setDisplay(VipItemAdapter.this, item, position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public void setDisplay(VipItemAdapter adapter, VipBean itemType, int position) {
            getView(R.id.vip_item_name_tv, TextView.class).setText(itemType.name);
            getView(R.id.vip_item_phone_tv, TextView.class).setText(itemType.phone);
            getView(R.id.vip_item_level_tv, TextView.class).setText(itemType.level);
            getView(R.id.vip_item_integral_tv, TextView.class).setText(itemType.integral+"");
            getView(R.id.vip_item_balance_tv, TextView.class).setText(itemType.balance+"");
        }


        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }

        public <T extends View> T getView(int resid, Class<T> tClass) {
            return (T) (itemView.findViewById(resid));
        }
    }
}
