package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.net.api.Api;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 我的账户列表adapter
 */
public class MyAccountAdapter extends SimpleRecAdapter<ItemType, MyAccountAdapter.PhotoViewHolder> {

    private Activity mActivity;
    private OnItemCheckListener onItemCheckListener;

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
                //选中
                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemCheked((GameAccountResultModel.ListBean)item);
                }
            }
        });
    }


    /**
     * 清除选中状态
     */
    private void clearStatus() {
        for (ItemType item : data) {
            ((GameAccountResultModel.ListBean) item).setSelected(false);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_account;
    }

    public void addOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public interface OnItemCheckListener {
        void onItemCheked(GameAccountResultModel.ListBean gameBean);
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'home_page_adapter_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @BindView(R.id.iv_vip_level)
        RoundedImageView ivVipLevel;
        @BindView(R.id.gameAccountNameTv)
        TextView gameAccountNameTv;
        @BindView(R.id.gameName)
        TextView gameName;
        @BindView(R.id.nameLayout)
        LinearLayout nameLayout;
        @BindView(R.id.channelName)
        TextView channelName;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            GameAccountResultModel.ListBean data = (GameAccountResultModel.ListBean) itemType;

            //游戏icon
            ILFactory.getLoader().loadNet(ivAvatar, Api.API_PAY_OR_IMAGE_URL.concat(data.getGame_logo()), ILoader.Options.defaultOptions());

            //游戏账号
            gameAccountNameTv.setText(data.getGame_account());
            //游戏名称
            gameName.setText(data.getGame_name());
            //游戏渠道名
            channelName.setText(data.getGame_channel_name());
            //vip等级
            int vipLevel = data.getVip_level();
            switch (vipLevel) {
                case 0:
//                    iv_vip_level.setImageResource(R.mipmap.vip0);
                    ivVipLevel.setVisibility(View.GONE);
                    break;
                case 1:
                    ivVipLevel.setVisibility(View.VISIBLE);
                    ivVipLevel.setImageResource(R.drawable.vip1_with_white_bg);
                    break;
                case 2:
                    ivVipLevel.setVisibility(View.VISIBLE);
                    ivVipLevel.setImageResource(R.drawable.vip2_with_white_bg);
                    break;
                case 3:
                    ivVipLevel.setVisibility(View.VISIBLE);
                    ivVipLevel.setImageResource(R.drawable.vip3_with_white_bg);
                    break;
                default:
                    break;
            }


        }
    }
}
