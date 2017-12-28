package com.game.helper.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.helper.R;
import com.game.helper.adapters.RechargeCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.InvatationResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.api.ApiService;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
import com.game.helper.views.widget.StateView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.RecyclerAdapter;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * 邀请记录
 */
public class ExtensionHistoryFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = ExtensionHistoryFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_extension_list)
    XRecyclerContentLayout mContent;

    private ExtensionHistoryAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    public static ExtensionHistoryFragment newInstance(){
        return new ExtensionHistoryFragment();
    }

    public ExtensionHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_extension_history;
    }

    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_extension_history));
        mHeadBack.setOnClickListener(this);

        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(mContent.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }
        if (null != errorView.getParent()) ((ViewGroup) errorView.getParent()).removeView(errorView);
        if (loadingView == null) loadingView = View.inflate(context, R.layout.view_loading, null);
        if (null != loadingView.getParent()) ((ViewGroup) loadingView.getParent()).removeView(loadingView);
        mContent.errorView(errorView);
        mContent.loadingView(loadingView);
        mContent.showLoading();
        initList();
        getDataFromNet(1);
    }

    private void initList(){
        mAdapter = null;
        mAdapter = new ExtensionHistoryAdapter(getContext(), null);
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.getRecyclerView().setHasFixedSize(true);
        mContent.getRecyclerView().verticalLayoutManager(context);
        mContent.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mContent.getRecyclerView().setAdapter(mAdapter);
        mContent.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(1);
            }

            @Override
            public void onLoadMore(int page) {
                getDataFromNet(page);
            }
        });
        mContent.getRecyclerView().useDefLoadMoreView();
    }

    /**
     * 获取数据
     * */
    private void getDataFromNet(final int page){
        Flowable<HttpResultModel<InvatationResults>> fr = DataService.getInvatationList(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<InvatationResults>>() {
            @Override
            public void accept(HttpResultModel<InvatationResults> invatationResultsHttpResultModel) throws Exception {
                notifyData(invatationResultsHttpResultModel.data,page);
                mContent.getRecyclerView().setPage(invatationResultsHttpResultModel.current_page,invatationResultsHttpResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void notifyData(InvatationResults data, int page){
        mAdapter.setData(data,page == 1 ? true : false);
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showContent();
    }

    public void showError(NetError error) {
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showError();
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }

    class ExtensionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List data = new ArrayList();
        private Context context;

        public ExtensionHistoryAdapter(Context context, List data) {
            this.context = context;
            if (data != null) this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_extension_history_item, parent, false);
            return new ExtensionHistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ExtensionHistoryHolder){
                ExtensionHistoryHolder extensionHistoryHolder = (ExtensionHistoryHolder) holder;
                extensionHistoryHolder.onBind(position);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(InvatationResults data, boolean clear){
            if (data == null) return;
            List list = data.list;
            if (clear) this.data = list;
            else this.data.addAll(list);
            notifyDataSetChanged();
        }

        class ExtensionHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private int position = 0;
            private View rootView;
            private RoundedImageView avatar;
            private ImageView vip;
            private TextView name;
            private TextView time;
            private TextView content;

            public ExtensionHistoryHolder(View view) {
                super(view);
                rootView = view;
                avatar = view.findViewById(R.id.iv_avatar);
                name = view.findViewById(R.id.tv_name);
                vip = view.findViewById(R.id.iv_vip_level);
                time = view.findViewById(R.id.tv_time);
                content = view.findViewById(R.id.tv_content);
            }

            public void onBind(int position){
                this.position = position;
                rootView.setOnClickListener(this);
                InvatationResults.InvatationListItem item = (InvatationResults.InvatationListItem) data.get(position);
//                Glide.with(context).load(Api.API_BASE_URL+item.member.icon).into(avatar);
                ILFactory.getLoader().loadNet(avatar,Api.API_BASE_URL.concat(item.member.icon),null);
                vip.setImageResource(Utils.getExtensionVipIcon(item.member.vip_level.level));
                name.setText(item.member.nick_name);
                String[] split = item.member.user.date_joined.split(" ");
                time.setText(split[0]);
                content.setText(item.member.signature);
            }

            @Override
            public void onClick(View v) {
            }
        }
    }
}
