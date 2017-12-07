package com.game.helper.net;

import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.RegistResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.net.model.VerifyRequestBody;

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

    public static Flowable<HttpResultModel<ClassicalResults>> getGameClassical() {
        return Api.CreateApiService().getApiClassicalData();
    }

    public static Flowable<HttpResultModel<CommonResults>> getGameCommon() {
        return Api.CreateApiService().getApiCommonData();
    }

    public static Flowable<HttpResultModel<RegistResults>> regist(RegistRequestBody requestBody) {
        return Api.CreateApiService().ApiRegitst(requestBody);
    }

    public static Flowable<HttpResultModel<LoginResults>> login(LoginRequestBody requestBody) {
        return Api.CreateApiService().ApiLogin(requestBody);
    }

    public static Flowable<HttpResultModel<LogoutResults>> logout() {
        return Api.CreateApiService().ApiLogout();
    }

    public static Flowable<HttpResultModel<MemberInfoResults>> getMemberInfo() {
        return Api.CreateApiService().getApiMemberInfo();
    }

    public static Flowable<HttpResultModel<VerifyResults>> getVerify(VerifyRequestBody verifyRequestBody) {
        return Api.CreateApiService().getApiVerify(verifyRequestBody);
    }
}
