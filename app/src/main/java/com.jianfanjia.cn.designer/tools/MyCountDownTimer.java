package com.jianfanjia.cn.designer.tools;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author fengliang
 * @ClassName: MyCountDownTimer
 * @Description: 倒计时器
 * @date 2015-8-18 下午3:04:15
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
        textView.setText("重获验证码");
        textView.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setEnabled(false);
        textView.setText("(" + millisUntilFinished / 1000 + "s)重新获取");
    }

}
