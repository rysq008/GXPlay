package com.game.helper.net.api;

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
import com.game.helper.model.MarketExpectedFlowlistResults;
import com.game.helper.model.MarketFlowlistResults;
import com.game.helper.model.MarketInfoResults;
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
import com.game.helper.net.model.FeedbackRequestBody;
import com.game.helper.net.model.SetTradeRequestBody;
import com.game.helper.net.model.SingleGameIdRequestBody;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.net.model.AddGameAccountRequestBody;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.CashToRequestBody;
import com.game.helper.net.model.ChannelListRequestBody;
import com.game.helper.net.model.CheckTradePasswdRequestBody;
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

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("/member/login/")
    Flowable<HttpResultModel<LoginResults>> getApiLoginData(@Body LoginRequestBody baseRequestBody);

    @POST("/sys/get_banner_list/")
    Flowable<HttpResultModel<BannerResults>> getApiBannerData(@Body BannerRequestBody bannerRequestBody);

    //首页通告
    @POST("/sys/get_notification_list/")
    Flowable<HttpResultModel<NoticeResults>> getApiNoticeData();

    //首页专题
    //    @HTTP(method = "",path = "/user/delete",hasBody = true)
    @POST("/game/get_game_theme_list/")
    Flowable<HttpResultModel<SpecialResults>> getApiSpecialData(@Body BaseRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    //首页热门推荐数据
    @POST("/game/get_recommend_game_list/")
    Flowable<HttpResultModel<HotResults>> getApiHotData(@Body BaseRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    //首页自定义推荐数据
    @POST("/game/get_game_list/")
    Flowable<HttpResultModel<RecommendResults>> getApiRecommendData(@Body RecommendRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    //经典分类列表
    @POST("/game/get_class_type_list/")
    Flowable<HttpResultModel<ClassicalResults>> getApiClassicalData();

    //普通分类列表
    @POST("/game/get_type_list/")
    Flowable<HttpResultModel<CommonResults>> getApiCommonData();

    //推广帐号信息
    @POST("/marketing/get_marketing_info/")
    Flowable<HttpResultModel<GeneralizeResults>> getApiGeneralizeAccountData();

    @POST("/member/register/")
//注册
    Flowable<HttpResultModel<RegistResults>> ApiRegitst(@Body RegistRequestBody baseRequestBody/*@Path("phone") String phone, @Path("code") String code, @Path("market_num") String market_num*/);

    @POST("/member/login/")
//登陆
    Flowable<HttpResultModel<LoginResults>> ApiLogin(@Body LoginRequestBody baseRequestBody/*@Path("phone") String phone, @Path("code") String code, @Path("type") String type, @Path("channel_num") String channel_num*/);

    @POST("/member/logout/")
//登出
    Flowable<HttpResultModel<LogoutResults>> ApiLogout();

    @POST("/member/info/")
//会员信息
    Flowable<HttpResultModel<MemberInfoResults>> getApiMemberInfo();

    @POST("/public/get_tel_verify/")
//验证码
    Flowable<HttpResultModel<VerifyResults>> getApiVerify(@Body VerifyRequestBody verifyRequestBody/*@Path("phone") String phone, @Path("usefor") String usefor*/);

    //支付
    @POST("/G9game/paymentController.do?payment")
    Flowable<HttpResultModel<PayResultModel>> ApiPay(@Body PayRequestBody payRequestBody);

    //人脉排行榜
    @POST("/marketing/get_friend_rank/")
    Flowable<HttpResultModel<FriendRangeResultModel>> getFriendRank(@Body FriendRangeRequestBody payRequestBody/*@Path("page") int pageNum*/);

    //推广收益排行榜
    @POST("/marketing/get_income_rank/")
    Flowable<HttpResultModel<FriendRangeResultModel>> getIncomeRank(@Body FriendRangeRequestBody payRequestBody/*@Path("page") int pageNum*/);

    //推广收益列表
    @POST("/marketing/get_marketing_flow_list/")
    Flowable<HttpResultModel<IncomeResultModel>> getIncomeList(@Body FriendRangeRequestBody payRequestBody/*@Path("page") int pageNum*/);

    //推广账号信息
    @POST("/marketing/get_marketing_info/")
    Flowable<HttpResultModel<GeneralizeAccountInfoResultModel>> getGeneralizeAccountInfo();

    //获取游戏账号列表
    @POST("/member/get_game_account_list/")
    Flowable<HttpResultModel<GameAccountResultModel>> getGameAccountList(@Body GameAccountRequestBody gameAccountRequestBody/*@Path("page") int page, @Path("plat_id") int plat_id, @Path("option_game_id") int option_game_id, @Path("option_channel_id") int option_channel_id*/);

    //根据关键词搜索游戏
    @POST("/game/query_game_list/")
    Flowable<HttpResultModel<GameListResultModel>> getGameListAccordingKey(@Body GameListRequestBody payRequestBody/*@Path("page") int pageNum, @Path("key") String key*/);

    //根据游戏搜索平台
    @POST("/game/get_game_channel_list/")
    Flowable<HttpResultModel<ChannelListResultModel>> getChannelAccordingGame(@Body ChannelListRequestBody payRequestBody);

    //添加游戏账号
    @POST("/member/add_game_account/")
    Flowable<HttpResultModel<LogoutResults>> addGameAccount(@Body AddGameAccountRequestBody payRequestBody);

    //获取可用红包卡券
    @POST("/activity/get_red_packet_list/")
    Flowable<HttpResultModel<AvailableRedpackResultModel>> getRedPackInfo(@Body AvailableRedpackRequestBody payRequestBody);

    @POST("/member/reset_passwd/")
//重置密码
    Flowable<HttpResultModel<ResetPasswdResults>> resetPassWord(@Body ResetPasswdRequestBody resetPasswdRequestBody);

    @POST("/account/get_consume_list/")
//钱包-消费明细
    Flowable<HttpResultModel<ConsumeListResults>> getConsumeListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/account/get_recharge_list/")
//钱包-充值明细
    Flowable<HttpResultModel<RechargeListResults>> getRechargeListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/marketing/get_marketing_reflect_list/")
//钱包-提现明细
    Flowable<HttpResultModel<CashListResults>> getCashListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/marketing/get_marketing_flow_list/")
//钱包-收益明细
    Flowable<HttpResultModel<ProfitListResults>> getProfitListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/G9game/paymentController.do?reflect")
//钱包-提现
    Flowable<HttpResultModel<CashToResults>> cashTo(@Body CashToRequestBody cashToRequestBody);

    @POST("/member/check_trade_passwd/")
//钱包-验证交易密码
    Flowable<HttpResultModel<CheckTradePasswdResults>> checkTradePassWord(@Body CheckTradePasswdRequestBody checkTradePasswdRequestBody);

    @POST("/marketing/get_invitation_list/")
//邀请记录
    Flowable<HttpResultModel<InvatationResults>> getInvatationList(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/member/reset_trade_password/")
//重置交易密码
    Flowable<HttpResultModel<ResetTradeResults>> resetTradePassword(@Body ResetTradeRequestBody resetTradeRequestBody);

    @POST("/member/set_trade_password/")//设置交易密码
    Flowable<HttpResultModel<ResetTradeResults>> setTradePassword(@Body SetTradeRequestBody setTradeRequestBody);

    @POST("/member/set_or_update_apliy_info/")
//重置支付宝
    Flowable<HttpResultModel<ResetAlipayResults>> resetAlipayAccount(@Body ResetAlipayRequestBody resetAlipayRequestBody);

    @POST("/member/get_left_vip_game_count/")
//获取vip充值游戏账号数量
    Flowable<HttpResultModel<VipGameAccountResults>> getVipGameAccount();

    @POST("/game/get_game_discount/")
//获取游戏折扣
    Flowable<HttpResultModel<GameAccountDiscountResults>> getGameAccountDiscount(@Body SingleGameIdRequestBody singleGameIdRequestBody);

    @POST("/member/get_vip_list")
//获取vip列表
    Flowable<HttpResultModel<VipLevelResults>> getVipLevel();

    @POST("/game/get_hot_game_list/")
//搜索热词列表
    Flowable<HttpResultModel<HotWordResults>> getApiHotWordData(@Body BaseRequestBody baseRequestBody);

    @POST("/game/query_game_list/")
//搜索列表
    Flowable<HttpResultModel<SearchListResults>> getApiSearchByWordData(@Body SearchRequestBody searchRequestBody);

    //多文件上传无进度值
    @POST("/member/set_icon/")
    Flowable<HttpResultModel> setApiUserIcon(@Body RequestBody Body);

    //单个文件上传有进度
    @Multipart
    @POST("/member/set_icon/")
    Flowable<HttpResultModel> setApiUserIcon(@Part MultipartBody.Part file);

    @POST("/member/set_nickname/")
//设置昵称
    Flowable<HttpResultModel<NotConcernResults>> updateNickname(@Body UpdateNicknameRequestBody updateNicknameRequestBody);

    @POST("/member/set_icon/")
//设置头像
    Flowable<HttpResultModel<NotConcernResults>> updateAvatar(@Body UpdateAvatarRequestBody updateAvatarRequestBody);

    @POST("/member/set_birthday/")
//设置生日
    Flowable<HttpResultModel<NotConcernResults>> updateBirthday(@Body UpdateBirthdayRequestBody updateBirthdayRequestBody);

    @POST("/member/set_phone/")
//设置手机
    Flowable<HttpResultModel<NotConcernResults>> updatePhone(@Body UpdatePhoneRequestBody updatePhoneRequestBody);

    @POST("/member/set_gender/")
//设置性别
    Flowable<HttpResultModel<NotConcernResults>> updateGender(@Body UpdateGenderRequestBody updateGenderRequestBody);

    @POST("/member/set_signature/")
//设置签名
    Flowable<HttpResultModel<NotConcernResults>> updateSignatrue(@Body UpdateSignatrueRequestBody updateSignatrueRequestBody);

    @POST("/sys/submit_feedback/")
//反馈
    Flowable<HttpResultModel<NotConcernResults>> feedBack(@Body FeedbackRequestBody feedbackRequestBody);

    @POST("/sys/get_feedback_list/")
//反馈列表
    Flowable<HttpResultModel<FeedbackListResults>> feedBackList();


    @POST("/marketing/get_marketing_info/")//推广账号详情
    Flowable<HttpResultModel<MarketInfoResults>> getMarketInfo();

    @POST("/marketing/get_marketing_flow_list/")//推广收益列表
    Flowable<HttpResultModel<MarketFlowlistResults>> getMarketFlowList(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/marketing/get_expected_flow_list/")//推广预期收益列表
    Flowable<HttpResultModel<MarketExpectedFlowlistResults>> getMarketExpectedFlowList(@Body SinglePageRequestBody singlePageRequestBody);
}
