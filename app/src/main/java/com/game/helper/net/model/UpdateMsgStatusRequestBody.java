package com.game.helper.net.model;

public class UpdateMsgStatusRequestBody extends BaseRequestBody {
    public static final String MESSAGE_PERSONAL = "0";
    public static final String MESSAGE_SYSTEM = "1";
    public static final String MESSAGE_OPTION_READ = "1";
    public static final String MESSAGE_OPTION_DELETE = "2";

    public String message_id;
    public String message_type;
    public String message_option;

    public UpdateMsgStatusRequestBody(String message_id,String message_type,String message_option) {
        super(1);
        this.message_id = message_id;
        this.message_type = message_type;
        this.message_option = message_option;
    }

}
