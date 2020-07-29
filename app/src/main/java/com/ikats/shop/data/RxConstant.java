package com.ikats.shop.data;


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

    public static final String PRODUCTS ="4902806437980,4902806437980,曼丹婴儿肌毛孔细致玻尿酸面膜蓝盒5片,https://shop90485387.m.youzan.com/wscgoods/detail/2g0jo1ifn7ftv?scan=1&activity=none&from=kdt&qr=directgoods_633465213\n" +
            "4902806438161,4902806438161,曼丹婴儿肌胶原蛋白弹力面膜橙盒5片,https://shop90485387.m.youzan.com/wscgoods/detail/2g0jo1ifn7ftv?scan=1&activity=none&from=kdt&qr=directgoods_633465213\n" +
            "4902806438161,4902806438161,曼丹婴儿肌维C美白面膜粉盒5片,https://shop90485387.m.youzan.com/wscgoods/detail/2g0jo1ifn7ftv?scan=1&activity=none&from=kdt&qr=directgoods_633465213\n" +
            "4901417630674,4901417630674,肌美精3D面膜4片/盒 粉色,https://shop90485387.m.youzan.com/wscgoods/detail/2fueao7pfx2lv?scan=1&activity=none&from=kdt&qr=directgoods_661805453\n" +
            "4901417631381,4901417631381,肌美精3D面膜4片/盒 蓝色,https://shop90485387.m.youzan.com/wscgoods/detail/2fueao7pfx2lv?scan=1&activity=none&from=kdt&qr=directgoods_661805453\n" +
            "4901417630988,4901417630988,肌美精3D面膜4片/盒 橙色,https://shop90485387.m.youzan.com/wscgoods/detail/2fueao7pfx2lv?scan=1&activity=none&from=kdt&qr=directgoods_661805453\n" +
            "4544877506549,4544877506549,红蛇眼膜,https://shop90485387.m.youzan.com/wscgoods/detail/360paouzw4ktf?scan=1&activity=none&from=kdt&qr=directgoods_638893196\n" +
            "4901482837144,4901482837144,资生堂红色发膜,https://shop90485387.m.youzan.com/wscgoods/detail/2xmxb39440xn7?scan=1&activity=none&from=kdt&qr=directgoods_633667359\n" +
            "4964596457821,4964596457821,豆乳化妆水,https://shop90485387.m.youzan.com/wscgoods/detail/3eqtruqvkvrtv?scan=1&activity=none&from=kdt&qr=directgoods_668641914\n" +
            "4903335695551,4903335695551,薏仁霜,https://shop90485387.m.youzan.com/wscgoods/detail/2oudj1uelfzjn?scan=1&activity=none&from=kdt&qr=directgoods_639451481\n" +
            "4580504130183,4580504130183,AG面膜蓝色,https://shop90485387.m.youzan.com/wscgoods/detail/1y81iz3fqryrn?scan=1&activity=none&from=kdt&qr=directgoods_668906267\n" +
            "4580504130169,4580504130169,AG面膜金色,https://shop90485387.m.youzan.com/wscgoods/detail/1y81iz3fqryrn?scan=1&activity=none&from=kdt&qr=directgoods_668906267\n" +
            "4582167870017,4582167870017,珂润去角质,https://shop90485387.m.youzan.com/wscgoods/detail/2x9eqin9j0e2r?scan=1&activity=none&from=kdt&qr=directgoods_668726291\n" +
            "4987107616524,4987107616524,miono乳液,https://shop90485387.m.youzan.com/wscgoods/detail/2xlpb4c3rq79f?scan=1&activity=none&from=kdt&qr=directgoods_639583732\n" +
            "4987107616395,4987107616395,miono化妆水2号,https://shop90485387.m.youzan.com/wscgoods/detail/2x6xiwhzi5j4j?scan=1&activity=none&from=kdt&qr=directgoods_639578391\n" +
            "14971710358077,14971710358077,黛珂紫苏水本土,https://shop90485387.m.youzan.com/wscgoods/detail/2olqy1h80vbz7?scan=1&activity=none&from=kdt&qr=directgoods_639479646\n" +
            "4901872459957,4901872459957,资生堂金色发膜,https://shop90485387.m.youzan.com/wscgoods/detail/1yj60dg8hxsar?scan=1&activity=none&from=kdt&qr=directgoods_668902278\n" +
            "493255411775,493255411775,艾杜莎睫毛膏,https://shop90485387.m.youzan.com/wscgoods/detail/2fo9slqa24ev7?scan=1&activity=none&from=kdt&qr=directgoods_639576773\n" +
            "4961503571809,4961503571809,洛丽塔发油,https://shop90485387.m.youzan.com/wscgoods/detail/1y5lvx7af8qdv?scan=1&activity=none&from=kdt&qr=directgoods_668900148\n" +
            "4901433036504,4901433036504,kissme眼线笔,https://shop90485387.m.youzan.com/wscgoods/detail/2xeawew2q2shv?scan=1&activity=none&from=kdt&qr=directgoods_668924902\n" +
            "4901433036962,4901433036962,kissme睫毛膏3代,https://shop90485387.m.youzan.com/wscgoods/detail/2xj7kugmgr2sz?scan=1&activity=none&from=kdt&qr=directgoods_668922337\n" +
            "49664596457814,49664596457814,豆乳洗面奶,https://shop90485387.m.youzan.com/wscgoods/detail/27bpm8xkisrur?scan=1&activity=none&from=kdt&qr=directgoods_661806271\n" +
            "4901872449699,4901872449699,资生堂uno男士洗面奶,https://shop90485387.m.youzan.com/wscgoods/detail/1y9akhdq2dgdv?scan=1&activity=none&from=kdt&qr=directgoods_662031830\n" +
            "4901872463404,4901872463404,资生堂专科洗面奶,https://shop90485387.m.youzan.com/wscgoods/detail/2xi03tsn1lp03?scan=1&activity=none&from=kdt&qr=directgoods_662022188\n" +
            "4973167158425,4973167158425,芙丽芳丝洗面奶本土版,https://shop90485387.m.youzan.com/wscgoods/detail/2oeccxfu2iacj?scan=1&activity=none&from=kdt&qr=directgoods_661743093\n" +
            "4953923303900,4953923303900,pola黑BA洗面奶,https://shop90485387.m.youzan.com/wscgoods/detail/3eodycg8y73b7?scan=1&activity=none&from=kdt&qr=directgoods_662025076\n" +
            "4901301236210,4901301236210,珂润面霜,https://shop90485387.m.youzan.com/wscgoods/detail/1y6ueb2oxud0z?scan=1&activity=none&from=kdt&qr=directgoods_661809438\n" +
            "4987107616647,4987107616647,miono面膜,https://shop90485387.m.youzan.com/wscgoods/detail/27cwelacyhz77?scan=1&activity=none&from=kdt&qr=directgoods_633453128\n" +
            "4901433039611,4901433039611,kissme睫毛膏2代,https://shop90485387.m.youzan.com/wscgoods/detail/27cxsh89gkf9v?scan=1&activity=none&from=kdt&qr=directgoods_639575681\n" +
            "49492440034713,49492440034713,大米面膜,https://shop90485387.m.youzan.com/wscgoods/detail/2xbtuwsyoquwj?scan=1&activity=none&from=kdt&qr=directgoods_633368457\n" +
            "4961989409016,4961989409016,酒粕面膜涂抹面膜,https://shop90485387.m.youzan.com/wscgoods/detail/2oi231t8uly5v?scan=1&activity=none&from=kdt&qr=directgoods_661715741\n" +
            "4902468236037,4902468236037,明色眼霜,https://shop90485387.m.youzan.com/wscgoods/detail/26vpuaymto6wz?scan=1&activity=none&from=kdt&qr=directgoods_638888598\n" +
            "4901872449705,4901872449705,uno红色面霜,https://shop90485387.m.youzan.com/wscgoods/detail/3equ4auezl9gj?scan=1&activity=none&from=kdt&qr=directgoods_639490180\n" +
            "4901872460793,4901872460793,uno蓝色面霜,https://shop90485387.m.youzan.com/wscgoods/detail/2g0kvu8e5fgjn?scan=1&activity=none&from=kdt&qr=directgoods_668824831\n" +
            "4901301236173,4901301236173,珂润乳液,https://shop90485387.m.youzan.com/wscgoods/detail/3ezhe3xmjt97n?scan=1&activity=none&from=kdt&qr=directgoods_639350817\n" +
            "4901301236043,4901301236043,珂润一号水,https://shop90485387.m.youzan.com/wscgoods/detail/364fw5v76c3gz?scan=1&activity=none&from=kdt&qr=directgoods_638992200\n" +
            "4901301236197,4901301236197,珂润二号水,https://shop90485387.m.youzan.com/wscgoods/detail/3ngwoyvao3ro3?scan=1&activity=none&from=kdt&qr=directgoods_639055890\n" +
            "4901301236180,4901301236180,珂润三号水,https://shop90485387.m.youzan.com/wscgoods/detail/369cec1y0omab?scan=1&activity=none&from=kdt&qr=directgoods_639090125\n" +
            "4901301269348,4901301269348,珂润洗面奶,https://shop90485387.m.youzan.com/wscgoods/detail/3f1w07l0bzqwz?scan=1&activity=none&from=kdt&qr=directgoods_668141341\n" +
            "4562270778103,4562270778103,小玻尿酸,https://shop90485387.m.youzan.com/wscgoods/detail/364fjhv1ojrmb?scan=1&activity=none&from=kdt&qr=directgoods_639578356\n" +
            "4562270778219,4562270778219,大玻尿酸,https://shop90485387.m.youzan.com/wscgoods/detail/3nwyjv5ourkf7?scan=1&activity=none&from=kdt&qr=directgoods_662069841\n" +
            "4987241135011,4987241135011,cc美容液,https://shop90485387.m.youzan.com/wscgoods/detail/3npk57gk0tr37?scan=1&activity=none&from=kdt&qr=directgoods_668900454\n" +
            "49325263,49325263,资生堂红色护手霜,https://shop90485387.m.youzan.com/wscgoods/detail/36e9lsjcd0srn?scan=1&activity=none&from=kdt&qr=directgoods_668900747\n" +
            "4524734123294,4524734123294,城野377精华,https://shop90485387.m.youzan.com/wscgoods/detail/36d1m2dniwnsz?scan=1&activity=none&from=kdt&qr=directgoods_668918728\n" +
            "4931449432526,4931449432526,ipsa流金水本土,https://shop90485387.m.youzan.com/wscgoods/detail/26y54xonp8e6r?scan=1&activity=none&from=kdt&qr=directgoods_668735210\n" +
            "4524734500583,4524734500583,小城野水,https://shop90485387.m.youzan.com/wscgoods/detail/3eqswp68sq35f?scan=1&activity=none&from=kdt&qr=directgoods_668910065\n" +
            "4964596457845,4964596457845,豆乳水乳,https://shop90485387.m.youzan.com/wscgoods/detail/3eqtruqvkvrtv?scan=1&activity=none&from=kdt&qr=directgoods_668641914\n" +
            "4903335695254,4903335695254,薏仁水,https://shop90485387.m.youzan.com/wscgoods/detail/1y5m1w1ecez9v?scan=1&activity=none&from=kdt&qr=directgoods_668712184\n" +
            "4901234299214,4901234299214,佑天兰果冻面膜红色,https://shop90485387.m.youzan.com/wscgoods/detail/3f0ocnv7znyoj?scan=1&activity=none&from=kdt&qr=directgoods_661802456\n" +
            "4901234299313,4901234299313,佑天兰果冻面膜蓝色,https://shop90485387.m.youzan.com/wscgoods/detail/3f0ocnv7znyoj?scan=1&activity=none&from=kdt&qr=directgoods_661802456\n" +
            "4901234299719,4901234299719,佑天兰果冻面膜橙色,https://shop90485387.m.youzan.com/wscgoods/detail/3f0ocnv7znyoj?scan=1&activity=none&from=kdt&qr=directgoods_661802456\n" +
            "4901234303416,4901234303416,佑天兰果冻面膜浅蓝色,https://shop90485387.m.youzan.com/wscgoods/detail/3f0ocnv7znyoj?scan=1&activity=none&from=kdt&qr=directgoods_661802456\n" +
            "4987107616340,4987107616340,miono化妆水1号,https://shop90485387.m.youzan.com/wscgoods/detail/2x6xiwhzi5j4j?scan=1&activity=none&from=kdt&qr=directgoods_639578391\n" +
            "8809540519339,8809540519339,维他命亮白安瓶面膜,https://shop90485387.m.youzan.com/wscgoods/detail/2fudlnlx0a8n7?scan=1&activity=none&from=kdt&qr=directgoods_668711492";
}
