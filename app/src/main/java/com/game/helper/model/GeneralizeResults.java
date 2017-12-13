package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.model.MemberBean;

public class GeneralizeResults extends XBaseModel {

    public String yue_history;// 0.00,
    public String yujizongshouyi;// 200.00,
    public String url;// http;////127.0.0.1;//8000/?market_id=2&market_num=10000&signature=081425df59351e65f96ea0ee1f6fe8b4,
    public int viprenshu;// 0,
    public String zongshouyi;// 0.00,
    public MemberBean member;// { nick_name;// 这个昵称很好,id;// 1,icon;// },
    public int total_promotion_number;// 4,
    public String num;// 10000,
    public String dongjie;// 0.00,
    public String yue;// 0.00,
    public int id;// 2

    public String getYue_history() {
        return yue_history;
    }

    public void setYue_history(String yue_history) {
        this.yue_history = yue_history;
    }

    public String getYujizongshouyi() {
        return yujizongshouyi;
    }

    public void setYujizongshouyi(String yujizongshouyi) {
        this.yujizongshouyi = yujizongshouyi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViprenshu() {
        return viprenshu;
    }

    public void setViprenshu(int viprenshu) {
        this.viprenshu = viprenshu;
    }

    public String getZongshouyi() {
        return zongshouyi;
    }

    public void setZongshouyi(String zongshouyi) {
        this.zongshouyi = zongshouyi;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public int getTotal_promotion_number() {
        return total_promotion_number;
    }

    public void setTotal_promotion_number(int total_promotion_number) {
        this.total_promotion_number = total_promotion_number;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDongjie() {
        return dongjie;
    }

    public void setDongjie(String dongjie) {
        this.dongjie = dongjie;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int itemType() {
        return RxConstant.GeneralizeModeType.Generalize_Balance_Account_Info_type;
    }

}
