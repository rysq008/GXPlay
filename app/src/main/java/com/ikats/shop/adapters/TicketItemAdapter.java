package com.ikats.shop.adapters;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikats.shop.R;
import com.ikats.shop.database.PrintTableEntiry;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class TicketItemAdapter extends SimpleRecAdapter<PrintTableEntiry, TicketItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public List<PrintTableEntiry> getSelect_list() {
        return select_list;
    }

    private List<PrintTableEntiry> select_list = new ArrayList<>();

    public TicketItemAdapter(Context context) {
        super(context);
        select_list.clear();
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_unprint_ticket_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PrintTableEntiry item = data.get(position);
        holder.setDisplay(TicketItemAdapter.this, item, position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public void setDisplay(TicketItemAdapter adapter, PrintTableEntiry itemType, int position) {
            getView(R.id.dialog_item_ticket_order_tv, TextView.class).setText(itemType.sell_code);
            getView(R.id.dialog_item_ticket_amount_tv, TextView.class).setText(itemType.total);
            getView(R.id.dialog_item_ticket_discounts_tv, TextView.class).setText(itemType.discounts);
            getView(R.id.dialog_item_ticket_cope_tv, TextView.class).setText(itemType.cope + "");
            getView(R.id.dialog_item_ticket_paid_tv, TextView.class).setText(itemType.paid + "");
            getView(R.id.dialog_item_ticket_select_cb, CheckBox.class).setOnCheckedChangeListener((buttonView, isChecked) -> {
                itemType.isSelect = isChecked;
                if (isChecked) adapter.select_list.add(itemType);
                else adapter.select_list.remove(itemType);
            });
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
