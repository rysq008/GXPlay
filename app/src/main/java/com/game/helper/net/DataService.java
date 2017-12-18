package com.game.helper.net;

import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.ChannelListResultModel;
import com.game.helper.model.CashListResults;
import com.game.helper.model.CashToResults;
import com.game.helper.model.CheckTradePasswdResults;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.FriendRangeResultModel;
import com.game.helper.model.GameAccountDiscountResults;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.GameListResultModel;
import com.game.helper.model.GeneralizeAccountInfoResultModel;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.IncomeResultModel;
import com.game.helper.model.InvatationResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.RegistResults;
import com.game.helper.model.ResetAlipayResults;
import com.game.helper.model.ResetPasswdResults;
import com.game.helper.model.ResetTradeResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.model.RechargeListResults;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.ChannelListRequestBody;
import com.game.helper.net.model.CashToRequestBody;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.net.model.FriendRangeRequestBody;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.net.model.GameListRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.PayRequestBody;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.net.model.ResetAlipayRequestBody;
import com.game.helper.net.model.ResetPasswdRequestBody;
import com.game.helper.net.model.ResetTradeRequestBody;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.net.model.VerifyRequestBody;

import io.reactivex.Flowable;

public class DataService {


    public static Flowable<HttpResultModel<BannerResults>> getHomeBanner(BannerRequestBody bannerRequestBody) {
        return Api.CreateApiService().getApiBannerData(bannerRequestBody);
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

    public static Flowable<HttpResultModel<GeneralizeResults>> getGeneralizeData() {
        return Api.CreateApiService().getApiGeneralizeAccountData();
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

    public static Flowable<HttpResultModel<ResetPasswdResults>> resetPassWord(ResetPasswdRequestBody resetPasswdRequestBody){
        return Api.CreateApiService().resetPassWord(resetPasswdRequestBody);
    }

    public static Flowable<HttpResultModel<PayResultModel>> ApiPay(PayRequestBody payRequestBody) {
        return Api.CreatePayOrImageApiService().ApiPay(payRequestBody);
    }

    public static Flowable<HttpResultModel<FriendRangeResultModel>> getFriendRank(FriendRangeRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getFriendRank(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<FriendRangeResultModel>> getIncomeRank(FriendRangeRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getIncomeRank(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<IncomeResultModel>> getIncomeList(FriendRangeRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getIncomeList(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<GeneralizeAccountInfoResultModel>> getGeneralizeAccountInfo() {
        return Api.CreateApiService().getGeneralizeAccountInfo();
    }

    public static Flowable<HttpResultModel<GameAccountResultModel>> getGameAccountList(GameAccountRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getGameAccountList(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<GameListResultModel>> getGameAccountList(GameListRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getGameListAccordingKey(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<ChannelListResultModel>> getChannelAccordingGame(ChannelListRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getChannelAccordingGame(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<LogoutResults>> addGameAccount(AddGameAccountRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().addGameAccount(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<ConsumeListResults>> getConsumeListData(SinglePageRequestBody pageRequestBody) {
        return Api.CreateApiService().getConsumeListData(pageRequestBody);
    }

    public static Flowable<HttpResultModel<RechargeListResults>> getRechargeListData(SinglePageRequestBody pageRequestBody) {
        return Api.CreateApiService().getRechargeListData(pageRequestBody);
    }

    public static Flowable<HttpResultModel<CashListResults>> getCashListData(SinglePageRequestBody pageRequestBody) {
        return Api.CreateApiService().getCashListData(pageRequestBody);
    }

    public static Flowable<HttpResultModel<ProfitListResults>> getProfitListData(SinglePageRequestBody pageRequestBody) {
        return Api.CreateApiService().getProfitListData(pageRequestBody);
    }

    public static Flowable<HttpResultModel<AvailableRedpackResultModel>> getRedPackInfo(AvailableRedpackRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getRedPackInfo(friendRangeRequestBody);
    }
    public static Flowable<HttpResultModel<CashToResults>> cashTo(CashToRequestBody cashToRequestBody) {
        return Api.CreatePayOrImageApiService().cashTo(cashToRequestBody);
    }

    public static Flowable<HttpResultModel<CheckTradePasswdResults>> checkTradePassword(CheckTradePasswdRequestBody checkTradePasswdRequestBody) {
        return Api.CreateApiService().checkTradePassWord(checkTradePasswdRequestBody);
    }

    public static Flowable<HttpResultModel<InvatationResults>> getInvatationList(SinglePageRequestBody singlePageRequestBody) {
        return Api.CreateApiService().getInvatationList(singlePageRequestBody);
    }

    public static Flowable<HttpResultModel<ResetTradeResults>> resetTradePasswrd(ResetTradeRequestBody resetTradePasswrd) {
        return Api.CreateApiService().resetTradePassword(resetTradePasswrd);
    }

    public static Flowable<HttpResultModel<ResetAlipayResults>> resetAlipayAccount(ResetAlipayRequestBody resetAlipayRequestBody) {
        return Api.CreateApiService().resetAlipayAccount(resetAlipayRequestBody);
    }

    public static Flowable<HttpResultModel<VipGameAccountResults>> getVipGameAccount() {
        return Api.CreateApiService().getVipGameAccount();
    }

    public static Flowable<HttpResultModel<GameAccountDiscountResults>> getGameAccountDiscount(SingleGameIdRequestBody singleGameIdRequestBody) {
        return Api.CreateApiService().getGameAccountDiscount(singleGameIdRequestBody);
    }

    public static Flowable<HttpResultModel<VipLevelResults>> getVipLevel() {
        return Api.CreateApiService().getVipLevel();
    }
}
