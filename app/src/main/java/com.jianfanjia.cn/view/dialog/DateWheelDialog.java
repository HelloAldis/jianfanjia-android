package com.jianfanjia.cn.view.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.wheel.NumericWheelAdapter;
import com.jianfanjia.cn.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.view.wheel.WheelView;

import java.util.Calendar;

public class DateWheelDialog extends CommonDialog implements
        OnWheelChangedListener {

    public static final int VISIABLECOUNT = 3;

    private Calendar startCalendar;// 起始时间
    private Calendar chooseCalendar;// 选择时间
    private Calendar currentCalendar = Calendar.getInstance();
//	private Calendar endCalendar;//结束时间

    private int minYear;
    private int minMonth;
    private int minDate;
    private int maxYear;

    private LayoutInflater inflater;
    private WheelView wheelView1;
    private WheelView wheelView2;
    private WheelView wheelView3;

    public DateWheelDialog(Context context, Calendar calendar) {
        super(context);
        inflater = LayoutInflater.from(context);
        startCalendar = Calendar.getInstance();
        minYear = startCalendar.get(Calendar.YEAR);
        minMonth = startCalendar.get(Calendar.MONTH);
        minDate = startCalendar.get(Calendar.DAY_OF_MONTH);
        chooseCalendar = currentCalendar;
        initView();
    }

    public Calendar getChooseCalendar() {
        return chooseCalendar;
    }

    private void initView() {
        View view = inflater.inflate(R.layout.commont_wheel, null);
        setContent(view);
        wheelView1 = (WheelView) view.findViewById(R.id.wheel_item1);
        wheelView2 = (WheelView) view.findViewById(R.id.wheel_item2);
        wheelView3 = (WheelView) view.findViewById(R.id.wheel_item3);
        wheelView1.setVisibleItems(VISIABLECOUNT);
        wheelView2.setVisibleItems(VISIABLECOUNT);
        wheelView3.setVisibleItems(VISIABLECOUNT);

        // month
        int curMonth = currentCalendar.get(Calendar.MONTH);
        wheelView2.setAdapter(new NumericWheelAdapter(minMonth + 1, 12));
//        wheelView2.setCurrentItem(curMonth);
        wheelView2.addChangingListener(this);
        wheelView2.setCyclic(false);

        // year
        int curYear = currentCalendar.get(Calendar.YEAR);
        wheelView1.setAdapter(new NumericWheelAdapter(minYear, minYear + 1));
        wheelView1.setCurrentItem(curYear - minYear);
        wheelView1.addChangingListener(this);
        wheelView1.setCyclic(false);

        // day
        wheelView3.setCyclic(false);
        int maxDays = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        wheelView3.setAdapter(new NumericWheelAdapter(minDate, maxDays));
//        wheelView3.setCurrentItem(0);
        wheelView3.addChangingListener(this);

        Log.i(this.getClass().getName(), curYear + "-" + (curMonth + 1) + "-"
                + startCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays() {
        LogTool.d(this.getClass().getName(), "updateDays");
        int maxDays = chooseCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(chooseCalendar.get(Calendar.YEAR) == startCalendar.get(Calendar.YEAR)  && chooseCalendar.get(Calendar.MONTH) == startCalendar.get(Calendar.MONTH)){
            minDate = startCalendar.get(Calendar.DAY_OF_MONTH);
            LogTool.d(this.getClass().getName(), "minDate =" + minDate);
        }else{
            minDate = 1;
        }
        LogTool.d(this.getClass().getName(), "chooseCalendar.get(Calendar.YEAR) =" + chooseCalendar.get(Calendar.YEAR) + "," + "startCalendar.get(Calendar.YEAR) =" + startCalendar.get(Calendar.YEAR));
        LogTool.d(this.getClass().getName(), "chooseCalendar.get(Calendar.MONTH) =" + chooseCalendar.get(Calendar.MONTH) + "," + "startCalendar.get(Calendar.MONTH) =" + startCalendar.get(Calendar.MONTH));
        wheelView3.setAdapter(new NumericWheelAdapter(minDate, maxDays));
        wheelView3.setCurrentItem(0, true);
    }

    void updateMonth() {
        LogTool.d(this.getClass().getName(), "updateMonth");
        if (chooseCalendar.get(Calendar.YEAR) == startCalendar.get(Calendar.YEAR)) {
            LogTool.d(this.getClass().getName(), "same month");
            minMonth = startCalendar.get(Calendar.MONTH);
        } else {
            minMonth = 0;
        }
        wheelView2.setAdapter(new NumericWheelAdapter(minMonth + 1, 12));
        wheelView2.setCurrentItem(0, true);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // updateDays(wheelView1, wheelView2, wheelView3);
//		wheel.setCurrentItem(newValue);
        LogTool.d(this.getClass().getName(), "wheel.getCurrentItem() =" + wheel.getCurrentItem());
        if (wheel == wheelView1) {
            LogTool.d(this.getClass().getName(), "year change");
            chooseCalendar.set(Calendar.YEAR, minYear + wheelView1.getCurrentItem());
            LogTool.d(this.getClass().getName(), "Calendar.YEAR = " + chooseCalendar.get(Calendar.YEAR));
//			updateDays(wheelView3);
            updateMonth();
        } else if (wheel == wheelView2) {
            LogTool.d(this.getClass().getName(), "month change");
            LogTool.d(this.getClass().getName(), "minmonth =" + minMonth);
            chooseCalendar.set(Calendar.MONTH, minMonth + wheelView2.getCurrentItem() > 12 ? minMonth : (minMonth + wheelView2.getCurrentItem()));
            LogTool.d(this.getClass().getName(), "Calendar.YEAR = " + chooseCalendar.get(Calendar.YEAR));
            updateDays();
        } else {
            LogTool.d(this.getClass().getName(), "day change");
            chooseCalendar.set(Calendar.DAY_OF_MONTH,
                    wheelView3.getCurrentItem() + minDate);
            

            Log.i(this.getClass().getName(), chooseCalendar.get(Calendar.YEAR) + "-" + (chooseCalendar.get(Calendar.MONTH) + 1) + "-"
                    + chooseCalendar.get(Calendar.DAY_OF_MONTH));
        }
    }
}
