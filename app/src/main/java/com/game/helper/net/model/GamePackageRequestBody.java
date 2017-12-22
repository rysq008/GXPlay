package com.game.helper.net.model;

/**
 * Created by Tian on 2017/12/22.
 * 获取游戏安装包列表
 */

public class GamePackageRequestBody extends BaseRequestBody {
    public int game_id;
    public int plat_id;
    public int page;
    public GamePackageRequestBody(int page,int gameId,int platId) {
        super(page);
        game_id = gameId;
        plat_id = platId;
    }
}
