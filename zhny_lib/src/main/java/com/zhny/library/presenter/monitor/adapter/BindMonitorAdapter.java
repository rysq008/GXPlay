package com.zhny.library.presenter.monitor.adapter;

import android.graphics.Color;
import android.graphics.Point;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhny.library.R;
import com.zhny.library.presenter.monitor.custom.PolygonShapeView;
import com.zhny.library.presenter.monitor.helper.MonitorHelper;
import com.zhny.library.presenter.monitor.model.vo.MapPath;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.ImageLoaderUtil;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

/**
 * created by liming
 */
@BindingMethods({
        @BindingMethod(type = TextView.class, attribute = "textRes", method = "bindText"),
        @BindingMethod(type = TextView.class, attribute = "textStrNotNull", method = "bindTextNotNull"),
        @BindingMethod(type = TextView.class, attribute = "textStrTwoLine", method = "bindTextStrTwoLine"),
        @BindingMethod(type = TextView.class, attribute = "textStrThreeLine", method = "bindTextStrThreeLine"),
        @BindingMethod(type = ImageView.class, attribute = "imgSrc", method = "bindImg"),
        @BindingMethod(type = ImageView.class, attribute = "roundImg", method = "showRoundImage"),
        @BindingMethod(type = PolygonShapeView.class, attribute = "plotPath", method = "showPlotPath"),
        @BindingMethod(type = PolygonShapeView.class, attribute = "plotDrawPath", method = "showPlotDrawPath")
})
public class BindMonitorAdapter {

    @BindingAdapter("textRes")
    public static void bindText(TextView textView, int testResId) {
        textView.setText(testResId);
    }

    @BindingAdapter("textStrNotNull")
    public static void bindTextNotNull(TextView textView, String msg) {
        textView.setText(TextUtils.isEmpty(msg) ? textView.getContext().getString(R.string.monitor_str_not_null) : msg);
    }

    @BindingAdapter("textStrTwoLine")
    public static void bindTextStrTwoLine(TextView textView, String msg) {
        String result;
        if (msg.length() > 12) {
            result = msg.substring(0, 11).concat("...");
        } else {
            result = msg;
        }
        if (TextUtils.isEmpty(msg)) {
            result = "-";
        }
        textView.setText(result);
    }

    @BindingAdapter("textStrThreeLine")
    public static void bindTextStrThreeLine(TextView textView, String msg) {
        String result;
        if (msg.length() > 16) {
            result = msg.substring(0, 15).concat("...");
        } else {
            result = msg;
        }
        if (TextUtils.isEmpty(msg)) {
            result = "-";
        }
        textView.setText(result);
    }


    @BindingAdapter("textConvertTime")
    public static void bindTextConvertTime(TextView textView, long updatedTimestamp) {
        textView.setText(MonitorHelper.getPreTimeStr(textView.getContext(), updatedTimestamp));
    }

    @BindingAdapter("imgSrc")
    public static void bindImg(ImageView imageView, int imgResId) {
        imageView.setImageResource(imgResId);
    }

    @BindingAdapter("roundImg")
    public static void showRoundImage(ImageView imageView, String url) {
        String imgUrl;
        if (!TextUtils.isEmpty(url) && url.contains(",")) imgUrl = url.split(",")[0];
        else imgUrl = url;
        ImageLoaderUtil.loadRoundImage(imageView.getContext(),
                imgUrl,
                10,
                imageView);
    }

    private static PolygonShapeView.ShapeStyle shapeStyle = new PolygonShapeView.ShapeStyle(
            Color.parseColor("#40009688"),
            Color.parseColor("#009688"),
            DisplayUtils.dp2px(2f), true);

    @BindingAdapter("plotPath")
    public static void showPlotPath(PolygonShapeView view, MapPath mapPath) {
        List<Point> points = PolygonShapeView.mapToPoints(
                mapPath.projection,
                MonitorHelper.getLatLngs(mapPath.coordinates)
        );
        view.setPolygon(points, shapeStyle);
    }

    @BindingAdapter("plotDrawPath")
    public static void showPlotDrawPath(PolygonShapeView view, MapPath mapPath) {
        try {
            List<Point> points = PolygonShapeView.mapToPoints(mapPath.projection, mapPath.latLngs);
            view.setPolygon(points, shapeStyle);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
