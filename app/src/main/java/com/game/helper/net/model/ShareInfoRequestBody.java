package com.game.helper.net.model;

public class ShareInfoRequestBody extends BaseRequestBody {
//    share_type	int	分享推广类型 1为分享收益 3为分享二维码 默认为1

    public int share_type;

    public ShareInfoRequestBody(int type) {
        super(1);
        share_type = type;
    }
}
