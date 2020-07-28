package com.know_action.foresight.model;


import com.know_action.foresight.model.BaseModel.XBaseModel;

public class LoginBean extends XBaseModel {

    public String userId = "";
    public String loginName;
    public String userName;
    public String email;
    public String phonenumber;
    public String sex;
    public String avatar;
    public long createTime;

    public String access_token="";

}
