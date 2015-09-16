package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;

/**
 * 
 * @ClassName: PhotoBrowseActivity
 * @Description: Í¼Æ¬Ô¤ÀÀ
 * @author fengliang
 * @date 2015-9-14 ÏÂÎç6:10:50
 * 
 */
public class PhotoBrowseActivity extends BaseActivity {
	private static final String TAG = PhotoBrowseActivity.class.getName();
	private ViewPager viewPager = null;
	private ViewGroup group = null;
	private ImageView[] tips;
	private List<View> bannerList = new ArrayList<View>();

	@Override
	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		group = (ViewGroup) findViewById(R.id.viewGroup);

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_browse;
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
