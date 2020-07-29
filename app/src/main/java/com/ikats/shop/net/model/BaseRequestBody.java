package com.ikats.shop.net.model;

import java.io.Serializable;

public class BaseRequestBody implements Serializable {
    public int page;
    public int plat_id = 1;//平台 1android，2ios

    public BaseRequestBody(int page) {
        this.page = page;
    }

    public static class Builder {
        public int page;

        public Builder(int page) {
            this.page = page;
        }

        public Builder setPage(int page) {
            this.page = page;
            return this;
        }

        public BaseRequestBody builder() {
            return new BaseRequestBody(page);
        }
    }
}
