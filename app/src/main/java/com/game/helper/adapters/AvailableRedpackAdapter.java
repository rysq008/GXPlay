package com.game.helper.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.model.AvailableRedpackResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 人脉排行榜adapter
 */
public class AvailableRedpackAdapter extends SimpleRecAdapter<ItemType, AvailableRedpackAdapter.ViewHolder> {

    private Activity mActivity;
    private AvailableRedpackResultModel.ListBean bean;
    private OnItemCheckListener onItemCheckListener;

    public AvailableRedpackAdapter(Activity context) {
        super(context);
        mActivity = context;
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new AvailableRedpackAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item,mActivity,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStatus();
                ((AvailableRedpackResultModel.ListBean)item).setSelect(!(((AvailableRedpackResultModel.ListBean) item).isSelect()));
                recordAccount((AvailableRedpackResultModel.ListBean)item);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_available_redpack;
    }

    public interface OnItemCheckListener{
        void onItemCheked(AvailableRedpackResultModel.ListBean bean);
    }

    public void addOnItemCheckListener(OnItemCheckListener onItemCheckListener){
        this.onItemCheckListener = onItemCheckListener;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'home_page_adapter_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.redPackName)
        TextView redPackName;
        @BindView(R.id.redPackDesc)
        TextView redPackDesc;
        @BindView(R.id.redpackNum)
        TextView recpackNum;
        @BindView(R.id.timeLimit)
        TextView timeLimit;
        @BindView(R.id.rootRl)
        RelativeLayout rootRl;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity,final int position) {
            final AvailableRedpackResultModel.ListBean data = (AvailableRedpackResultModel.ListBean)itemType;

            //名字
            redPackName.setText(data.getName());
            //描述
            redPackDesc.setText(redPackName.getContext().getString(R.string.redpack_limit,data.getMoney_limit()));
            //推广人数
            recpackNum.setText(redPackName.getContext().getString(R.string.redpack_amount,data.getAmount()));
            //到期时间
            timeLimit.setText(data.getEnd_date());

            if(data.isSelect()){
                rootRl.setSelected(true);
                if (onItemCheckListener != null){
                    onItemCheckListener.onItemCheked(data);
                }
            }else{
                rootRl.setSelected(false);
            }

        }
    }

    /**
     * 清除选中状态
     */
    private void clearStatus() {
        for (ItemType item : data) {
            ((AvailableRedpackResultModel.ListBean)item).setSelect(false);
        }
    }

    /**
     * 记录选中
     * @param bean
     */
    public void recordAccount(AvailableRedpackResultModel.ListBean bean) {
        this.bean = bean;
    }

    public AvailableRedpackResultModel.ListBean getRecordAccount() {
        return bean;
    }
}
