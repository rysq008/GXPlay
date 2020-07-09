package com.zhny.library.presenter.data.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.zhny.library.R;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.DialogCalendarRangeBinding;
import com.zhny.library.presenter.data.util.DataStatisticsUtil;
import com.zhny.library.utils.DisplayUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

/**
 * 日期区间选择view
 */
public class CalendarRangeDialog extends AppCompatDialogFragment implements View.OnClickListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnCalendarRangeSelectListener,
        CalendarView.OnMonthChangeListener {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

    private DialogCalendarRangeBinding binding;
    private OnCalendarRangeFinishListener onCalendarRangeFinishListener;

    private Window mWindow;
    private String title;
    private String yearCompany, monthCompany;
    private String startDate, endDate;

    public CalendarRangeDialog(OnCalendarRangeFinishListener listener) {
        this.onCalendarRangeFinishListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calendar_range, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rlCalendarRangeImgClose.setOnClickListener(this);
        binding.rlCalendarRangeLeft.setOnClickListener(this);
        binding.rlCalendarRangeRight.setOnClickListener(this);
        binding.tvSelectDate.setOnClickListener(this);
        binding.tvSevenDay.setOnClickListener(this);
        binding.tvYearDay.setOnClickListener(this);
        binding.tvAllDay.setOnClickListener(this);

        Context context = getDialog().getContext();
        yearCompany = context.getString(R.string.tv_calendar_view_title_year);
        monthCompany = context.getString(R.string.tv_calendar_view_title_month);

        binding.cvCalendarRange.setOnCalendarRangeSelectListener(this);
        binding.cvCalendarRange.setOnMonthChangeListener(this);
        //设置日期拦截事件，当前有效
        binding.cvCalendarRange.setOnCalendarInterceptListener(this);
        int mCalendarHeight = DisplayUtils.dp2px(43.2f);
        binding.cvCalendarRange.setCalendarItemHeight(mCalendarHeight);

        //设置title
        int year = binding.cvCalendarRange.getCurYear();
        int month = binding.cvCalendarRange.getCurMonth();
        String monthString = month < 10 ? "0" + month : month + "";
        title = year + yearCompany + monthString + monthCompany;
        binding.tvCalendarRangeTitle.setText(title);

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        mWindow = getDialog().getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.gravity = Gravity.BOTTOM;
            mWindow.setAttributes(params);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar todayCalendar = getCalendar(-1);
        Calendar preCalendar = getCalendar(-7);
        binding.cvCalendarRange.setSelectCalendarRange(preCalendar, todayCalendar);
        binding.setIsSelected(true);
        binding.setQuickMenu(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWindow != null) {
            mWindow.setBackgroundDrawableResource(R.color.white);
            mWindow.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = DisplayUtils.dp2px(467.60f);
            mWindow.setAttributes(lp);
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.rlCalendarRangeImgClose.getId()) {
            dismiss();
        } else if (v == binding.rlCalendarRangeLeft) {
            binding.cvCalendarRange.scrollToPre();
        } else if (v == binding.rlCalendarRangeRight) {
            binding.cvCalendarRange.scrollToNext();
        } else if (v == binding.tvSelectDate) {
            List<Calendar> calendars = binding.cvCalendarRange.getSelectCalendarRange();
            if (calendars == null || calendars.size() == 0) {
                //没有选择日直接关闭
                dismiss();
            } else {
                Calendar startCalendar = calendars.get(0);
                Calendar endCalendar = calendars.get(calendars.size() - 1);
                startDate = DataStatisticsUtil.getStandardYMD(startCalendar.toString());
                endDate = DataStatisticsUtil.getStandardYMD(endCalendar.toString());
                if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                    onCalendarRangeFinishListener.onCalendarRangeFinish(startDate, endDate);
                    dismiss();
                }
            }
        } else if (v == binding.tvSevenDay) {
            selectRangeDate(1);
        } else if (v == binding.tvYearDay) {
            selectRangeDate(2);
        } else if (v == binding.tvAllDay) {
            selectRangeDate(3);
        }
    }

    private boolean isClickQuick;

    private void selectRangeDate(int type) {
        binding.setQuickMenu(type);
        Calendar startDate = null, endDate;
        switch (type) {
            case 1: //近7日
                startDate = getCalendar(-7);
                break;
            case 2: //本年度
                startDate = getCalendarYear();
                break;
            case 3: //全部
                startDate = getCalendar(Constant.FINALVALUE.RANGE_CALENDAR_DATE);
                break;
        }
        endDate = getCalendar(-1);
        isClickQuick = true;
        binding.cvCalendarRange.setSelectCalendarRange(startDate, endDate);
    }


    /**
     * 设置拦截条件
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        //拦截今天及以后的日期
        Calendar now = new Calendar();
        now.setYear(binding.cvCalendarRange.getCurYear());
        now.setMonth(binding.cvCalendarRange.getCurMonth());
        now.setDay(binding.cvCalendarRange.getCurDay());
        return calendar.differ(now) > -1;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        //拦截日期的回调
    }

    @Override
    public void onCalendarSelectOutOfRange(Calendar calendar) {
        //日期超出范围的回调
    }

    @Override
    public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {

    }

    //选中区域监听
    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
        if (isEnd) {
            endDate = DataStatisticsUtil.getStandardYMD(calendar.toString());
            binding.setIsSelected(true);
        } else {
            startDate = DataStatisticsUtil.getStandardYMD(calendar.toString());
            binding.setIsSelected(false);
            if (!isClickQuick) {
                binding.setQuickMenu(0);
            }
            isClickQuick = false;
        }
    }

    //月历变化监听 for:更换title
    @Override
    public void onMonthChange(int year, int month) {
        String monthString = month < 10 ? "0" + month : month + "";
        title = year + yearCompany + monthString + monthCompany;
        binding.tvCalendarRangeTitle.setText(title);
    }


    //监听按钮动作
    public interface OnCalendarRangeFinishListener {
        void onCalendarRangeFinish(@Nullable String start, @Nullable String end);
    }


    //获取Calendar
    private Calendar getCalendar(int index) {
        java.util.Calendar tempCalendar = java.util.Calendar.getInstance();
        tempCalendar.clear();
        tempCalendar.setTime(new Date());
        tempCalendar.add(java.util.Calendar.DATE, index);
        String[] dateArr = dateFormat.format(tempCalendar.getTime()).split("-");
        Calendar calendar = new Calendar();
        calendar.setYear(Integer.valueOf(dateArr[0]));
        calendar.setMonth(Integer.valueOf(dateArr[1]));
        calendar.setDay(Integer.valueOf(dateArr[2]));
        return calendar;
    }


    //获取日期
    private Calendar getCalendar(String date) {
        String[] dateArr = date.split("-");
        Calendar calendar = new Calendar();
        calendar.setYear(Integer.valueOf(dateArr[0]));
        calendar.setMonth(Integer.valueOf(dateArr[1]));
        calendar.setDay(Integer.valueOf(dateArr[2]));
        return calendar;
    }

    //获取本年度1月1日
    private Calendar getCalendarYear() {
        java.util.Calendar tempCalendar = java.util.Calendar.getInstance();
        tempCalendar.clear();
        tempCalendar.setTime(new Date());
        Calendar calendar = new Calendar();
        calendar.setYear(tempCalendar.get(java.util.Calendar.YEAR));
        calendar.setMonth(1);
        calendar.setDay(1);
        return calendar;
    }


}
