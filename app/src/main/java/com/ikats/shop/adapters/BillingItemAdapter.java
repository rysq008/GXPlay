package com.ikats.shop.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikats.shop.R;
import com.ikats.shop.model.GoodsBean;

import java.text.DecimalFormat;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

public class BillingItemAdapter extends SimpleRecAdapter<GoodsBean, BillingItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public BillingItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_billing_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GoodsBean item = data.get(position);
        holder.setDisplay(BillingItemAdapter.this, item, position);
        holder.itemView.setOnClickListener(v -> {
            if (getRecItemClick() != null) {
                getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
            }
        });
    }

    public void addOrupdateData(GoodsBean bean) {
        int index = data.indexOf(bean);
        if (index != -1) {
            bean.count++;
//            notifyItemChanged(index, 0);
            updateElement(bean, index);
        } else {
            bean.count++;
//            data.add(bean);
//            notifyItemInserted(this.data.size());
            addElement(bean);
        }
    }

    public void clearAndRestData() {
        for (GoodsBean bean : data) {
            bean.amount = 0;
            bean.count = 0;
        }
        clearData();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

        public void setDisplay(BillingItemAdapter adapter, GoodsBean itemType, int position) {
            itemType.pos_in_list = position;
            goodsTv.setText(itemType.name);
            codeTv.setText(itemType.barcode);

            minusTv.setOnClickListener(v -> {
                if (--itemType.count >= 0) {
                    adapter.notifyItemChanged(position, 0);
                } else {
                    delIv.performClick();
                }
            });
            inputEt.setText(itemType.count + "");
            inputEt.setShowSoftInputOnFocus(false);
            addTv.setOnClickListener(v -> {
                ++itemType.count;
                adapter.notifyItemChanged(position, 0);
            });

            priceTv.setText(decimalFormat.format(itemType.origin_price));
            priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            sellTv.setText(decimalFormat.format(itemType.sell_price));
            delIv.setOnClickListener(v -> {
                itemType.count = 0;
                itemType.amount = 0;
                adapter.removeElement(position);
            });
        }

        @BindView(R.id.billing_item_goods_tv)
        TextView goodsTv;
        @BindView(R.id.billing_item_code_tv)
        TextView codeTv;
        @BindView(R.id.billing_item_minus_tv)
        TextView minusTv;
        @BindView(R.id.billing_item_input_et)
        EditText inputEt;
        @BindView(R.id.billing_item_add_tv)
        TextView addTv;
        @BindView(R.id.billing_item_org_price_tv)
        TextView priceTv;
        @BindView(R.id.billing_item_sell_price_tv)
        TextView sellTv;
        @BindView(R.id.billing_item_del_iv)
        ImageView delIv;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
