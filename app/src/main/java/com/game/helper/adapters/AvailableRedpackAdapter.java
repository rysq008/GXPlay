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
import cn.droidlover.xdroidmvp.kit.Kits;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * 可用红包adapter
 */
public class AvailableRedpackAdapter extends SimpleRecAdapter<ItemType, AvailableRedpackAdapter.ViewHolder> {

    private Activity mActivity;
    private AvailableRedpackResultModel.ListBean mBean;
    private OnItemCheckListener onItemCheckListener;

    public AvailableRedpackAdapter(Activity context, AvailableRedpackResultModel.ListBean bean) {
        super(context);
        mActivity = context;
        mBean = bean;
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new AvailableRedpackAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item, mActivity, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvailableRedpackResultModel.ListBean) item).setSelect(!(((AvailableRedpackResultModel.ListBean) item).isSelect()));
                notifyDataSetChanged();
                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemCheked((AvailableRedpackResultModel.ListBean) item, data.size());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_available_redpack;
    }

    public interface OnItemCheckListener {
        void onItemCheked(AvailableRedpackResultModel.ListBean bean, int redPackNum);
    }

    public void addOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
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
        @BindView(R.id.userule)
        TextView rule;
        @BindView(R.id.timeLimit)
        TextView timeLimit;
        @BindView(R.id.rootRl)
        RelativeLayout rootRl;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDisplay(ItemType itemType, final Activity activity, final int position) {
            final AvailableRedpackResultModel.ListBean data = (AvailableRedpackResultModel.ListBean) itemType;

            //选中之前选中的
            if (null != mBean) {
                if (mBean.equals(data)) {
                    data.setSelect(true);
                } else {
                    data.setSelect(false);
                }
            }
            //名字
            redPackName.setText(data.getName());
            //使用限制
            redPackDesc.setText(redPackName.getContext().getString(R.string.redpack_limit, data.getMoney_limit()));
            //红包金额
            recpackNum.setText(redPackName.getContext().getString(R.string.redpack_amount, data.getAmount()));
            //到期时间
            timeLimit.setText("有效期至" + data.getEnd_date());
            //红包规则
            rule.setText(Kits.Empty.check(data.getUse_rule()) ? "" : data.getUse_rule());

            if (data.isSelect()) {
                rootRl.setSelected(true);
            } else {
                rootRl.setSelected(false);
            }

        }
    }

}
