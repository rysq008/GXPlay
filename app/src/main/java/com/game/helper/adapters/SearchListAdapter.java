package com.game.helper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.game.helper.R;
import com.game.helper.model.SearchListResults;
import com.game.helper.views.RecommendView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import zlc.season.practicalrecyclerview.ItemType;

/**
 *
 */
public class SearchListAdapter extends SimpleRecAdapter<ItemType, SearchListAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public SearchListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_game_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemType item = data.get(position);
        holder.setDisplay(item);
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

        @BindView(R.id.game_item_layout)
        RecommendView recommondLayout;


        public void setDisplay(ItemType itemType) {
            recommondLayout.setData((SearchListResults.RecommendItem) itemType);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
