package com.game.helper.data;

/**
 * Created by zr on 2017-10-13.
 * 常量类
 */

public final class RxConstant {

    public static final int Head_Image_Change_Type = 40;
    public static final int Login_Type = 200;

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
    }

    public static final class MineModeType {//游戏推广数据类型
        public static final int Mine_Balance_Wallet_type = 30;//钱包余额
    }

}
