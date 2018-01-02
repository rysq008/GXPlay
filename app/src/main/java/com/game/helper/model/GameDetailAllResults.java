package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2017/12/28.
 */

public class GameDetailAllResults extends XBaseModel {
    public GamePackageInfoResult gamePackageInfoResult;
    public H5UrlListResults h5UrlListResults;
    public MemberInfoResults memberInfoResults;

    public GameDetailAllResults(GamePackageInfoResult gamePackageInfoResult, H5UrlListResults h5UrlListResults, MemberInfoResults memberInfoResults) {
        this.gamePackageInfoResult = gamePackageInfoResult;
        this.h5UrlListResults = h5UrlListResults;
        this.memberInfoResults = memberInfoResults;
    }

    public GameDetailAllResults(GamePackageInfoResult gamePackageInfoResult, H5UrlListResults h5UrlListResults) {
        this.gamePackageInfoResult = gamePackageInfoResult;
        this.h5UrlListResults = h5UrlListResults;
    }
}
