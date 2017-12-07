package com.game.helper.net;

import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.RecommendRequestBody;

import io.reactivex.Flowable;

public class DataService {

    public static Flowable<HttpResultModel<LoginResults>> getLoginData(LoginRequestBody baseRequestBody) {
        return Api.CreateApiService().getApiLoginData(baseRequestBody);
    }

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

    public static Flowable<HttpResultModel<ClassicalResults>> getGameClassical() {
        return Api.CreateApiService().getApiClassicalData();
    }

    public static Flowable<HttpResultModel<CommonResults>> getGameCommon() {
        return Api.CreateApiService().getApiCommonData();
    }

    public static Flowable<HttpResultModel<GeneralizeResults>> getGeneralizeData(){
        return Api.CreateApiService().getApiGeneralizeAccountData();
    }
}
