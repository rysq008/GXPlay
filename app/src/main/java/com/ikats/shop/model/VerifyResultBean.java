package com.ikats.shop.model;


import com.google.gson.annotations.SerializedName;
import com.ikats.shop.model.BaseModel.XBaseModel;

import java.io.Serializable;
import java.util.List;

public class VerifyResultBean extends XBaseModel {

    /**
     * id : null
     * createdDate : null
     * lastModifiedDate : null
     * version : null
     * isEnabled : null
     * isLocked : null
     * lockDate : null
     * lastLoginIp : null
     * lastLoginDate : null
     * socialUsers : []
     * paymentTransactions : []
     * auditLogs : []
     * fromMessages : []
     * toMessages : []
     * user1MessageGroups : []
     * user2MessageGroups : []
     * username : null
     * password : null
     * encodedPassword : null
     * email : 13121528060@163.com
     * mobile : 13121528060
     * point : null
     * balance : null
     * frozenAmount : null
     * amount : null
     * name : 罗松松
     * idCard : 410222198706134038
     * gender : null
     * birth : null
     * address : null
     * zipCode : null
     * phone : null
     * attributeValue0 : null
     * attributeValue1 : null
     * attributeValue2 : null
     * attributeValue3 : null
     * attributeValue4 : null
     * attributeValue5 : null
     * attributeValue6 : null
     * attributeValue7 : null
     * attributeValue8 : null
     * attributeValue9 : null
     * safeKey : null
     * area : null
     * memberRank : null
     * distributor : null
     * cart : null
     * orders : []
     * memberDepositLogs : []
     * couponCodes : []
     * receivers : [{"id":204,"createdDate":1597912675000,"lastModifiedDate":1597912675000,"version":0,"consignee":"球球","areaName":"天津市河东区","address":"龙腾小区","zipCode":"000000","phone":"13121528060","isDefault":false,"area":null,"member":null,"new":false}]
     * reviews : []
     * consultations : []
     * productFavorites : []
     * storeFavorites : []
     * productNotifies : []
     * pointLogs : []
     * aftersales : []
     * availableBalance : 0
     * credentials : null
     * isDistributor : false
     * principal : null
     * displayName : null
     * new : true
     */

    public String id;
    public String createdDate;
    public String lastModifiedDate;
    public String version;
    public String isEnabled;
    public String isLocked;
    public String lockDate;
    public String lastLoginIp;
    public String lastLoginDate;
    public String username;
    public String password;
    public String encodedPassword;
    public String email;
    public String mobile;
    public String point;
    public String balance;
    public String frozenAmount;
    public String amount;
    public String name;
    public String idCard;
    public String gender;
    public String birth;
    public String address;
    public String zipCode;
    public String phone;
    public String attributeValue0;
    public String attributeValue1;
    public String attributeValue2;
    public String attributeValue3;
    public String attributeValue4;
    public String attributeValue5;
    public String attributeValue6;
    public String attributeValue7;
    public String attributeValue8;
    public String attributeValue9;
    public String safeKey;
    public String area;
    public String memberRank;
    public String distributor;
    public String cart;
    public int availableBalance;
    public String credentials;
    public boolean isDistributor;
    public String principal;
    public String displayName;
    @SerializedName("new")
    public boolean newX;
    public List<?> socialUsers;
    public List<?> paymentTransactions;
    public List<?> auditLogs;
    public List<?> fromMessages;
    public List<?> toMessages;
    public List<?> user1MessageGroups;
    public List<?> user2MessageGroups;
    public List<?> orders;
    public List<?> memberDepositLogs;
    public List<?> couponCodes;
    public List<ReceiversBean> receivers;
    public List<?> reviews;
    public List<?> consultations;
    public List<?> productFavorites;
    public List<?> storeFavorites;
    public List<?> productNotifies;
    public List<?> pointLogs;
    public List<?> aftersales;

    @Override
    public int itemType() {
        return 0;
    }


    public static class ReceiversBean implements Serializable {
        /**
         * id : 204
         * createdDate : 1597912675000
         * lastModifiedDate : 1597912675000
         * version : 0
         * consignee : 球球
         * areaName : 天津市河东区
         * address : 龙腾小区
         * zipCode : 000000
         * phone : 13121528060
         * isDefault : false
         * area : null
         * member : null
         * new : false
         */

        public int id;
        public long createdDate;
        public long lastModifiedDate;
        public int version;
        public String consignee;
        public String areaName;
        public String address;
        public String zipCode;
        public String phone;
        public boolean isDefault;
        public String area;
        public String member;
        @SerializedName("new")
        public boolean newX;
    }
}
