package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class LoginResults extends XBaseModel {

    /**
     * {
     * "message": "成功",
     * "code": "0000",
     * "data": {
     * "has_trade_passwd": true,
     * "phone": "13312341234",
     * "has_passwd": true,
     * "has_alipay_account": true,
     * "member_id": 1,
     * "icon": "/media/touxiang/1_icon_crop_FLVIHPp.jpg"
     * }
     * }
     * 参数名	类型	说明
     * phone	string	手机号
     * icon	string	用户头像
     * member_id	int	账号id
     * has_passwd	bool	是否有密码
     * has_trade_passwd	bool	是否有交易密码
     * has_alipay_account	bool	是否有支付宝账号
     */
    public String phone;
    public int member_id;
    public String icon;
    public boolean has_passwd = false;
    public boolean has_trade_passwd = false;
    public boolean has_alipay_account = false;

    @Override
    public int itemType() {
        return RxConstant.Login_Type;
    }
}
