package com.game.helper.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MineOrderlistResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineOrderFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = MineOrderFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_list)
    XRecyclerContentLayout mContent;

    private MineOrderAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    public static MineOrderFragment newInstance(){
        return new MineOrderFragment();
    }

    public MineOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine_single_list;
    }


    private void initView(){
        mHeadTittle.setText(getResources().getString(R.string.common_mine_order));
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
        mAdapter = new MineOrderAdapter(getContext(), null);
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
        Flowable<HttpResultModel<MineOrderlistResults>> fr = DataService.getMineOrderList(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MineOrderlistResults>>() {
            @Override
            public void accept(HttpResultModel<MineOrderlistResults> invatationResultsHttpResultModel) throws Exception {
                notifyData(invatationResultsHttpResultModel.data,page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void notifyData(MineOrderlistResults data, int page){
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

    class MineOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List data = new ArrayList();
        private Context context;

        public MineOrderAdapter(Context context, List data) {
            this.context = context;
            if (data != null) this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_mine_order_list_item, parent, false);
            return new MineOrderAdapter.MineOrderHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MineOrderAdapter.MineOrderHolder){
                MineOrderAdapter.MineOrderHolder extensionHistoryHolder = (MineOrderAdapter.MineOrderHolder) holder;
                extensionHistoryHolder.onBind(position);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(MineOrderlistResults data, boolean clear){
            if (data == null) return;
            List list = data.list;
            if (clear) this.data = list;
            else this.data.addAll(list);
            notifyDataSetChanged();
        }

        class MineOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private int position = 0;
            private View rootView;
            private RoundedImageView avatar;
            private TextView name;
            private TextView time;
            private TextView account;
            private TextView price;
            private ImageView status;

            public MineOrderHolder(View view) {
                super(view);
                rootView = view;
                avatar = view.findViewById(R.id.riv_avatar);
                name = view.findViewById(R.id.tv_name);
                time = view.findViewById(R.id.tv_time);
                account = view.findViewById(R.id.tv_account);
                price = view.findViewById(R.id.tv_price);
                status = view.findViewById(R.id.iv_status);
                rootView.setOnClickListener(this);
            }

            public void onBind(int position){
                this.position = position;
                MineOrderlistResults.MineOrderlistItem item = (MineOrderlistResults.MineOrderlistItem) data.get(position);
                rootView.setTag(item);
                ILFactory.getLoader().loadNet(avatar, Api.API_PAY_OR_IMAGE_URL + item.game_logo, ILoader.Options.defaultOptions());
                name.setText(item.game_name);
                time.setText(item.create_time);
                account.setText(item.game_account);
                price.setText(item.amount);
                if (item.status == 1){
                    status.setImageResource(R.mipmap.ic_mine_order_undo);
                }
                if (item.status == 2){
                    status.setImageResource(R.mipmap.ic_mine_order_finish);
                }
                if (item.status == 3){
                    status.setImageResource(R.mipmap.ic_mine_order_fail);
                }
            }

            @Override
            public void onClick(View v) {
                if (v == rootView){
                    Toast.makeText(getContext(), ((MineOrderlistResults.MineOrderlistItem)rootView.getTag()).game_name, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
