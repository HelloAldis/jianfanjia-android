package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.NoteListAdapter;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.ProcedureInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.view.ScrollLayout;

/**
 * 
 * @ClassName: DesignerSiteManageFragment
 * @Description:设计师工地管理
 * @author fengliang
 * @date 2015-8-28 上午11:10:21
 * 
 */
public class DesignerSiteManageFragment extends BaseFragment {
	private static final String TAG = DesignerSiteManageFragment.class
			.getClass().getName();
	private PullToRefreshScrollView mPullRefreshScrollView = null;
	private ScrollView mScrollView = null;
	private ArrayList<ProcedureInfo> procedureList;
	private SiteInfo site;
	private int currentPro;
	private LayoutInflater mLayoutInflater;
	private ViewPager viewPager;
	private ImageView icon_user_head = null;
	private ListView detailNodeListView;
	private NoteListAdapter mNoteListAdapter;
	private ScrollLayout scrollLayout;
	private String[] pro = null;
	private int size;
	private List<View> list = new ArrayList<View>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		site = JianFanJiaApiClient.getAllSites(getActivity(), "18107218595",
				"1").get(0);// 默认拿到第一个工地
		currentPro = site.getCurrentPro();
		procedureList = site.getProcedures();
		size = procedureList.size();
		pro = getResources().getStringArray(R.array.site_procedure);
		Log.i(this.getClass().getName(), "pro ="
				+ procedureList.get(currentPro).getNodeList().size());
		Log.i(TAG, "pro =" + procedureList.get(currentPro).getNodeList().size());
	}

	@Override
	public void initView(View view) {
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		mScrollView = mPullRefreshScrollView.getRefreshableView();
		icon_user_head = (ImageView) view.findViewById(R.id.icon_user_head);
		initScrollLayout(view);
		initListView(view, procedureList.get(currentPro));
	}

	private void initScrollLayout(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		// scrollLayout = (ScrollLayout) view
		// .findViewById(R.id.site_scroller_layout);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < pro.length; i++) {
			View siteHead = inflater.inflate(R.layout.site_head_item, null);
			initItem(siteHead, i);
			// scrollLayout.addView(siteHead, lp);
			list.add(siteHead);
		}
		ViewPageAdapter pageAdapter = new ViewPageAdapter(getActivity(), list);
		viewPager.setAdapter(pageAdapter);
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

			}
		});
		//
		// scrollLayout = (ScrollLayout) view
		// .findViewById(R.id.site_scroller_layout);
		// scrollLayout.setmCurScreen(currentPro);
		// scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {
		//
		// @Override
		// public void OnViewChange(int view) {
		// mNoteListAdapter.setProcedureInfo(procedureList.get(view));
		// mNoteListAdapter.notifyDataSetChanged();
		// }
		// });
		initListView(view, procedureList.get(currentPro));
	}

	private void initItem(View siteHead, int position) {
		View lineView = (View) siteHead.findViewById(R.id.lineView);
		if (position == 0) {
			lineView.setVisibility(View.INVISIBLE);
		}
		TextView proName = (TextView) siteHead
				.findViewById(R.id.site_head_procedure_name);
		proName.setText(pro[position]);
		TextView proDate = (TextView) siteHead
				.findViewById(R.id.site_head_procedure_date);
		proDate.setText(procedureList.get(position >= size ? 0 : position)
				.getDate());
		ImageView icon = (ImageView) siteHead
				.findViewById(R.id.site_head_procedure_icon);
	}

	private void initListView(View view, ProcedureInfo procedure) {
		detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
		mNoteListAdapter = new NoteListAdapter(procedure, getActivity());
		detailNodeListView.setAdapter(mNoteListAdapter);
		detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View lastClickItem = parent.getChildAt(mNoteListAdapter
						.getLastClickItem());
				if (position != mNoteListAdapter.getLastClickItem()) {
					lastClickItem.findViewById(
							R.id.site_listview_item_content_expand)
							.setVisibility(View.GONE);
					lastClickItem.findViewById(
							R.id.site_listview_item_content_small)
							.setVisibility(View.VISIBLE);
					view.findViewById(R.id.site_listview_item_content_expand)
							.setVisibility(View.VISIBLE);
					view.findViewById(R.id.site_listview_item_content_small)
							.setVisibility(View.GONE);
					mNoteListAdapter.setLastClickItem(position);
				} else {
					int visible = view.findViewById(
							R.id.site_listview_item_content_expand)
							.getVisibility();
					if (visible == View.GONE) {
						view.findViewById(
								R.id.site_listview_item_content_expand)
								.setVisibility(View.VISIBLE);
						view.findViewById(R.id.site_listview_item_content_small)
								.setVisibility(View.GONE);
					} else {
						view.findViewById(
								R.id.site_listview_item_content_expand)
								.setVisibility(View.GONE);
						view.findViewById(R.id.site_listview_item_content_small)
								.setVisibility(View.VISIBLE);
					}
				}
			}
		});

	}

	@Override
	public void setListener() {
		icon_user_head.setOnClickListener(this);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// 下拉刷新(从第一页开始装载数据)
						mPullRefreshScrollView.onRefreshComplete();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// 上拉加载更多(加载下一页数据)
						// mPullRefreshScrollView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon_user_head:
			((MainActivity) getActivity()).getSlidingPaneLayout().openPane();
			break;
		default:
			break;
		}
	}

	class HeadViewHold {
		ImageView headIcon;
		TextView headTile;
		TextView headDate;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_designer_site_manage;
	}

}
