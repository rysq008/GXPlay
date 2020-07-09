package com.zhny.library.presenter.work.custom;

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
public class ValueSelectorView extends LinearLayout {

    private static final String CM = " CM";

    private int selectValue;
    private WheelPicker wheelPicker;
    private List<String> listData = new ArrayList<>();

    public ValueSelectorView(Context context) {
        super(context);
        initView();
    }

    public ValueSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ValueSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_value_select, this);
        wheelPicker = view.findViewById(R.id.wp_value_select);

        wheelPicker.setOnItemSelectedListener((picker, data, position) ->
                selectValue = Integer.valueOf(listData.get(position).replace(CM, "")));
    }



    public void refreshData(int min, int max, int defaultValue) {
        listData.clear();
        for (int i = min; i <= max; i++) {
            listData.add(i + CM);
        }
        wheelPicker.setData(listData);

        this.selectValue = defaultValue;
        wheelPicker.setSelectedItemPosition(listData.indexOf(defaultValue+ CM), false);
    }

    public int getSelectedValue() {
        return selectValue;
    }




}
