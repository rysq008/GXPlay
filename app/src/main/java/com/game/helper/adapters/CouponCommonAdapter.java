package com.game.helper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.recharge.RechargeFragment;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.UnAvailableRedpackResultModel;
import com.game.helper.net.api.Api;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;

/**
 * Created by sung on 2017/12/21.
 */

public class CouponCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int Type_Coupon_Canuse = 0;
    public static final int Type_Coupon_Hasuse = 1;
    public static final int Type_Coupon_Outuse = 2;

    private List data = new ArrayList();
    private int type;
    private Context context;

    public CouponCommonAdapter(Context context,List data, int type) {
        this.context = context;
        this.type = type;
        if (data == null) return;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (type == Type_Coupon_Canuse){
            view = inflater.inflate(R.layout.item_can_use_coupon,null,false);
            return new CanUseCouponHolder(view);
        }
        if (type == Type_Coupon_Hasuse){
            view = inflater.inflate(R.layout.item_has_used_coupon,null,false);
            return new HasUseCouponHolder(view);
        }
        if (type == Type_Coupon_Outuse){
            view = inflater.inflate(R.layout.item_out_used_coupon,null,false);
            return new OutUseCouponHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CanUseCouponHolder){
            CanUseCouponHolder holder1 = (CanUseCouponHolder) holder;
            holder1.onBind(position);
        }
        if (holder instanceof HasUseCouponHolder){
            HasUseCouponHolder holder1 = (HasUseCouponHolder) holder;
            holder1.onBind(position);
        }
        if (holder instanceof OutUseCouponHolder){
            OutUseCouponHolder holder1 = (OutUseCouponHolder) holder;
            holder1.onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List list, boolean clear){
        if (list == null || list.size() == 0) return;
        if (clear) {
            data.clear();
        }
        data.addAll(list);notifyDataSetChanged();
    }

    /**
     * 未使用
     * */
    public class CanUseCouponHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View rootView;
        private TextView name;
        private TextView desc;
        private TextView limit;
        private TextView num;
        private LinearLayout game;
        private LinearLayout container;

        public CanUseCouponHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootRl);
            name = itemView.findViewById(R.id.redPackName);
            desc = itemView.findViewById(R.id.redPackDesc);
            limit = itemView.findViewById(R.id.timeLimit);
            num = itemView.findViewById(R.id.redpackNum);
            game = itemView.findViewById(R.id.ll_has_game);
            container = itemView.findViewById(R.id.game_container);
            rootView.setOnClickListener(this);
        }

        void onBind(int position){
            if(data.get(position) instanceof AvailableRedpackResultModel.ListBean){
                AvailableRedpackResultModel.ListBean item = (AvailableRedpackResultModel.ListBean) data.get(position);
                name.setText(item.getName());
                desc.setText("购满"+item.getMoney_limit()+"可使用");
                limit.setText("有效期至\t"+item.getEnd_date());
                num.setText(item.getAmount()+"元现金券");

                if (item.getGames().size() <= 0 || item.getType() != 2){
                    game.setVisibility(View.GONE);
                    return;
                }
                container.removeAllViews();
                for (int i = 0; i < item.getGames().size(); i++) {
                    inflateGameItem(item.getGames().get(i),i,item.getGames().size());
                }
            }
        }

        private void inflateGameItem(final AvailableRedpackResultModel.ListBean.GameBean gameBean, final int position, int totalSize){
            if (container.getChildCount() >= totalSize) return;
            View view = LayoutInflater.from(context).inflate(R.layout.item_can_use_coupon_game_item, null, false);
            view.setTag(position);
            ImageView icon = view.findViewById(R.id.game_icon);
            TextView name = view.findViewById(R.id.game_name);

            name.setText(gameBean.getName());
            ILFactory.getLoader().loadNet(icon, Api.API_PAY_OR_IMAGE_URL + gameBean.getLogo(), ILoader.Options.defaultOptions());

            View click = view.findViewById(R.id.game_click);
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailFragmentsActivity.launch(context,null, RechargeFragment.newInstance());
                    //Toast.makeText(context, gameBean.getName()+"/"+position, Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(view);
        }

        @Override
        public void onClick(View v) {
            if (v == rootView){
                Toast.makeText(context, "未使用！", Toast.LENGTH_SHORT).show();
                container.setVisibility(container.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        }
    }

    /**
     * 已使用
     * */
    public class HasUseCouponHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View rootView;
        private TextView name;
        private TextView desc;
        private TextView limit;
        private TextView num;

        public HasUseCouponHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootRl);
            name = itemView.findViewById(R.id.redPackName);
            desc = itemView.findViewById(R.id.redPackDesc);
            limit = itemView.findViewById(R.id.timeLimit);
            num = itemView.findViewById(R.id.redpackNum);
            rootView.setOnClickListener(this);
        }

        void onBind(int position){
            if(data.get(position) instanceof UnAvailableRedpackResultModel.UnAvailableRedpackItem){
                UnAvailableRedpackResultModel.UnAvailableRedpackItem item = (UnAvailableRedpackResultModel.UnAvailableRedpackItem) data.get(position);
                name.setText(item.name);
                desc.setText("购满"+item.money_limit+"可使用");
                limit.setText("有效期至\t"+item.end_date);
                num.setText(item.amount+"元现金券");
            }
        }

        @Override
        public void onClick(View v) {
            if (v == rootView){
                Toast.makeText(context, "已使用！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 过期
     * */
    public class OutUseCouponHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View rootView;
        private TextView name;
        private TextView desc;
        private TextView limit;
        private TextView num;

        public OutUseCouponHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootRl);
            name = itemView.findViewById(R.id.redPackName);
            desc = itemView.findViewById(R.id.redPackDesc);
            limit = itemView.findViewById(R.id.timeLimit);
            num = itemView.findViewById(R.id.redpackNum);
            rootView.setOnClickListener(this);
        }

        void onBind(int position){
            if(data.get(position) instanceof UnAvailableRedpackResultModel.UnAvailableRedpackItem){
                UnAvailableRedpackResultModel.UnAvailableRedpackItem item = (UnAvailableRedpackResultModel.UnAvailableRedpackItem) data.get(position);
                name.setText(item.name);
                desc.setText("购满"+item.money_limit+"可使用");
                limit.setText("有效期至\t"+item.end_date);
                num.setText(item.amount+"元现金券");
            }
        }

        @Override
        public void onClick(View v) {
            if (v == rootView){
                Toast.makeText(context, "已过期！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
