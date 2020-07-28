package com.know_action.foresight.data;


public final class RxConstant {

    public static final int WRITE_PERMISSION_REQ_CODE = 2;
    public static final String ROOT_DIR = "gxplayer";
    public static final String HOME_BOTTOM_TAB_TAG = "CustomBadgeItem";

    public static final String VERIFY_USER_FOR_LOGIN = "0";
    public static final String VERIFY_USER_FOR_REGIST = "1";

    public static final int PLATFORM_ANDROID = 1;

    /**
     * 登录share preference
     */
    public static final String LOGIN_PREFERENCE_NAME = "login_share_preference";
    public static final String LOGIN_PREFERENCE_KEY_STATUS = "login_preference_islogin";
    public static final String LOGIN_PREFERENCE_KEY_USER = "login_preference_user";

    public static final int Head_Image_Change_Type = 40;
    public static final int Login_Type = 200;
    public static final int Proving_Trade_Password = 201;
    public static final int WX_PAY = 300;
    public static final int WX_PAY_FREE_PLAY = 400;

    public static final class HomeModeType {//首页模块数据类型描述
        public static final int Banner_Model_Type = 0;
        public static final int Notice_Model_Type = 1;
        public static final int Special_Model_Type = 2;
        public static final int Recommend_Model_Type = 3;
        public static final int Hot_Model_Type = 4;
        public static final int H5_Model_Type = 5;
    }


    public static final class AccountModeType {
        public static final int Account_Regist_type = 50;//注册
        public static final int Account_Login_type = 51;//登录
        public static final int Account_Logout_type = 52;//登出
        public static final int Account_Member_Info_type = 53;//用户详情
        public static final int Account_Vertify_type = 54;//验证码
        public static final int Account_ResetPasswd_type = 55;//重置密码
    }

    public static final class WalletModelType {
        public static final int Wallet_Consume_type = 60;
        public static final int Wallet_Recharge_type = 61;
        public static final int Wallet_Cash_type = 62;
        public static final int Wallet_Prifit_type = 63;

        public static final int Wallet_Cash_To_Type = 67;
    }

    //第三方的一些appkey
    public static final class ThirdPartKey {
        //微信分享

        //微信支付
    }

    /**
     * 监管APPID, SECRET
     */
    public static final String WX_APP_ID = "";
    public static final String WX_APP_SECRET = "";
    public static final String WX_BIND = "weixinlogin";
    /**
     * 请求成功 code
     */
    public static final String RESULT_OK = "1";

    /**
     * 手机号校验 1开头 11位数字
     */
    public static final String PHONE_CHECK = "^1\\d{10}$";

    /**
     * Budly AppId
     */
    public static final String BUGLY_APPID = "18a078f77e";
}
