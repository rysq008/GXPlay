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

    public static final String PROVINCE ="[{\n" +
            "    \"code\": \"110000\",\n" +
            "    \"name\": \"北京市\",\n" +
            "    \"cityList\": [\n" +
            "      {\n" +
            "        \"code\": \"110000\",\n" +
            "        \"name\": \"北京市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"110101\",\n" +
            "            \"name\": \"东城区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110102\",\n" +
            "            \"name\": \"西城区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110105\",\n" +
            "            \"name\": \"朝阳区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110106\",\n" +
            "            \"name\": \"丰台区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110107\",\n" +
            "            \"name\": \"石景山区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110108\",\n" +
            "            \"name\": \"海淀区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110109\",\n" +
            "            \"name\": \"门头沟区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110111\",\n" +
            "            \"name\": \"房山区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110112\",\n" +
            "            \"name\": \"通州区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110113\",\n" +
            "            \"name\": \"顺义区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110114\",\n" +
            "            \"name\": \"昌平区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110115\",\n" +
            "            \"name\": \"大兴区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110116\",\n" +
            "            \"name\": \"怀柔区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110117\",\n" +
            "            \"name\": \"平谷区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110118\",\n" +
            "            \"name\": \"密云区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"110119\",\n" +
            "            \"name\": \"延庆区\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"code\": \"120000\",\n" +
            "    \"name\": \"天津市\",\n" +
            "    \"cityList\": [\n" +
            "      {\n" +
            "        \"code\": \"120000\",\n" +
            "        \"name\": \"天津市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"120101\",\n" +
            "            \"name\": \"和平区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120102\",\n" +
            "            \"name\": \"河东区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120103\",\n" +
            "            \"name\": \"河西区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120104\",\n" +
            "            \"name\": \"南开区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120105\",\n" +
            "            \"name\": \"河北区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120106\",\n" +
            "            \"name\": \"红桥区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120110\",\n" +
            "            \"name\": \"东丽区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120111\",\n" +
            "            \"name\": \"西青区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120112\",\n" +
            "            \"name\": \"津南区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120113\",\n" +
            "            \"name\": \"北辰区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120114\",\n" +
            "            \"name\": \"武清区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120115\",\n" +
            "            \"name\": \"宝坻区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120116\",\n" +
            "            \"name\": \"滨海新区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120117\",\n" +
            "            \"name\": \"宁河区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120118\",\n" +
            "            \"name\": \"静海区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"120119\",\n" +
            "            \"name\": \"蓟州区\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"code\": \"130000\",\n" +
            "    \"name\": \"河北省\",\n" +
            "    \"cityList\": [\n" +
            "      {\n" +
            "        \"code\": \"130100\",\n" +
            "        \"name\": \"石家庄市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130102\",\n" +
            "            \"name\": \"长安区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130104\",\n" +
            "            \"name\": \"桥西区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130105\",\n" +
            "            \"name\": \"新华区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130107\",\n" +
            "            \"name\": \"井陉矿区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130108\",\n" +
            "            \"name\": \"裕华区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130109\",\n" +
            "            \"name\": \"藁城区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130110\",\n" +
            "            \"name\": \"鹿泉区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130111\",\n" +
            "            \"name\": \"栾城区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130121\",\n" +
            "            \"name\": \"井陉县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130123\",\n" +
            "            \"name\": \"正定县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130125\",\n" +
            "            \"name\": \"行唐县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130126\",\n" +
            "            \"name\": \"灵寿县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130127\",\n" +
            "            \"name\": \"高邑县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130128\",\n" +
            "            \"name\": \"深泽县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130129\",\n" +
            "            \"name\": \"赞皇县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130130\",\n" +
            "            \"name\": \"无极县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130131\",\n" +
            "            \"name\": \"平山县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130132\",\n" +
            "            \"name\": \"元氏县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130133\",\n" +
            "            \"name\": \"赵县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130181\",\n" +
            "            \"name\": \"辛集市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130183\",\n" +
            "            \"name\": \"晋州市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130184\",\n" +
            "            \"name\": \"新乐市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130200\",\n" +
            "        \"name\": \"唐山市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130202\",\n" +
            "            \"name\": \"路南区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130203\",\n" +
            "            \"name\": \"路北区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130204\",\n" +
            "            \"name\": \"古冶区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130205\",\n" +
            "            \"name\": \"开平区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130207\",\n" +
            "            \"name\": \"丰南区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130208\",\n" +
            "            \"name\": \"丰润区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130209\",\n" +
            "            \"name\": \"曹妃甸区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130224\",\n" +
            "            \"name\": \"滦南县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130225\",\n" +
            "            \"name\": \"乐亭县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130227\",\n" +
            "            \"name\": \"迁西县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130229\",\n" +
            "            \"name\": \"玉田县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130281\",\n" +
            "            \"name\": \"遵化市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130283\",\n" +
            "            \"name\": \"迁安市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130284\",\n" +
            "            \"name\": \"滦州市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130300\",\n" +
            "        \"name\": \"秦皇岛市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130302\",\n" +
            "            \"name\": \"海港区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130303\",\n" +
            "            \"name\": \"山海关区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130304\",\n" +
            "            \"name\": \"北戴河区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130306\",\n" +
            "            \"name\": \"抚宁区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130321\",\n" +
            "            \"name\": \"青龙满族自治县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130322\",\n" +
            "            \"name\": \"昌黎县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130324\",\n" +
            "            \"name\": \"卢龙县\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130400\",\n" +
            "        \"name\": \"邯郸市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130402\",\n" +
            "            \"name\": \"邯山区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130403\",\n" +
            "            \"name\": \"丛台区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130404\",\n" +
            "            \"name\": \"复兴区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130406\",\n" +
            "            \"name\": \"峰峰矿区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130407\",\n" +
            "            \"name\": \"肥乡区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130408\",\n" +
            "            \"name\": \"永年区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130423\",\n" +
            "            \"name\": \"临漳县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130424\",\n" +
            "            \"name\": \"成安县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130425\",\n" +
            "            \"name\": \"大名县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130426\",\n" +
            "            \"name\": \"涉县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130427\",\n" +
            "            \"name\": \"磁县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130430\",\n" +
            "            \"name\": \"邱县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130431\",\n" +
            "            \"name\": \"鸡泽县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130432\",\n" +
            "            \"name\": \"广平县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130433\",\n" +
            "            \"name\": \"馆陶县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130434\",\n" +
            "            \"name\": \"魏县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130435\",\n" +
            "            \"name\": \"曲周县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130481\",\n" +
            "            \"name\": \"武安市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130500\",\n" +
            "        \"name\": \"邢台市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130502\",\n" +
            "            \"name\": \"桥东区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130503\",\n" +
            "            \"name\": \"桥西区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130521\",\n" +
            "            \"name\": \"邢台县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130522\",\n" +
            "            \"name\": \"临城县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130523\",\n" +
            "            \"name\": \"内丘县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130524\",\n" +
            "            \"name\": \"柏乡县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130525\",\n" +
            "            \"name\": \"隆尧县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130526\",\n" +
            "            \"name\": \"任县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130527\",\n" +
            "            \"name\": \"南和县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130528\",\n" +
            "            \"name\": \"宁晋县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130529\",\n" +
            "            \"name\": \"巨鹿县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130530\",\n" +
            "            \"name\": \"新河县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130531\",\n" +
            "            \"name\": \"广宗县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130532\",\n" +
            "            \"name\": \"平乡县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130533\",\n" +
            "            \"name\": \"威县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130534\",\n" +
            "            \"name\": \"清河县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130535\",\n" +
            "            \"name\": \"临西县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130581\",\n" +
            "            \"name\": \"南宫市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130582\",\n" +
            "            \"name\": \"沙河市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130600\",\n" +
            "        \"name\": \"保定市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130602\",\n" +
            "            \"name\": \"竞秀区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130606\",\n" +
            "            \"name\": \"莲池区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130607\",\n" +
            "            \"name\": \"满城区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130608\",\n" +
            "            \"name\": \"清苑区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130609\",\n" +
            "            \"name\": \"徐水区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130623\",\n" +
            "            \"name\": \"涞水县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130624\",\n" +
            "            \"name\": \"阜平县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130626\",\n" +
            "            \"name\": \"定兴县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130627\",\n" +
            "            \"name\": \"唐县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130628\",\n" +
            "            \"name\": \"高阳县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130629\",\n" +
            "            \"name\": \"容城县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130630\",\n" +
            "            \"name\": \"涞源县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130631\",\n" +
            "            \"name\": \"望都县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130632\",\n" +
            "            \"name\": \"安新县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130633\",\n" +
            "            \"name\": \"易县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130634\",\n" +
            "            \"name\": \"曲阳县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130635\",\n" +
            "            \"name\": \"蠡县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130636\",\n" +
            "            \"name\": \"顺平县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130637\",\n" +
            "            \"name\": \"博野县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130638\",\n" +
            "            \"name\": \"雄县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130681\",\n" +
            "            \"name\": \"涿州市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130682\",\n" +
            "            \"name\": \"定州市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130683\",\n" +
            "            \"name\": \"安国市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130684\",\n" +
            "            \"name\": \"高碑店市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130700\",\n" +
            "        \"name\": \"张家口市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130702\",\n" +
            "            \"name\": \"桥东区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130703\",\n" +
            "            \"name\": \"桥西区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130705\",\n" +
            "            \"name\": \"宣化区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130706\",\n" +
            "            \"name\": \"下花园区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130708\",\n" +
            "            \"name\": \"万全区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130709\",\n" +
            "            \"name\": \"崇礼区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130722\",\n" +
            "            \"name\": \"张北县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130723\",\n" +
            "            \"name\": \"康保县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130724\",\n" +
            "            \"name\": \"沽源县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130725\",\n" +
            "            \"name\": \"尚义县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130726\",\n" +
            "            \"name\": \"蔚县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130727\",\n" +
            "            \"name\": \"阳原县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130728\",\n" +
            "            \"name\": \"怀安县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130730\",\n" +
            "            \"name\": \"怀来县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130731\",\n" +
            "            \"name\": \"涿鹿县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130732\",\n" +
            "            \"name\": \"赤城县\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130800\",\n" +
            "        \"name\": \"承德市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130802\",\n" +
            "            \"name\": \"双桥区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130803\",\n" +
            "            \"name\": \"双滦区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130804\",\n" +
            "            \"name\": \"鹰手营子矿区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130821\",\n" +
            "            \"name\": \"承德县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130822\",\n" +
            "            \"name\": \"兴隆县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130824\",\n" +
            "            \"name\": \"滦平县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130825\",\n" +
            "            \"name\": \"隆化县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130826\",\n" +
            "            \"name\": \"丰宁满族自治县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130827\",\n" +
            "            \"name\": \"宽城满族自治县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130828\",\n" +
            "            \"name\": \"围场满族蒙古族自治县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130881\",\n" +
            "            \"name\": \"平泉市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"130900\",\n" +
            "        \"name\": \"沧州市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"130902\",\n" +
            "            \"name\": \"新华区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130903\",\n" +
            "            \"name\": \"运河区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130921\",\n" +
            "            \"name\": \"沧县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130922\",\n" +
            "            \"name\": \"青县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130923\",\n" +
            "            \"name\": \"东光县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130924\",\n" +
            "            \"name\": \"海兴县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130925\",\n" +
            "            \"name\": \"盐山县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130926\",\n" +
            "            \"name\": \"肃宁县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130927\",\n" +
            "            \"name\": \"南皮县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130928\",\n" +
            "            \"name\": \"吴桥县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130929\",\n" +
            "            \"name\": \"献县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130930\",\n" +
            "            \"name\": \"孟村回族自治县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130981\",\n" +
            "            \"name\": \"泊头市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130982\",\n" +
            "            \"name\": \"任丘市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130983\",\n" +
            "            \"name\": \"黄骅市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"130984\",\n" +
            "            \"name\": \"河间市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"131000\",\n" +
            "        \"name\": \"廊坊市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"131002\",\n" +
            "            \"name\": \"安次区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131003\",\n" +
            "            \"name\": \"广阳区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131022\",\n" +
            "            \"name\": \"固安县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131023\",\n" +
            "            \"name\": \"永清县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131024\",\n" +
            "            \"name\": \"香河县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131025\",\n" +
            "            \"name\": \"大城县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131026\",\n" +
            "            \"name\": \"文安县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131028\",\n" +
            "            \"name\": \"大厂回族自治县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131081\",\n" +
            "            \"name\": \"霸州市\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131082\",\n" +
            "            \"name\": \"三河市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"code\": \"131100\",\n" +
            "        \"name\": \"衡水市\",\n" +
            "        \"areaList\": [\n" +
            "          {\n" +
            "            \"code\": \"131102\",\n" +
            "            \"name\": \"桃城区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131103\",\n" +
            "            \"name\": \"冀州区\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131121\",\n" +
            "            \"name\": \"枣强县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131122\",\n" +
            "            \"name\": \"武邑县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131123\",\n" +
            "            \"name\": \"武强县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131124\",\n" +
            "            \"name\": \"饶阳县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131125\",\n" +
            "            \"name\": \"安平县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131126\",\n" +
            "            \"name\": \"故城县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131127\",\n" +
            "            \"name\": \"景县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131128\",\n" +
            "            \"name\": \"阜城县\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"code\": \"131182\",\n" +
            "            \"name\": \"深州市\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }]";

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

            "HKDBL0004,4901872353637,资生堂 六角眉笔 3 浅棕色 ,https://shop90485387.m.youzan.com/wscgoods/detail/2x6xiwhzi5j4j?scan=1&activity=none&from=kdt&qr=directgoods_639578391\n" +
            "HKDBL0003,4901872353620,资生堂 六角眉笔 2 深棕色,https://shop90485387.m.youzan.com/wscgoods/detail/2x6xiwhzi5j4j?scan=1&activity=none&from=kdt&qr=directgoods_639578391\n" +

            "8809540519339,8809540519339,维他命亮白安瓶面膜,https://shop90485387.m.youzan.com/wscgoods/detail/2fudlnlx0a8n7?scan=1&activity=none&from=kdt&qr=directgoods_668711492";
}
