package com.game.helper.data;

/**
 * Created by zr on 2017-10-13.
 * 常量类
 */

public final class RxConstant {

    public static final int WRITE_PERMISSION_REQ_CODE = 2;
    public static final String ROOT_DIR = "gxplayer";

    public static final String VERIFY_USER_FOR_LOGIN = "0";
    public static final String VERIFY_USER_FOR_REGIST = "1";

    /**
     * 登陆share preference
     * */
    public static final String LOGIN_PREFERENCE_NAME = "login_share_preference";
    public static final String LOGIN_PREFERENCE_KEY_STATUS = "login_preference_islogin";
    public static final String LOGIN_PREFERENCE_KEY_USER = "login_preference_user";

    public static final int Head_Image_Change_Type = 40;
    public static final int Login_Type = 200;
    public static final int Proving_Trade_Password = 201;
    public static final int Chooice_RedPack = 300;

    public static final class HomeModeType {//首页模块数据类型描述
        public static final int Banner_Model_Type = 0;
        public static final int Notice_Model_Type = 1;
        public static final int Special_Model_Type = 2;
        public static final int Recommend_Model_Type = 3;
        public static final int Hot_Model_Type = 4;
    }

    public static final class GameModeType {//游戏模块数据类型
        public static final int Game_Classical_type = 10;//经典类型
        public static final int Game_Common_type = 11;//普通类型
        public static final int Game_List_type = 12;//子类型列表数据
    }

    public static final class GeneralizeModeType {//游戏推广数据类型
        public static final int Generalize_Balance_Amount_type = 20;//总收益
        public static final int Generalize_Balance_Withdraw_type = 21;//可提现
        public static final int Generalize_Balance_Expect_type = 22;//待提现（预期）
        public static final int Generalize_Balance_Account_Info_type = 23;//推广帐号信息
    }

    public static final class MineModeType {//游戏推广数据类型
        public static final int Mine_Balance_Wallet_type = 30;//钱包余额
    }

    public static final class AccountModeType {
        public static final int Account_Regist_type = 50;//注册
        public static final int Account_Login_type = 51;//登陆
        public static final int Account_Logout_type = 52;//登出
        public static final int Account_Member_Info_type = 53;//用户详情
        public static final int Account_Vertify_type = 54;//验证码
        public static final int Account_ResetPasswd_type = 55;//重置密码
    }

    public static final class WalletModelType{
        public static final int Wallet_Consume_type = 60;
        public static final int Wallet_Recharge_type = 61;
        public static final int Wallet_Cash_type = 62;
        public static final int Wallet_Prifit_type = 63;

        public static final int Wallet_Cash_To_Type = 67;
    }

    //第三方的一些appkey
    public static final class ThirdPartKey {
        //微信分享
        public static final String WeixinId = "wx1d5e45ad3dc2019a";
        public static final String WeixinSecret = "d33400dd7f4e358a435602e26d45e881";

        //微信支付
        public static final String WeixinPayId = "wx5c644291162536ba";
        public static final String WeixinPayKey = "384274bea7a4dee9382b5d3cea217094";
        public static final String WX_MCH_iD = "1313036501";
        public static final String WX_NOTIFY_URL = "http://60.205.204.218:8080/G9game/paymentController.do?wxReturn";


        public static final String QQId = "1105689325";
        public static final String QQKey = "hMJbCLDB4eiTTTSy";

        public static final String SinaWeiboKey = "734669220";
        public static final String SinaWeiboSecret = "4c643b2c952fd78d86902e007607e377";
        public static final String SinaWeiboRedirectUrl = "https://api.weibo.com/oauth2/default.html";

    }

}
