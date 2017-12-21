package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.adapters.SpecialMoreItemAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.widget.SCommonItemDecoration;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/20.
 */

public class SpecialMoreFragment extends XBaseFragment{
    public static final String TAG = SpecialMoreFragment.class.getSimpleName();

    public static SpecialMoreFragment newInstance() {
        return new SpecialMoreFragment();
    }
    private StateView errorView;

    @BindView(R.id.special_more_XRecyclerContentLayout)
    XRecyclerContentLayout mXRv;

    @BindView(R.id.action_bar_tittle)
    TextView mTvTittle;


    SpecialMoreItemAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH,1);
        loadAdapterData(1);
    }

    private void initAdapter() {
        mTvTittle.setText("主题");
        mXRv.getRecyclerView().verticalLayoutManager(context);
        if (mAdapter == null) {
            mAdapter = new SpecialMoreItemAdapter(context);
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, SpecialMoreItemAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, SpecialMoreItemAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    Bundle bundle = new Bundle();
                    bundle.putString("imagerurl",((SpecialResults.SpecialItem)model).image);
                    bundle.putString("content",((SpecialResults.SpecialItem)model).content);
                    bundle.putString("name",((SpecialResults.SpecialItem)model).name);
                    bundle.putInt("id",((SpecialResults.SpecialItem)model).id);
                    DetailFragmentsActivity.launch(context, bundle, SpecialDetailFragment.newInstance());
                }
            });
        }
       mXRv.getRecyclerView().setAdapter(mAdapter);
        /*int verticalSpace = Utils.dip2px(context,0);
        int horizontalSpace = Utils.dip2px(context,10);
        SparseArray<SCommonItemDecoration.ItemDecorationProps> propMap = new SparseArray<>();
        SCommonItemDecoration.ItemDecorationProps prop1 =
                new SCommonItemDecoration.ItemDecorationProps(horizontalSpace, verticalSpace, true, false);
        propMap.put(0, prop1);
        mXRv.getRecyclerView().addItemDecoration(new SCommonItemDecoration(propMap));*/
        mXRv.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH,1);
                loadAdapterData(1);
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.REFRESH,page);
                loadAdapterData(page);
            }
        });
        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(mXRv.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }

        mXRv.getRecyclerView().useDefLoadMoreView();
        mXRv.errorView(errorView);
        mXRv.loadingView(View.inflate(context, R.layout.view_loading, null));
        mXRv.showLoading();
    }

    private void loadAdapterData(int page) {
        Flowable<HttpResultModel<SpecialResults>> fr = DataService.getSpecialMoreList(new BaseRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<SpecialResults>>() {
            @Override
            public void accept(HttpResultModel<SpecialResults> specialResultsHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(specialResultsHttpResultModel.data.list);
                showData(specialResultsHttpResultModel.current_page, specialResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_special_more_list;
    }

    @Override
    public Object newP() {
        return null;
    }

    public void showError(NetError error) {
        mXRv.getLoadingView().setVisibility(View.GONE);
        mXRv.refreshState(false);
        mXRv.showError();
    }

    public void showData(int cur_page, int total_page, List model) {

        //mXRv.getLoadingView().setVisibility(View.GONE);
        if (model.size() < 1) {
            mXRv.showEmpty();

        } else {
            //mXRv.showContent();
            if (cur_page > 1) {
                mAdapter.addData(model);
            } else {
                mAdapter.setData(model);
            }
            mXRv.getRecyclerView().setPage(cur_page, total_page);
            //mXRv.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.action_bar_back)
    public void onClick(){
        getActivity().finish();
    }


}
