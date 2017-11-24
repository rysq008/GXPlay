package com.game.helper.net;

import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.HotResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.RecommendRequestBody;

import io.reactivex.Flowable;

public class DataService {

    public static Flowable<HttpResultModel<BannerResults>> getHomeBanner() {
        return Api.CreateApiService().getApiBannerData();
    }

    public static Flowable<HttpResultModel<NoticeResults>> getHomeNotice() {
        return Api.CreateApiService().getApiNoticeData();
    }

    public static Flowable<HttpResultModel<SpecialResults>> getHomeSpecial(BaseRequestBody baseRequestBody) {
        return Api.CreateApiService().getApiSpecialData(baseRequestBody);
    }

    public static Flowable<HttpResultModel<HotResults>> getHomeHot(BaseRequestBody baseRequestBody) {
        return Api.CreateApiService().getApiHotData(baseRequestBody);
    }

    public static Flowable<HttpResultModel<RecommendResults>> getHomeRecommend(RecommendRequestBody requestBody) {
        return Api.CreateApiService().getApiRecommendData(requestBody);
    }

}
