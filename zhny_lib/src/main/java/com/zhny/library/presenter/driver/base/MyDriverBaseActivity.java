package com.zhny.library.presenter.driver.base;

import android.graphics.Color;
import android.view.ViewGroup;

import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.utils.DisplayUtils;

/**
 * created by liming
 */
public abstract class MyDriverBaseActivity extends BaseActivity {

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    // 菜单创建器
    public SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(MyDriverBaseActivity.this).setBackgroundColor(Color.RED)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(DisplayUtils.dp2px(70))
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

}
