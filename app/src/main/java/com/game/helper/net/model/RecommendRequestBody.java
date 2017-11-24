package com.game.helper.net.model;

public class RecommendRequestBody extends BaseRequestBody {
    public Integer class_type_id;//	否	int	游戏经典分类id 如单机 网游等
    public Integer type_id;//	否	int	游戏分类id 如卡牌 策略等

    public RecommendRequestBody(Integer page, int class_type_id, int type_id) {
        super(page);
        this.class_type_id = class_type_id;
        this.type_id = type_id;
    }
}
