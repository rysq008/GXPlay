package com.game.helper.model;

import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2018/1/2.
 */

public class UserInfoAndVipLevelResults extends XBaseModel {
   public VipLevelResults vipLevelResults;
   public MemberInfoResults memberInfoResults;

   public UserInfoAndVipLevelResults(VipLevelResults vipLevelResults, MemberInfoResults memberInfoResults) {
      this.vipLevelResults = vipLevelResults;
      this.memberInfoResults = memberInfoResults;
   }

}
