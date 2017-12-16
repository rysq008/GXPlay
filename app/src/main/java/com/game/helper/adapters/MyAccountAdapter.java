package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.helper.R;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.utils.StringUtils;
import com.game.helper.views.HeadImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 我的账户列表adapter
 */
public class MyAccountAdapter extends SimpleRecAdapter<ItemType, MyAccountAdapter.PhotoViewHolder> {

    private Activity mActivity;
    private GameAccountResultModel.ListBean bean;

    public MyAccountAdapter(Activity context) {
        super(context);
        mActivity = context;
    }

    @Override
    public PhotoViewHolder newViewHolder(View itemView) {
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item, mActivity, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStatus();
                ((GameAccountResultModel.ListBean)item).setSelected(!(((GameAccountResultModel.ListBean) item).isSelected()));
                recordAccount((GameAccountResultModel.ListBean)item);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 记录选中
     * @param bean
     */
    public void recordAccount(GameAccountResultModel.ListBean bean) {
        this.bean = bean;
    }

    /**
     * 清除选中状态
     */
    private void clearStatus() {
            for (ItemType item : data) {
                ((GameAccountResultModel.ListBean)item).setSelected(false);
            }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_account;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'home_page_adapter_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        HeadImageView ivAvatar;
        @BindView(R.id.gameAccountName)
        TextView gameAccountName;
        @BindView(R.id.gameChannelName)
        TextView gameChannelName;
        @BindView(R.id.gameLevel)
        TextView gameLevel;
        @BindView(R.id.checkStatusIv)
        ImageView checkStatusIv;


        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            GameAccountResultModel.ListBean data = (GameAccountResultModel.ListBean) itemType;

            //头像
            if (!StringUtils.isEmpty(data.getGame_logo())) {
                Glide.with(activity).load(data.getGame_logo()).into(ivAvatar.getAvatarView());
            }
            //游戏账号
            gameAccountName.setText(data.getGame_account());
            //游戏渠道名
            gameChannelName.setText(data.getGame_channel_name());
            //vip等级
            gameLevel.setText("VIP"+data.getVip_level());

            //选中
            if(data.isSelected()){
                checkStatusIv.setVisibility(View.VISIBLE);
            }else{
                checkStatusIv.setVisibility(View.INVISIBLE);
            }

        }
    }
}
