package com.game.helper.views.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.game.helper.R;

import java.lang.ref.WeakReference;

public class CustomBadgeItem extends BadgeItem {

    private WeakReference<TextView> mTextViewRef;
    private boolean isPoint = false;

    public CustomBadgeItem setPoint(boolean isPoint) {
        this.isPoint = isPoint;
        return this;
    }

    public void refreshDrawable() {
        if (isWeakReferenceValid()) {
            TextView textView = mTextViewRef.get();

            if (isPoint) {
                FrameLayout.LayoutParams layoutParams =
                        (FrameLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.gravity = this.getGravity();
                layoutParams.width = 20;
                layoutParams.height = 20;
                textView.setLayoutParams(layoutParams);
                textView.setBackgroundDrawable(getBadgePointerDrawable(this, textView.getContext()));
            } else {
                FrameLayout.LayoutParams layoutParams =
                        (FrameLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.gravity = this.getGravity();
                layoutParams.setMargins(0, 0, 0, 0);
                textView.setPadding(0, 0, 0, 0);
                layoutParams.width = textView.getResources().getDimensionPixelSize(R.dimen.dp_15);
                layoutParams.height = textView.getResources().getDimensionPixelSize(R.dimen.dp_15);
                if (!TextUtils.isEmpty(getText())) {
                    String text = getText().length() > 2 ? "99+" : (String) getText();
                    textView.setText(text);
                    if (text.length() < 3) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    } else {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                    }
                }
                textView.setLayoutParams(layoutParams);
                textView.setBackgroundDrawable(getBadgeDrawable(this, textView.getContext()));
            }

        }
    }

    public GradientDrawable getBadgeDrawable(BadgeItem badgeItem, Context context) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(this.getBackgroundColor(context));
        return shape;
    }

    public GradientDrawable getBadgePointerDrawable(BadgeItem badgeItem, Context context) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape
                .setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.badge_corner_radius));
        shape.setColor(this.getBackgroundColor(context));
        shape.setStroke(this.getBorderWidth(), this.getBorderColor(context));
        shape.setSize(1, 1);
        return shape;
    }

    private boolean isWeakReferenceValid() {
        return mTextViewRef != null && mTextViewRef.get() != null;
    }

    protected BadgeItem setTextView(TextView mTextView) {
        this.mTextViewRef = new WeakReference<>(mTextView);
        return this;
    }
}
