package com.aldis.hud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Aldis on 16/3/25.
 */
public class HudDialog extends Dialog {
    private SpinnerView spinnerView;
    private TextView label;
    private String text;
    private FrameLayout customViewContainer;
    private DialogLayout dialogLayout;
//    private int width;
//    private int height;

    public HudDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hud);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = Hud.Style.dimAmount;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        setCanceledOnTouchOutside(false);

        initViews();
    }

    private void initViews() {
        this.dialogLayout = (DialogLayout) findViewById(R.id.background);
        this.dialogLayout.initLayoutUI(Hud.Style.backgroundColor, Hud.Style.cornerRadius);

        this.spinnerView = new SpinnerView(this.getContext());
        this.customViewContainer = (FrameLayout) findViewById(R.id.container);
        this.addViewToContainer(this.spinnerView);

        this.label = (TextView) findViewById(R.id.label);
        if (this.text != null) {
            this.label.setText(this.text);
            this.label.setVisibility(View.VISIBLE);
        } else {
            this.label.setVisibility(View.GONE);
        }
    }

    private void addViewToContainer(View view) {
        if (view == null) return;
        int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
        this.customViewContainer.addView(view, params);
    }
//
//    private void updateDialogSize() {
//        if (width > 0) {
//            ViewGroup.LayoutParams params = this.dialogLayout.getLayoutParams();
//            params.width = Util.dp2Pixel(this.width, getContext());
//            params.height = Util.dp2Pixel(this.height, getContext());
//            dialogLayout.setLayoutParams(params);
//        }
//    }

//    public void setView(View view) {
//        if (view != null) {
//            if (isShowing()) {
//                this.customViewContainer.removeAllViews();
//                addViewToContainer(view);
//            }
//        }
//    }

    public void setLabel(String text) {
        this.text = text;
        if (this.label != null) {
            if (this.label != null) {
                this.label.setText(this.text);
                this.label.setVisibility(View.VISIBLE);
            } else {
                this.label.setVisibility(View.GONE);
            }
        }
    }

//    public void setSize(int width, int height) {
//        this.width = width;
//        this.height = height;
//        if (dialogLayout != null) {
//            updateDialogSize();
//        }
//    }
}
