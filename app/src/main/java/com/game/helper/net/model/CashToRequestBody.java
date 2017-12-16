package com.game.helper.net.model;

public class CashToRequestBody extends BaseRequestBody {
    private String memberId;
    private String amount;
    private String isAccount;
    private String tradePassword;

    public CashToRequestBody(String memberId, String amount, String isAccount, String tradePassword) {
        super(1);
        this.memberId = memberId;
        this.amount = amount;
        this.isAccount = isAccount;
        this.tradePassword = tradePassword;
    }
}
