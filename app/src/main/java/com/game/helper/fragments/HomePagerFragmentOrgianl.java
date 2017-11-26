//package com.game.helper.fragments;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.game.helper.GameMarketApplication;
//import com.game.helper.adapters.HomeItemAdapter;
//import com.game.helper.event.BusProvider;
//import com.game.helper.event.MsgEvent;
//import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;
//import com.game.helper.model.BannerResults;
//import com.game.helper.model.HotResults;
//import com.game.helper.model.RecommendResults;
//import com.game.helper.model.SpecialResults;
//import com.game.helper.multipleitem.MultiItemAdapter;
//import com.game.helper.views.widget.TotoroToast;
//
//import cn.droidlover.xdroidmvp.net.IModel;
//import io.reactivex.functions.Consumer;
//import zlc.season.practicalrecyclerview.AbstractAdapter;
//
///**
// * Created by zr on 2017-10-13.
// */
//
//public class HomePagerFragmentOrgianl extends HomeBasePagerFragment {
//
//    AbstractAdapter adapter;
//    HomeItemAdapter mAdapter;
//
//    @Override
//    public AbstractAdapter getAdapter() {
//        if (mAdapter == null) {
//            mAdapter = new HomeItemAdapter();
//            BusProvider.getBus().register(HomeItemAdapter.class).subscribe(new Consumer<HomeItemAdapter>() {
//                @Override
//                public void accept(HomeItemAdapter homeItemAdapter) throws Exception {
//                    mAdapter.clear();
//                    TotoroToast.makeText(getContext(),"onRegister",1).show();
//                }
//            });
//        BusProvider.getBus().register(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
//            @Override
//            public void accept(MsgEvent msgEvent) throws Exception {
//                if(msgEvent.getRequestCode() == 0){
//                    mAdapter.showError();
//                    TotoroToast.makeText(GameMarketApplication.getContext(),"onMsgRegister",1).show();
//                }
//            }
//        });
//        }
//
//        return mAdapter;
//    }
//
//    @Override
//    public <T extends IModel> AbstractAdapter getAdapter(Class<T> type) {
//        if (type == BannerResults.class) {
//            if (adapter == null) {
//                adapter = new MultiItemAdapter();
////                adapter = new ;
////            adapter.setRecItemClick(new RecyclerItemCallback<GankResults.Item, HomeAdapter.ViewHolder>() {
////                @Override
////                public void onItemClick(int position, GankResults.Item model, int tag, HomeAdapter.ViewHolder holder) {
////                    super.onItemClick(position, model, tag, holder);
////                    switch (tag) {
////                        case HomeAdapter.TAG_VIEW:
////                            WebActivity.launch(context, model.getUrl(), model.getDesc());
////                            break;
////                    }
////                }
////            });
//            }
//            return adapter;
//
//        } else if (type == SpecialResults.class) {
//
//        } else if (type == HotResults.class) {
//
//        } else if (type == RecommendResults.class) {
//
//        }
//        return null;
//    }
//
//    @Override
//    public <T extends IModel> Void setLayoutManager(Class<T> t, RecyclerView recyclerView) {
//        if (t == BannerResults.class) {
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        } else if (t == SpecialResults.class) {
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        } else if (t == HotResults.class) {
//
//        } else if (t == RecommendResults.class) {
//
//        }
//        return null;
//    }
//
//    @Override
//    public <T extends IModel> String getType(Class<T> clz) {
//        if (clz == BannerResults.class) {
//
//        } else if (clz == SpecialResults.class) {
//
//        } else if (clz == HotResults.class) {
//
//        } else if (clz == RecommendResults.class) {
//
//        }
//        return null;
//    }
//
//    public static HomePagerFragmentOrgianl newInstance() {
//        return new HomePagerFragmentOrgianl();
//    }
//
//}
