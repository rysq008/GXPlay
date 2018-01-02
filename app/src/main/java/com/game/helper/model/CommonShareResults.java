package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * 统一的分享model
 */
public class CommonShareResults extends XBaseModel {

    public String url;//分享出去的url
    public String title;//分享出去的title
    public String content;//分享出去的一句话描述
    public String logo;//分享出去的logo的url

    public CommonShareResults(String url, String title, String content, String logo) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.logo = logo;
    }

    public CommonShareResults() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int itemType() {
        return 0;
    }

}
