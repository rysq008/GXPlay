package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

public class AllAccountsResultsModel extends XBaseModel {

    public String total_flow;
    public String total_consume;
    public String yue;
    public String total_reflect;
    public String balance;
    public String total;
    public String total_recharge;

    public String getTotal_flow() {
        return total_flow;
    }

    public void setTotal_flow(String total_flow) {
        this.total_flow = total_flow;
    }

    public String getTotal_consume() {
        return total_consume;
    }

    public void setTotal_consume(String total_consume) {
        this.total_consume = total_consume;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public String getTotal_reflect() {
        return total_reflect;
    }

    public void setTotal_reflect(String total_reflect) {
        this.total_reflect = total_reflect;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal_recharge() {
        return total_recharge;
    }

    public void setTotal_recharge(String total_recharge) {
        this.total_recharge = total_recharge;
    }

    @Override
    public int itemType() {
            return 0;
        }
}
