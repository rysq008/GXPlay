package com.game.helper.net.model;

public class SearchRequestBody extends BaseRequestBody {

    public String word;

    public SearchRequestBody(int page, String word) {
        super(page);
        this.word = word;
    }
}