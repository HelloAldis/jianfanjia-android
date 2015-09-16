package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;

/**
 * 
 * @ClassName: HelpActivity
 * @Description: 帮助
 * @author fengliang
 * @date 2015-9-13 下午2:15:12
 * 
 */
public class HelpActivity extends BaseActivity {
	private static final String TAG = HelpActivity.class.getName();
	private ViewPager viewPager;
	private ViewGroup group = null;
	private ImageView[] tips;
	private List<View> bannerList = new ArrayList<View>();
	private static final int IMG_ID[] = { R.drawable.pic_welcome1,
			R.drawable.pic_welcome2, R.drawable.pic_welcome3,
			R.drawable.pic_welcome4 };
	private Bitmap bitmap = null;

	@Override
	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		group = (ViewGroup) findViewById(R.id.viewGroup);
		for (int i = 0; i < IMG_ID.length; i++) {
			ImageView view = new ImageView(this);
			view.setImageResource(IMG_ID[i]);
			bannerList.add(view);
		}
		// 将点点加入到ViewGroup中
		tips = new ImageView[bannerList.size()];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(HelpActivity.this);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.new_gallery_dianpu_selected);
			} else {
				tips[i].setBackgroundResource(R.drawable.new_gallery_dianpu_normal);
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(20, 20));
			layoutParams.leftMargin = 15;
			layoutParams.rightMargin = 15;
			group.addView(imageView, layoutParams);
		}
		ViewPageAdapter pageAdapter = new ViewPageAdapter(HelpActivity.this,
				bannerList);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				setImageBackground(arg0 % bannerList.size());
			}
		});
		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(0);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置选中的索引的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.new_gallery_dianpu_selected);
			} else {
				tips[i].setBackgroundResource(R.drawable.new_gallery_dianpu_normal);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "---onDestroy()");
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	public void processMessage(Message msg) {
		Bundle bundle = msg.getData();
		NotifyMessage message = (NotifyMessage) bundle
				.getSerializable("Notify");
		switch (msg.what) {
		case Constant.SENDBACKNOTICATION:
			sendNotifycation(message);
			break;
		case Constant.SENDNOTICATION:
			showNotify(message);
			break;
		default:
			break;
		}
	}
}
