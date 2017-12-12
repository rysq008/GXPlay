package com.game.helper.net.model;

public class PayRequestBody extends BaseRequestBody {

    public String memberId;//用户ID
    public String amount;//金额（单位:元）
    public String payWay;//支付方式（1：支付宝、2：微信）
    public String payPurpose;//支付用途（1：普通充值、2：充值会员）
    public String vipLevel;//预充值的VIP等级 （充值VIP时不能为空）

    public PayRequestBody(String memberId, String amount, String payWay, String payPurpose, String vipLevel) {
        super(1);
        this.memberId = memberId;
        this.amount = amount;
        this.payWay = payWay;
        this.payPurpose = payPurpose;
        this.vipLevel = vipLevel;
    }

}
