package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.IncomeResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 推广收益列表adapter
 */
public class IncomeListAdapter extends SimpleRecAdapter<ItemType, IncomeListAdapter.PhotoViewHolder> {

    private Activity mActivity;

    public IncomeListAdapter(Activity context) {
        super(context);
        mActivity = context;
    }

    @Override
    public PhotoViewHolder newViewHolder(View itemView) {
        return new IncomeListAdapter.PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item, mActivity, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_generalize_income;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.incomeTypeTv)
        TextView incomeTypeTv;
        @BindView(R.id.coinTv)
        TextView coinTv;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            IncomeResultModel.ListBean data = (IncomeResultModel.ListBean) itemType;
            //时间
            timeTv.setText(data.getCreate_time());
            //收入类型
            incomeTypeTv.setText(data.getType());
            //金币
            coinTv.setText(data.getReward());

        }
    }
}
