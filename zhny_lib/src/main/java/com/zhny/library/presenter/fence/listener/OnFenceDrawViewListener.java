package com.zhny.library.presenter.fence.listener;

/**
 * created by liming
 */
public interface OnFenceDrawViewListener {

    /**
     * 添加点
     */
    void onDrawPoint();

    /**
     * 删除点
     */
    void onBackPoint();

    /**
     * 重绘制
     */
    void onClearAll();

    /**
     * 恢复
     */
    void onReset();

}
