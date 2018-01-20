package com.game.helper.fragments.coupon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;

import com.game.helper.R;
import com.game.helper.adapters.CouponCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.UnAvailableRedpackResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.net.model.UnAvailableRedpackRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponListFragment extends XBaseFragment implements View.OnClickListener {
    public static final String TAG = CouponListFragment.class.getSimpleName();
    private int Show_List_Type = CouponCommonAdapter.Type_Coupon_Canuse;
    @BindView(R.id.rc_wallet_list)
    XReloadableRecyclerContentLayout mContent;

    private CouponCommonAdapter mAdapter;

    public static CouponListFragment newInstance(int type) {
        return new CouponListFragment(type);
    }

    public CouponListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CouponListFragment(int type) {
        // Required empty public constructor
        this.Show_List_Type = type;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coupon_list;
    }

    private void initView() {
        mContent.showLoading();
        initList();
        getDataFromNet(1);
        SwipeBackHelper.getCurrentPage(getActivity()).setDisallowInterceptTouchEvent(true);
    }

    /**
     * 获取数据
     */
    private void getDataFromNet(int page) {
        if (Show_List_Type == CouponCommonAdapter.Type_Coupon_Canuse) getCanUseCouponListData(page);
        if (Show_List_Type == CouponCommonAdapter.Type_Coupon_Hasuse) getHasUseCouponListData(page);
        if (Show_List_Type == CouponCommonAdapter.Type_Coupon_Outuse) getOutUseCouponListData(page);
    }

    private void initList() {
        mAdapter = null;
        mAdapter = new CouponCommonAdapter(getContext(), null, Show_List_Type);
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
     * 未使用
     */
    private void getCanUseCouponListData(final int page) {
        Flowable<HttpResultModel<AvailableRedpackResultModel>> fr = DataService.getRedPackInfo(new AvailableRedpackRequestBody(page, 0, "999999.99"));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<AvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<AvailableRedpackResultModel> availableRedpackResultModel) throws Exception {
                notifyData(availableRedpackResultModel.data.getList(), page);
                mContent.getRecyclerView().setPage(availableRedpackResultModel.current_page, availableRedpackResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 已使用
     */
    private void getHasUseCouponListData(final int page) {
        Flowable<HttpResultModel<UnAvailableRedpackResultModel>> fr = DataService.getUnuseRedPackInfo(new UnAvailableRedpackRequestBody(page, UnAvailableRedpackRequestBody.Type_Used_Coupon));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<UnAvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<UnAvailableRedpackResultModel> unAvailableRedpackResultModel) throws Exception {
                notifyData(unAvailableRedpackResultModel.data.list, page);
                mContent.getRecyclerView().setPage(unAvailableRedpackResultModel.current_page, unAvailableRedpackResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 过期
     */
    private void getOutUseCouponListData(final int page) {
        Flowable<HttpResultModel<UnAvailableRedpackResultModel>> fr = DataService.getUnuseRedPackInfo(new UnAvailableRedpackRequestBody(page, UnAvailableRedpackRequestBody.Type_Out_Time));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<UnAvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<UnAvailableRedpackResultModel> unAvailableRedpackResultModel) throws Exception {
                notifyData(unAvailableRedpackResultModel.data.list, page);
                mContent.getRecyclerView().setPage(unAvailableRedpackResultModel.current_page, unAvailableRedpackResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void notifyData(List data, int page) {
        mAdapter.setData(data, page == 1 ? true : false);
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        if (mAdapter.getItemCount() < 1) {
            mContent.showEmpty();
            return;
        } else {
            mContent.showContent();
        }
    }

    public void showError(NetError error) {
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showError();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public Object newP() {
        return null;
    }
}
