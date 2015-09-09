package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;

/**
 * 
 * @ClassName: NavigateActivity
 * @Description: 引导
 * @author fengliang
 * @date 2015-8-28 下午3:23:37
 * 
 */
public class NavigateActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener {
	private ViewPager viewPager = null;
	private ImageView btn_welcome_off = null;
	private LinearLayout btnLayout = null;
	private Button btnRegister = null;
	private Button btnLogin = null;
	private List<View> list = new ArrayList<View>();
	private ViewPageAdapter adapter = null;
	private int currentItem = 0; // 当前图片的索引号
	private static final int imgId[] = { R.drawable.navigate_1,
			R.drawable.navigate_2, R.drawable.navigate_3, R.drawable.navigate_4 };

	@Override
	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		btn_welcome_off = (ImageView) findViewById(R.id.btn_welcome_off);
		btnLayout = (LinearLayout) findViewById(R.id.btnLayout);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		// 导航测试资源
		for (int i = 0; i < imgId.length; i++) {
			ImageView view = new ImageView(this);
			view.setBackgroundResource(imgId[i]);
			list.add(view);
		}
		adapter = new ViewPageAdapter(NavigateActivity.this, list);
		viewPager.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		viewPager.setOnPageChangeListener(this);
		btn_welcome_off.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		sharedPrefer.setValue(Constant.ISFIRST, 1);
		switch (v.getId()) {
		case R.id.btn_welcome_off:
			startActivity(LoginActivity.class);
			finish();
			break;
		case R.id.btnRegister:
			startActivity(RegisterActivity.class);
			finish();
			break;
		case R.id.btnLogin:
			startActivity(LoginActivity.class);
			finish();
			break;
		default:
			break;
		}
	}

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
		currentItem = arg0;
		if (currentItem == list.size() - 1) {
			btnLayout.setVisibility(View.VISIBLE);
		} else {
			btnLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_navigate;
	}

}
