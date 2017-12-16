package com.game.helper.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.AddAccountActivity;
import com.game.helper.model.ChannelListResultModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 游戏列表adapter
 */
public class ChannelListAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<ChannelListResultModel.ListBean> datas = new ArrayList<>();

    public ChannelListAdapter(Activity activity) {
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
    }

    public void fillDatas(List<ChannelListResultModel.ListBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    private ChannelListResultModel.ListBean getItem(int pos) {
        return datas.get(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root;
        RecyclerView.ViewHolder viewHolder = null;
        root = mInflater.inflate(R.layout.item_game, parent, false);
        viewHolder = new ViewHolder(root);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ChannelListResultModel.ListBean recordModel = getItem(position);
        if (recordModel != null) {
            ViewHolder anyingViewHolder = ((ViewHolder) holder);
            anyingViewHolder.bindItem(recordModel, mActivity, position);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.game_name_tv)
        TextView name;

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(final ChannelListResultModel.ListBean data, Context context, final int pos) {
            //产品名字
            name.setText(data.getChannel_name());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mActivity instanceof AddAccountActivity){
                        ((AddAccountActivity)mActivity).onChannelSelected(data.getChannel_name(),data.getChannel_id());
                    }
                }
            });
        }

    }

}
