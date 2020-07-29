package com.ikats.shop.dialog;

import java.io.IOException;

public interface IDialogResultListener<T> {
    void onDataResult(T result) throws IOException;
}