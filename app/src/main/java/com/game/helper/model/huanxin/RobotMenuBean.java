package com.game.helper.model.huanxin;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

/**
 * Created by Tian on 2018/1/12.
 */

public class RobotMenuBean extends XBaseModel{

    /**
     * msg : {"type":"txt","msg":" "}
     * ext : {"msgtype":{"choice":{"items":[{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}],"title":"列表的标题"}}}
     */

    private MsgBean msg;
    private ExtBean ext;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public ExtBean getExt() {
        return ext;
    }

    public void setExt(ExtBean ext) {
        this.ext = ext;
    }

    public static class MsgBean {
        /**
         * type : txt
         * msg :
         */

        private String type;
        private String msg;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class ExtBean {
        /**
         * msgtype : {"choice":{"items":[{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}],"title":"列表的标题"}}
         */

        private MsgtypeBean msgtype;

        public MsgtypeBean getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(MsgtypeBean msgtype) {
            this.msgtype = msgtype;
        }

        public static class MsgtypeBean {
            /**
             * choice : {"items":[{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}],"title":"列表的标题"}
             */

            private ChoiceBean choice;

            public ChoiceBean getChoice() {
                return choice;
            }

            public void setChoice(ChoiceBean choice) {
                this.choice = choice;
            }

            public static class ChoiceBean {
                /**
                 * items : [{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}]
                 * title : 列表的标题
                 */

                private String title;
                private List<ItemsBean> items;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public List<ItemsBean> getItems() {
                    return items;
                }

                public void setItems(List<ItemsBean> items) {
                    this.items = items;
                }

                public static class ItemsBean {
                    /**
                     * id : xxx
                     * name : 1.列表内容
                     */

                    private String id;
                    private String name;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }
        }
    }
}
