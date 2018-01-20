package com.game.helper.net.model;

public class ConsumeRequestBody extends BaseRequestBody {

    public String gameAccountId;//游戏帐号ID
    public String consumeAmount;//消费金额
    public String accountAmount;//使用账户金额 （未使用传0）
    public String marketingAmount;//使用推广账户金额（未使用传0）
    public String rechargeAmount;//充值金额（未使用传0）
    public String is_vip;//是否vip充值
    public String tradePassword;//交易密码
    public String payWay;//交易密码
    public String redpacketType;//	红包分类（0:没有使用红包，1：单发，2：群发）
    public String redpacketId;//红包ID （redpacketType不为0时为必选，redpacketType为1时，redpacketId = 我的红包Id; 为2时，redpacketId = 群发红包ID）

    public ConsumeRequestBody(String gameAccountId, String consumeAmount, String accountAmount,
                              String marketingAmount, String rechargeAmount, String is_vip,
                              String tradePassword, String redpacketType, String redpacketId,String payWay
    ) {
        super(1);
        this.gameAccountId = gameAccountId;
        this.consumeAmount = consumeAmount;
        this.accountAmount = accountAmount;
        this.marketingAmount = marketingAmount;
        this.rechargeAmount = rechargeAmount;
        this.is_vip = is_vip;
        this.tradePassword = tradePassword;
        this.redpacketType = redpacketType;
        this.redpacketId = redpacketId;
        this.payWay = payWay;
    }

}
