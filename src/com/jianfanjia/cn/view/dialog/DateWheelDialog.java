package com.jianfanjia.cn.view.dialog;

import java.util.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.view.wheel.NumericWheelAdapter;
import com.jianfanjia.cn.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.view.wheel.WheelView;

public class DateWheelDialog extends CommonDialog implements OnWheelChangedListener{
	
	public static final int VISIABLECOUNT = 5;
	
	private Calendar startCalendar;//开始时间
	private Calendar chooseCalendar;//选择时间
	
	int minYear;
	int maxYear;
	
	private LayoutInflater inflater;
	private WheelView wheelView1;
	private WheelView wheelView2;
	private WheelView wheelView3;

	public DateWheelDialog(Context context, boolean flag,
			OnCancelListener listener,Calendar calendar) {
		super(context, flag, listener);
		inflater = LayoutInflater.from(context);
		startCalendar = calendar;
		chooseCalendar = calendar;
		initView();
		// TODO Auto-generated constructor stub
	}

	public DateWheelDialog(Context context, int defStyle,Calendar calendar) {
		super(context, defStyle);
		inflater = LayoutInflater.from(context);
		startCalendar = calendar;
		chooseCalendar = calendar;
		initView();
		// TODO Auto-generated constructor stub
	}

	public DateWheelDialog(Context context,Calendar calendar) {
		super(context);
		inflater = LayoutInflater.from(context);
		startCalendar = calendar;
		chooseCalendar = calendar;
		initView();
	}
	
	public Calendar getChooseCalendar() {
		return chooseCalendar;
	}

	public void setStartCalendar(Calendar startCalendar) {
		this.startCalendar = startCalendar;
	}

	private void initView() {
		View view = inflater.inflate(R.layout.commont_wheel,null);
		setContent(view);
		wheelView1 = (WheelView)view.findViewById(R.id.wheel_item1);
		wheelView2 = (WheelView)view.findViewById(R.id.wheel_item2);
		wheelView3 = (WheelView)view.findViewById(R.id.wheel_item3);
		wheelView1.setVisibleItems(VISIABLECOUNT);
		wheelView2.setVisibleItems(VISIABLECOUNT);
		wheelView3.setVisibleItems(VISIABLECOUNT);
		
     // month  
        int curMonth = startCalendar.get(Calendar.MONTH);  
        wheelView2.setAdapter(new NumericWheelAdapter(1, 12));  
        wheelView2.setCurrentItem(curMonth);  
        wheelView2.addChangingListener(this);  
        wheelView2.setCyclic(true);  
      
        // year  
        int curYear = startCalendar.get(Calendar.YEAR);  
        minYear = curYear - 2;
        wheelView1.setAdapter(new NumericWheelAdapter(minYear,minYear + 4));  
        wheelView1.setCurrentItem(curYear- minYear);  
        wheelView1.addChangingListener(this);  
        wheelView1.setCyclic(true);  
          
        //day  
        wheelView3.setCyclic(true);  
        int maxDays = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
        wheelView3.setAdapter(new NumericWheelAdapter(1, maxDays));  
        wheelView3.setCurrentItem(startCalendar.get(Calendar.DAY_OF_MONTH)-1);  
        wheelView3.addChangingListener(this);  
        
        Log.i(this.getClass().getName(),curYear + "-" +curMonth + "-" + startCalendar.get(Calendar.DAY_OF_MONTH));
	}
	
	/** 
     * Updates day wheel. Sets max days according to selected month and year 
     */  
    void updateDays(WheelView day) { 
    	
    	int maxDays = chooseCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
        day.setAdapter(new NumericWheelAdapter(1, maxDays));  
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);  
        day.setCurrentItem(curDay - 1, true);  
          
        chooseCalendar.set(Calendar.DAY_OF_MONTH, curDay);  
    }

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
//		updateDays(wheelView1, wheelView2, wheelView3);
		wheel.setCurrentItem(newValue);
		if(wheel == wheelView1){
	    	chooseCalendar.set(Calendar.YEAR, minYear + wheel.getCurrentItem()); 
	    	updateDays(wheelView3);
		}else if(wheel == wheelView2){
	    	chooseCalendar.set(Calendar.MONTH,wheel.getCurrentItem()); 
	    	updateDays(wheelView3);
		}else{
		    chooseCalendar.set(Calendar.DAY_OF_MONTH, wheelView3.getCurrentItem() + 1);  
		}
	} 

}
