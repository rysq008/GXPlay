package com.game.helper.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import com.game.helper.R;

/**
 * 方块格密码输入edittext
 * */
public class PasswordEditText extends AppCompatEditText {
    private OnEditCompleteListener onEditCompleteListener;
    // 画笔
    private Paint mPaint;
    // 一个密码所占的宽度
    private int mPasswordItemWidth;
    // 密码的个数默认为6位数
    private int mPasswordNumber = 6;
    // 背景边框颜色
    private int mBgColor = Color.parseColor("#d1d2d6");
    // 背景边框大小
    private int mBgSize = 1;
    // 背景边框圆角大小
    private int mBgCorner = 0;
    // 分割线的颜色
    private int mDivisionLineColor = mBgColor;
    // 分割线的大小
    private int mDivisionLineSize = 1;
    // 密码圆点的颜色
    private int mPasswordColor = Color.BLACK;
    // 密码圆点的半径大小
    private int mPasswordRadius = 4;
    //明文颜色
    private int mVisibleTextColor = Color.BLACK;
    //明文字体大小
    private int mVisibleTextSize = 18;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAttributeSet(context, attrs);
        // 设置输入模式是密码仅数字
        setInputType(EditorInfo.TYPE_CLASS_NUMBER|EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
        // 不显示光标
        setCursorVisible(false);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6 && onEditCompleteListener != null) {
                    //Toast.makeText(getContext(), "密码充满", Toast.LENGTH_SHORT).show();
                    onEditCompleteListener.onEditComplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 初始化属性
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        // 获取大小
        mDivisionLineSize = (int) array.getDimension(R.styleable.PasswordEditText_divisionLineSize, dip2px(mDivisionLineSize));
        mPasswordRadius = (int) array.getDimension(R.styleable.PasswordEditText_passwordRadius, dip2px(mPasswordRadius));
        mBgSize = (int) array.getDimension(R.styleable.PasswordEditText_bgSize, dip2px(mBgSize));
        mBgCorner = (int) array.getDimension(R.styleable.PasswordEditText_bgCorner, 0);
        mVisibleTextSize = (int) array.getDimension(R.styleable.PasswordEditText_visibleSize, dip2px(mVisibleTextSize));
        // 获取颜色
        mBgColor = array.getColor(R.styleable.PasswordEditText_bgColor, mBgColor);
        mDivisionLineColor = array.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor);
        mPasswordColor = array.getColor(R.styleable.PasswordEditText_passwordColor, mPasswordColor);
        mVisibleTextColor = array.getColor(R.styleable.PasswordEditText_visibleColor, mVisibleTextColor);
        array.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    /**
     * dip 转 px
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int passwordWidth = getWidth() - (mPasswordNumber - 1) * mDivisionLineSize;
        mPasswordItemWidth = passwordWidth / mPasswordNumber;
        // 绘制背景
        drawBg(canvas);
        // 绘制分割线
        drawDivisionLine(canvas);
        // 绘制密码
        /*if (getInputType() == EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD
                || getInputType() == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) {
            drawHidePassword(canvas);
        }else {
        // 绘制明文
            drawVisiblePassword(canvas);
        }*/
        drawHidePassword(canvas);
    }

    public void addOnEditCompleteListener(OnEditCompleteListener onEditCompleteListener){
        this.onEditCompleteListener = onEditCompleteListener;
    }

    public interface OnEditCompleteListener{
        void onEditComplete(String password);
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {
        mPaint.setColor(mBgColor);
        // 设置画笔为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBgSize);
        RectF rectF = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        // 如果没有设置圆角，就画矩形
        if (mBgCorner == 0) {
            canvas.drawRect(rectF, mPaint);
        } else {
            // 如果有设置圆角就画圆矩形
            canvas.drawRoundRect(rectF, mBgCorner, mBgCorner, mPaint);
        }
    }

    /**
     * 绘制明文
     */
    private void drawVisiblePassword(Canvas canvas) {
        char[] password = getText().toString().toCharArray();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mVisibleTextColor);
        mPaint.setTextSize(mVisibleTextSize);
        for (int i = 0; i < password.length; i++) {
            Rect rect = new Rect();
            mPaint.getTextBounds(String.valueOf(password[i]), 0, String.valueOf(password[i]).length(), rect);
            int charWidth = rect.width();//文字宽
            int charHeight = rect.height();//文字高
            int cx = i * mDivisionLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize
                    - charWidth / 2;
            canvas.drawText(String.valueOf(password[i]), cx, getHeight() / 2 + charHeight / 2, mPaint);
        }
    }

    /**
     * 绘制隐藏的密码
     */
    private void drawHidePassword(Canvas canvas) {
        int passwordLength = getText().length();
        mPaint.setColor(mPasswordColor);
        // 设置画笔为实心
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < passwordLength; i++) {
            int cx = i * mDivisionLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize;
            canvas.drawCircle(cx, getHeight() / 2, mPasswordRadius, mPaint);
        }
    }

    /**
     * 绘制分割线
     */
    private void drawDivisionLine(Canvas canvas) {
        mPaint.setStrokeWidth(mDivisionLineSize);
        mPaint.setColor(mDivisionLineColor);
        for (int i = 0; i < mPasswordNumber - 1; i++) {
            int startX = (i + 1) * mDivisionLineSize + (i + 1) * mPasswordItemWidth + mBgSize;
            canvas.drawLine(startX, mBgSize, startX, getHeight() - mBgSize, mPaint);
        }
    }

    /**
     * 添加密码
     */
    public void addPassword(String number) {
        number = getText().toString().trim() + number;
        if (number.length() > mPasswordNumber) {
            return;
        }
        setText(number);
    }

    /**
     * 删除最后一位密码
     */
    public void deleteLastPassword() {
        String currentText = getText().toString().trim();
        if (TextUtils.isEmpty(currentText)) {
            return;
        }
        currentText = currentText.substring(0, currentText.length() - 1);
        setText(currentText);
    }
}