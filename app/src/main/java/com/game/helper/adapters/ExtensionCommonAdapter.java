package com.game.helper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.helper.R;
import com.game.helper.fragments.ExtensionProfitItemFragment;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.CashListResults;
import com.game.helper.model.MarketFlowlistResults;
import com.game.helper.model.MarketExpectedFlowlistResults;
import com.game.helper.model.MarketFlowlistResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.MarketExpectedFlowlistResults;
import com.game.helper.net.api.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2017/11/19.
 */
public class ExtensionCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ExtensionCommonAdapter.class.getSimpleName();

    private List data = new ArrayList();
    private Context context;
    private int type = ExtensionProfitItemFragment.Type_Extension_Gold;

    public ExtensionCommonAdapter(Context context, XBaseModel data, int type) {
        this.context = context;
        this.type = type;
        if (data == null) return;
        if (data instanceof MarketFlowlistResults){
            this.data = ((MarketFlowlistResults) data).list;
        }else if (data instanceof MarketExpectedFlowlistResults){
            this.data = ((MarketExpectedFlowlistResults) data).list;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MarketFlowHolder(inflater.inflate(R.layout.layout_extension_profit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MarketFlowHolder) {
            MarketFlowHolder viewHolder = (MarketFlowHolder) holder;
            viewHolder.onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(XBaseModel data,boolean clear){
        if (data == null) return;
        List list = null;
        if (data instanceof MarketFlowlistResults){
            list = ((MarketFlowlistResults) data).list;
        }else if (data instanceof MarketExpectedFlowlistResults){
            list = ((MarketExpectedFlowlistResults) data).list;
        }
        if (clear) this.data = list;
        else this.data.addAll(list);
        notifyDataSetChanged();
    }

    class MarketFlowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position = -1;
        private View mRootView;
        private TextView left;
        private TextView center;
        private TextView right;

        public MarketFlowHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            left = itemView.findViewById(R.id.tv_left);
            center = itemView.findViewById(R.id.tv_center);
            right = itemView.findViewById(R.id.tv_right);
        }

        void onBind(int position) {
            this.position = position;

            if (type == ExtensionProfitItemFragment.Type_Extension_Gold) {
                if (!(data.get(position) instanceof MarketFlowlistResults.MarketFlowlistItem))
                    return;

                MarketFlowlistResults.MarketFlowlistItem item = (MarketFlowlistResults.MarketFlowlistItem) data.get(position);
                left.setText(item.create_time);
                center.setText(item.type);
                right.setText(item.reward);
            }
            if (type == ExtensionProfitItemFragment.Type_Plan_Gold){
                if (!(data.get(position) instanceof MarketExpectedFlowlistResults.MarketExpectedFlowlistItem))
                    return;

                MarketExpectedFlowlistResults.MarketExpectedFlowlistItem item = (MarketExpectedFlowlistResults.MarketExpectedFlowlistItem) data.get(position);
                left.setText(item.name);
                center.setText(item.amount+"元");
                right.setText(item.status);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == mRootView) {
            }
        }
    }
}
