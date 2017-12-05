package com.game.helper.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.game.helper.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by zr on 2017-10-13.
 */

public class SearchComponentView extends LinearLayout {


    @BindView(R.id.common_search_back_iv)
    ImageView iv_back;
    @BindView(R.id.common_search_left_iv)
    HeadImageView iv_left;
    @BindView(R.id.common_search_right_iv)
    ImageView iv_right;
    @BindView(R.id.common_search_center_et)
    EditText et_center;

    static final int RES_NONE = -1;

    public SearchComponentView(Context context) {
        super(context);
        setupView(context, null);
    }

    public SearchComponentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    public SearchComponentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context, attrs);
    }

    private void setupView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonSearchView);
        boolean is_back = typedArray.getBoolean(R.styleable.CommonSearchView_is_back, false);
        int left_res_id = typedArray.getResourceId(R.styleable.CommonSearchView_left_iv_res, RES_NONE);
        int center_res_id = typedArray.getResourceId(R.styleable.CommonSearchView_cen_et_res, RES_NONE);
        int right_res_id = typedArray.getResourceId(R.styleable.CommonSearchView_right_tv_res, RES_NONE);
        typedArray.recycle();

        inflate(context, R.layout.common_search_layout, this);
        KnifeKit.bind(this);
        if (is_back) {
            iv_back.setVisibility(VISIBLE);
            iv_left.setVisibility(GONE);
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).onBackPressed();
            }
        } else {
            iv_left.setVisibility(VISIBLE);
            iv_back.setVisibility(GONE);
        }
    }

    public void setLeftViewOnClick(OnClickListener listener) {
        if (null != listener) {
            iv_left.setOnClickListener(listener);
        }
    }

    public void setRightViewOnClick(OnClickListener listener) {
        if (null != listener) {
            iv_right.setOnClickListener(listener);
        }
    }

    public void setCenterViewOnClick(TextWatcher textWatcher) {
        if (null != textWatcher) {
            et_center.addTextChangedListener(textWatcher);
        }
    }
}