package com.game.helper.net.model;

import java.io.File;

public class FeedbackRequestBody extends BaseRequestBody {
    public String content;

    public FeedbackRequestBody(String content) {
        super(1);
        this.content = content;
    }
}
