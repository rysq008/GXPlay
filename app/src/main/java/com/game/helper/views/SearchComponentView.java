package com.game.helper.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.SearchFragment;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by zr on 2017-10-13.
 */

public class SearchComponentView extends LinearLayout {

    @BindView(R.id.common_search_left_layout)
    RelativeLayout layout_left;
    @BindView(R.id.common_search_back_iv)
    ImageView iv_back;
    @BindView(R.id.common_search_left_iv)
    HeadImageView iv_left;
    @BindView(R.id.common_search_right_tv)
    TextView tv_right;
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
        String center_res_str = typedArray.getString(R.styleable.CommonSearchView_center_et_res);
        Drawable right_res_id_img = typedArray.getDrawable(R.styleable.CommonSearchView_right_tv_res_img);
        String right_res_id_txt = typedArray.getString(R.styleable.CommonSearchView_right_tv_res_txt);
        int color = typedArray.getColor(R.styleable.CommonSearchView_right_tv_res_color, Color.WHITE);
        boolean isSearchpage = typedArray.getBoolean(R.styleable.CommonSearchView_is_search_page, false);
        typedArray.recycle();

        inflate(context, R.layout.common_search_layout, this);
        KnifeKit.bind(this);
        if (is_back) {
            iv_back.setVisibility(VISIBLE);
            iv_left.setVisibility(GONE);
            layout_left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).onBackPressed();
                    }
                }
            });
        } else {
            iv_left.setVisibility(VISIBLE);
            iv_back.setVisibility(GONE);
        }
        et_center.setHint(Kits.Empty.check(center_res_str) ? "" : center_res_str);

        tv_right.setBackgroundDrawable(right_res_id_img);
        tv_right.setText(right_res_id_txt);
        tv_right.setTextColor(color);
        if (!isSearchpage) {
            et_center.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        DetailFragmentsActivity.launch(getContext(), SearchFragment.newInstance());
                    }
                    return false;
                }
            });
        }
    }

    public void setLeftViewOnClick(OnClickListener listener) {
        if (null != listener && !iv_back.isShown()) {
            layout_left.setOnClickListener(listener);
        }
    }

    public void setRightViewOnClick(OnClickListener listener) {
        if (null != listener) {
            tv_right.setOnClickListener(listener);
        }
    }

    public void setCenterViewOnClick(TextWatcher textWatcher) {
        if (null != textWatcher) {
            et_center.addTextChangedListener(textWatcher);
        }
    }
}