package com.game.helper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.helper.R;

import java.util.List;

/**
 * Created by sung on 2017/11/20.
 * <p>
 * 需要改item的背景色 请修改@drawable/bg_gradient_radius_gray
 */
public class SearchHistoryItemView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = SearchHistoryItemView.class.getSimpleName();
    private OnHistoryItemClickListener onHistoryItemClickListener;

    private LinearLayout mLinesContainer;
    private List<String> mData;
    private Context context;

    private int last_lines = 0;
    private int current_position = 0;

    public SearchHistoryItemView(Context context, List mData) {
        super(context);
        this.mData = mData;
        this.context = context;
        initView();
    }

    public SearchHistoryItemView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SearchHistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public SearchHistoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        this.setGravity(Gravity.CENTER);
        this.setOrientation(VERTICAL);

        if (context == null || mData == null || mData.size() == 0)
            return;
        /* 单行计算宽度 */
        int singleLinesWith = 0;
        /* 屏幕宽度（限制宽度） */
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        for (int i = 0; i < mData.size(); i++) {
            /* 事先初始化一行的容器 */
            if (mLinesContainer == null) {
                mLinesContainer = new LinearLayout(context);
                this.addView(mLinesContainer, new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            /* 获取单个item宽度 */
            View item = getSingleItem(i);
            /* view未展示之前先计算 */
            int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            item.measure(width, height);
            /* 计算了再获取实际的宽度 */
            int itemWidth = item.getMeasuredWidth();

            /* 如果当前item超过屏幕宽度 直接添加 return */
            if (itemWidth >= screenWidth) {
                mLinesContainer = new LinearLayout(context);
                this.addView(mLinesContainer, new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mLinesContainer.addView(item);

                current_position++;
                last_lines++;
                mLinesContainer = null;
                singleLinesWith = 0;
                continue;
            }
            singleLinesWith = singleLinesWith + itemWidth;
            /* 如果添加一个item没有超过屏幕宽度的话直接add */
            if (singleLinesWith < screenWidth) {
                mLinesContainer.addView(item);
                /* 已在显示的个数+1 */
                current_position++;
            }
            /* 如果添加一个item超出了屏幕宽度就置空 for循环 */
            else {
                mLinesContainer = null;
                singleLinesWith = 0;
                /* 行数记录+1 */
                last_lines++;
            }
        }

        Log.e(TAG, "init result: total lines-" + last_lines + "\t\tposition-" + current_position);
    }

    private View getSingleItem(int position) {
        if (context == null || mData == null || mData.size() == 0)
            return null;

        /* 获取单个item */
        View view = LayoutInflater.from(context).inflate(R.layout.layout_search_item, null, false);
        TextView text = view.findViewById(R.id.tv_search_text);
        text.setText(mData.get(position));
        view.setTag(mData.get(position));
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof View) {
            if (v.getTag() != null) {
                String text = (String) v.getTag();
                if (onHistoryItemClickListener != null) {
                    onHistoryItemClickListener.onHistoryItemClick(text);
                }
            }
        }
    }

    public interface OnHistoryItemClickListener {
        void onHistoryItemClick(String text);
    }


    // TODO: 2017/11/20 public method

    /**
     * 监听器
     */
    public void setOnHistoryItemClickListener(OnHistoryItemClickListener onHistoryItemClickListener) {
        this.onHistoryItemClickListener = onHistoryItemClickListener;
    }

    /**
     * 清除所有数据
     */
    public void clearData() {
        last_lines = 0;
        current_position = 0;

        if (mData == null || mLinesContainer == null)
            return;
        mData.clear();
        mLinesContainer.removeAllViews();
        mData = null;
        mLinesContainer = null;
    }

    /**
     * 重设所有数据
     */
    public void resetData(List mData) {
        clearData();
        this.mData = mData;
        initView();
    }

    public int getTotalLines() {
        return last_lines + 1;
    }

    public int getToatalPosition() {
        return mData.size();
    }
}
