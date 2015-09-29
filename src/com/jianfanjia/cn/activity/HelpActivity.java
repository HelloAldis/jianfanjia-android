package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.ImageView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.tools.ViewPagerManager;
import com.jianfanjia.cn.tools.ViewPagerManager.ShapeType;

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
	private static final int IMG_ID[] = { R.drawable.p1, R.drawable.p2,
			R.drawable.p3, R.drawable.p4 };

	@Override
	public void initView() {
		init();
	}

	private void init() {
		ViewPagerManager contoler = new ViewPagerManager(HelpActivity.this);
		contoler.setmShapeType(ShapeType.OVAL);// 设置指示器的形状为矩形，默认是圆形
		List<View> bannerList = new ArrayList<View>();
		for (int i = 0; i < IMG_ID.length; i++) {
			ImageView imageView = new ImageView(HelpActivity.this);
			imageView.setBackgroundResource(IMG_ID[i]);
			bannerList.add(imageView);
		}
		contoler.init(bannerList);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_help;
	}

}
