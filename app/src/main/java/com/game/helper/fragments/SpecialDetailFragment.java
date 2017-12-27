package com.game.helper.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.adapters.SpecialDetailAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.SpecialDetailResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.SpecialDetailRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
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

public class SpecialDetailFragment extends XBaseFragment{
    public static final String TAG = SpecialDetailFragment.class.getSimpleName();
    private View headView;
    private int mSpecialId;

    public static SpecialDetailFragment newInstance() {
        return new SpecialDetailFragment();
    }
    private StateView errorView;

    @BindView(R.id.xrcl_special_detail)
    XReloadableRecyclerContentLayout mXRv;

    @BindView(R.id.action_bar_tittle)
    TextView mTvTittle;

    @OnClick(R.id.action_bar_back)
    public void onClick(){
        getActivity().finish();
    }

    ImageView mIv;
    TextView mTvName;
    TextView mTvContent;

    private SpecialDetailAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String imagerurl = arguments.getString("imagerurl");
        String content = arguments.getString("content");
        String name = arguments.getString("name");
        mSpecialId = arguments.getInt("id");
        headView = layoutInflater.inflate(R.layout.head_special_detail, null);
        /*FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,0,Utils.dip2px(context,40));
        headView.setLayoutParams(params);*/

        mIv = headView.findViewById(R.id.item_special_more_iv);
        mTvName = headView.findViewById(R.id.item_special_more_name_tv);
        mTvContent = headView.findViewById(R.id.item_special_more_content_tv);
        if(!TextUtils.isEmpty(imagerurl)){
            ILFactory.getLoader().loadNet(mIv, Api.API_PAY_OR_IMAGE_URL.concat(imagerurl), ILoader.Options.defaultOptions());
            mTvName.setText(name);
            mTvContent.setText(content);
        }
        initAdapter();
        loadAdapterData(1,mSpecialId,true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_special_detail;
    }

    @Override
    public Object newP() {
        return null;
    }

    private void initAdapter() {
        mTvTittle.setText("主题");
        mXRv.getRecyclerView().verticalLayoutManager(context);

        if (mAdapter == null) {
            mAdapter = new SpecialDetailAdapter(context);
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, SpecialDetailAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, SpecialDetailAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);

                }
            });
        }
        mXRv.getRecyclerView().setAdapter(mAdapter);
        mXRv.getRecyclerView().addHeaderView(headView);
        mXRv.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadAdapterData(1,mSpecialId,false);
            }

            @Override
            public void onLoadMore(int page) {
                loadAdapterData(page,mSpecialId,false);
            }
        });
    }

    private void loadAdapterData(int page,int specialId,boolean showLoading) {
        Flowable<HttpResultModel<SpecialDetailResults>> fr = DataService.getSpecialDetailList(new SpecialDetailRequestBody(specialId,page));
        RxLoadingUtils.subscribeWithReload(mXRv,fr, bindToLifecycle(), new Consumer<HttpResultModel<SpecialDetailResults>>() {
            @Override
            public void accept(HttpResultModel<SpecialDetailResults> specialDetailResultsHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(specialDetailResultsHttpResultModel.data.list);
                showData(specialDetailResultsHttpResultModel.current_page, specialDetailResultsHttpResultModel.total_page, list);
            }
        },null,null,showLoading);
    }

    public void showData(int cur_page, int total_page, List model) {
        if (model.size() < 1) {
            mXRv.showEmpty();

        } else {
            if (cur_page > 1) {
                mAdapter.addData(model);
            } else {
                mAdapter.setData(model);
            }
            mXRv.getRecyclerView().setPage(cur_page, total_page);
        }
    }
}
