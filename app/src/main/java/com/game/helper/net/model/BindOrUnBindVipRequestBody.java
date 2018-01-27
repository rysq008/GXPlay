package com.game.helper.net.model;

public class BindOrUnBindVipRequestBody extends BaseRequestBody {
    public int game_account_id;//	游戏账号id号
    public Boolean bind_vip;//bool	true表示绑定vip，false表示解绑vip

    public BindOrUnBindVipRequestBody(int game_account_id, Boolean bool) {
        super(1);
        this.game_account_id = game_account_id;
        this.bind_vip = bool;
    }
}
