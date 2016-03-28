package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;

/**
 * Description:com.jianfanjia.cn.view
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 21:58
 */
public class MainHeadView extends RelativeLayout {
    private LayoutInflater inflater;
    private View mainHead;
    private RelativeLayout mainLayout;
    private RelativeLayout backLayout;
    //	private CircleImageView headImageView;
    private TextView mainTitle;
    private TextView rigthTitle;
    private View divider;// 分割线

    public MainHeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflater = LayoutInflater.from(context);
        mainHead = inflater.inflate(R.layout.common_head, null);
        mainLayout = (RelativeLayout) mainHead.findViewById(R.id.main_head_layout);
        mainTitle = (TextView) mainLayout.findViewById(R.id.head_center_title);
        rigthTitle = (TextView) mainLayout.findViewById(R.id.head_right_title);
        divider = mainLayout.findViewById(R.id.head_divier);
        backLayout = (RelativeLayout) mainLayout.findViewById(R.id.head_back_layout);
        addView(mainHead, new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public MainHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MainHeadView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    public void setMianTitle(String message) {
        if (message == null)
            return;
        mainTitle.setText(message);
    }

    public void setMianTitleColor() {
        SpannableStringBuilder builder = new SpannableStringBuilder(mainTitle.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#fe7003"));
        builder.setSpan(span, mainTitle.getText().length() - 2, mainTitle.getText().length() - 1, Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        mainTitle.setText(builder);
    }

    public void setRightTitle(String message) {
        if (message == null)
            return;
        rigthTitle.setText(message);
    }

    public void setBackgroundTransparent() {
        mainLayout.setBackgroundResource(R.color.transparent);
    }

    public void setLayoutBackground(int resColor) {
        mainLayout.setBackgroundResource(resColor);
    }

    public void setRightTitleVisable(int visibility) {
        if (visibility != View.GONE || visibility != View.VISIBLE)
            return;
        rigthTitle.setVisibility(visibility);
    }

    public void setBackLayoutVisable(int visibility) {
        if (visibility != View.GONE && visibility != View.VISIBLE)
            return;
        backLayout.setVisibility(visibility);
    }

    public void setDividerVisable(int visibility) {
        if (visibility != View.GONE && visibility != View.VISIBLE)
            return;
        divider.setVisibility(visibility);
    }

    public void setBackListener(OnClickListener onClickListener) {
        if (onClickListener == null)
            return;
        backLayout.setOnClickListener(onClickListener);
    }

    public void setRightTextListener(OnClickListener onClickListener) {
        if (onClickListener == null)
            return;
        rigthTitle.setOnClickListener(onClickListener);
    }

    public void setRigthTitleEnable(boolean isAble) {
        rigthTitle.setEnabled(isAble);
        if (isAble) {
            rigthTitle.setTextColor(getResources().getColor(R.color.orange_color));
        } else {
            rigthTitle.setTextColor(getResources().getColor(R.color.middle_grey_color));
        }
    }


}

