package com.game.helper.adapters;

import android.view.ViewGroup;

import com.game.helper.data.RxConstant;
import com.game.helper.model.HotResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.viewholders.HotViewHolder;
import com.game.helper.viewholders.NoticeViewHolder;
import com.game.helper.viewholders.RecommendViewHolder;
import com.game.helper.viewholders.SpecialViewHolder;

import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.AbstractViewHolder;
import zlc.season.practicalrecyclerview.ItemType;

/**
 *
 */
public class HomeItemAdapter extends AbstractAdapter<ItemType, AbstractViewHolder> {

    @Override
    protected AbstractViewHolder onNewCreateViewHolder(ViewGroup parent, int viewType) {
/*        if (viewType == RxConstant.HomeModeType.Banner_Model_Type) {
            return new BannerViewHolder(parent);
        } else */
        if (viewType == RxConstant.HomeModeType.Notice_Model_Type) {
            return new NoticeViewHolder(parent);
        } else if (viewType == RxConstant.HomeModeType.Special_Model_Type) {
            return new SpecialViewHolder(parent);
        } else if (viewType == RxConstant.HomeModeType.Hot_Model_Type) {
            return new HotViewHolder(parent);
        } else if (viewType == RxConstant.HomeModeType.Recommend_Model_Type) {
            return new RecommendViewHolder(parent);
        }
        return null;
    }

    @Override
    protected void onNewBindViewHolder(AbstractViewHolder holder, int position) {
/*        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).setData((BannerResults) get(position));
        } else*/
        if (holder instanceof NoticeViewHolder) {
            ((NoticeViewHolder) holder).setData((NoticeResults) get(position));
        } else if (holder instanceof SpecialViewHolder) {
            ((SpecialViewHolder) holder).setData((SpecialResults) get(position));
        } else if (holder instanceof HotViewHolder) {
            ((HotViewHolder) holder).setData((HotResults) get(position));
        } else if (holder instanceof RecommendViewHolder) {
            ((RecommendViewHolder) holder).setData((RecommendResults.RecommendItem) get(position));
        }
    }

}
