package com.game.helper.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.adapters.GameItemAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.HotWordResults;
import com.game.helper.model.SearchListResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.SearchRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.SearchComponentView;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.game.helper.views.XReloadableStateContorller;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class SearchFragment extends XBaseFragment {
    public static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;
    @BindView(R.id.common_search_reload_layout)
    XReloadableStateContorller xReloadableStateContorller;
    @BindView(R.id.common_search_xrecycler)
    XReloadableRecyclerContentLayout xReloadableRecyclerContentLayout;
    @BindView(R.id.iv_delete)
    ImageView mDelete;
    @BindView(R.id.ll_hot)
    LinearLayout mHotContainer;
    @BindView(R.id.ll_history)
    LinearLayout mHistoryContainer;
    @BindView(R.id.hot_flowlayout)
    TagFlowLayout hotFlowlayout;
    @BindView(R.id.histtory_flowlayout)
    TagFlowLayout historyFlowlayout;
    @BindView(R.id.common_search_word_layout)
    LinearLayout searchWordLayout;


    private List<HotWordResults.HotWordItem> hotWordList;
    private List<String> historyWordList;
    private GameItemAdapter searchListAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private void initView() {
        hotWordList = new ArrayList<>();
        historyWordList = new ArrayList<>();
        if (!Kits.Empty.check(SharedPreUtil.getSearchHistoryList())) {
            historyWordList.addAll(SharedPreUtil.getSearchHistoryList());
        }
        hotFlowlayout.setAdapter(new TagAdapter<HotWordResults.HotWordItem>(hotWordList) {
            @Override
            public View getView(FlowLayout parent, int position, HotWordResults.HotWordItem hotWordItem) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.text_tab_hot_search,null);
                //TextView tv = new TextView(context);
                tv.setText(hotWordItem.name);
                return tv;
            }
        });
        hotFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                HotWordResults.HotWordItem hot = hotWordList.get(position);
//                Toast.makeText(getActivity(), hot.name, Toast.LENGTH_SHORT).show();
                searchComponentView.setTextContent(hot.name);
                loadSearchListData(true, 1, hot.name);
                return true;
            }
        });

        historyFlowlayout.setAdapter(new TagAdapter<String>(historyWordList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.text_tab_hot_search,null);
                tv.setText(s);
                return tv;
            }
        });
        historyFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                Toast.makeText(getActivity(), historyWordList.get(position), Toast.LENGTH_SHORT).show();
                searchComponentView.setTextContent(historyWordList.get(position));
                loadSearchListData(true, 1, historyWordList.get(position));
                return true;
            }
        });

        xReloadableRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == searchListAdapter) {
            searchListAdapter = new GameItemAdapter(context);
        }
        xReloadableRecyclerContentLayout.getRecyclerView().setAdapter(searchListAdapter);
        xReloadableRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
        xReloadableRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadSearchListData(false, 1, searchComponentView.getTextContent());
            }

            @Override
            public void onLoadMore(int page) {
                loadSearchListData(false, page, searchComponentView.getTextContent());
            }
        });
        loadHotWordData();

    }

    @OnClick({R.id.iv_delete})
    public void onButterKnifeBtnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                historyWordList.clear();
                historyFlowlayout.getAdapter().notifyDataChanged();
                SharedPreUtil.saveSearchHistoryList(historyWordList);
                break;
        }
    }

    public void loadHotWordData() {
        Flowable<HttpResultModel<HotWordResults>> flowable = DataService.getApiHotWordData(new BaseRequestBody(1));
        RxLoadingUtils.subscribeWithReload(xReloadableStateContorller, flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<HotWordResults>>() {
            @Override
            public void accept(HttpResultModel<HotWordResults> hotWordResultsHttpResultModel) throws Exception {
                hotWordList.addAll(hotWordResultsHttpResultModel.data.list);
                if (Kits.Empty.check(hotWordList)) {
                    xReloadableStateContorller.showEmpty();
                    return;
                }
                xReloadableStateContorller.showContent();
                hotFlowlayout.getAdapter().notifyDataChanged();
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                xReloadableStateContorller.showEmpty();
            }
        }, null, false);

    }

    public void loadSearchListData(final boolean showLoading, int page, String word) {
        xReloadableRecyclerContentLayout.setVisibility(View.VISIBLE);
        xReloadableStateContorller.setVisibility(View.GONE);
        if (!historyWordList.contains(word)) {
            historyWordList.add(word);
            historyFlowlayout.getAdapter().notifyDataChanged();
            SharedPreUtil.saveSearchHistoryList(historyWordList);
        }
        Flowable<HttpResultModel<SearchListResults>> flowable = DataService.getApiSearchByWordData(new SearchRequestBody(page, word));
        RxLoadingUtils.subscribeWithReload(xReloadableRecyclerContentLayout, flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<SearchListResults>>() {
            @Override
            public void accept(HttpResultModel<SearchListResults> searchListResultsHttpResultModel) throws Exception {
                ArrayList<SearchListResults.RecommendItem> list = new ArrayList<>();
                list.addAll(searchListResultsHttpResultModel.data.list);
                showData(searchListResultsHttpResultModel.current_page, searchListResultsHttpResultModel.total_page, list);
            }
        }, null, null, showLoading);
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            searchListAdapter.addData(model);
        } else {
            searchListAdapter.setData(model);
        }
        xReloadableRecyclerContentLayout.getRecyclerView().setPage(cur_page, total_page);
//        xReloadableRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        if (searchListAdapter.getItemCount() < 1) {
            xReloadableRecyclerContentLayout.showEmpty();
            return;
        } else {
            xReloadableRecyclerContentLayout.showContent();
            return;
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
//                Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        searchComponentView.setRightViewOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "search !", Toast.LENGTH_SHORT).show();
                String str = searchComponentView.getTextContent();
                loadSearchListData(true, 1, str);
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
