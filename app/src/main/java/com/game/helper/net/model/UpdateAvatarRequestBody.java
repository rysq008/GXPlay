package com.game.helper.net.model;

import java.io.File;

public class UpdateAvatarRequestBody extends BaseRequestBody {
    public File image;

    public UpdateAvatarRequestBody(File image) {
        super(1);
        this.image = image;
    }
}
