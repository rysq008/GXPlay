package com.game.helper.net.model;

public class UploadShareInfoRequestBody extends BaseRequestBody {
    public int share_type;//事件类型
    public int share_way;//分享方式

    public UploadShareInfoRequestBody(int ety, int sty) {
        super(1);
        this.share_type = ety;
        this.share_way = sty;
    }
}
