package com.game.helper.views.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.adapters.GameListAdapter;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GameListResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GameListRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.List;

import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/15.
 */

public class GamePopupWindow extends PopupWindow {

    public static final String TAG = "GamePopupWindow";

    private Activity context;
    private View contentView;

    private SmartRefreshLayout refresh_layout;
    private RecyclerView recycler_view;
    private TextView no_match;

    GameListAdapter adapter;
    private int page = 1;
    private int totalPage;
    List<GameListResultModel.ListBean> mDatas = null;

    private String word = "";

    public GamePopupWindow(Activity context) {
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.fragment_game_adapter_layout, null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        initView();
    }

    public void init(String key) {
        this.word = key;
        getGameListAccordingKey(page, word);
    }

    private void initView() {
        refresh_layout = contentView.findViewById(R.id.refresh_layout);
        recycler_view = contentView.findViewById(R.id.recycler_view);
        no_match = contentView.findViewById(R.id.no_match);

        adapter = new GameListAdapter(context);
        recycler_view.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setNestedScrollingEnabled(true);

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context);//指定为经典Footer，默认是 BallPulseFooter
            }
        });


        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                getGameListAccordingKey(page, word);
            }
        });

        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (++page <= totalPage) {
                    getGameListAccordingKey(page, word);
                } else {
                    noMoreData();
                }

            }
        });

    }

    public void stopRefresh() {
        if (refresh_layout.isRefreshing()) {
            refresh_layout.finishRefresh();
        }
        if (refresh_layout.isLoading()) {
            refresh_layout.finishLoadmore();
        }
    }

    @Override
    public void dismiss() {
        page = 1;
        super.dismiss();
    }

    /**
     * 根据关键词搜游戏
     *
     * @param page
     */
    private void getGameListAccordingKey(int page, String word) {
        Flowable<HttpResultModel<GameListResultModel>> fr = DataService.getGameAccountList(new GameListRequestBody(page, word));
        RxLoadingUtils.subscribe(fr, new FlowableTransformer() {
            @Override
            public Publisher apply(Flowable upstream) {
                return upstream;
            }
        }, new Consumer<HttpResultModel<GameListResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameListResultModel> recommendResultsHttpResultModel){
                Log.d(TAG,"showData");
                totalPage = recommendResultsHttpResultModel.total_page;
                showData(recommendResultsHttpResultModel.data.getList(), recommendResultsHttpResultModel.current_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG,"NetErrorNetErrorNetErrorNetErrorNetError");
//                noData();
//                Toast.makeText(context,"请求出错了",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showData(List<GameListResultModel.ListBean> datas, int cur_page) {
        Log.d(TAG, "展示数据");
        no_match.setVisibility(View.GONE);
        stopRefresh();

        boolean isEmpty = false;
        if (datas == null || datas.isEmpty()) {
            isEmpty = true;
        }

        if (cur_page > 1) {
            if (!isEmpty) {
                if (mDatas == null) {
                    mDatas = datas;
                } else {
                    mDatas.addAll(datas);
                }
            }
        } else {
            mDatas = datas;
        }

        adapter.fillDatas(mDatas);
    }

    public void noData() {
        Log.d(TAG, "noData: 没有数据");
        no_match.setVisibility(View.VISIBLE);
        adapter.fillDatas(Collections.<GameListResultModel.ListBean>emptyList());
        stopRefresh();
    }

    public void noMoreData() {
        Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
        stopRefresh();
        page--;
    }

}
