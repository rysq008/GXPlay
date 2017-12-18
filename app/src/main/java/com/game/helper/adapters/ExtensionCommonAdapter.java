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
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.CashListResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.RechargeListResults;
import com.game.helper.net.api.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2017/11/19.
 * 详情账号
 */
public class ExtensionCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ExtensionCommonAdapter.class.getSimpleName();
    public static final int Type_Account_Consume = 0;
    public static final int Type_Account_Recharge = 1;

    private List data = new ArrayList();
    private Context context;
    private int type = Type_Account_Consume;

    public ExtensionCommonAdapter(Context context, XBaseModel data, int type) {
        this.context = context;
        this.type = type;
        if (data == null) return;
        if (data instanceof ConsumeListResults){
            this.data = ((ConsumeListResults) data).list;
        }else if (data instanceof RechargeListResults){
            this.data = ((RechargeListResults) data).list;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (type == Type_Account_Recharge){
            return new AccountRechargeHolder(inflater.inflate(R.layout.layout_account_list_item_recharge, parent, false));
        }
        return new AccountConsumeHolder(inflater.inflate(R.layout.layout_account_list_item_consume, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AccountConsumeHolder) {
            AccountConsumeHolder viewHolder = (AccountConsumeHolder) holder;
            viewHolder.onBind(position);
        }
        if (holder instanceof AccountRechargeHolder) {
            AccountRechargeHolder viewHolder = (AccountRechargeHolder) holder;
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
        if (data instanceof ConsumeListResults){
            list = ((ConsumeListResults) data).list;
        }else if (data instanceof RechargeListResults){
            list = ((RechargeListResults) data).list;
        }
        if (clear) this.data = list;
        else this.data.addAll(list);
        notifyDataSetChanged();
    }

    /**
    * 消费明细
    * */
    class AccountConsumeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position = -1;
        private View mRootView;
        private TextView name;
        private ImageView icon;
        private TextView value;
        private TextView account;
        private TextView platform;
        private TextView time;

        public AccountConsumeHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            name = itemView.findViewById(R.id.tv_name);
            icon = itemView.findViewById(R.id.iv_icon);
            value = itemView.findViewById(R.id.tv_value);
            account = itemView.findViewById(R.id.tv_account);
            platform = itemView.findViewById(R.id.tv_platform);
            time = itemView.findViewById(R.id.tv_time);
        }

        void onBind(int position) {
            this.position = position;
            if (!(data.get(position) instanceof ConsumeListResults.ConsumeListItem)) return;
            ConsumeListResults.ConsumeListItem consumeListItem = (ConsumeListResults.ConsumeListItem) data.get(position);
            name.setText(consumeListItem.name);
            Glide.with(context).load(Api.API_PAY_OR_IMAGE_URL+consumeListItem.img).into(icon);
            value.setText("-"+consumeListItem.amount);
            account.setText(consumeListItem.game_account);
            platform.setText(consumeListItem.channel_name);
            time.setText(consumeListItem.finish_time);
        }

        @Override
        public void onClick(View v) {
            if (v == mRootView) {
            }
        }
    }

    /**
     * 充值明细
     * */
    class AccountRechargeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position = -1;
        private View mRootView;
        private TextView value;
        private TextView recharge;
        private TextView time;

        public AccountRechargeHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            value = itemView.findViewById(R.id.tv_value);
            recharge = itemView.findViewById(R.id.tv_recharge);
            time = itemView.findViewById(R.id.tv_time);
        }

        void onBind(int position) {
            this.position = position;
            if (!(data.get(position) instanceof RechargeListResults.RechargeListItem)) return;
            RechargeListResults.RechargeListItem rechargeListItem = (RechargeListResults.RechargeListItem) data.get(position);
            value.setText("+"+rechargeListItem.jine);
            time.setText(rechargeListItem.finish_time);
        }

        @Override
        public void onClick(View v) {
            if (v == mRootView) {
            }
        }
    }
}
