package com.zhny.library.presenter.driver.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class CircleImageView extends AppCompatImageView {

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private Paint mPaint;

    public CircleImageView(Context context) {
        this(context, null);
    }


    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits();
    }

    /**
     * 初始化
     */
    private void inits() {

        if (mBitmap == null) {
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitMapFromDrawable(drawable);
        inits();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitMapFromDrawable(getDrawable()) : null;
        inits();
    }

    /**
     * 从bitmapDrawable colorDrawable中获取bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitMapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas cavas = new Canvas(bitmap);
            drawable.setBounds(0, 0, cavas.getWidth(), cavas.getHeight());
            drawable.draw(cavas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        inits();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null || mBitmapShader == null) {
            return;
        }
        if (mBitmap.getWidth() == 0 || mBitmap.getHeight() == 0) {
            return;
        }
        updateBitmapShader();
        mPaint.setShader(mBitmapShader);
        canvas.drawCircle((float) getWidth() / 2, (float) getHeight() / 2, Math.min((float) getWidth() / 2, (float) getHeight() / 2), mPaint);
    }

    /**
     * 根据当前实际宽高，和图片宽高做一些调整
     */
    private void updateBitmapShader() {
        if (mBitmap == null) {
            return;
        }
        int canvasWidth = Math.min(getWidth(), getHeight());
        if (canvasWidth != mBitmap.getWidth() || canvasWidth != mBitmap.getHeight()) {
            float scale = canvasWidth / (float) mBitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            mBitmapShader.setLocalMatrix(matrix);
        }
    }
}
