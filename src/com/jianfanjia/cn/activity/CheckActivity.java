package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.MyGridViewAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.GridItem;

/**
 * 
 * @ClassName: CheckActivity
 * @Description: 验收
 * @author fengliang
 * @date 2015-8-28 下午2:25:36
 * 
 */
public class CheckActivity extends BaseActivity implements OnClickListener {
	private TextView backView=null;// 返回视图
	private GridView gridView = null;
	private static final int ICON[] = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher };
	private List<GridItem> gridList = new ArrayList<GridItem>();

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.check_pic_back);
		gridView = (GridView) findViewById(R.id.mygridview);
		gridView.setFocusable(false);
		initData();
	}

	private void initData() {
		for (int i = 0; i < ICON.length; i++) {
			GridItem item = new GridItem();
			item.setImgId(ICON[i]);
			gridList.add(item);
		}
		MyGridViewAdapter adapter = new MyGridViewAdapter(CheckActivity.this,
				gridList);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_check_pic;
	}
}
