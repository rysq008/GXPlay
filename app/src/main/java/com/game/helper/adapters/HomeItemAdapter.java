package com.game.helper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.model.BannerResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.PlatformMessageResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.views.BannerView;
import com.game.helper.views.HotView;
import com.game.helper.views.NoticeView;
import com.game.helper.views.RecommendView;
import com.game.helper.views.SpecialView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import zlc.season.practicalrecyclerview.ItemType;

/**
 *
 */
public class HomeItemAdapter extends SimpleRecAdapter<ItemType, HomeItemAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public HomeItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_home;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        int type = item.itemType();
        holder.setDisplay(type, item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_banner_layout)
        BannerView bannerLayout;
        @BindView(R.id.home_notice_layout)
        NoticeView noticeLayout;
        @BindView(R.id.home_special_layout)
        SpecialView specialLayout;
        @BindView(R.id.home_hot_layout)
        HotView hotLayout;
        @BindView(R.id.home_recommond_layout)
        RecommendView recommondLayout;

        Map<Integer, View> map = new HashMap<Integer, View>();

        public void setDisplay(int type, ItemType itemType) {
            for (Map.Entry<Integer, View> entry : map.entrySet()) {
                if (entry.getKey() == type) {
                    entry.getValue().setVisibility(View.VISIBLE);
                    switch (type) {
                        case RxConstant.HomeModeType.Banner_Model_Type:
                            ((BannerView) entry.getValue()).setData((BannerResults) itemType);
                            break;
                        case RxConstant.HomeModeType.Notice_Model_Type:
                            ((NoticeView) entry.getValue()).setData((PlatformMessageResults) itemType);
                            break;
                        case RxConstant.HomeModeType.Special_Model_Type:
                            ((SpecialView) entry.getValue()).setData((SpecialResults) itemType);
                            break;
                        case RxConstant.HomeModeType.Hot_Model_Type:
                            ((HotView) entry.getValue()).setData((HotResults) itemType);
                            break;
                        case RxConstant.HomeModeType.Recommend_Model_Type:
                            ((RecommendView) entry.getValue()).setData((RecommendResults.RecommendItem) itemType);
                            break;
                    }
                } else {
                    entry.getValue().setVisibility(View.GONE);
                }
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            map.put(RxConstant.HomeModeType.Banner_Model_Type, bannerLayout);
            map.put(RxConstant.HomeModeType.Notice_Model_Type, noticeLayout);
            map.put(RxConstant.HomeModeType.Special_Model_Type, specialLayout);
            map.put(RxConstant.HomeModeType.Hot_Model_Type, hotLayout);
            map.put(RxConstant.HomeModeType.Recommend_Model_Type, recommondLayout);
        }
    }
}
