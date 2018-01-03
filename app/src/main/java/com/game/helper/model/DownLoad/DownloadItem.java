package com.game.helper.model.DownLoad;

import io.reactivex.disposables.Disposable;
import zlc.season.practicalrecyclerview.ItemType;
import zlc.season.rxdownload2.entity.DownloadRecord;

public class DownloadItem implements ItemType {
    public Disposable disposable;
    public DownloadRecord record;

    @Override
    public int itemType() {
        return 0;
    }
}
