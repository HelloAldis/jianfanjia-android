package com.jianfanjia.cn.activity.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jianfanjia.cn.activity.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.activity.view.wheel.WheelAdapter;
import com.jianfanjia.cn.activity.view.wheel.WheelView;
import com.jianfanjia.cn.activity.R;

public class CommonWheelDialog extends CommonDialog {

	private LayoutInflater inflater;
	private WheelView wheelView1;
	private WheelView wheelView2;
	private WheelView wheelView3;

	public CommonWheelDialog(Context context, boolean flag,
			OnCancelListener listener) {
		super(context, flag, listener);
		inflater = LayoutInflater.from(context);
		initView();
	}

	public CommonWheelDialog(Context context, int defStyle) {
		super(context, defStyle);
		inflater = LayoutInflater.from(context);
		initView();
	}

	public CommonWheelDialog(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		initView();
	}

	private void initView() {
		View view = inflater.inflate(R.layout.commont_wheel, null);
		setContent(view);
		wheelView1 = (WheelView) view.findViewById(R.id.wheel_item1);
		wheelView2 = (WheelView) view.findViewById(R.id.wheel_item2);
		wheelView3 = (WheelView) view.findViewById(R.id.wheel_item3);
	}

	public void setVisiableItem(int count) {
		wheelView1.setVisibleItems(count);
		wheelView2.setVisibleItems(count);
		wheelView3.setVisibleItems(count);
	}

	public void setWheelChangeListen(
			OnWheelChangedListener onWheelChangedListener) {
		wheelView1.addChangingListener(onWheelChangedListener);
		wheelView2.addChangingListener(onWheelChangedListener);
		wheelView3.addChangingListener(onWheelChangedListener);
	}

	public WheelView getWheelView1() {
		return wheelView1;
	}

	public WheelView getWheelView2() {
		return wheelView2;
	}

	public WheelView getWheelView3() {
		return wheelView3;
	}

	public void setWheelAdapter1(WheelAdapter wheelAdapter) {
		wheelView1.setAdapter(wheelAdapter);
		wheelView1.setCurrentItem(1);
	}

	public void setWheelAdapter2(WheelAdapter wheelAdapter) {
		wheelView2.setAdapter(wheelAdapter);
		wheelView2.setCurrentItem(1);
	}

	public void setWheelAdapter3(WheelAdapter wheelAdapter) {
		wheelView3.setAdapter(wheelAdapter);
		wheelView3.setCurrentItem(1);
	}

}
