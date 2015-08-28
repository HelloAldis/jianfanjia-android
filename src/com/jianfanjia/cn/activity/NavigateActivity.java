package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseActivity;

/**
 * 
 * @ClassName: NavigateActivity
 * @Description: 引导
 * @author fengliang
 * @date 2015-8-28 下午3:23:37
 * 
 */
public class NavigateActivity extends BaseActivity implements OnClickListener {
	private ViewPager viewPager = null;
	private ViewGroup group = null;
	private Button enterBtn = null;
	private List<View> list = new ArrayList<View>();
	private ViewPageAdapter adapter = null;
	private int currentItem = 0; // 当前图片的索引号

	// private static final int imgId[] = { R.drawable.bg_1, R.drawable.bg_2,
	// R.drawable.bg_3 };

	@Override
	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		enterBtn = (Button) findViewById(R.id.enterMain);
		group = (ViewGroup) findViewById(R.id.viewGroup);
		// 导航测试资源
		// for (int i = 0; i < imgId.length; i++) {
		// ImageView view = new ImageView(this);
		// view.setBackgroundResource(imgId[i]);
		// list.add(view);
		// }
	}

	@Override
	public void setListener() {
		enterBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enterMain:

			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_navigate;
	}

}
