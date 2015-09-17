package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.CaiGouNotifyFragment;
import com.jianfanjia.cn.fragment.FuKuanNotifyFragment;
import com.jianfanjia.cn.fragment.YanQiNotifyFragment;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.TabPageIndicator;

/**
 * 
 * @ClassName: NotifyActivity
 * @Description: 提醒
 * @author fengliang
 * @date 2015-9-11 上午9:49:10
 * 
 */
public class NotifyActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = NotifyActivity.class.getName();
	private SwitchFragmentListener listener = null;
	private MainHeadView mainHeadView = null;
	private TabPageIndicator mPageIndicator = null;
	private MyFragmentPagerAdapter adapter = null;
	private ViewPager mPager = null;// 页卡内容
	private List<SelectItem> listViews = new ArrayList<SelectItem>(); // Tab页面列表

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		try {
			listener = (SwitchFragmentListener) fragment;
		} catch (ClassCastException e) {
			LogTool.d(TAG, "e:" + e);
		}
	}

	@Override
	public void initView() {
		initMainHeadView();
		mPageIndicator = (TabPageIndicator) this
				.findViewById(R.id.page_indicator);
		mPager = (ViewPager) this.findViewById(R.id.vPager);
		initItem();
	}

	private void initMainHeadView() {
		mainHeadView = (MainHeadView) findViewById(R.id.notify_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources().getString(R.string.my_tip));
		mainHeadView.setBackgroundTransparent();
		mainHeadView.setDividerVisable(View.GONE);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	private void initItem() {
		SelectItem caigouItem = new SelectItem(new CaiGouNotifyFragment(),
				"采购提醒");
		SelectItem fukuanItem = new SelectItem(new FuKuanNotifyFragment(),
				"付款提醒");
		SelectItem yanqiItem = new SelectItem(new YanQiNotifyFragment(), "延期提醒");
		listViews.add(caigouItem);
		listViews.add(fukuanItem);
		listViews.add(yanqiItem);
		adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
		mPager.setAdapter(adapter);
		mPager.setOffscreenPageLimit(1);
		mPageIndicator.setViewPager(mPager, 0);
		mPageIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int pos) {

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_notify;
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
