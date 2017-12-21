package com.game.helper.net;

import com.game.helper.model.AllAccountsResultsModel;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CashListResults;
import com.game.helper.model.CashToResults;
import com.game.helper.model.ChannelListResultModel;
import com.game.helper.model.CheckTradePasswdResults;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.FeedbackListResults;
import com.game.helper.model.FriendRangeResultModel;
import com.game.helper.model.GameAccountDiscountResults;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.GameListResultModel;
import com.game.helper.model.GeneralizeAccountInfoResultModel;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.HotWordResults;
import com.game.helper.model.IncomeResultModel;
import com.game.helper.model.InvatationResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.NotConcernResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.RechargeListResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.RegistResults;
import com.game.helper.model.ResetAlipayResults;
import com.game.helper.model.ResetPasswdResults;
import com.game.helper.model.ResetTradeResults;
import com.game.helper.model.SearchListResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.CashToRequestBody;
import com.game.helper.net.model.ChannelListRequestBody;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.net.model.ConsumeRequestBody;
import com.game.helper.net.model.FeedbackRequestBody;
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
import com.game.helper.net.model.SearchRequestBody;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.net.model.UpdateAvatarRequestBody;
import com.game.helper.net.model.UpdateBirthdayRequestBody;
import com.game.helper.net.model.UpdateGenderRequestBody;
import com.game.helper.net.model.UpdateNicknameRequestBody;
import com.game.helper.net.model.UpdatePhoneRequestBody;
import com.game.helper.net.model.UpdateSignatrueRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.utils.UploadUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public static Flowable<HttpResultModel<HotWordResults>> getApiHotWordData(BaseRequestBody baseRequestBody) {
        return Api.CreateApiService().getApiHotWordData(baseRequestBody);
    }

    public static Flowable<HttpResultModel<SearchListResults>> getApiSearchByWordData(SearchRequestBody searchRequestBody) {
        return Api.CreateApiService().getApiSearchByWordData(searchRequestBody);
    }

    //多个文件上传没有进度值
    public static Flowable<HttpResultModel> setApiUserIcon(List<File> list) {
        //构建body
        //addFormDataPart()第一个参数为表单名字，这是和后台约定好的
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //注意，file是后台约定的参数，如果是多图，file[]，如果是单张图片，file就行
        for (File file : list) {
            //这里上传的是多图
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        RequestBody requestBody = builder.build();
        return Api.CreateApiService().setApiUserIcon(requestBody);
    }

    //单个文件上传有进度监听
    public static Flowable<HttpResultModel> setApiUserIcon(File file, UploadUtils.FileUploadProgress fileUploadFlowable) {
        UploadUtils.UploadFileRequestBody uploadFileRequestBody = new UploadUtils.UploadFileRequestBody(file, fileUploadFlowable);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        return Api.CreateApiService().setApiUserIcon(part);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateNickname(UpdateNicknameRequestBody updateNicknameRequestBody) {
        return Api.CreateApiService().updateNickname(updateNicknameRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateAvatar(UpdateAvatarRequestBody updateAvatarRequestBody) {
        return Api.CreateApiService().updateAvatar(updateAvatarRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateBirthday(UpdateBirthdayRequestBody updateBirthdayRequestBody) {
        return Api.CreateApiService().updateBirthday(updateBirthdayRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateGender(UpdateGenderRequestBody updateGenderRequestBody) {
        return Api.CreateApiService().updateGender(updateGenderRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updatePhone(UpdatePhoneRequestBody updatePhoneRequestBody) {
        return Api.CreateApiService().updatePhone(updatePhoneRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateSignatrue(UpdateSignatrueRequestBody updateSignatrueRequestBody) {
        return Api.CreateApiService().updateSignatrue(updateSignatrueRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> feedBack(FeedbackRequestBody feedbackRequestBody) {
        return Api.CreateApiService().feedBack(feedbackRequestBody);
    }

    public static Flowable<HttpResultModel<FeedbackListResults>> feedBackList() {
        return Api.CreateApiService().feedBackList();
    }

    public static Flowable<HttpResultModel<AllAccountsResultsModel>> getAllAccounts() {
        return Api.CreateApiService().getAllAccounts();
    }

    public static Flowable<HttpResultModel<FeedbackListResults>> consume(ConsumeRequestBody consumeRequestBody) {
        return Api.CreatePayOrImageApiService().consume(consumeRequestBody);
    }

}
