package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * 不关心结果的result／只需要判断code = 0000
 * */
public class FeedbackListResults extends XBaseModel {
/*
{
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "reply_time": "",
                "feedback_time": "2017-11-30 15:44:52",
                "id": 2,
                "content": "1",
                "member_id": 1,
                "is_solved": 2,
                "reply_content": ""
            },
            {
                "reply_time": "",
                "feedback_time": "2017-11-14 18:17:39",
                "id": 1,
                "content": "你们这个平台不错哦",
                "member_id": 1,
                "is_solved": 1,
                "reply_content": ""
            }
        ]
    }
}
* */
    @SerializedName("list")
    public List<FeedbackItem> list;

    public static class FeedbackItem implements ItemType{
/*
*{
                "reply_time": "",
                "feedback_time": "2017-11-30 15:44:52",
                "id": 2,
                "content": "1",
                "member_id": 1,
                "is_solved": 2,
                "reply_content": ""
            },
* */
        public String reply_time;
        public String feedback_time;
        public int id;
        public String content;
        public int member_id;
        public int is_solved;
        public String reply_content;

        @Override
        public int itemType() {
            return 0;
        }
    }
    @Override
    public int itemType() {
            return 0;
        }
}
