package com.jianfanjia.cn.designer.view;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.jianfanjia.cn.designer.R;

import java.util.Calendar;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-25 11:40
 */
public class DateTimePicker extends FrameLayout {
    private final NumberPicker mDateSpinner;
    private final NumberPicker mHourSpinner;
    private final NumberPicker mMinuteSpinner;
    private Calendar mDate;
    private Calendar mMinDate;
    private int mHour, mMinute;
    private int mInitHour,mInitMinute;
    private String[] mDateDisplayValues = new String[7];
    private String[] mMiniDisplayValues = new String[6];
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    public DateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
         *獲取系統時間
         */
        mDate = Calendar.getInstance();
        int minute = mDate.get(Calendar.MINUTE) % 10 == 0 ? (mDate.get(Calendar.MINUTE) / 10 * 10) : (mDate.get(Calendar.MINUTE) / 10 + 1) * 10;//取当前时间之后的最近的一个整数分钟
        mDate.set(mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH), mDate.get(Calendar.HOUR_OF_DAY), minute);
        mMinDate = Calendar.getInstance();
        mMinDate.setTimeInMillis(mDate.getTimeInMillis());
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);
        mInitHour = mHour;
        mInitMinute = mMinute;
        /**
         * 加载布局
         */
        inflate(context, R.layout.view_datepicker, this);
        /**
         * 初始化控件
         */
        mDateSpinner = (NumberPicker) this.findViewById(R.id.np_date);
        mDateSpinner.setMinValue(0);
        mDateSpinner.setMaxValue(6);
        updateDateControl();
        mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);

        mHourSpinner = (NumberPicker) this.findViewById(R.id.np_hour);
        mHourSpinner.setMaxValue(23);
        mHourSpinner.setMinValue(0);
        mHourSpinner.setValue(mHour);
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

        mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_minute);
        mMinuteSpinner.setMaxValue(5);
        mMinuteSpinner.setMinValue(0);
        for (int i = 0; i < mMiniDisplayValues.length; i++) {
            mMiniDisplayValues[i] = "" + i * 10;//设置分钟为10分钟间隔
        }
        mMinuteSpinner.setDisplayedValues(mMiniDisplayValues);
        mMinuteSpinner.setValue(mMinute / 10);
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);

        mDateSpinner.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        mHourSpinner.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        mMinuteSpinner.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
    }

    public DateTimePicker(Context context) {
        this(context, null);
    }

    /**
     * 控件监听器
     */
    private NumberPicker.OnValueChangeListener mOnDateChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
            if (mDate.getTimeInMillis() < mMinDate.getTimeInMillis()) {
                mDate.setTimeInMillis(mMinDate.getTimeInMillis());
            }
            /**
             * 更新日期
             */
            /**
             * 给接口传值
             */
            onDateTimeChanged();
            updateDateControl();
        }
    };

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mHour = mHourSpinner.getValue();
            mDate.set(Calendar.HOUR_OF_DAY, mHour);
            if (mDate.getTimeInMillis() < mMinDate.getTimeInMillis()) {
                mDate.setTimeInMillis(mMinDate.getTimeInMillis());
                mHourSpinner.setValue(mInitHour);
            } else {
                onDateTimeChanged();
            }

        }
    };

    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mMinute = mMinuteSpinner.getValue();
            mDate.set(Calendar.MINUTE, mMinute * 10);
            if (mDate.getTimeInMillis() < mMinDate.getTimeInMillis()) {
                mDate.setTimeInMillis(mMinDate.getTimeInMillis());
                mMinuteSpinner.setValue(mInitMinute / 10);
            } else {
                onDateTimeChanged();
            }
        }
    };

    public void setMinDate(Calendar calendar) {
//        mDate.setTimeInMillis(calendar.getTimeInMillis());
//        updateDateControl();
//        calendar.set(Calendar.MINUTE, (mDate.get(Calendar.MINUTE) / 10) * 10);
//        mHour = calendar.get(Calendar.HOUR_OF_DAY);
//        mMinute = calendar.get(Calendar.MINUTE);
//        mMinDate = calendar;
//        mDate.setTimeInMillis(calendar.getTimeInMillis());
        updateDateControl();
        onDateTimeChanged();
    }

    private void updateDateControl() {
        /**
         * 星期几算法
         */
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
        mDateSpinner.setDisplayedValues(null);
        for (int i = 0; i < 7; ++i) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            mDateDisplayValues[i] = (String) DateFormat.format("MM.dd EEEE",
                    cal);
        }
        mDateSpinner.setDisplayedValues(mDateDisplayValues);
        mDateSpinner.setValue(7 / 2);
        mDateSpinner.invalidate();
    }


    /*
     *接口回调 参数是当前的View 年月日小时分钟
     */
    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimePicker view, int year, int month,
                               int day, int hour, int minute);
    }

    /*
     *对外的公开方法
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
                    mDate.get(Calendar.DAY_OF_MONTH), mDate.get(Calendar.HOUR_OF_DAY), mDate.get(Calendar.MINUTE));
        }
    }
}
