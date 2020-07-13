package com.zhny.zhny_app.fragments.BaseFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;
import com.zhny.zhny_app.R;
import com.zhny.zhny_app.model.BaseModel.XBaseModel;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XFragment;

public abstract class XBaseFragment<P extends IPresent> extends XFragment<P> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimary));
    }
    
    /**
     * @param data
     * @param loadMore
     * @desc 页面内容处理
     */
    public void showContent(XBaseModel data, boolean loadMore){}

    /**
     * @param tips
     * @desc 空数据处理
     */
    public void showEmpty(String tips){}

    /**
     * @param code  错误码
     * @param error 异常
     * @desc 异常处理
     */
    public void showError(int code, Exception error){}
}
