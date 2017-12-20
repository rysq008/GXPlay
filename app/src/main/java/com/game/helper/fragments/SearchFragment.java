package com.game.helper.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.file_descriptor.FileDescriptorUriLoader;
import com.game.helper.R;
import com.game.helper.adapters.SearchListAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.HotWordResults;
import com.game.helper.model.SearchListResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.SearchRequestBody;
import com.game.helper.utils.FileUtil;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.UploadUtils;
import com.game.helper.views.ReloadableFrameLayout;
import com.game.helper.views.SearchComponentView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;
    @BindView(R.id.common_search_reload_layout)
    ReloadableFrameLayout reloadableFrameLayout;
    @BindView(R.id.common_search_xrecycler)
    XRecyclerContentLayout xRecyclerContentLayout;
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
    private SearchListAdapter searchListAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private void initView() {
        hotWordList = new ArrayList<>();
        historyWordList = new ArrayList<>();

        hotFlowlayout.setAdapter(new TagAdapter<HotWordResults.HotWordItem>(hotWordList) {
            @Override
            public View getView(FlowLayout parent, int position, HotWordResults.HotWordItem hotWordItem) {
                TextView tv = new TextView(context);
                tv.setText(hotWordItem.name);
                return tv;
            }
        });
        hotFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                HotWordResults.HotWordItem hot = hotWordList.get(position);
                Toast.makeText(getActivity(), hot.name, Toast.LENGTH_SHORT).show();
                if (!historyWordList.contains(hot.name)) {
                    historyWordList.add(hot.name);
                    historyFlowlayout.getAdapter().notifyDataChanged();
                }
                searchComponentView.setTextContent(hot.name);
                loadSearchListData(true, 1, hot.name);
                return true;
            }
        });

        historyFlowlayout.setAdapter(new TagAdapter<String>(historyWordList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = new TextView(context);
                tv.setText(s);
                return tv;
            }
        });
        historyFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(getActivity(), historyWordList.get(position), Toast.LENGTH_SHORT).show();
                searchComponentView.setTextContent(historyWordList.get(position));
                loadSearchListData(true, 1, historyWordList.get(position));
                return true;
            }
        });

        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        searchListAdapter = new SearchListAdapter(context);
        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
        xRecyclerContentLayout.getRecyclerView().setAdapter(searchListAdapter);
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
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
                selectFile(v);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mDelete) {
//            mHistoryView.clearData();
        }
    }

    public void loadHotWordData() {
        Flowable<HttpResultModel<HotWordResults>> flowable = DataService.getApiHotWordData(new BaseRequestBody(1));
        RxLoadingUtils.subscribeWithReload(reloadableFrameLayout, flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<HotWordResults>>() {
            @Override
            public void accept(HttpResultModel<HotWordResults> hotWordResultsHttpResultModel) throws Exception {
                hotWordList.addAll(hotWordResultsHttpResultModel.data.list);
                hotFlowlayout.getAdapter().notifyDataChanged();
            }
        });

    }

    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                setResult(data);
            }
        }
    }

    private void setResult(Intent data) {
        Uri uri = data.getData();
        String path = FileUtil.getPath(context, uri);
        File f = new File(path);


        List<File> list = new ArrayList<File>();
        File f1 = new File("/storage/sdcard1/wx_camera_1513403845294.jpg");
        list.add(f);
        list.add(f1);
        list.add(f);
        list.add(f1);

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        Flowable<HttpResultModel> ff = DataService.setApiUserIcon(list, new UploadUtils.FileUploadProgress() {
            @Override
            public void onProgress(final int progress) {
                dialog.setProgress(progress);
            }
        });

        RxLoadingUtils.subscribeWithDialog(dialog, ff, this.bindToLifecycle(), new Consumer<HttpResultModel>() {
            @Override
            public void accept(HttpResultModel httpResultModel) throws Exception {
                if(httpResultModel.isSucceful()){
                    Toast.makeText(context,((Map<String,String>)httpResultModel.data).get("image"),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Toast.makeText(context,netError.getMessage()+"--"+netError.getType(),Toast.LENGTH_SHORT).show();
            }
        }, null, true);
    }

    public void loadSearchListData(final boolean showLoading, int page, String word) {
        xRecyclerContentLayout.setVisibility(View.VISIBLE);
        searchWordLayout.setVisibility(View.GONE);
        if (showLoading) {
            xRecyclerContentLayout.showLoading();
        }
        Flowable<HttpResultModel<SearchListResults>> flowable = DataService.getApiSearchByWordData(new SearchRequestBody(page, word));
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<SearchListResults>>() {
            @Override
            public void accept(HttpResultModel<SearchListResults> searchListResultsHttpResultModel) throws Exception {
                ArrayList<SearchListResults.SearchListItem> list = new ArrayList<>();
                list.addAll(searchListResultsHttpResultModel.data.list);
                showData(searchListResultsHttpResultModel.current_page, searchListResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }


    public void showError(NetError error) {
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        xRecyclerContentLayout.refreshState(false);
        xRecyclerContentLayout.showError();
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            searchListAdapter.addData(model);
        } else {
            searchListAdapter.setData(model);
        }
        xRecyclerContentLayout.getRecyclerView().setPage(cur_page, total_page);
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        if (searchListAdapter.getItemCount() < 1) {
            xRecyclerContentLayout.showEmpty();
            return;
        } else {
            xRecyclerContentLayout.showContent();
            xRecyclerContentLayout.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
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
                Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        searchComponentView.setRightViewOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "search !", Toast.LENGTH_SHORT).show();
                loadSearchListData(true, 1, "倩女幽魂（02）");
                if (!historyWordList.contains(searchComponentView.getTextContent())) {
                    historyWordList.add(searchComponentView.getTextContent());
                }
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
