package com.game.helper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.views.SearchComponentView;
import com.game.helper.views.SearchHistoryItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;

public class SearchFragment extends XBaseFragment implements SearchHistoryItemView.OnHistoryItemClickListener, View.OnClickListener {
    public static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;
    @BindView(R.id.iv_delete)
    ImageView mDelete;
    @BindView(R.id.ll_hot)
    LinearLayout mHotContainer;
    @BindView(R.id.ll_history)
    LinearLayout mHistoryContainer;

    //grid view
    private SearchHistoryItemView mHotView;
    private SearchHistoryItemView mHistoryView;

    //data
    private List<String> mHotData;
    private List<String> mHistoryData;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private void initView() {
        String[] hot = {"决斗之夜", "雷鸣三国", "勇者大作战", "三国", "全情三国", "三国传", "泡泡堂", "皇室战争"};
        mHotData = new ArrayList<>();
        for (int i = 0; i < hot.length; i++) {
            mHotData.add(hot[i]);
        }

        String[] history = {"勇者大作战"};
        mHistoryData = new ArrayList<>();
        for (int i = 0; i < history.length; i++) {
            mHistoryData.add(history[i]);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(Kits.Dimens.dpToPxInt(getContext(), 10), 0, 0, 0);
        mHotView = new SearchHistoryItemView(getContext(), mHotData);
        mHotContainer.addView(mHotView, layoutParams);
        mHistoryView = new SearchHistoryItemView(getContext(), mHistoryData);
        mHistoryContainer.addView(mHistoryView, layoutParams);

        mHotView.setOnHistoryItemClickListener(this);
        mHistoryView.setOnHistoryItemClickListener(this);
        mDelete.setOnClickListener(this);
    }

    @Override
    public void onHistoryItemClick(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == mDelete) {
            mHistoryView.clearData();
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        searchComponentView.setCenterViewOnClick(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        searchComponentView.setRightViewOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "search !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public Object newP() {
        return null;
    }
}
