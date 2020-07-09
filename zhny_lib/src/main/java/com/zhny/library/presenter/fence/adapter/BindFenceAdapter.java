package com.zhny.library.presenter.fence.adapter;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhny.library.R;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

/**
 * created by liming
 */
@BindingMethods({
        @BindingMethod(type = ImageView.class, attribute = "checkTypeBg", method = "bindCheckTypeBg"),
        @BindingMethod(type = TextView.class, attribute = "selectColor", method = "bindSelectColor")
})
public class BindFenceAdapter {


    @BindingAdapter("selectColor")
    public static void bindSelectColor(TextView view, boolean selected) {
        view.setTextColor(Color.parseColor(selected ? "#009688" : "#666666"));
    }

    @BindingAdapter("checkTypeBg")
    public static void bindCheckTypeBg(ImageView view, int checkType) {
        int checkResId;
        if (checkType == 0) checkResId = R.drawable.icon_checkbox_null;
        else if (checkType == 1) checkResId = R.drawable.icon_checkbox;
        else checkResId = R.drawable.icon_checkbox_forbidden;
        view.setImageResource(checkResId);
    }



}
