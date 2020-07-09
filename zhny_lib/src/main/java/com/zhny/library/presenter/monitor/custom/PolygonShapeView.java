package com.zhny.library.presenter.monitor.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.util.ObjectsCompat;

/**
 * @author WingHawk
 */
public class PolygonShapeView extends View {

    private Paint mPaint;

    private double mLandRight;
    private double mLandBottom;
    private double mLandLeft;
    private double mLandTop;

    private double mLandWidth;
    private double mLandHeight;
    private Map<Path, ShapeStyle> mPolygonsMap = new HashMap<>();
    private Map<List<Point>, ShapeStyle> posMap = new HashMap<>();

    public PolygonShapeView(Context context) {
        super(context);
        init();
    }

    public PolygonShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PolygonShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setPolygon(List<Point> posList, ShapeStyle style) {
        HashMap<List<Point>, ShapeStyle> map = new HashMap<>(1);
        map.put(posList, style);
        setPolygons(map);
    }

    public void setPolygons(Map<List<Point>, ShapeStyle> posMap) {
        if (!ObjectsCompat.equals(this.posMap, posMap)) {
            this.posMap = posMap == null ? new HashMap<>(0) : posMap;
            mPolygonsMap.clear();
            invalidate();
        }
    }

    private Path mapToPath(List<Point> points, ShapeStyle style) {
        float usableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float usableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        double viewAspectRatio = usableHeight * 1.0 / usableWidth;
        double landAspectRatio = mLandHeight / mLandWidth;
        double sizeFactor;
        double offsetTop;
        double offsetLeft;

        if (viewAspectRatio > landAspectRatio) {
            // use view width as base size
            sizeFactor = usableWidth / mLandWidth;
            offsetLeft = 0;
            offsetTop = (usableHeight - mLandHeight * sizeFactor) / 2;
        } else {
            // use view height as base size
            sizeFactor = usableHeight / mLandHeight;
            offsetTop = 0;
            offsetLeft = (usableWidth - mLandWidth * sizeFactor) / 2;
        }
        Path path = new Path();
        boolean first = true;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (Point pt : points) {
            double dx = pt.x - mLandLeft;
            double dy = pt.y - mLandTop;
            float x = (float) (dx * sizeFactor + paddingLeft + offsetLeft);
            float y = (float) (dy * sizeFactor + paddingTop + offsetTop);
            if (first) {
                path.moveTo(x, y);
                first = false;
            } else {
                path.lineTo(x, y);
            }
        }
        if (style.closed) {
            path.close();
        }
        return path;
    }

    private void layoutAndMeasureLand(List<Point> points) {
        for (Point pt : points) {
            mLandLeft = Math.min(mLandLeft, pt.x);
            mLandRight = Math.max(mLandRight, pt.x);
            mLandTop = Math.min(mLandTop, pt.y);
            mLandBottom = Math.max(mLandBottom, pt.y);
        }
        mLandWidth = mLandRight - mLandLeft;
        mLandHeight = mLandBottom - mLandTop;
    }

    public static List<Point> mapToPoints(Projection projection, List<LatLng> posList) {
        ArrayList<Point> points = new ArrayList<>();
        for (LatLng latLng : posList) {
            Point point = projection.toScreenLocation(latLng);
            points.add(point);
        }
        return points;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!posMap.isEmpty() && mPolygonsMap.isEmpty()) {
            mLandRight = Long.MIN_VALUE;
            mLandBottom = Long.MIN_VALUE;
            mLandLeft = Long.MAX_VALUE;
            mLandTop = Long.MAX_VALUE;
            for (Map.Entry<List<Point>, ShapeStyle> entry : posMap.entrySet()) {
                List<Point> posList = entry.getKey();
                if (posList != null && !posList.isEmpty()) {
                    layoutAndMeasureLand(posList);
                }

                List<Point> points = entry.getKey();
                ShapeStyle shapeStyle = entry.getValue();
                Path path = mapToPath(points, shapeStyle);
                mPolygonsMap.put(path, shapeStyle);
            }
        }
        if (!mPolygonsMap.isEmpty()) {
            for (Map.Entry<Path, ShapeStyle> entry : mPolygonsMap.entrySet()) {
                Path path = entry.getKey();
                ShapeStyle style = entry.getValue();
                mPaint.setStrokeWidth(style.strokeWidth);
                if (style.closed) {
                    mPaint.setColor(style.fillColor);
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawPath(path, mPaint);
                    if (style.strokeColor != 0 && style.strokeWidth != 0) {
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPaint.setColor(style.strokeColor);
                        canvas.drawPath(path, mPaint);
                    }
                } else {
                    mPaint.setColor(style.strokeColor);
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawPath(path, mPaint);
                }
            }
        }
    }

    public static class ShapeStyle {
        int fillColor;
        int strokeColor;
        int strokeWidth;
        boolean closed;

        public ShapeStyle(int fillColor, int strokeColor, int strokeWidth, boolean closed) {
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
            this.closed = closed;
        }

        public ShapeStyle() {
        }
    }
}
