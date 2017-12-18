package com.game.helper.net.model;

public class ResetAlipayRequestBody extends BaseRequestBody {

    public String apliy_account;
    public String real_name;
    public String idno_six;
    public String code;
    public String old_apliy_account;

    public ResetAlipayRequestBody(String apliy_account, String real_name, String idno_six, String code, String old_apliy_account) {
        super(1);
        this.apliy_account = apliy_account;
        this.real_name = real_name;
        this.idno_six = idno_six;
        this.code = code;
        this.old_apliy_account = old_apliy_account;
    }
}
