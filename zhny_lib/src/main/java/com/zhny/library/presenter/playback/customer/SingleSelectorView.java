package com.zhny.library.presenter.playback.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zhny.library.R;
import com.zhny.library.presenter.work.custom.wheelview.WheelPicker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * created by liming
 */
public class SingleSelectorView extends LinearLayout {

    private int selectIndex;
    private WheelPicker wheelPicker;
    private List<String> listData = new ArrayList<>();

    public SingleSelectorView(Context context) {
        super(context);
        initView();
    }

    public SingleSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SingleSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_value_select, this);
        wheelPicker = view.findViewById(R.id.wp_value_select);
        wheelPicker.setOnItemSelectedListener((picker, data, position) -> selectIndex = position);
        wheelPicker.setVisibleItemCount(3);
    }


    public void refreshData(int defaultIndex, List<String> data) {
        listData.clear();
        listData.addAll(data);
        wheelPicker.setData(listData);
        selectIndex = defaultIndex;
        wheelPicker.setSelectedItemPosition(defaultIndex, false);
    }

    public int getSelectedIndex() {
        return selectIndex;
    }

}
