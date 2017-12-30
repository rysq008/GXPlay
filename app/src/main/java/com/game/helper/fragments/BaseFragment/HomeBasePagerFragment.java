package com.game.helper.fragments.BaseFragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.game.helper.R;
import com.game.helper.present.HomeFragmentPresent;
import com.game.helper.views.SearchComponentView;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.game.helper.views.XReloadableStateContorller;
import com.game.helper.views.widget.StateView;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by wanglei on 2016/12/31.
 */

public abstract class HomeBasePagerFragment extends XBaseFragment<HomeFragmentPresent> {

    @BindView(R.id.home_root_layout)
    XReloadableStateContorller xStateController;
    @BindView(R.id.contentLayout)
    XReloadableRecyclerContentLayout contentLayout;//content
    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;

    protected static final int MAX_PAGE = 10;
    private ObjectAnimator mAnimator;
    private boolean mShow = true;

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getP().onRefreshData(xStateController, true);
    }

    private void initAdapter() {
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView().setAdapter(getAdapter());
        contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getP().onRefreshData(xStateController, false);
            }

            @Override
            public void onLoadMore(int page) {
                getP().loadMoreData(contentLayout, page);
            }
        });

        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {//down
                    if (!mShow) {
                        animToolBar(1);//显示toolBar
                        mShow = !mShow;
                    }
                } else {//up
                    if (mShow) {
                        animToolBar(0);//隐藏toolBar
                        mShow = !mShow;
                    }
                }
            }
        });

    }

    private void animToolBar(int flag) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == 0) {//向上滑隐藏
            mAnimator = ObjectAnimator.ofFloat(searchComponentView, "translationY", searchComponentView.getTranslationY(), -searchComponentView.getHeight());
        } else {//向下滑
            mAnimator = ObjectAnimator.ofFloat(searchComponentView, "translationY", searchComponentView.getTranslationY(), 0);
        }
        mAnimator.start();
    }

    public abstract SimpleRecAdapter getAdapter();

    public abstract void setLayoutManager(XRecyclerView recyclerView);

    public abstract String getType();

    public void showError(NetError error) {
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            getAdapter().addData(model);
        } else {
            getAdapter().setData(model);
        }
        contentLayout.getRecyclerView().setPage(cur_page, total_page);
//        contentLayout.removeView(contentLayout.getLoadingView());
//        xStateController.removeView(xStateController.getLoadingView());
        if (getAdapter().getItemCount() < 1) {
            xStateController.showEmpty();
            contentLayout.showEmpty();
            return;
        } else {
            xStateController.showContent();
            contentLayout.showContent();
//            xStateController.removeView(xStateController.getEmptyView());
//            contentLayout.removeView(contentLayout.getEmptyView());
            return;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public HomeFragmentPresent newP() {
        return new HomeFragmentPresent();
    }

}
