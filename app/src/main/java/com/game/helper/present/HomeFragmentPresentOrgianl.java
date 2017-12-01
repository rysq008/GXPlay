//package com.game.helper.present;
//
//import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;
//import com.game.helper.model.BannerResults;
//import com.game.helper.model.BaseModel.HttpResultModel;
//import com.game.helper.model.BaseModel.XBaseModel;
//import com.game.helper.model.HotResults;
//import com.game.helper.model.NoticeResults;
//import com.game.helper.model.RecommendResults;
//import com.game.helper.model.SpecialResults;
//import com.game.helper.net.DataService;
//import com.game.helper.net.model.BaseRequestBody;
//import com.game.helper.net.model.RecommendRequestBody;
//import com.game.helper.utils.RxLoadingUtils;
//import com.game.helper.views.widget.Header;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.droidlover.xdroidmvp.kit.Kits;
//import cn.droidlover.xdroidmvp.mvp.XPresent;
//import cn.droidlover.xdroidmvp.net.NetError;
//import io.reactivex.Flowable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function5;
//import zlc.season.practicalrecyclerview.ItemType;
//
//public class HomeFragmentPresent extends XPresent<HomeBasePagerFragment> {
//    protected static final int PAGE_SIZE = 10;
//    protected XBaseModel mXBaseModel;
//
//    public void loadData() {
//        Flowable<HttpResultModel<BannerResults>> fb = DataService.getHomeBanner();
//        Flowable<HttpResultModel<NoticeResults>> fn = DataService.getHomeNotice();
//        Flowable<HttpResultModel<SpecialResults>> fs = DataService.getHomeSpecial(new BaseRequestBody(1));
//        Flowable<HttpResultModel<HotResults>> fh = DataService.getHomeHot(new BaseRequestBody(1));
//        Flowable<HttpResultModel<RecommendResults>> fr = DataService.getHomeRecommend(new RecommendRequestBody(1, -1, -1));
//
//        Flowable<HomeAllResultsData> fa = Flowable.zip(fb, fn, fs, fh, fr, new Function5<HttpResultModel<BannerResults>, HttpResultModel<NoticeResults>, HttpResultModel<SpecialResults>, HttpResultModel<HotResults>, HttpResultModel<RecommendResults>, HomeAllResultsData>() {
//            @Override
//            public HomeAllResultsData apply(HttpResultModel<BannerResults> bannerResults, HttpResultModel<NoticeResults> noticeResults, HttpResultModel<SpecialResults> specialResults, HttpResultModel<HotResults> hotResults, HttpResultModel<RecommendResults> recommendResults) throws Exception {
//                if (bannerResults.data == null) {
//                    Flowable.error(new NetError("fetch bannerResults failed", NetError.NoDataError));
////                    throw new NetError("fetch invitePageQrcode failed", NetError.NoDataError);
//                }
//                if (noticeResults.data == null) {
//                    Flowable.error(new NetError("fetch noticeResults failed", NetError.NoDataError));
//                }
//                if (specialResults.data == null) {
//                    Flowable.error(new NetError("fetch specialResults failed", NetError.NoDataError));
//                }
//                if (hotResults.data == null) {
//                    Flowable.error(new NetError("fetch hotResults failed", NetError.NoDataError));
//                }
//                if (recommendResults.data == null) {
//                    Flowable.error(new NetError("fetch recommendResults failed", NetError.NoDataError));
//                }
//                mXBaseModel = recommendResults;
//                return new HomeAllResultsData(bannerResults.data, noticeResults.data, specialResults.data, hotResults.data, recommendResults.data);
//
//            }
//        });
//
//        RxLoadingUtils.subscribeWithDialog(getV().getContext(), fa, getV().<HomeAllResultsData>bindToLifecycle(), new Consumer<HomeAllResultsData>() {
//            @Override
//            public void accept(HomeAllResultsData homeAllResultsData) throws Exception {
//                getV().getAdapter().clear();
//                getV().getAdapter().clearData();
//                getV().getAdapter().clearFooter();
//                List<ItemType> list = new ArrayList<>();
//                getV().getAdapter().addHeader(new Header(homeAllResultsData.bannerResults));
//                list.add(homeAllResultsData.noticeResults);
//                list.add(homeAllResultsData.specialResults);
//                list.add(homeAllResultsData.hotResults);
//                list.addAll(homeAllResultsData.recommendResults.list.subList(0, 2));
//                getV().getAdapter().addAll(list);
//
//            }
//        }, new Consumer<NetError>() {
//            @Override
//            public void accept(NetError netError) throws Exception {
//                getV().getAdapter().showError();
//            }
//        });
//    }
//
//    static boolean t = false;
//    public void loadMoreData() {
//        if (Kits.Empty.check(mXBaseModel)) {
//            getV().getAdapter().showError();
//        } else {
//            if (mXBaseModel.hasNextPage()||t) {
//                t = false;
//                Flowable<HttpResultModel<RecommendResults>> fr = DataService.getHomeRecommend(new RecommendRequestBody(mXBaseModel.nextPageNum(), 0, 0));
//
//                RxLoadingUtils.subscribeWithDialog(getV().getContext(), fr, getV().bindToLifecycle(), new Consumer<HttpResultModel<RecommendResults>>() {
//                    @Override
//                    public void accept(HttpResultModel<RecommendResults> recommendResultsHttpResultModel) throws Exception {
//                        getV().getAdapter().clear();
//                        List<ItemType> list = new ArrayList<>();
//                        list.addAll(recommendResultsHttpResultModel.data.list);
//                        getV().getAdapter().addAll(list);
//                    }
//                }, new Consumer<NetError>() {
//                    @Override
//                    public void accept(NetError netError) throws Exception {
//                        getV().getAdapter().loadMoreFailed();
//                    }
//                });
//            } else {
////                getV().getAdapter().clear();
////                getV().getContentLayout().setLoadMoreViewEnabled(false);
////                getV().getContentLayout().setNoMoreViewEnabled(true);
//                List<ItemType> list = new ArrayList<>();
//                getV().getAdapter().addAll(list);
//
////                getV().getAdapter().loadMoreFailed();
////                getV().getAdapter().manualLoadMore();
////                getV().getContentLayout().setLoadMoreViewEnabled(false);
////                getV().getContentLayout().setNoMoreViewEnabled(false);
//            }
//        }
//    }
//
//
//    public class HomeAllResultsData extends XBaseModel {
//
//        BannerResults bannerResults;
//        NoticeResults noticeResults;
//        SpecialResults specialResults;
//        HotResults hotResults;
//        RecommendResults recommendResults;
//
//        public HomeAllResultsData(BannerResults bannerResults, NoticeResults noticeResults, SpecialResults specialResults, HotResults hotResults, RecommendResults recommendResults) {
//            this.bannerResults = bannerResults;
//            this.noticeResults = noticeResults;
//            this.specialResults = specialResults;
//            this.hotResults = hotResults;
//            this.recommendResults = recommendResults;
//        }
//    }
//}
