package com.jianfanjia.cn.tools;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 
 * @ClassName: MyCountDownTimer
 * @Description: ����ʱ��
 * @author fengliang
 * @date 2015-8-18 ����3:04:15
 * 
 */
public class MyCountDownTimer extends CountDownTimer {
	private TextView textView;

	public MyCountDownTimer(long millisInFuture, long countDownInterval,
			TextView textView) {
		super(millisInFuture, countDownInterval);
		this.textView = textView;
	}

	@Override
	public void onFinish() {
		textView.setText("�ػ���֤��");
		textView.setEnabled(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		textView.setEnabled(false);
		textView.setText("(" + millisUntilFinished / 1000 + "s)���»�ȡ");
	}

}
