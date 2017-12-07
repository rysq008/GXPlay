package com.game.helper.net.api;

import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.RecommendRequestBody;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {

    @POST("/member/login/")
    Flowable<HttpResultModel<LoginResults>> getApiLoginData(@Body LoginRequestBody baseRequestBody);

    @POST("/sys/get_banner_list/")
    Flowable<HttpResultModel<BannerResults>> getApiBannerData();

    @POST("/sys/get_notification_list/")
//首页通告
    Flowable<HttpResultModel<NoticeResults>> getApiNoticeData();

    @POST("/game/get_game_theme_list/")
//首页专题
//    @HTTP(method = "",path = "/user/delete",hasBody = true)
    Flowable<HttpResultModel<SpecialResults>> getApiSpecialData(@Body BaseRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    @POST("/game/get_recommend_game_list/")
//首页热门推荐数据
    Flowable<HttpResultModel<HotResults>> getApiHotData(@Body BaseRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    @POST("/game/get_game_list/")
//首页自定义推荐数据
    Flowable<HttpResultModel<RecommendResults>> getApiRecommendData(@Body RecommendRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    @POST("/game/get_class_type_list/")
//经典分类列表
    Flowable<HttpResultModel<ClassicalResults>> getApiClassicalData();

    @POST("/game/get_type_list/")
//普通分类列表
    Flowable<HttpResultModel<CommonResults>> getApiCommonData();
}
