package com.game.helper.net.model;

public class UnAvailableRedpackRequestBody extends BaseRequestBody {
    private String type;
    public static final String Type_Out_Time = "expired";
    public static final String Type_Used_Coupon = "used";

    public UnAvailableRedpackRequestBody(int page, String type) {
        super(page);
        this.type = type;
    }

}
