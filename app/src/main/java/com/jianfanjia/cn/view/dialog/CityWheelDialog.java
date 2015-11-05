package com.jianfanjia.cn.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.CityFormatTool;
import com.jianfanjia.cn.view.wheel.ArrayWheelAdapter;
import com.jianfanjia.cn.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.view.wheel.WheelView;

import java.util.Map;

public class CityWheelDialog extends CommonDialog implements
		OnWheelChangedListener {

	public static final int VISIABLECOUNT = 5;

	private String provice;
	private String city;
	private String district;

	private int prolength;
	private int cityLength;
	private int districtLength;

	private int currentPro=0;
	private int currentCity = 0;
	private int currentDistrict = 0;

	private String jsondata;

	private String[] provices;
	private String[] citys;
	private String[] districts;
	private Map<String,String[]> cityMap;
	private Map<String,String[]> districtMap;

	private LayoutInflater inflater;
	private WheelView wheelView1;
	private WheelView wheelView2;
	private WheelView wheelView3;

	public CityWheelDialog(Context context, boolean flag,
						   OnCancelListener listener) {
		super(context, flag, listener);
		inflater = LayoutInflater.from(context);
		initData();
		initView();
	}

	private  void initData(){
		provices = CityFormatTool.getProviceList();
		cityMap = CityFormatTool.getCityMap();
		districtMap = CityFormatTool.getDistrictMap();

		provice = provices[0];
		citys = cityMap.get(provice);
		city = citys[0];
		districts = districtMap.get(city);
		district = districts[0];



		prolength = provices.length;
		cityLength = cityMap.get(provice).length;
		districtLength = districtMap.get(city).length;
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
		wheelView1.setAdapter(new ArrayWheelAdapter<String>(provices, prolength));
		wheelView1.setCurrentItem(currentPro);
		wheelView1.addChangingListener(this);
		wheelView1.setCyclic(false);

		// year
		wheelView2.setAdapter(new ArrayWheelAdapter<String>(citys, cityLength));
		wheelView2.setCurrentItem(currentCity);
		wheelView2.addChangingListener(this);
		wheelView2.setCyclic(false);

		// day
		// year
		wheelView3.setAdapter(new ArrayWheelAdapter<String>(districts, districtLength));
		wheelView3.setCurrentItem(currentDistrict);
		wheelView3.addChangingListener(this);
		wheelView3.setCyclic(false);

		/*Log.i(this.getClass().getName(), curYear + "-" + curMonth + "-"
				+ startCalendar.get(Calendar.DAY_OF_MONTH));*/
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView day) {

	/*	int maxDays = chooseCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setAdapter(new NumericWheelAdapter(1, maxDays));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);

		chooseCalendar.set(Calendar.DAY_OF_MONTH, curDay);*/
	}

	void updateCity(){

		provice = provices[wheelView1.getCurrentItem()];
		citys = cityMap.get(provice);
		cityLength = citys.length;
		currentCity = 0;
		city = citys[currentCity];
		wheelView2.setAdapter(new ArrayWheelAdapter<String>(citys,cityLength));
		wheelView2.setCurrentItem(0);

		updateDistrict();
	}

	void updateDistrict(){

		city = citys[wheelView2.getCurrentItem()];
		districts = districtMap.get(city);
		districtLength = districts.length;
		currentDistrict = 0;
		district = districts[0];
		wheelView3.setAdapter(new ArrayWheelAdapter<String>(districts,districtLength));
		wheelView3.setCurrentItem(currentDistrict);

	}


	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// updateDays(wheelView1, wheelView2, wheelView3);
		wheel.setCurrentItem(newValue);
		if (wheel == wheelView1) {
			updateCity();
		} else if (wheel == wheelView2) {
			updateDistrict();
		} else {
		}
	}

}
