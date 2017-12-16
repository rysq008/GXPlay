package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.FriendRangeResultModel;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.kit.Kits;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 收益排行榜adapter
 */
public class RangeIncomeAdapter extends SimpleRecAdapter<ItemType, RangeIncomeAdapter.PhotoViewHolder> {

    private Activity mActivity;


    public RangeIncomeAdapter(Activity context) {
        super(context);
        mActivity = context;
    }

    @Override
    public PhotoViewHolder newViewHolder(View itemView) {
        return new RangeIncomeAdapter.PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
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
        return R.layout.item_range_income;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'home_page_adapter_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.range_number)
        TextView rangeNumber;
        @BindView(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.person_desc)
        TextView personDesc;
        @BindView(R.id.income_coin)
        TextView incomeCoin;
        @BindView(R.id.ll_edit_user_info)
        LinearLayout llEditUserInfo;
        @BindView(R.id.vipLevel)
        ImageView vipLevel;


        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            FriendRangeResultModel.ListBean data = (FriendRangeResultModel.ListBean) itemType;
            //排名
            rangeNumber.setText("");
            switch (position) {
                case 0:
                    rangeNumber.setBackgroundResource(R.mipmap.range_1);
                    break;
                case 1:
                    rangeNumber.setBackgroundResource(R.mipmap.range_2);
                    break;
                case 2:
                    rangeNumber.setBackgroundResource(R.mipmap.range_3);
                    break;
                default:
                    rangeNumber.setBackgroundResource(0);
                    rangeNumber.setText((position + 1) + "");
                    break;
            }
            //头像
            if (!/*StringUtils.isEmpty(data.getMember().getIcon()*/Kits.Empty.check(data.getMember().getIcon())) {
//                Glide.with(activity).load(data.getMember().getIcon()).into(ivAvatar.getAvatarView());
                ILFactory.getLoader().loadNet(ivAvatar, data.getMember().getIcon(), null);
            }
            //名字
            tvName.setText(data.getMember().getNick_name());
            //描述
            personDesc.setText(data.getMember().getSignature());
            //收益金币
            incomeCoin.setText(data.getZongshouyi());

            //会员等级
            int level = data.getMember().getVip_level().getLevel();
            switch (level) {
                case 0:
                    vipLevel.setImageResource(R.mipmap.vip0);
                    break;
                case 1:
                    vipLevel.setImageResource(R.mipmap.vip1);
                    break;
                case 2:
                    vipLevel.setImageResource(R.mipmap.vip2);
                    break;
                case 3:
                    vipLevel.setImageResource(R.mipmap.vip3);
                    break;
            }
        }
    }
}
