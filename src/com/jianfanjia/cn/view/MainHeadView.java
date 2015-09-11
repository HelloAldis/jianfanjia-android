package com.jianfanjia.cn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.layout.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainHeadView extends RelativeLayout{
	
	private LayoutInflater inflater;
	private View mainHead;
	private RelativeLayout mainLayout;
	private CircleImageView headImageView;
	private TextView mainTitle;
	private TextView rigthTitle;
	private View divider;//·Ö¸îÏß

	public MainHeadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflater = LayoutInflater.from(context);
		mainHead = inflater.inflate(R.layout.common_head, null);
		mainLayout = (RelativeLayout) mainHead.findViewById(R.id.head_layout);
		headImageView = (CircleImageView) mainLayout.findViewById(R.id.icon_head);
		mainTitle = (TextView) mainLayout.findViewById(R.id.head_center_title);
		rigthTitle = (TextView) mainLayout.findViewById(R.id.head_right_title);
		divider = mainLayout.findViewById(R.id.head_divier);
		addView(mainHead,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}

	public MainHeadView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}

	public MainHeadView(Context context) {
		this(context,null,0);
		// TODO Auto-generated constructor stub
	}
	
	public void setMianTitle(String message){
		if(message == null) return;
		mainTitle.setText(message);
	}
	
	public void setRightTitle(String message){
		if(message == null)  return;
		rigthTitle.setText(message);
	}
	
	public void setBackgroundTransparent(){
		mainLayout.setBackgroundResource(R.color.transparent);
	}
	
	@Override
	public void setBackgroundColor(int resColor){
		mainLayout.setBackgroundResource(resColor);
	}
	
	public void setRightTitleVisable(int visibility){
		if(visibility != View.GONE || visibility != View.VISIBLE) return;
		rigthTitle.setVisibility(visibility);
	}
	
	public void setDividerVisable(int visibility){
		if(visibility != View.GONE || visibility != View.VISIBLE) return;
		divider.setVisibility(visibility);
	}
	
	public void setHeadImage(String imagePath){
		if(imagePath == null) return;
		ImageLoader.getInstance().displayImage(imagePath, headImageView);
	}
	
	public void setBackListener(OnClickListener onClickListener){
		if(onClickListener == null)  return;
		headImageView.setOnClickListener(onClickListener);
	}
	
	public void setRightTextListener(OnClickListener onClickListener){
		if(onClickListener == null)  return;
		rigthTitle.setOnClickListener(onClickListener);
	}
	
	public void setMainTextColor(int resColor){
		mainTitle.setTextColor(resColor);
	}
	
	
}
