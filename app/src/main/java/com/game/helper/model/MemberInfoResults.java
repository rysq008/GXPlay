package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class MemberInfoResults extends XBaseModel {
    //        {
//            "message": "成功",
//                "code": "0000",
//                "data": {
//            "icon_thumb": "",
//                    "nick_name": "这个昵称很好",
//                    "gender": "2",
//                    "phone": "13312341234",
//                    "vip_level": {
//                "image": "",
//                        "level": 1,
//                        "name": "",
//                        "descs": "享受vip充值折扣待遇"
//            },
//            "birthday": "1991-03-02",
//                    "signature": "",
//                    "address_count": 0,
//                    "balance": "920.00",
//                    "icon": ""
//        }
//        }
    public String icon_thumb;
    public String nick_name;
    public String gender;
    public String phone;
    //                        "image": "",
//                        "level": 1,
//                        "name": "",
//                        "descs": "享受vip充值折扣待遇"
    public VipLevel vip_level;
    public String birthday;
    public String signature;
    public int address_count;
    public String balance;
    public String market_balance;
    public String total_balance;
    public String icon;

    public String getIcon_thumb() {
        return icon_thumb;
    }

    public void setIcon_thumb(String icon_thumb) {
        this.icon_thumb = icon_thumb;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public VipLevel getVip_level() {
        return vip_level;
    }

    public void setVip_level(VipLevel vip_level) {
        this.vip_level = vip_level;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getAddress_count() {
        return address_count;
    }

    public void setAddress_count(int address_count) {
        this.address_count = address_count;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMarket_balance() {
        return market_balance;
    }

    public void setMarket_balance(String market_balance) {
        this.market_balance = market_balance;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public class VipLevel extends XBaseModel {
        public String image;
        public String level;
        public String name;
        public String descs;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescs() {
            return descs;
        }

        public void setDescs(String descs) {
            this.descs = descs;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        @Override
        public int itemType() {
            return RxConstant.AccountModeType.Account_Member_Info_type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.AccountModeType.Account_Member_Info_type;
    }
}
