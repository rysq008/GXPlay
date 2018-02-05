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
import com.game.helper.model.CommonShareResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.DeleteMineGiftResults;
import com.game.helper.model.EasemobAccountResults;
import com.game.helper.model.FeedbackListResults;
import com.game.helper.model.FriendRangeResultModel;
import com.game.helper.model.GameAccountDiscountResults;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.GameCommentListResult;
import com.game.helper.model.GameGiftListResult;
import com.game.helper.model.GameListResultModel;
import com.game.helper.model.GamePackageInfoResult;
import com.game.helper.model.GamePackageInfo_DetailResult;
import com.game.helper.model.GamePackageListResult;
import com.game.helper.model.GeneralizeAccountInfoResultModel;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.H5Results;
import com.game.helper.model.H5UrlListResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.HotWordResults;
import com.game.helper.model.IncomeResultModel;
import com.game.helper.model.InvatationResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.MarketExpectedFlowlistResults;
import com.game.helper.model.MarketFlowlistResults;
import com.game.helper.model.MarketInfoResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.MineGameDesclistResults;
import com.game.helper.model.MineGamelistResults;
import com.game.helper.model.MineGiftInfoResults;
import com.game.helper.model.MineGiftlistResults;
import com.game.helper.model.MineOrderlistResults;
import com.game.helper.model.NotConcernResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.PlatformMessageResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.RechargeListResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.RegistResults;
import com.game.helper.model.ResetAlipayResults;
import com.game.helper.model.ResetPasswdResults;
import com.game.helper.model.ResetTradeResults;
import com.game.helper.model.SearchListResults;
import com.game.helper.model.ShareInfoResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.model.SystemMessageResults;
import com.game.helper.model.UnAvailableRedpackResultModel;
import com.game.helper.model.VIPUpGradeCostResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.model.VersionCheckResults;
import com.game.helper.model.VersionInfoResults;
import com.game.helper.model.VipGameAccountResults;
import com.game.helper.model.VipLevelResults;
import com.game.helper.model.huanxin.HanXinResponse;
import com.game.helper.model.huanxin.RobotMenuBean;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.net.api.Api;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.BindOrUnBindVipRequestBody;
import com.game.helper.net.model.CashToRequestBody;
import com.game.helper.net.model.ChannelListRequestBody;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
import com.game.helper.net.model.ConsumeRequestBody;
import com.game.helper.net.model.DeleteGameRequestBody;
import com.game.helper.net.model.DeleteGiftRequestBody;
import com.game.helper.net.model.FeedbackRequestBody;
import com.game.helper.net.model.FeedbakcStatusRequestBody;
import com.game.helper.net.model.ForgetPasswdRequestBody;
import com.game.helper.net.model.FriendRangeRequestBody;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.net.model.GameDetailSendCommentContentRequestBody;
import com.game.helper.net.model.GameInfoCommentListRequestBody;
import com.game.helper.net.model.GameInfoGiftListRequestBody;
import com.game.helper.net.model.GameListRequestBody;
import com.game.helper.net.model.GamePackageInfoRequestBody;
import com.game.helper.net.model.GamePackageInfo_InfoRequestBody;
import com.game.helper.net.model.GamePackageRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.MineGameRequestBody;
import com.game.helper.net.model.MineGiftInfoRequestBody;
import com.game.helper.net.model.PayRequestBody;
import com.game.helper.net.model.ReceiveGiftRequestBody;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.net.model.ReportedRequestBody;
import com.game.helper.net.model.ResetAlipayRequestBody;
import com.game.helper.net.model.ResetPasswdRequestBody;
import com.game.helper.net.model.ResetTradeRequestBody;
import com.game.helper.net.model.SearchRequestBody;
import com.game.helper.net.model.SetPasswordRequestBody;
import com.game.helper.net.model.SetTradeRequestBody;
import com.game.helper.net.model.ShareInfoRequestBody;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.net.model.SpecialDetailRequestBody;
import com.game.helper.net.model.UnAvailableRedpackRequestBody;
import com.game.helper.net.model.UpdateBirthdayRequestBody;
import com.game.helper.net.model.UpdateGenderRequestBody;
import com.game.helper.net.model.UpdateMsgStatusRequestBody;
import com.game.helper.net.model.UpdateNicknameRequestBody;
import com.game.helper.net.model.UpdatePhoneRequestBody;
import com.game.helper.net.model.UpdateSignatrueRequestBody;
import com.game.helper.net.model.VIPUpGradfeRequestBody;
import com.game.helper.net.model.VerifyRequestBody;
import com.game.helper.net.model.VersionCheckRequestBody;
import com.game.helper.utils.UploadUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public class DataService {


    private static Flowable<HttpResultModel<H5UrlListResults>> h5UrlList;

    public static Flowable<HttpResultModel<BannerResults>> getHomeBanner(BannerRequestBody bannerRequestBody) {
        return Api.CreateApiService().getApiBannerData(bannerRequestBody);
    }

    public static Flowable<HttpResultModel<NoticeResults>> getHomeNotice() {
        return Api.CreateApiService().getApiNoticeData();
    }

    public static Flowable<HttpResultModel<H5Results>> getH5Data() {
        return Api.CreateApiService().getApiH5Data();
    }

    public static Flowable<HttpResultModel<SpecialResults>> getHomeSpecial(BaseRequestBody baseRequestBody) {
        return Api.CreateApiService().getApiSpecialData(baseRequestBody);
    }

    public static Flowable<HttpResultModel<HotResults>> getHomeHot(RecommendRequestBody baseRequestBody) {
        return Api.CreateApiService().getApiHotData(baseRequestBody);
    }

    public static Flowable<HttpResultModel<RecommendResults>> getHomeRecommend(BaseRequestBody requestBody) {
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

    public static Flowable<HttpResultModel<Map<String, Integer>>> getCouponCount() {
        return Api.CreateApiService().getApiCouponCount();
    }

    public static Flowable<HttpResultModel<VerifyResults>> getVerify(VerifyRequestBody verifyRequestBody) {
        return Api.CreateApiService().getApiVerify(verifyRequestBody);
    }

    public static Flowable<HttpResultModel<ResetPasswdResults>> resetPassWord(ResetPasswdRequestBody resetPasswdRequestBody) {
        return Api.CreateApiService().updatePassWord(resetPasswdRequestBody);
    }

    public static Flowable<HttpResultModel<ResetPasswdResults>> forgetPassWord(ForgetPasswdRequestBody forgetPasswdRequestBody) {
        return Api.CreateApiService().forgetPassWord(forgetPasswdRequestBody);
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

    public static Flowable<HttpResultModel<UnAvailableRedpackResultModel>> getUnuseRedPackInfo(UnAvailableRedpackRequestBody unAvailableRedpackRequestBody) {
        return Api.CreateApiService().getUnuseRedPackInfo(unAvailableRedpackRequestBody);
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

    public static Flowable<HttpResultModel<ResetTradeResults>> setTradePasswrd(SetTradeRequestBody setTradeRequestBody) {
        return Api.CreateApiService().setTradePassword(setTradeRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> setPasswrd(SetPasswordRequestBody setPasswordRequestBody) {
        return Api.CreateApiService().setPassword(setPasswordRequestBody);
    }

    public static Flowable<HttpResultModel<ResetAlipayResults>> resetAlipayAccount(ResetAlipayRequestBody resetAlipayRequestBody) {
        return Api.CreateApiService().resetAlipayAccount(resetAlipayRequestBody);
    }

    public static Flowable<HttpResultModel<VipGameAccountResults>> getVipGameAccount() {
        return Api.CreateApiService().getVipGameAccount();
    }
    /*public static Flowable<HttpResultModel<VipGameAccountResults>> getVipGameAccount(BindVipAccountNumRequestBody bindVipAccountNumRequestBody) {
        return Api.CreateApiService().getVipGameAccount(bindVipAccountNumRequestBody);
    }*/

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

    //多个文件上传,返回一个总体结果
    public static Flowable<HttpResultModel<Object>> uploadApiFilesForSingleResult(String url, List<File> list, UploadUtils.FileUploadProgress fileUploadFlowable) {
        //构建body
        //addFormDataPart()第一个参数为表单名字，这是和后台约定好的
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //注意，file是后台约定的参数，如果是多图，file[]，如果是单张图片，file就行
        for (File file : list) {
            //这里上传的是多图
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file)
            UploadUtils.UploadFileRequestBody uploadFileRequestBody = new UploadUtils.UploadFileRequestBody(file, fileUploadFlowable);
            builder.addFormDataPart("file[]", file.getName(), uploadFileRequestBody);
        }
        RequestBody requestBody = builder.build();
        return Api.CreateApiService().uploadApiFile(url, requestBody);
    }

    //单个文件上传
    public static Flowable<HttpResultModel<Object>> uploadApiSingleFile(String url, File file, UploadUtils.FileUploadProgress fileUploadFlowable) {
        UploadUtils.UploadFileRequestBody uploadFileRequestBody = new UploadUtils.UploadFileRequestBody(file, fileUploadFlowable);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        return Api.CreateApiService().uploadApiFile(url, part);
    }

    //头像上传
    public static Flowable<HttpResultModel<Object>> setApiUserIcon(File file, UploadUtils.FileUploadProgress fileUploadFlowable) {
        return uploadApiSingleFile("/member/set_icon/", file, fileUploadFlowable);
    }

//    //多个文件上传，返回每个文件的上传结果的列表（相当于把多个文件单独上传压缩成一个上传请求列表）
//    public static Flowable<List<HttpResultModel<Object>>> uploadApiFilesForListResult(String url, List<File> files, UploadUtils.FileUploadProgress fileUploadFlowable) {
//        if (!Kits.Empty.check(files)) {
//            List list = new ArrayList();
//            for (File file : files) {
//                list.add(uploadApiSingleFile(url, file, fileUploadFlowable));
//            }
//            return Flowable.zipIterable(list, new Function<Object[], ArrayList<HttpResultModel<Object>>>() {
//                @Override
//                public ArrayList<HttpResultModel<Object>> apply(Object[] objects) throws Exception {
//                    ArrayList<HttpResultModel<Object>> arrayList = new ArrayList<>();
//                    for (Object obj : objects) {
//                        arrayList.add((HttpResultModel<Object>) obj);
//                    }
//                    return arrayList;
//                }
//            }, true, 1);
//        }
//        return null;
//    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateNickname(UpdateNicknameRequestBody updateNicknameRequestBody) {
        return Api.CreateApiService().updateNickname(updateNicknameRequestBody);
    }

    //头像上传
    public static Flowable<HttpResultModel<Object>> updateAvatar(File file, UploadUtils.FileUploadProgress fileUploadFlowable) {
        return uploadApiSingleFile("/member/set_icon/", file, fileUploadFlowable);
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

    public static Flowable<HttpResultModel<SpecialResults>> getSpecialMoreList(BaseRequestBody baseRequestBody) {
        return Api.CreateApiService().getSpecialMoreList(baseRequestBody);
    }


    public static Flowable<HttpResultModel<MarketInfoResults>> getMarketInfo() {
        return Api.CreateApiService().getMarketInfo();
    }

    public static Flowable<HttpResultModel<MarketFlowlistResults>> getMarketFlowList(@Body SinglePageRequestBody singlePageRequestBody) {
        return Api.CreateApiService().getMarketFlowList(singlePageRequestBody);
    }

    public static Flowable<HttpResultModel<MarketExpectedFlowlistResults>> getMarketExpectedFlowList(@Body SinglePageRequestBody singlePageRequestBody) {
        return Api.CreateApiService().getMarketExpectedFlowList(singlePageRequestBody);
    }

    public static Flowable<HttpResultModel<RecommendResults>> getSpecialDetailList(@Body SpecialDetailRequestBody specialDetailRequestBody) {
        return Api.CreateApiService().getSpecialDetailList(specialDetailRequestBody);
    }


    public static Flowable<HttpResultModel<AllAccountsResultsModel>> getAllAccounts() {
        return Api.CreateApiService().getAllAccounts();
    }

    public static Flowable<HttpResultModel> consume(ConsumeRequestBody consumeRequestBody) {
        return Api.CreatePayOrImageApiService().consume(consumeRequestBody);
    }

    public static Flowable<HttpResultModel<GamePackageListResult>> getGamePackageList(GamePackageRequestBody getGamePackageList) {
        return Api.CreateApiService().getGamePackageList(getGamePackageList);
    }

    public static Flowable<HttpResultModel<MineGamelistResults>> getMineGameList(@Body MineGameRequestBody mineGameRequestBody) {
        return Api.CreateApiService().getMineGameList(mineGameRequestBody);
    }

    public static Flowable<HttpResultModel<MineGiftlistResults>> getMineGiftList(@Body MineGameRequestBody mineGameRequestBody) {
        return Api.CreateApiService().getMineGiftList(mineGameRequestBody);
    }

    public static Flowable<HttpResultModel<MineOrderlistResults>> getMineOrderList(@Body SinglePageRequestBody singlePageRequestBody) {
        return Api.CreateApiService().getMineOrderList(singlePageRequestBody);
    }

    public static Flowable<HttpResultModel<DeleteMineGiftResults>> deleteMineGiftCode(@Body DeleteGiftRequestBody deleteGiftRequestBody) {
        return Api.CreateApiService().deleteMineGiftCode(deleteGiftRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> deleteMineGame(@Body DeleteGameRequestBody deleteGameRequestBody) {
        return Api.CreateApiService().deleteMineGame(deleteGameRequestBody);
    }

    public static Flowable<HttpResultModel<MineGiftInfoResults>> getMineGiftCodeInfo(@Body MineGiftInfoRequestBody mineGiftInfoRequestBody) {
        return Api.CreateApiService().getMineGiftCodeInfo(mineGiftInfoRequestBody);
    }


    public static Flowable<HttpResultModel<GamePackageInfoResult>> getGamePackageInfo(GamePackageInfoRequestBody gamePackageInfoRequestBody) {
        return Api.CreateApiService().getGamePackageInfo(gamePackageInfoRequestBody);
    }

    public static Flowable<HttpResultModel<GamePackageInfo_DetailResult>> getGamePackageInfo_Info(GamePackageInfo_InfoRequestBody gamePackageInfo_infoRequestBody) {
        return Api.CreateApiService().getGamePackageInfo_Info(gamePackageInfo_infoRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> deleteGameAccount(SingleGameIdRequestBody singleGameIdRequestBody) {
        return Api.CreateApiService().deleteGameAccount(singleGameIdRequestBody);
    }

    public static Flowable<HttpResultModel<GameGiftListResult>> getGameGiftList(GameInfoGiftListRequestBody gameInfoGiftListRequestBody) {
        return Api.CreateApiService().getGameGiftList(gameInfoGiftListRequestBody);
    }

    public static Flowable<HttpResultModel<GameCommentListResult>> getGameCommentList(GameInfoCommentListRequestBody gameInfoCommentListRequestBody) {
        return Api.CreateApiService().getGameCommentList(gameInfoCommentListRequestBody);
    }

    public static Flowable<HttpResultModel<Object>> sendCommentContent(GameDetailSendCommentContentRequestBody gameDetailSendCommentContentRequestBody) {

        return Api.CreateApiService().sendCommentContent(gameDetailSendCommentContentRequestBody);
    }

    public static Flowable<HttpResultModel<H5UrlListResults>> getH5UrlList() {
        return Api.CreateApiService().getH5UrlList();
    }

    public static Flowable<HttpResultModel<CommonShareResults>> getG9Info() {
        return Api.CreateApiService().getG9Info();
    }

    public static Flowable<HttpResultModel<Object>> receiveGift(ReceiveGiftRequestBody receiveGiftRequestBody) {
        return Api.CreateApiService().receiveGift(receiveGiftRequestBody);
    }

    public static Flowable<HttpResultModel<MineGameDesclistResults>> getMineGameDescList(SingleGameIdRequestBody singleGameIdRequestBody) {
        return Api.CreateApiService().getMineGameDescList(singleGameIdRequestBody);
    }

    public static Flowable<HttpResultModel<SystemMessageResults>> getSystemMessage(SinglePageRequestBody singlePageRequestBody) {
        return Api.CreateApiService().getSystemMessage(singlePageRequestBody);
    }

    public static Flowable<HttpResultModel<PlatformMessageResults>> getPlatformMessage(SinglePageRequestBody singlePageRequestBody) {
        return Api.CreateApiService().getPlatformMessage(singlePageRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> feedbackStatus(FeedbakcStatusRequestBody feedbakcStatusRequestBody) {
        return Api.CreateApiService().feedbackStatus(feedbakcStatusRequestBody);
    }

    public static Flowable<HttpResultModel<VIPUpGradeCostResults>> getVIPUpGradeCost(VIPUpGradfeRequestBody vipUpGradfeRequestBody) {
        return Api.CreateApiService().getVIPUpGradeCost(vipUpGradfeRequestBody);
    }

    public static Flowable<HttpResultModel<EasemobAccountResults>> getEasemobIM() {
        return Api.CreateApiService().getEasemobIM();
    }

    public static Flowable<HttpResultModel<VersionInfoResults>> getVersionInfo() {
        return Api.CreateApiService().getVersionInfo();
    }

    public static Flowable<HttpResultModel<FriendRangeResultModel>> getRangeConsume(FriendRangeRequestBody friendRangeRequestBody) {
        return Api.CreateApiService().getRangeConsume(friendRangeRequestBody);
    }

    public static Flowable<HttpResultModel<NotConcernResults>> updateMsgStatus(UpdateMsgStatusRequestBody updateMsgStatusRequestBody) {
        return Api.CreateApiService().updateMsgStatus(updateMsgStatusRequestBody);
    }

    public static Flowable<HanXinResponse<RobotMenuBean>> getHanXinRobotMenu() {
        return Api.CreateHanXinApiService().getHuanXinRobotMenu();
    }

    public static Flowable<HttpResultModel<VersionCheckResults>> updateVersion(VersionCheckRequestBody versionCheckRequestBody) {
        return Api.CreateApiService().updateVersion(versionCheckRequestBody);

    }

    public static Flowable<HttpResultModel> reportedData(ReportedRequestBody reportedRequestBody) {
        return Api.CreateApiService().reportedData(reportedRequestBody);
    }

    public static Flowable<HttpResultModel<ShareInfoResults>> getApiShareInfoData(ShareInfoRequestBody shareInfoRequestBody) {
        return Api.CreateApiService().getApiShareInfoData(shareInfoRequestBody);
    }

    public static Flowable<HttpResultModel<Map<String, String>>> getApiPackageInfoShareInfoData(GamePackageInfoRequestBody gamePackageRequestBody) {
        return Api.CreateApiService().getApiPackageInfoShareInfoData(gamePackageRequestBody);
    }

    public static Flowable<HttpResultModel<Map<String, Integer>>> getApiBindOrUnBindVip(BindOrUnBindVipRequestBody bindVipRequestBody) {
        return Api.CreateApiService().getApiBindOrUnBindVip(bindVipRequestBody);
    }
}
