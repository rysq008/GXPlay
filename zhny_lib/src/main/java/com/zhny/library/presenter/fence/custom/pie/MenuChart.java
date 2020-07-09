package com.zhny.library.presenter.fence.custom.pie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhny.library.presenter.fence.listener.OnFenceDrawViewListener;
import com.zhny.library.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;


public class MenuChart extends View {
    public static final String TAG = "QDX";

    //宽高
    private int mWidth;
    private int mHeight;
    //数据
    private List<PieData> mPieData = new ArrayList<>();
    // 每块模块的 间隔  以下简称饼状图像为模块
    private float PIE_SPACING = 7f;
    //整个view展示的角度, 360为圆
    private int PIE_VIEW_ANGLE = 360;
    private float animatedValue;
    //绘制模块  以及 弹出模块上的中心文字
    private Paint mPaint = new Paint();

    //XferMode御用的笔
    private Paint mXPaint = new Paint();
    private float startAngle = 0;

    private RectF rectF, rectFGold, rectFIn, rectFLabel;

    private float rLabel;
    private float r;
    private float rGold;
    private float rIn;


    /**
     * 每一个模块占有的总角度 （前几模块角度之和）
     */
    private float[] pieAngles;
    /**
     * 选中位置，对应触摸的模块位置
     */
    private int touchSelectedId;

    //最外层标签
    private double labelRadioRectF = 1.2;
    //绘制bitmap圆环半径比例
    private double widthBmpRadioRectF = 0.5;
    //白金层
    private double goldRadioRectF = 0.5;
    //最里面透明层
    private double insideRadiusScale = 0.3;

    //绘制每个模块里的图片
    private List<Bitmap> bmpList = new ArrayList<>();

    private OnFenceDrawViewListener onSelectMenuListener;


    public MenuChart(Context context) {
        this(context, null);
    }

    public MenuChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置画布宽高
        setMeasuredDimension(DisplayUtils.dp2px(400), DisplayUtils.dp2px(200));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        rLabel = (float) (Math.max(mWidth, mHeight) / 2 * widthBmpRadioRectF * labelRadioRectF);
        rectFLabel = new RectF(-rLabel, -rLabel, rLabel, rLabel);

        //标签层内绘制bmp
        r = (float) (Math.max(mWidth, mHeight) / 2 * widthBmpRadioRectF);// 饼状图半径
        rectF = new RectF(-r, -r, r, r);

        r = (float) (Math.max(mWidth, mHeight) / 2 * widthBmpRadioRectF);// 饼状图

        //白金圆弧
        rGold = (float) (r * goldRadioRectF);
        rectFGold = new RectF(-rGold, -rGold, rGold, rGold);

        //透明层圆
        rIn = (float) (r * insideRadiusScale);
        rectFIn = new RectF(-rIn, -rIn, rIn, rIn);


        initDate(mPieData);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPieData == null)
            return;

        canvas.translate(mWidth / 2, mHeight / 2);// 将画板坐标原点移动到中心位置


        //先画扇形的每一模块
        drawPieRectF(canvas);

        //再绘制扇形上的文字/图片
        drawTextAndBmp(canvas);
    }

    //负责绘制扇形的所有 Rect , Arc
    private void drawPieRectF(Canvas canvas) {
        float currentStartAngle = 0;// 当前已经绘制的角度,我们从0开始，直到animatedValue
        canvas.save();
        canvas.rotate(startAngle);
        float sweepAngle;   //当前要绘制的角度

        for (int i = 0; i < mPieData.size(); i++) {
            PieData pie = mPieData.get(i);

            sweepAngle = Math.min(pie.getAngle() - PIE_SPACING, animatedValue - currentStartAngle);   //-1 是为了显示每个模块之间的空隙

//            if (currentStartAngle + sweepAngle == PIE_VIEW_ANGLE - PIE_SPACING) {   //防止最后一个模块缺角 ，如果是 360° 且最后一块希望是缺觉，可以注释这段
//                sweepAngle = pie.getAngle();
//            }

            int center = (int) (mPieData.size() * 1f / 2);
            if ( i == center) {
                sweepAngle = pie.getAngle();
            }

            if (sweepAngle > 0) {
                drawArc(canvas, currentStartAngle, sweepAngle, pie, rectF, rectFGold, rectFIn, i == touchSelectedId);
            }
            if (mPieData.size() > 2 && i == mPieData.size() - 2){
                currentStartAngle += pie.getAngle() + PIE_SPACING;
            } else {
                currentStartAngle += pie.getAngle();
            }

        }

        canvas.restore();
    }

    /**
     * 绘制所有的文字 | bitmap
     * 绘制文字和绘制扇形的RectF/Arc 有所不同，绘制文字是当animatedValue达到某一值的时候讲文字绘制
     * 而绘制扇形的时候是根据animatedValue一个角度一个角度绘制的
     */
    private void drawTextAndBmp(Canvas canvas) {
        float currentStartAngle = startAngle;

        for (int i = 0; i < mPieData.size(); i++) {
            PieData pie = mPieData.get(i);

            int pivotX;   //中心点X
            int pivotY;   //中心点Y

            if (animatedValue > pieAngles[i] - pie.getAngle() / 3) {

                pivotX = (int) (Math.cos(Math.toRadians(currentStartAngle + (pie.getAngle() / 2))) * (r + rGold) / 2);
                pivotY = (int) (Math.sin(Math.toRadians(currentStartAngle + (pie.getAngle() / 2))) * (r + rGold) / 2);
                if (bmpList != null && bmpList.size() > 0 && bmpList.get(i) != null) {
                    float left = (pivotX - (int) (pie.getMax_drawable_size() / 2));
                    float top = (pivotY - (int) (pie.getMax_drawable_size() / 2));

                    //绘制图片
                    canvas.drawBitmap(bmpList.get(i), left, top - 25, mPaint);


                    //绘制文字
                    if (currentStartAngle < 210) {
                        pivotX -= 20;
                    } else if (currentStartAngle > 270) {
                        pivotX += 20;
                    }
                    canvas.drawText(mPieData.get(i).getName(), pivotX, pivotY + 55, mPaint);
                }

                currentStartAngle += pie.getAngle();
            }
        }
    }

    /**
     * 画出扇形   ,两种不同情况 展示|触摸弹出
     */
    private void drawArc(Canvas canvas, float currentStartAngle, float sweepAngle, PieData pie, RectF outRectF, RectF midRectF, RectF inRectF, boolean isTouch) {
        int layerID;

        layerID = canvas.saveLayer(rectFLabel, mPaint, Canvas.ALL_SAVE_FLAG);

        mXPaint.setColor(isTouch ? Color.parseColor("#999999") : Color.WHITE);
        canvas.drawArc(outRectF, currentStartAngle, sweepAngle, true, mXPaint);

        mXPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawArc(midRectF, currentStartAngle - PIE_SPACING, sweepAngle + PIE_SPACING * 2, true, mXPaint);  //先把白金区域 擦拭干净。再绘制
        mXPaint.setXfermode(null);

        mXPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawArc(inRectF, -1f, PIE_VIEW_ANGLE + 2f, true, mXPaint);   //透明圈

        mXPaint.setXfermode(null);

        canvas.restoreToCount(layerID);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return onPieTouchEvent(event, event.getX(), event.getY());
    }

    /**
     * 该方法可提供给外界使用
     * 即设置当前屏幕触控点对应 我们控件的坐标，可实现功能: 触摸-->(显示我们控件的btn，我们控件显示)-->手指触摸不放，移动手指弹出 模块
     * 使用方法为传入 event, event.getRawX() - pieLocation[0], event.getRawY() - pieLocation[1]
     *
     * @param eventPivotX 在我们控件内对应的 x 坐标
     * @param eventPivotY 在我们控件内对应的 y 坐标
     */
    public boolean onPieTouchEvent(MotionEvent event, float eventPivotX, float eventPivotY) {
        if (mPieData.size() > 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                case MotionEvent.ACTION_MOVE:

                    getParent().requestDisallowInterceptTouchEvent(true);
                    float x = eventPivotX - (mWidth / 2);    //因为我们已经将坐标中心点(0,0)移动到   mWidth / 2,mHeight / 2
                    float y = eventPivotY - (mHeight / 2);

                    float touchAngle = 0;   //Android 绘图坐标和我们的数学认知不同，安卓由第4 - 3 - 2 -1 象限绘制角度
                    if (x < 0) {   //x<0 的时候 ,求出的角度实为 [0°,-90°) ,此时我们给它掰正
                        touchAngle += 180;
                    }
                    touchAngle += Math.toDegrees(Math.atan(y / x));
                    touchAngle = touchAngle - startAngle;
                    if (touchAngle < 0) {
                        touchAngle = touchAngle + 360;
                    }
                    float touchRadius = (float) Math.sqrt(y * y + x * x);//求出触摸的半径

                    //我们用touchSeleteId来表示当前选中的模块
                    if (rIn < touchRadius && touchRadius < rLabel) {//触摸的范围在绘制的区域
                        if (-Arrays.binarySearch(pieAngles, touchAngle) - 1 == touchSelectedId) {   //如果模块有变动(手指挪到其他模块)再绘制
                            return true;   //如果触摸的地方是已经展开的，那么就不再重复绘制
                        }
                        touchSelectedId = -Arrays.binarySearch(pieAngles, touchAngle) - 1;
                        invalidate();

                    } else if (0 < touchRadius && touchRadius < rIn) {  //触摸的范围在原点bmp范围
                        if (event.getAction() == MotionEvent.ACTION_MOVE) {   //有展开模块时候，手指移动到开关，收起模块
                            touchSelectedId = -1;
                            invalidate();
                        }
                    }

                    return true;


                case MotionEvent.ACTION_UP:

                    if (animatedValue == PIE_VIEW_ANGLE) {     //如果有触摸且已经显示了 那么隐藏
                        try {
                            if (mPieData.get(touchSelectedId).getMenuType() == 1) { // 1, "清空"
                                onSelectMenuListener.onClearAll();
                            } else if (mPieData.get(touchSelectedId).getMenuType() == 2) { //2, "绘制"
                                onSelectMenuListener.onDrawPoint();
                            } else if (mPieData.get(touchSelectedId).getMenuType() == 3) { //3, "撤回"
                                onSelectMenuListener.onBackPoint();
                            } else if (mPieData.get(touchSelectedId).getMenuType() == 4) { //4, "恢复"
                                onSelectMenuListener.onReset();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    getParent().requestDisallowInterceptTouchEvent(false);

                    touchSelectedId = -1;
                    invalidate();
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }


    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);//抗锯齿
        //文字颜色
        mPaint.setColor(Color.parseColor("#009688"));
        mPaint.setTextSize(34);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mXPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * 在onSizeChanged 后调用，这样才可以拿到 r 和 rGold 的值。否则无法测量 扇形内最大的bitmap
     */
    private void initDate(List<PieData> mPieData) {

        if (mPieData == null || mPieData.size() == 0)
            return;
        pieAngles = new float[mPieData.size()];
        float sumValue = 0;   //总 权重
        for (int i = 0; i < mPieData.size(); i++) {
            PieData pie = mPieData.get(i);
            sumValue += pie.getWeight();
        }

        float sumAngle = 0;
        for (int i = 0; i < mPieData.size(); i++) {
            PieData pie = mPieData.get(i);
            float percentage = pie.getWeight() / sumValue;
            float angle = percentage * PIE_VIEW_ANGLE;
            pie.setAngle(angle);
            sumAngle += angle;
            pieAngles[i] = sumAngle;

            /****************防止cos90° 取值极其小*********************/

            double centerR = (r + rGold) / 2;
            double maxH = r - rGold;
            double maxW = Math.sin(Math.toRadians(angle / 2)) * centerR * Math.sqrt(2);
            maxW = maxW < 1 ? maxH : maxW;
            maxH = maxH < 1 ? maxW : maxH;
            /****************防止cos90° 取值极其小*********************/

            //中间bitmap收缩倍数
            float bmpScale = 0.4f;
            pie.setMax_drawable_size(Math.min(maxW, maxH) * bmpScale);
            //设置 扇形内最大的bitmap size
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), pie.getDrawableId());
            bitmap = PieUtils.zoomImage(bitmap, pie.getMax_drawable_size(), pie.getMax_drawable_size());
            bmpList.add(bitmap);

        }
        touchSelectedId = -1;
    }


    public void show() {
        animatedValue = PIE_VIEW_ANGLE;
        invalidate();
    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle 起始角度
     */
    public void setStartAngle(float mStartAngle) {
        while (mStartAngle < 0) {
            mStartAngle = mStartAngle + 360;
        }
        while (mStartAngle > 360) {
            mStartAngle = mStartAngle - 360;
        }
        this.startAngle = mStartAngle;
    }

    /**
     * 设置数据
     *
     * @param mPieData 数据
     */
    public void setPieData(List<PieData> mPieData) {
        this.mPieData = mPieData;
    }

    /**
     * 整个view的展示角度
     */
    public void setPieShowAngle(int angle) {
        this.PIE_VIEW_ANGLE = angle;
    }


    public void setOnSelectMenuListener(OnFenceDrawViewListener onSelectMenuListener) {
        this.onSelectMenuListener = onSelectMenuListener;
    }
}
