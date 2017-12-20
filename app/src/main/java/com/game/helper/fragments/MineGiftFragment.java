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
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.MineGiftlistResults;
import com.game.helper.model.MineGiftlistResults;
import com.game.helper.net.DataService;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.MineGameRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.Utils;
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
public class MineGiftFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = MineGiftFragment.class.getSimpleName();
    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_list)
    XRecyclerContentLayout mContent;

    private MineGiftAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    public static MineGiftFragment newInstance(){
        return new MineGiftFragment();
    }

    public MineGiftFragment() {
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
        mHeadTittle.setText(getResources().getString(R.string.common_mine_gift));
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
        mAdapter = new MineGiftAdapter(getContext(), null);
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
        Flowable<HttpResultModel<MineGiftlistResults>> fr = DataService.getMineGiftList(new MineGameRequestBody(page, RxConstant.PLATFORM_ANDROID));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MineGiftlistResults>>() {
            @Override
            public void accept(HttpResultModel<MineGiftlistResults> mineGiftlistResultsHttpResultModel) throws Exception {
                notifyData(mineGiftlistResultsHttpResultModel.data,page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void notifyData(MineGiftlistResults data, int page){
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

    class MineGiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List data = new ArrayList();
        private Context context;

        public MineGiftAdapter(Context context, List data) {
            this.context = context;
            if (data != null) this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_mine_gift_list_item, parent, false);
            return new MineGiftAdapter.MineGiftHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MineGiftAdapter.MineGiftHolder){
                MineGiftAdapter.MineGiftHolder extensionHistoryHolder = (MineGiftAdapter.MineGiftHolder) holder;
                extensionHistoryHolder.onBind(position);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(MineGiftlistResults data, boolean clear){
            if (data == null) return;
            List list = data.list;
            if (clear) this.data = list;
            else this.data.addAll(list);
            notifyDataSetChanged();
        }

        class MineGiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private int position = 0;
            private View rootView;
            private RoundedImageView icon;
            private TextView name;
            private TextView time;
            private TextView desc;
            private ImageView copy;

            public MineGiftHolder(View view) {
                super(view);
                rootView = view;
                icon = itemView.findViewById(R.id.riv_avatar);
                name = itemView.findViewById(R.id.tv_name);
                time = itemView.findViewById(R.id.tv_time);
                desc = itemView.findViewById(R.id.tv_desc);
                copy = itemView.findViewById(R.id.iv_copy);
                rootView.setOnClickListener(this);
            }

            public void onBind(int position){
                this.position = position;
                rootView.setOnClickListener(this);
                MineGiftlistResults.MineGiftlistItem item = (MineGiftlistResults.MineGiftlistItem) data.get(position);
                ILFactory.getLoader().loadNet(icon, Api.API_PAY_OR_IMAGE_URL+item.gift_code.gift.game.logo,ILoader.Options.defaultOptions());
                name.setText(item.gift_code.gift.giftname);
                time.setText("有效期："+item.gift_code.gift.end_time);
                desc.setText("礼包码："+item.gift_code.code);
                rootView.setTag(item.gift_code.code);
            }

            @Override
            public void onClick(View v) {
                if (v == rootView){
                    if (rootView.getTag()!=null){
                        Utils.copyToClipboard(getContext(),(String) rootView.getTag());
                        Toast.makeText(getContext(), "礼包码已复制到剪贴板！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
