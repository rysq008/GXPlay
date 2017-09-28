package com.example.zr.gxapplication.net;

import com.shandianshua.totoro.data.cache.CacheManager;
import com.shandianshua.totoro.data.net.model.AccountInfo;
import com.shandianshua.totoro.data.net.model.AdvertisingWalls;
import com.shandianshua.totoro.data.net.model.AgentDetailRequestBody;
import com.shandianshua.totoro.data.net.model.AgentGuide;
import com.shandianshua.totoro.data.net.model.AgentRequestBody;
import com.shandianshua.totoro.data.net.model.AgentSortType;
import com.shandianshua.totoro.data.net.model.AgentTaskCollect;
import com.shandianshua.totoro.data.net.model.AgentValidTask;
import com.shandianshua.totoro.data.net.model.AgentValidTaskInfo;
import com.shandianshua.totoro.data.net.model.AlimamaOrderList;
import com.shandianshua.totoro.data.net.model.AppAgents;
import com.shandianshua.totoro.data.net.model.AppChannelPackages;
import com.shandianshua.totoro.data.net.model.AppChannels;
import com.shandianshua.totoro.data.net.model.AppIncome;
import com.shandianshua.totoro.data.net.model.AppIncomeBanner;
import com.shandianshua.totoro.data.net.model.AppIncomeBannerDetail;
import com.shandianshua.totoro.data.net.model.AppPlugins;
import com.shandianshua.totoro.data.net.model.AreaPackAllType;
import com.shandianshua.totoro.data.net.model.BaseModel;
import com.shandianshua.totoro.data.net.model.BaseRequestBody;
import com.shandianshua.totoro.data.net.model.BaseResponse;
import com.shandianshua.totoro.data.net.model.BroadcastAdItems;
import com.shandianshua.totoro.data.net.model.ChannelApkDetailInfo;
import com.shandianshua.totoro.data.net.model.ChannelPackages;
import com.shandianshua.totoro.data.net.model.Component;
import com.shandianshua.totoro.data.net.model.ConfigModel;
import com.shandianshua.totoro.data.net.model.FAQDetailItemModel;
import com.shandianshua.totoro.data.net.model.FollowResult;
import com.shandianshua.totoro.data.net.model.IncomeList;
import com.shandianshua.totoro.data.net.model.IncomeTotal;
import com.shandianshua.totoro.data.net.model.InvitePageQrcode;
import com.shandianshua.totoro.data.net.model.LaunchTasks;
import com.shandianshua.totoro.data.net.model.MsgVerifyRequestModel;
import com.shandianshua.totoro.data.net.model.NewPersonTask;
import com.shandianshua.totoro.data.net.model.PlatinumRequestBody;
import com.shandianshua.totoro.data.net.model.PosterData;
import com.shandianshua.totoro.data.net.model.ProfitOverview;
import com.shandianshua.totoro.data.net.model.QiniuToken;
import com.shandianshua.totoro.data.net.model.RecommendTask;
import com.shandianshua.totoro.data.net.model.RedEnvelopeInfo;
import com.shandianshua.totoro.data.net.model.RefundInfo;
import com.shandianshua.totoro.data.net.model.SendMsgRequestBody;
import com.shandianshua.totoro.data.net.model.SendMsgVerifyCodeResult;
import com.shandianshua.totoro.data.net.model.ShareAgentResult;
import com.shandianshua.totoro.data.net.model.ShareAgentTask;
import com.shandianshua.totoro.data.net.model.Skipped;
import com.shandianshua.totoro.data.net.model.TaskDetail;
import com.shandianshua.totoro.data.net.model.TaskStepDetail;
import com.shandianshua.totoro.data.net.model.TuiaModel;
import com.shandianshua.totoro.data.net.model.VerifyMsgCodeResult;
import com.shandianshua.totoro.data.net.model.WithdrawRecordWrapper;
import com.shandianshua.totoro.data.net.model.WithdrawRedPackets;
import com.shandianshua.totoro.data.net.model.WithdrawResult;
import com.shandianshua.totoro.data.net.model.WxUserInfo;
import com.shandianshua.totoro.data.net.model.request.AlimamaOrderListBody;
import com.shandianshua.totoro.data.net.model.request.IncomeListBody;
import com.shandianshua.totoro.data.net.model.request.LoginBody;
import com.shandianshua.totoro.data.net.model.request.OrderSaveBody;
import com.umeng.update.UpdateResponse;

import java.util.List;

import rx.Observable;

/**
 * author: zhou date: 2016/6/2.
 */
public class DataService {
  /**
   * 查询用户账户信息
   *
   * @param unionId 用户unionId
   * @return Observable<AccountInfo>
   */
  public static Observable<AccountInfo> getAccountInfo(String unionId) {
    return DataLoadHelper.bindCacheUpdate(CacheManager.getAccountInfoCache(),
        RetrofitNetHelper.getUserApi().getAccountInfo(unionId));
  }

  /**
   * 用户提现接口
   *
   * @param unionId 用户unionId
   * @param wdAmount 提现金额
   * @return Observable<WithdrawResult>
   */
  public static Observable<WithdrawResult> withdraw(String unionId, long wdAmount,
                                                    Long wdRedPacketId) {
    return RetrofitNetHelper.getUserApi().withdraw(unionId, wdAmount, wdRedPacketId);
  }

  /**
   * 用户首次提现，发送短信验证码接口
   *
   * @param sendMsgRequestBody 参数@see #SendMsgRequestBody
   * @return Observable<SendMsgVerifyCodeResult>
   */
  public static Observable<SendMsgVerifyCodeResult> sendMsgVerifyCode(
      SendMsgRequestBody sendMsgRequestBody) {
    return RetrofitNetHelper.getUserApi().sendMsgVerifyCode(sendMsgRequestBody);
  }

  /**
   * 验证短信验证码接口
   *
   * @param unionId 用户unionId
   * @param msgVerifyRequestModel 手机号和验证码
   * @return Observable<VerifyMsgCodeResult>
   */
  public static Observable<VerifyMsgCodeResult> verifyMsgCode(String unionId,
      MsgVerifyRequestModel msgVerifyRequestModel) {
    return RetrofitNetHelper.getUserApi().verifyMsgCode(unionId,
        msgVerifyRequestModel);
  }

  /**
   * 获取用户提现纪录
   *
   * @param unionId 用户unionId
   * @return Observable<List<WithdrawRecord>>
   */
  public static Observable<WithdrawRecordWrapper> getWithdrawRecords(String unionId) {
    return RetrofitNetHelper.getUserApi().getWithdrawRecords(unionId);
  }

  /**
   * 带参数二维码和微信订阅量
   *
   * @param unionId 用户unionId
   * @return Observable<InvitePageQrcode>
   */
  public static Observable<InvitePageQrcode> getInvitePageQrcode(String unionId) {
    return RetrofitNetHelper.getUserApi().getInvitePageQrcode(unionId);
  }

  /**
   * 收益汇总明细
   *
   * @param unionId 用户unionId
   * @return Observable<ProfitOverview>
   */
  public static Observable<ProfitOverview> getProfitOverview(String unionId) {
    return DataLoadHelper.bindCacheUpdate(CacheManager.getProfitOverviewCache(),
        RetrofitNetHelper.getUserApi().getProfitOverview(unionId));
  }

  /**
   * 用户是否关注公众号
   *
   * @param unionId 用户unionId
   * @return Observable<FollowResult>
   */
  public static Observable<FollowResult> getUserFollowResult(String unionId) {
    return RetrofitNetHelper.getUserApi().getUserFollowResult(unionId);
  }

  /**
   * 拉取全局配置
   *
   * @return Observable<BaseResponse<ConfigModel>>
   */
  public static Observable<BaseResponse<ConfigModel>> getConfig() {
    return RetrofitNetHelper.getUserApi().getConfig();
  }

  /**
   * 获取渠道包
   *
   * @return List<AppPackage>
   */
  public static Observable<ChannelPackages> getAppPackages(String unionId) {
    return DataLoadHelper.bindCacheUpdate(CacheManager.getChannelPackagesCache(),
        RetrofitNetHelper.getUserApi().getAppPackages(unionId));
  }

  /**
   * 获取应用墙
   *
   * @return List<AppWall>
   */
  public static Observable<AdvertisingWalls> getAppWall() {
    return DataLoadHelper.bindCacheUpdate(CacheManager.getAppWallCache(),
        RetrofitNetHelper.getUserApi().getAppWall());
  }

  /**
   * 获取插件列表
   *
   * @return List<AppPlugin>
   */
  public static Observable<AppPlugins> getAppPlugins() {
    return DataLoadHelper.bindCacheUpdate(CacheManager.getAppPluginsCache(),
        RetrofitNetHelper.getUserApi().getAppPlugins());
  }

  /**
   * 获取渠道列表
   *
   * @return List<AppChannels>
   */
  public static Observable<BaseResponse<List<AppChannels>>> getAppChannels() {
    return RetrofitNetHelper.getUserApi().getAppChannels();
  }

  /**
   * 获取渠道包列表
   *
   * @return List<AppChannelPackages>
   */
  public static Observable<BaseResponse<List<AppChannelPackages>>> getAppChannelPackages(
      String channel,
      PlatinumRequestBody requestBody) {
    return RetrofitNetHelper.getUserApi().getAppChannelPackages(channel, requestBody);
  }

  /**
   * 获取APP首页特工任务列表
   *
   * @return List<AppAgent>
   */
  public static Observable<AppAgents> getAppAgents() {
    return DataLoadHelper.bindCacheUpdate(CacheManager.getAppAgentsCache(),
        RetrofitNetHelper.getUserApi().getAppAgents());
  }

  /**
   * 获取特工有效任务统计数据
   *
   * @return AgentTaskCollect
   */
  public static Observable<AgentTaskCollect> getAppAgentTaskCollect(String unionId) {
    return RetrofitNetHelper.getUserApi().getAgentTaskCollect(unionId);
  }

  /**
   * 新任务通知消息已读
   *
   * @param unionId
   * @param type //0:失败审核已读，1:成功审核已读
   * @return
   */
  public static Observable<BaseModel> getAgentTaskCollectReadStatus(String unionId, int type) {
    return RetrofitNetHelper.getUserApi().getAgentTaskCollectReadStatus(unionId, type);
  }

  /**
   * 获取特工分类
   * 
   * @return
   */
  public static Observable<AgentSortType> getAgentSortType() {
    return RetrofitNetHelper.getUserApi().getAgentSortType();
  }

  /**
   * 显示或者隐藏任务(忽略)
   *
   */
  public static Observable<BaseResponse> getAgentValidTaskIgnore(long id, boolean status) {
    return RetrofitNetHelper.getUserApi().getAgentValidTaskIgnore(id, status);
  }

  /**
   * 获取特工有效任务列表
   *
   * @return List<>
   */
  public static Observable<BaseResponse<AgentValidTaskInfo>> getAppAgentListTask(
      AgentRequestBody baseRequestBody) {
    return RetrofitNetHelper.getUserApi().getAgentValidTask(baseRequestBody);
  }

  /**
   * 获取特工有效任务列表
   *
   * @return
   */
  public static Observable<BaseResponse> saveLocation(String lat, String lng) {
    return RetrofitNetHelper.getUserApi().saveLocation(lat, lng);
  }

  /**
   * 获取特工任务类型子列表
   *
   * @return List<AgentValidTaskBean>
   */
  public static Observable<AgentValidTask> getAppAgentSubListTask(String unionId, long agentType,
                                                                  BaseRequestBody baseRequestBody) {
    return RetrofitNetHelper.getUserApi().getAgentListTask(unionId, agentType, baseRequestBody);
  }

  /**
   * 获取渠道包详情页信息
   *
   * @param packageName Apk名称
   * @return ChannelApkDetailInfo
   */
  public static Observable<ChannelApkDetailInfo> getApkDetailInfo(String packageName) {
    return RetrofitNetHelper.getUserApi().getApkDetailInfo(packageName);
  }

  /**
   * 获取海报数据
   *
   * @return List<PosterData>
   */
  public static Observable<PosterData> getPosterData() {
    return RetrofitNetHelper.getUserApi().getPosterData();
  }

  /**
   * 获取留存应用任务列表
   *
   * @return LaunchTasks
   */
  public static Observable<BaseResponse<LaunchTasks>> getLaunchTasks() {
    return RetrofitNetHelper.getUserApi().getLaunchTasks();
  }

  /**
   * 获取可用提现红包
   *
   * @param unionId 用户unionId
   * @param wdAmount 提现金额
   * @return 提现红包
   */
  public static Observable<WithdrawRedPackets> getWithdrawRedPackets(String unionId,
      long wdAmount) {
    return RetrofitNetHelper.getUserApi().getWithdrawRedPackets(unionId, wdAmount);
  }

  /**
   * 特工任务详情
   *
   * @return TaskDetail
   */
  public static Observable<BaseResponse<TaskDetail>> getAgentTaskDetail(
      AgentDetailRequestBody agentDetailRequestBody) {
    return RetrofitNetHelper.getUserApi().getAgentTaskDetail(agentDetailRequestBody);
  }

  /**
   * 特工任务分步列表
   * 
   * @return
   */
  public static Observable<BaseResponse<List<TaskStepDetail>>> getAgentTaskStepList(long taskNo,
                                                                                    long certId) {
    return RetrofitNetHelper.getUserApi().getAgentTaskStepList(taskNo, certId);
  }

  /**
   * 获取七牛token
   */
  public static Observable<QiniuToken> getQiniuToken() {
    return RetrofitNetHelper.getUserApi().getQiniuToken();
  }

  /**
   * 获取FAQ列表
   *
   * @return
   */
  public static Observable<FAQDetailItemModel> getFAQList() {
    return RetrofitNetHelper.getUserApi().getFAQList();
  }

  /**
   * 获取红包列表
   *
   * @param unionId
   * @return
   */
  public static Observable<RedEnvelopeInfo> getRedEnvelopeInfo(String unionId) {
    return RetrofitNetHelper.getUserApi().getRedEnvelopeInfo(unionId);
  }

  /**
   * 获取晒收入列表
   *
   * @param baseRequestBody
   * @return
   */
  public static Observable<AppIncome> getAppIncome(BaseRequestBody baseRequestBody) {
    return RetrofitNetHelper.getUserApi().getAppIncome(baseRequestBody);
  }

  /**
   * 获取晒收入Banner
   *
   * @return
   */
  public static Observable<AppIncomeBanner> getAppIncomeBanner() {
    return RetrofitNetHelper.getUserApi().getAppIncomeBanner();
  }

  /**
   * 获取晒收入立即参加
   *
   * @return
   */
  public static Observable<TaskDetail> getAppIncomeJoin(String unionId) {
    return RetrofitNetHelper.getUserApi().getAppIncomeJoin(unionId);
  }

  /**
   * 获取晒收入Banner详情
   *
   * @return
   */
  public static Observable<AppIncomeBannerDetail> getAppIncomeBannerDetail() {
    return RetrofitNetHelper.getUserApi().getAppIncomeBannerDetail();
  }

  /**
   * 获取当前用户新手任务进度
   */
  public static Observable<NewPersonTask> getNewPersonTask(String unionId) {
    return RetrofitNetHelper.getUserApi().getNewPersonTask(unionId);
  }

  /**
   * 获取推荐特工任务
   */
  public static Observable<RecommendTask> getRecommendTask(String unionId) {
    return RetrofitNetHelper.getUserApi().getRecommendTask(unionId);
  }

  /**
   * 版本更新
   */
  public static Observable<UpdateResponse> getUpdate(String version) {
    return RetrofitNetHelper.getUserApi().getUpdate(version);
  }

  /**
   * 百度（紫金）任务是否跳过安装直接给奖励
   */
  public static Observable<Skipped> getSkipped() {
    return RetrofitNetHelper.getUserApi().getSkipped();
  }

  /**
   * 分享特工任务
   */
  public static Observable<ShareAgentTask> getTaskShare(String unionId, long taskNo) {
    return RetrofitNetHelper.getUserApi().getTaskShare(unionId, String.valueOf(taskNo));
  }

  /**
   * 首页banner
   */
  public static Observable<BroadcastAdItems> getBroadcastAd(int type) {
    return RetrofitNetHelper.getUserApi().getBroadcastAd(type);
  }

  /**
   * 获取任务的凭证信息
   */
  public static Observable<BaseResponse<Component>> getSubmitInfo(long taskNo, long certId) {
    return RetrofitNetHelper.getUserApi().getSubmitInfo(taskNo, certId);
  }

  /**
   * 保存任务凭证信息
   */
  public static Observable<BaseResponse> saveTaskStepInfo(Object taskStepInfoBody) {
    return RetrofitNetHelper.getUserApi().saveTaskStepInfo(taskStepInfoBody);
  }

  /**
   * 上传任务凭证信息
   */
  public static Observable<BaseResponse> submitInfo(Object submitCertBody) {
    return RetrofitNetHelper.getUserApi().submitTask(submitCertBody);
  }

  /**
   * 收益明细总汇
   */
  public static Observable<BaseResponse<List<IncomeTotal>>> getIncomeTotal() {
    return RetrofitNetHelper.getUserApi().getIncomeTotal();
  }

  /**
   * 各项收益明细
   */
  public static Observable<BaseResponse<IncomeList>> getIncomeList(IncomeListBody incomeListBody) {
    return RetrofitNetHelper.getUserApi().getIncomeList(incomeListBody);
  }

  /**
   * 登录
   */
  public static Observable<BaseResponse<WxUserInfo>> login(LoginBody loginBody) {
    return RetrofitNetHelper.getUserApi().login(loginBody);
  }

  /**
   * 检查今天是否需要签到
   * 
   * @return
   */
  public static Observable<BaseResponse<Boolean>> checkInExits() {
    return RetrofitNetHelper.getUserApi().checkInExist();
  }

  /**
   * 特工新手引导
   *
   * @return
   */
  public static Observable<BaseResponse<AgentGuide>> getNewerAgentGuide() {
    return RetrofitNetHelper.getUserApi().getAgentNewerGuide();
  }

  /**
   * 保存阿里妈妈订单信息
   * 
   * @param orderSaveBody
   * @return
   */
  public static Observable<BaseResponse> saveAlimamaOrder(OrderSaveBody orderSaveBody) {
    return RetrofitNetHelper.getUserApi().saveAlimamaOrder(orderSaveBody);
  }

  /**
   * 获取返钱列表
   * 
   * @param lastTime
   * @return
   */
  public static Observable<BaseResponse<List<AlimamaOrderList>>> getAlimamaOrderList(
      AlimamaOrderListBody lastTime) {
    return RetrofitNetHelper.getUserApi().getAlimamaOrderList(lastTime);
  }

  /**
   * 获取返钱比率
   * 
   * @return
   */
  public static Observable<BaseResponse<RefundInfo>> getRefundInfo() {
    return RetrofitNetHelper.getUserApi().getRefundRate();
  }

  /**
   * 试玩专区所有渠道加载
   *
   * @return
   */
  public static Observable<BaseResponse<List<AreaPackAllType>>> getListAllType() {
    return RetrofitNetHelper.getUserApi().getListAllType();
  }

  /**
   * <<<<<<< Updated upstream
   * 推啊配置参数获取
   *
   */
  public static Observable<BaseResponse<TuiaModel>> getTuiaData() {
    return RetrofitNetHelper.getUserApi().getTuiaData();
  }

  /**
   * 获取投吧进入入口链接
   * 
   * @return
   */
  public static Observable<BaseResponse> getTuiBaData() {
    return RetrofitNetHelper.getUserApi().getTouBaData();
  }

  /**
   * 获取已做的用户列表
   *
   * @param uid
   * @param id
   * @return
   */
  public static Observable<BaseResponse<ShareAgentResult>> getSearchResult(String uid, String id) {
    return RetrofitNetHelper.getUserApi().getSearchResult(uid, id);
  }
}
