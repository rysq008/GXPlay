package com.game.helper.net.model;

public class ReportedRequestBody extends BaseRequestBody {
    //    参数名	必选	类型	说明
//    game_id	是	int	游戏id号
//    channel_id	否	int	渠道id号
//    plat_id	否	int	平台id号
    public int game_id;
    public int channel_id;
    public int plat_id;

    public ReportedRequestBody(int gid, int cid, int pid) {
        super(1);
        game_id = gid;
        channel_id = cid;
        plat_id = pid;
    }
}
