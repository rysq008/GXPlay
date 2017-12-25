package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
    private GameAccountResultModel.ListBean bean;
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
                clearStatus();
                ((GameAccountResultModel.ListBean) item).setSelected(!(((GameAccountResultModel.ListBean) item).isSelected()));
                recordAccount((GameAccountResultModel.ListBean) item);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 记录选中
     *
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
        RoundedImageView iv_vip_level;
        @BindView(R.id.gameAccountName)
        TextView gameAccountName;
        @BindView(R.id.gameChannelName)
        TextView gameChannelName;
        @BindView(R.id.checkStatusIv)
        ImageView checkStatusIv;
        @BindView(R.id.gameName)
        TextView gameName;


        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            GameAccountResultModel.ListBean data = (GameAccountResultModel.ListBean) itemType;

            //游戏icon
            ILFactory.getLoader().loadNet(ivAvatar, Api.API_PAY_OR_IMAGE_URL.concat(data.getGame_logo()), ILoader.Options.defaultOptions());

            //游戏账号
            gameAccountName.setText(data.getGame_account());
            //游戏渠道名
            gameChannelName.setText(data.getGame_channel_name());
            //vip等级
            int vipLevel = data.getVip_level();
            switch (vipLevel) {
                case 0:
//                    iv_vip_level.setImageResource(R.mipmap.vip0);
                    iv_vip_level.setVisibility(View.GONE);
                    break;
                case 1:
                    iv_vip_level.setVisibility(View.VISIBLE);
                    iv_vip_level.setImageResource(R.mipmap.vip1);
                    break;
                case 2:
                    iv_vip_level.setVisibility(View.VISIBLE);
                    iv_vip_level.setImageResource(R.mipmap.vip2);
                    break;
                case 3:
                    iv_vip_level.setVisibility(View.VISIBLE);
                    iv_vip_level.setImageResource(R.mipmap.vip3);
                    break;
                default:
                    break;
            }

            //游戏名称
            gameName.setText(data.getGame_name());

            //选中
            if (data.isSelected()) {
                checkStatusIv.setVisibility(View.VISIBLE);
                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemCheked(data);
                }
            } else {
                checkStatusIv.setVisibility(View.INVISIBLE);
            }

        }
    }
}
