package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class IncomeResultModel extends XBaseModel {


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

        private RecommendedBean recommended;
        private String type;
        private String reward;
        private String create_time;

        public RecommendedBean getRecommended() {
            return recommended;
        }

        public void setRecommended(RecommendedBean recommended) {
            this.recommended = recommended;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class RecommendedBean {

            private String nick_name;

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }
        }
    }

}
