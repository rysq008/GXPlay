package com.game.helper.present;

import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;
import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.H5Results;
import com.game.helper.model.HotResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.XReloadableRecyclerContentLayout;
import com.game.helper.views.XReloadableStateContorller;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function6;
import zlc.season.practicalrecyclerview.ItemType;

public class HomeFragmentPresent extends XPresent<HomeBasePagerFragment> {
    protected static final int PAGE_SIZE = 10;
    protected XBaseModel mXBaseModel;

    public void onRefreshData(XReloadableStateContorller reloadableStateContorller, boolean showLoading) {
        Flowable<HttpResultModel<BannerResults>> fb = DataService.getHomeBanner(new BannerRequestBody(1));
        Flowable<HttpResultModel<NoticeResults>> fn = DataService.getHomeNotice();
        Flowable<HttpResultModel<H5Results>> f5 = DataService.getH5Data();
        Flowable<HttpResultModel<SpecialResults>> fs = DataService.getHomeSpecial(new BaseRequestBody(1));
        Flowable<HttpResultModel<HotResults>> fh = DataService.getHomeHot(new RecommendRequestBody(1, 0, 0));
        Flowable<HttpResultModel<RecommendResults>> fr = DataService.getHomeRecommend((new BaseRequestBody(1)));


        final Flowable<HomeAllResultsData> fa = Flowable.zip(fb, fn, f5, fs, fh, fr, new Function6<HttpResultModel<BannerResults>, HttpResultModel<NoticeResults>, HttpResultModel<H5Results>, HttpResultModel<SpecialResults>, HttpResultModel<HotResults>, HttpResultModel<RecommendResults>, HomeAllResultsData>() {
            @Override
            public HomeAllResultsData apply(HttpResultModel<BannerResults> bannerResults, HttpResultModel<NoticeResults> noticeResults, HttpResultModel<H5Results> h5Results, HttpResultModel<SpecialResults> specialResults, HttpResultModel<HotResults> hotResults, HttpResultModel<RecommendResults> recommendResults) throws Exception {
//                if (bannerResults.data.isNull()) {
//                    Flowable.error(new NetError("fetch bannerResults failed", NetError.NoDataError));
////                    throw new NetError("fetch invitePageQrcode failed", NetError.NoDataError);
//                }
//                if (noticeResults.data.isNull()) {
//                    Flowable.error(new NetError("fetch noticeResults failed", NetError.NoDataError));
//                }
//                if (specialResults.data.isNull()) {
//                    Flowable.error(new NetError("fetch specialResults failed", NetError.NoDataError));
//                }
//                if (hotResults.data.isNull()) {
//                    Flowable.error(new NetError("fetch hotResults failed", NetError.NoDataError));
//                }
//                if (recommendResults.data.isNull()) {
//                    Flowable.error(new NetError("fetch recommendResults failed", NetError.NoDataError));
//                }
                mXBaseModel = recommendResults;
                HomeAllResultsData homeAllResultsData = new HomeAllResultsData(bannerResults.data, noticeResults.data, h5Results.data, specialResults.data, hotResults.data, recommendResults.data);
                return homeAllResultsData;
            }
        });

        RxLoadingUtils.subscribeWithReload(reloadableStateContorller, fa, getV().<HomeAllResultsData>bindToLifecycle(), new Consumer<HomeAllResultsData>() {
            @Override
            public void accept(HomeAllResultsData homeAllResultsData) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.add(homeAllResultsData.bannerResults);
                list.add(homeAllResultsData.noticeResults);
                list.add(homeAllResultsData.specialResults);
                list.add(homeAllResultsData.hotResults);
                list.addAll(homeAllResultsData.recommendResults.list);
                if (null == mXBaseModel) {
                    getV().showData(1, 1, list);
                } else {
                    getV().showData(mXBaseModel.current_page, mXBaseModel.total_page, list);
                }
//                getV().showSearchView(true);
            }
        }, null, null, showLoading);
    }

    public void loadMoreData(XReloadableRecyclerContentLayout reloadableRecyclerContentLayout, int page) {
//        Flowable<HttpResultModel<HotResults>> fr = DataService.getHomeHot(new RecommendRequestBody(page, 0, 0));
        Flowable<HttpResultModel<RecommendResults>> fr = DataService.getHomeRecommend((new BaseRequestBody(page)));
        RxLoadingUtils.subscribeWithReload(reloadableRecyclerContentLayout, fr, getV().bindToLifecycle(), new Consumer<HttpResultModel<RecommendResults>>() {
            @Override
            public void accept(HttpResultModel<RecommendResults> hotResultsHttpResultModel) throws Exception {
                mXBaseModel = hotResultsHttpResultModel;
                List<ItemType> list = new ArrayList<>();
                list.addAll(hotResultsHttpResultModel.data.list);
                getV().showData(hotResultsHttpResultModel.current_page, hotResultsHttpResultModel.total_page, list);
            }
        }, null, null, false);
    }


    public class HomeAllResultsData extends XBaseModel {

        BannerResults bannerResults;
        NoticeResults noticeResults;
        H5Results h5Results;
        SpecialResults specialResults;
        HotResults hotResults;
        RecommendResults recommendResults;

        public HomeAllResultsData(BannerResults bannerResults, NoticeResults noticeResults, H5Results h5Results, SpecialResults specialResults, HotResults hotResults, RecommendResults recommendResults) {
            this.bannerResults = bannerResults;
            this.noticeResults = noticeResults;
            this.h5Results = h5Results;
            this.noticeResults.h5Results = this.h5Results;
            this.specialResults = specialResults;
            this.hotResults = hotResults;
            this.recommendResults = recommendResults;
        }
    }
}
