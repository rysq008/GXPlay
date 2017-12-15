package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class ChannelListResultModel extends XBaseModel {


    private List<ListBean> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements ItemType {

        private String channel_name;
        private int channel_id;

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        @Override
        public int itemType() {
            return 0;
        }

    }

}
