package com.game.helper.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.AccountDescFragment;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.net.api.Api;
import com.game.helper.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;

/**
 * Created by sung on 2017/11/19.
 */
public class AccountManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AccountManagerAdapter.class.getSimpleName();
    private OnActionListener onActionListener;
    private List data = new ArrayList();
    private Context context;

    public AccountManagerAdapter(Context context, XBaseModel data) {
        this.context = context;
        if (data == null) return;
        if (data instanceof GameAccountResultModel) {
            this.data = ((GameAccountResultModel) data).getList();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AccountHolder(inflater.inflate(R.layout.layout_account_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AccountHolder) {
            AccountHolder viewHolder = (AccountHolder) holder;
            viewHolder.onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(XBaseModel data, boolean clear) {
        if (data == null) return;
        List list = null;
        if (data instanceof GameAccountResultModel) {
            list = ((GameAccountResultModel) data).getList();
        }
        if (clear) this.data = list;
        else this.data.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(GameAccountResultModel.ListBean bean) {
        if (data.indexOf(bean) != -1) {
            data.set(data.indexOf(bean), bean);
            notifyDataSetChanged();
        }
    }

    class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position = -1;
        private View mRootView;
        private ImageView avatar;
        private ImageView vip;
        private TextView name;
        private TextView platform;
        private TextView accountIdenty;
        private TextView creatTime;
        private ImageView action;

        public AccountHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            avatar = itemView.findViewById(R.id.iv_avatar);
            vip = itemView.findViewById(R.id.iv_vip_level);
            name = itemView.findViewById(R.id.tv_name);
            platform = itemView.findViewById(R.id.tv_platform);
            accountIdenty = itemView.findViewById(R.id.tv_identy);
            creatTime = itemView.findViewById(R.id.tv_time);
            action = itemView.findViewById(R.id.iv_action);
        }

        void onBind(int position) {
            this.position = position;
            if (!(data.get(position) instanceof GameAccountResultModel.ListBean))
                return;

            GameAccountResultModel.ListBean item = (GameAccountResultModel.ListBean) data.get(position);
            ILFactory.getLoader().loadNet(avatar, Api.API_PAY_OR_IMAGE_URL + item.getGame_logo(), ILoader.Options.defaultOptions());
            name.setText(item.getGame_name());
            platform.setText(item.getGame_channel_name());
            accountIdenty.setText(item.getGame_account());
            creatTime.setText(item.getCreate_time());
            if (item.isIs_vip()) {
                vip.setImageResource(Utils.getExtensionVipIcon(item.getVip_level()));
                vip.setVisibility(View.VISIBLE);
            } else {
                vip.setVisibility(View.GONE);
            }
            if (item.isIs_successed()) {
                action.setImageResource(R.mipmap.ic_jingzhi);
            } else {
                action.setImageResource(R.mipmap.ic_delete);
            }
            action.setTag(item.isIs_successed());
            action.setOnClickListener(this);
            mRootView.setTag(item);
            mRootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == mRootView) {
                if (mRootView.getTag() == null) return;
                AccountDescFragment fragment = AccountDescFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AccountDescFragment.TAG, (GameAccountResultModel.ListBean) mRootView.getTag());
                fragment.setArguments(bundle);
                DetailFragmentsActivity.launch(context, bundle, fragment);
            }
            if (v == action) {
                if (action.getTag() instanceof Boolean) {
                    boolean isSuccess = (boolean) action.getTag();
                    if (isSuccess) {
                        Toast.makeText(context, "该账号不可删除！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (onActionListener != null) {
                        onActionListener.onDelete(((GameAccountResultModel.ListBean) data.get(position)).getId());
                    }
                }
            }
        }
    }

    public void addOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public interface OnActionListener {
        void onDelete(int gameid);
    }
}
