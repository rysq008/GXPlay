package com.zhny.library.common;

/**
 * 全局常量
 */
public interface Constant {

    /**
     * todo: 配置1： 授权方式配置
     */
    boolean IS_LIB_TOKEN = true; // true:壳子打包；false:内部打包


    /**
     * 参数配置
     *      0、lib或独立app授权
     *      1、不同环境服务器地址
     *      2、不同环境webSocket地址
     *      3、lib app不同环境secret
     *      4、独立app不同环境secret
     */
    interface Server {

        //******************【参数配置区域】**************************************************/

        /**
         * todo : 配置2： 农机（设备）监控 webSocket 地址配置
         */
//        String WEB_SOCKET_URL = "http://119.3.169.97:30090/alarm/websocket/";//测试
        String WEB_SOCKET_URL = "https://an.mapfarm.com:8920/alarm/websocket/";//生产

        /**
         * todo: 配置3: 服务器地地址配置
         */
//        String BASE_URL = "http://119.3.169.97:30088/";//测试
        String BASE_URL = "https://an.mapfarm.com:8080/";//生产

        /**
         * CLIENT_MAP_SECRET  todo: 配置4: lib app 不同环境 CLIENT_MAP_SECRET 参数配置
         */
//        String CLIENT_MAP_SECRET = "map-secret"; // lib app 测试
        String CLIENT_MAP_SECRET = "6443909d52d211e9bc9cc7b624380327"; //lib app 生产



        //******************【参数配置区域】**************************************************/

        /**
         * CLIENT_ID
         */
        String CLIENT_MAP_ID = "map-client";//lib app
        String CLIENT_ID = "client"; // 独立app
        String CLIENT_SECRET = "secret"; // 独立app

        /**
         * 授权码模式
         */
        String GRANT_TYPE = "password";


        //服务器超时时间 24 秒
        int TIME_OUT = 24;

        String USER_NOT_ACTIVE = "lib_user_not_active";
    }
    /**
     * 服务器返回的状态码
     */
    interface RespCode {
        //处理成功
        String R000 = "10000";
    }

    /**
     * sp 常量
     */
    interface SP {
        //勿改 对接系统配置 sp文件名
        String SP_NAME = "zhny_library_sp";
        //勿改 对接系统配置
        String TOKEN = "zhny_library_token";
        //获取登陆用户名
        String LOGIN_USERNAME = "zhny_library_login_username";


        //设置犁地作业标准值
        String WORK_TRACK_SETTING_PLOWING_STANDARD_VALUE = "work_track_setting_plowing_standard_value";
        int WORK_TRACK_SETTING_PLOWING_STANDARD = 35;
        //设置犁地作业浮动值
        String WORK_TRACK_SETTING_PLOWING_FLOAT_VALUE = "work_track_setting_plowing_float_value";
        int WORK_TRACK_SETTING_PLOWING_FLOAT = 5;

        //无效值
        String WORK_INVALID_VALUE = "work_invalid_value";
        int WORK_INVALID = 20;

        //设置是否显示图片
        String WORK_TRACK_OPEN_PICTURE = "work_track_open_picture";
        boolean WORK_TRACK_OPEN_PICTURE_DEFAULT = false;

        //设置是否显示幅宽
        String WORK_TRACK_OPEN_WIDTH = "work_track_open_width";
        boolean WORK_TRACK_OPEN_WIDTH_DEFAULT = true;
    }

    /**
     * 全局常量
     */
    interface FINALVALUE {
        String USER_ID = "userid";
        String USERNAME = "username";
        String PASSWORD = "password";
        String ORGANIZATION_ID = "organizationId";



        String WORKING = "working";
        String RUNNING = "running";
        String OFF_LINE = "unline";


        // 显示作业质量深度的作业类型
        String DEEP_OF_WORK_TYPE = "1,2"; // 1 深翻作业  2 耕地作业
        String DEEP_OF_WORK_TYPE_CODE = "SHOW_DEPTH_JOB_TYPE"; // 快码

        // 轨迹离线超时时间
        int OFF_LINE_SECOND = 300;//离线间隔时间(默认300s)
        String OFF_LINE_SECOND_CODE = "ZHNY.PUBLIC_CONFIG"; //快码

        //进度条总长度 秒
        int SECOND_SINGLE_DAY = 86400;

        //0.5X
        float SPEED_TIME_DOUBLE = 83.3f; //1000f / 12
        //1.0X
        float SPEED_TIME = 41.7f; //1000f / 24
        //2.0X
        float SPEED_TIME_HALF = 20.8f;//1000f / 48
        //4.0X
        float SPEED_TIME_FOUR_HALF = 10.4f;//1000f / 96

        //range calendar first date
        String RANGE_CALENDAR_DATE = "2020-01-01";

        //地块距离轨迹中心距离（用于计算要展示的地块）
        int DEVICE_DISTANCE = 2500;

    }


}
