package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.InfinitePagerAdapter;
import com.jianfanjia.cn.adapter.MyViewPageAdapter;
import com.jianfanjia.cn.adapter.SectionItemAdapter;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.ProcedureInfo;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.layout.ScrollLayout;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;

/**
 * 
 * @ClassName: DesignerSiteManageFragment
 * @Description:设计师工地管理
 * @author fengliang
 * @date 2015-8-28 上午11:10:21
 * 
 */
public class DesignerSiteManageFragment extends BaseFragment implements
		OnRefreshListener2<ScrollView> {
	private static final String TAG = DesignerSiteManageFragment.class
			.getName();
	private SwitchFragmentListener listener;
	private PullToRefreshScrollView mPullRefreshScrollView = null;
	private ScrollView mScrollView = null;
	private ArrayList<ProcedureInfo> procedureList;
	private SiteInfo site;
	private int currentPro;
	private LayoutInflater mLayoutInflater;
	private ViewPager bannerViewPager;
	private ViewGroup group = null;
	private ImageView[] tips;
	private List<View> bannerList = new ArrayList<View>();
	private ViewPager viewPager;
	private ImageView icon_user_head = null;
	private TextView head_right_title = null;
	private ListView detailNodeListView;
	private SectionItemAdapter sectionItemAdapter;
	private InfinitePagerAdapter infinitePagerAdapter = null;
	private MyViewPageAdapter myViewPageAdapter = null;
	private ScrollLayout scrollLayout;
	private String[] proTitle = null;
	private int size;
	private List<ViewPagerItem> list = new ArrayList<ViewPagerItem>();
	
	private RelativeLayout listHeadView;// list头视图
	private RelativeLayout smallHeadLayout;// 折叠layout
	private RelativeLayout expandHeadLayout;// 打开layout
	private TextView openCheckNode;// 打开验收节点名称
	private TextView closeCheckNode;// 折叠验收节点名称
	
	private ProcessInfo processInfo;

	private static final int CHANGE_PHOTO = 1;
	private static final int CHANGE_TIME = 5000;// 图片自动切换时间
	private static final int BANNER_ICON[] = { R.drawable.bg_home_banner1,
			R.drawable.bg_home_banner2, R.drawable.bg_home_banner3,
			R.drawable.bg_home_banner4 };

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_PHOTO:
				int index = bannerViewPager.getCurrentItem();
				if (index == bannerList.size() - 1) {
					index = -1;
				}
				bannerViewPager.setCurrentItem(index + 1);
				handler.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (SwitchFragmentListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		proTitle = getResources().getStringArray(R.array.site_procedure);
		Log.i(TAG, "proTitle=" + proTitle);
	}

	@Override
	public void initView(View view) {
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		mScrollView = mPullRefreshScrollView.getRefreshableView();
		icon_user_head = (ImageView) view.findViewById(R.id.icon_user_head);
		head_right_title = (TextView) view.findViewById(R.id.head_right_title);
		initBannerView(view);
		initProcessInfo();
		initScrollLayout(view);
		initListView(view);
	}
	
	private void initProcessInfo(){
		processInfo = DataManager.getInstance().getDefaultProcessInfo();
	}
	
	private void initListView(View view) {
		detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
		detailNodeListView.setFocusable(false);
		initHeadView(detailNodeListView);
		detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					// 点击listview head头视图，先判断头视图是否显示，是否有对比验收
					if (listHeadView.getVisibility() == View.VISIBLE) {
						if (expandHeadLayout.getVisibility() == View.VISIBLE) {
							expandHeadLayout.setVisibility(View.GONE);
							smallHeadLayout.setVisibility(View.VISIBLE);
						} else {
							expandHeadLayout.setVisibility(View.VISIBLE);
							smallHeadLayout.setVisibility(View.GONE);
						}
					}
				} else {
					// 点击listview item项
					sectionItemAdapter.setLastClickItem(position - 1,false);
					sectionItemAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	// 初始化listview head视图
	private void initHeadView(ListView listView) {
		View view = inflater.inflate(R.layout.site_listview_head, null);
		listHeadView = (RelativeLayout) view
				.findViewById(R.id.site_listview_item_container);
		openCheckNode = (TextView) listHeadView
				.findViewById(R.id.site_list_item_content_expand_node_name);
		closeCheckNode = (TextView) listHeadView
				.findViewById(R.id.site_list_item_content_small_node_name);
		listHeadView.findViewById(R.id.site_list_head_check)
				.setOnClickListener(this);
		listHeadView.findViewById(R.id.site_list_head_delay)
				.setOnClickListener(this);
		smallHeadLayout = (RelativeLayout) listHeadView
				.findViewById(R.id.site_listview_item_content_small);
		expandHeadLayout = (RelativeLayout) listHeadView
				.findViewById(R.id.site_listview_item_content_expand);
		listView.addHeaderView(view);
	}

	private void initBannerView(View view) {
		bannerViewPager = (ViewPager) view.findViewById(R.id.bannerViewPager);
		group = (ViewGroup) view.findViewById(R.id.viewGroup);
		for (int i = 0; i < BANNER_ICON.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setBackgroundResource(BANNER_ICON[i]);
			bannerList.add(imageView);
		}
		// 将点点加入到ViewGroup中
		tips = new ImageView[bannerList.size()];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(getActivity());
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
		ViewPageAdapter pageAdapter = new ViewPageAdapter(getActivity(),
				bannerList);
		bannerViewPager.setOnPageChangeListener(new OnPageChangeListener() {
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
		bannerViewPager.setAdapter(pageAdapter);
		bannerViewPager.setCurrentItem(0);
		handler.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
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

	private void initScrollLayout(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		for (int i = 0; i < proTitle.length; i++) {
			ViewPagerItem viewPagerItem = new ViewPagerItem();
			viewPagerItem.setResId(getResources().getIdentifier(
					"icon_home_normal" + (i + 1), "drawable",
					MyApplication.getInstance().getPackageName()));
			viewPagerItem.setTitle(proTitle[i]);
			list.add(viewPagerItem);
		}
		myViewPageAdapter = new MyViewPageAdapter(getActivity(), list);
		infinitePagerAdapter = new InfinitePagerAdapter(myViewPageAdapter);
		viewPager.setAdapter(infinitePagerAdapter);
	}

	@Override
	public void setListener() {
		icon_user_head.setOnClickListener(this);
		head_right_title.setOnClickListener(this);
		mPullRefreshScrollView.setOnRefreshListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon_user_head:
			((MainActivity) getActivity()).getSlidingPaneLayout().openPane();
			break;
		case R.id.head_right_title:
			listener.switchFragment(Constant.MYSITE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 下拉刷新(从第一页开始装载数据)
		mPullRefreshScrollView.onRefreshComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 上拉加载更多(加载下一页数据)
		mPullRefreshScrollView.onRefreshComplete();
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
