package com.jianfanjia.cn.designer.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.bean.NotifyMessage;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.interf.DialogListener;
import com.jianfanjia.cn.designer.tools.LogTool;

/**
 * @author fengliang
 * @ClassName: NotifyDialog
 * @Description:提醒对话框
 * @date 2015-8-29 上午10:17:02
 */
public class NotifyDialog extends Dialog implements
        View.OnClickListener {
    private static final String TAG = NotifyDialog.class.getName();
    private DialogListener listener;
    private NotifyMessage message;

    public NotifyDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public NotifyDialog(Context context, NotifyMessage message, int theme,
                        DialogListener listener) {
        super(context, theme);
        this.message = message;
        this.listener = listener;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.notify_dialog);
        TextView titleTv = (TextView) findViewById(R.id.titleTv);
        TextView contentTv = (TextView) findViewById(R.id.contentTv);
        Button ok = (Button) findViewById(R.id.btn_ok);
        Button agree = (Button) findViewById(R.id.btn_agree);
        Button refuse = (Button) findViewById(R.id.btn_refuse);
        ok.setOnClickListener(this);
        agree.setOnClickListener(this);
        refuse.setOnClickListener(this);
        String status = message.getStatus();
        LogTool.d(TAG, "status=" + status);
        String type = message.getType();
        if (type.equals(Constant.CAIGOU_NOTIFY)) {
            titleTv.setText(context.getResources().getString(
                    R.string.caigouText));
            ok.setVisibility(View.VISIBLE);
            agree.setVisibility(View.GONE);
            refuse.setVisibility(View.GONE);
        } else if (type.equals(Constant.FUKUAN_NOTIFY)) {
            titleTv.setText(context.getResources().getString(
                    R.string.fukuanText));
            ok.setVisibility(View.VISIBLE);
            agree.setVisibility(View.GONE);
            refuse.setVisibility(View.GONE);
        } else if (type.equals(Constant.YANQI_NOTIFY)) {
            if (!TextUtils.isEmpty(status)) {
                if (status.equals(Constant.YANQI_BE_DOING)) {
                    titleTv.setText(context.getResources().getString(
                            R.string.yanqiText));
                    ok.setVisibility(View.GONE);
                    agree.setVisibility(View.VISIBLE);
                    refuse.setVisibility(View.VISIBLE);
                } else if (status.equals(Constant.YANQI_AGREE)) {
                    titleTv.setText(context.getResources().getString(
                            R.string.yanqiText));
                    ok.setVisibility(View.VISIBLE);
                    agree.setVisibility(View.GONE);
                    refuse.setVisibility(View.GONE);
                } else if (status.equals(Constant.YANQI_REFUSE)) {
                    titleTv.setText(context.getResources().getString(
                            R.string.yanqiText));
                    ok.setVisibility(View.VISIBLE);
                    agree.setVisibility(View.GONE);
                    refuse.setVisibility(View.GONE);
                }
            }
        } else {
            titleTv.setText(context.getResources().getString(
                    R.string.yanshouText));
            ok.setVisibility(View.VISIBLE);
            agree.setVisibility(View.GONE);
            refuse.setVisibility(View.GONE);
        }
        contentTv.setText(message.getContent());
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.btn_ok:
                dismiss();
                listener.onConfirmButtonClick();
                break;
            case R.id.btn_agree:
                dismiss();
                listener.onPositiveButtonClick();
                break;
            case R.id.btn_refuse:
                dismiss();
                listener.onNegativeButtonClick();
                break;
            default:
                break;
        }
    }

}
