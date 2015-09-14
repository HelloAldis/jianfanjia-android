package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jianfanjia.cn.base.BaseActivity;

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

}
