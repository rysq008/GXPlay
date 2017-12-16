package com.game.helper.fragments;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.utils.Utils;

import butterknife.BindView;

public class GameDetailInfoFragment extends XBaseFragment {
    @BindView(R.id.ll_img_container)
    LinearLayout mImgContainer;

    public static GameDetailInfoFragment newInstance() {
        GameDetailInfoFragment fragment = new GameDetailInfoFragment();
        return fragment;
    }

    private void inflateImg() {
        ImageView img = (ImageView) View.inflate(context, R.layout.game_detail_img_item_layout, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(getContext(), 150), Utils.dip2px(getContext(), 240));
        layoutParams.setMargins(Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 10), 0, 0);
        mImgContainer.addView(img, layoutParams);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        for (int i = 0; i < 5; i++) {
            inflateImg();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_detail_info;
    }

    @Override
    public Object newP() {
        return null;
    }
}
