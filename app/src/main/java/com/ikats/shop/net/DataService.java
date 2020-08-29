package com.ikats.shop.net;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.LoginBean;
import com.ikats.shop.model.LogoutBean;
import com.ikats.shop.model.RegisterBean;
import com.ikats.shop.net.api.Api;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.net.model.BaseRequestBody;
import com.ikats.shop.net.model.LoginRequestBody;
import com.ikats.shop.net.model.RegistRequestBody;
import com.ikats.shop.utils.UploadUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.net.progress.ProgressHelper;
import cn.droidlover.xdroidmvp.net.progress.ProgressListener;
import ikidou.reflect.typeimpl.ParameterizedTypeImpl;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;

import static okhttp3.MultipartBody.FORM;

public class DataService {

    public static Flowable<HttpResultModel<RegisterBean>> login(RegistRequestBody requestBody) {
        return Api.CreateApiService().Regitst(requestBody);
    }

    public static Flowable<HttpResultModel<LoginBean>> login(LoginRequestBody requestBody) {
        return Api.CreateApiService().Login(requestBody);
    }

    public static Flowable<HttpResultModel<LogoutBean>> logout() {
        return Api.CreateApiService().Logout();
    }

    public static Flowable<HttpResultModel> getCode(String mobile) {
        return Api.CreateApiService().getCode(mobile);
    }

    //多个文件上传,返回一个总体结果
    public static Flowable<HttpResultModel<Object>> uploadApiFilesForSingleResult(String url, List<File> list, UploadUtils.FileUploadProgress fileUploadFlowable) {
        //构建body
        //addFormDataPart()第一个参数为表单名字，这是和后台约定好的
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(FORM);
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
    public static Flowable<HttpResultModel<Object>> uploadApiSingleFile(String url, File file, String file_name, UploadUtils.FileUploadProgress fileUploadFlowable) {
//        UploadUtils.UploadFileRequestBody uploadFileRequestBody = new UploadUtils.UploadFileRequestBody(file, fileUploadFlowable);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(FORM)
                .addFormDataPart("file", file_name,
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        return Api.CreateApiService().uploadApiFile(url, requestBody);
    }

    //头像上传
    public static Flowable<HttpResultModel<Object>> setApiUserIcon(File file, String fname, UploadUtils.FileUploadProgress fileUploadFlowable) {
        return uploadApiSingleFile("/mw/app/uploadImg", file, fname, fileUploadFlowable);
    }

    //多个文件上传，返回每个文件的上传结果的列表（相当于把多个文件单独上传压缩成一个上传请求列表）
    public static Flowable<List<HttpResultModel<Object>>> uploadApiFilesForListResult(String url, List<File> files, UploadUtils.FileUploadProgress fileUploadFlowable) {
        if (!Kits.Empty.check(files)) {
            List list = new ArrayList();
            for (File file : files) {
                list.add(uploadApiSingleFile(url, file, file.getName(), fileUploadFlowable));
            }
            return Flowable.zipIterable(list, new Function<Object[], ArrayList<HttpResultModel<Object>>>() {
                @Override
                public ArrayList<HttpResultModel<Object>> apply(Object[] objects) throws Exception {
                    ArrayList<HttpResultModel<Object>> arrayList = new ArrayList<>();
                    for (Object obj : objects) {
                        arrayList.add((HttpResultModel<Object>) obj);
                    }
                    return arrayList;
                }
            }, true, 1);
        }
        return null;
    }

    public static Flowable<HttpResultModel<LoginBean>> getApiLoginData(@Body LoginRequestBody loginRequestBody) {
        return Api.CreateApiService().getApiLoginData(loginRequestBody);
    }

    public static Flowable<HttpResultModel<Object>> upLoadImageFile(File file, String fn) {
        return uploadApiSingleFile("app/common/upload", file, fn, null);
    }

    public static DataServiceBuilder builder() {
        return new DataServiceBuilder();
    }

    public static class DataServiceBuilder {
        private Map<String, Object> params = new WeakHashMap<>();
        private RequestBody requestBody = null;
        private BaseRequestBody baseRequestBody = null;
        private File file = null;
        private String url;
        private Type type;
        private boolean interceptconvert;
        private Class clz;
        private ProgressListener progressListener;

        public DataServiceBuilder buildReqUrl(String url) {
            this.url = url;
            return this;
        }

        public DataServiceBuilder buildReplaceUrl(String from, String to) {
            if (url.contains(from))
                this.url = url.replace(from, to);
            return this;
        }

        public DataServiceBuilder buildInterceptconvert(boolean interceptconvert) {
            this.interceptconvert = interceptconvert;
            return this;
        }

        public DataServiceBuilder buildReqParams(String key, Object obj) {
            this.params.put(key, obj);
            return this;
        }

        public DataServiceBuilder buildProgress(ProgressListener progressListener) {
            this.progressListener = progressListener;
            return this;
        }

        public DataServiceBuilder buildReqParams(File file) {
            if (null == file || !file.exists()) {
                ToastUtils.showLong("文件不存在！");
                return this;
            }
            this.file = file;
            return this;
        }

        public DataServiceBuilder buildParseDataClass(Class clz) {
            this.clz = clz;
            return this;
        }

        public DataServiceBuilder buildParseDataType(Type type) {
            this.type = type;
            return this;
        }

        public DataServiceBuilder builderRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public DataServiceBuilder builderRequestBody(BaseRequestBody baseRequestBody) {
            this.baseRequestBody = baseRequestBody;
            return this;
        }

        public DataServiceBuilder buildReqParams(Map map) {
            this.params.putAll(map);
            return this;
        }

        public Flowable request(ApiService.HttpMethod method) {
            Flowable<ResponseBody> call = null;
            switch (method) {
                case GET:
                    call = DataService.getData(url, params);
                    break;
                case POST:
                    call = DataService.postData(url, params);
                    break;
                case POST_BODY:
                    call = DataService.postData(url, requestBody);
                    break;
                case POST_JSON:
                    call = DataService.postData(url, baseRequestBody);
                    break;
                case UPLOAD:
                    RequestBody requestBody = new MultipartBody.Builder().setType(FORM)
                            .addFormDataPart("file", file.getName(), RequestBody.create(FORM, file))
                            .build();
                    call = DataService.postData(url, requestBody);
                    break;
                case DOWNLOAD:
                    ProgressHelper.get().addResponseListener(url, this.progressListener);
                    call = DataService.downloadData(url);
                    break;
                default:
                    call = XApi.get("http://192.168.0.101:3000/", ApiService.class).getData(url, params);
                    break;
            }
            if (method == ApiService.HttpMethod.DOWNLOAD) return call;
            if (interceptconvert) return call;
            return call.flatMap((Function<ResponseBody, Flowable<?>>) responseBody -> {
//                    type = TypeBuilder.newInstance(HttpResultModel.class)
//                            .addTypeParam(clz)
//                            .build();
                if (clz == null) clz = Object.class;
                if (type == null)
                    type = new ParameterizedTypeImpl(HttpResultModel.class, new Class[]{clz}, null);
                String str = responseBody.string();
                HttpResultModel<?> st = new Gson().fromJson(str, type);
                return Flowable.just(st);
            });
        }
    }

    public static Flowable<ResponseBody> getData(String url, Map map) {
        return Api.CreateApiService().getData(url, map);
    }

    public static Flowable<ResponseBody> postData(String url, Map map) {
        return Api.CreateApiService().postData(url, map);
    }

    public static Flowable<ResponseBody> postData(String url, RequestBody requestBody) {
        return Api.CreateApiService().postData(url, requestBody);
    }

    public static Flowable<ResponseBody> postData(String url, BaseRequestBody requestBody) {
        return Api.CreateApiService().postData(url, requestBody);
    }

    public static Flowable<ResponseBody> downloadData(String url) {
        return Api.CreateApiService().downloadData(url);
    }

}
