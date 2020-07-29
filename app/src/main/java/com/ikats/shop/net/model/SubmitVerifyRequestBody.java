package com.ikats.shop.net.model;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SubmitVerifyRequestBody extends BaseRequestBody {

    public String collectId;//
    public String innocuousId;//
    public String reportId;//
    public File autograph;//
    public List lengthPhotosModels;//
    public String collectPosition;//
    public String collectAddress;
    public String lengthId;
    public String photoUrl;
    public String count;
    public List environmentPhotosModels;//
    public String carId;

    public SubmitVerifyRequestBody() {
        super(1);
    }

    public static class Builder {

        public Builder setCollectPosition(String collectPosition) {
            requestBody.collectPosition = collectPosition;
            mBuilder.addFormDataPart("collectPosition",collectPosition);
            return this;
        }

        public Builder setCollectAddress(String collectAddress) {
            requestBody.collectAddress = collectAddress;
            mBuilder.addFormDataPart("collectAddress",collectAddress);
            return this;
        }

        public Builder setLengthId(String lengthId) {
            requestBody.lengthId = lengthId;
            mBuilder.addFormDataPart("lengthId",lengthId);
            return this;
        }

        public Builder setPhotoUrl(String photoUrl) {
            requestBody.photoUrl = photoUrl;
            mBuilder.addFormDataPart("photoUrl",photoUrl);
            return this;
        }

        public Builder setCount(String count) {
            requestBody.count = count;
            mBuilder.addFormDataPart("count",count);
            return this;
        }

        public Builder setEnvironmentPhotosModels(List environmentPhotosModels) {
            requestBody.environmentPhotosModels = environmentPhotosModels;
            mBuilder.addFormDataPart("environmentPhotosModels",new Gson().toJson(environmentPhotosModels));
            return this;
        }


        public Builder setCollectId(String collectId) {
            requestBody.collectId = collectId;
            mBuilder.addFormDataPart("collectId",collectId);
            return this;
        }

        public Builder setInnocuousId(String innocuousId) {
            requestBody.innocuousId = innocuousId;
            mBuilder.addFormDataPart("innocuousId",innocuousId);
            return this;
        }

        public Builder setReportId(String reportId) {
            requestBody.reportId = reportId;
            mBuilder.addFormDataPart("reportId",reportId);
            return this;
        }

        public Builder setLengthPhotosModels(List lengthPhotosModels) {
            requestBody.lengthPhotosModels = lengthPhotosModels;
            mBuilder.addFormDataPart("lengthPhotosModels",new Gson().toJson(lengthPhotosModels));
            return this;
        }

        public Builder setAutograph(File autograph) {
            requestBody.autograph = autograph;
            mBuilder.addFormDataPart("autograph",autograph.getName(),RequestBody.create(MediaType.parse("multipart/form-data"),autograph));
            return this;
        }

        public Builder setCarId(String carId){
            requestBody.carId = carId;
            mBuilder.addFormDataPart("carId",carId);
            return this;
        }

        SubmitVerifyRequestBody requestBody;
        MultipartBody.Builder mBuilder;

        public Builder() {
            requestBody = new SubmitVerifyRequestBody();
            mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }

        public SubmitVerifyRequestBody build() {
            return requestBody;
        }

        public RequestBody requestBody(){
            return mBuilder.build();
        }

    }

}
