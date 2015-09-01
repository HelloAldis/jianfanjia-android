package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyViewPageAdapter;
import com.jianfanjia.cn.adapter.SectionItemAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: OwnerSiteManageFragment
 * @Description: 业主工地管理
 * @author fengliang
 * @date 2015-8-26 上午11:14:00
 * 
 */
public class OwnerSiteManageFragment extends BaseFragment implements
		OnRefreshListener2<ScrollView> {
	private static final String TAG = OwnerSiteManageFragment.class.getName();
	private PullToRefreshScrollView mPullRefreshScrollView = null;
	private ScrollView mScrollView = null;
	private ArrayList<SectionInfo> sectionInfos;
	private ArrayList<SectionItemInfo> sectionItemInfos;
	private SectionInfo sectionInfo;
	private ProcessInfo processInfo;
	private int currentPro = 0;
	private ViewPager viewPager;
	private ImageView icon_user_head = null;
	private ListView detailNodeListView;
	private SectionItemAdapter sectionItemAdapter;
	private MyViewPageAdapter myViewPageAdapter;
	private String[] pro = null;
	private int size;
	private List<View> list = new ArrayList<View>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		processInfo = MyApplication.getInstance().getProcessInfo();
		if (processInfo == null) {
			getOwnerProcess();
		} else {
			setData();
		}
		pro = getResources().getStringArray(R.array.site_procedure);
	}

	private void getOwnerProcess() {
		JianFanJiaApiClient.get_Owner_Process(getApplication(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						try {
							if (response.has(Constant.DATA)) {
								processInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										ProcessInfo.class);
								MyApplication.getInstance().setProcessInfo(
										processInfo);
								handlerSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	private void setData() {
		if (processInfo != null) {
			currentPro = MyApplication.getInstance().getPositionByItemName(
					processInfo.getGoing_on());
			sectionInfos = processInfo.getSections();
			sectionInfo = sectionInfos.get(currentPro);
			sectionItemInfos = sectionInfo.getItems();
		}
	}

	private void handlerSuccess() {
		setData();
		sectionItemAdapter.setSectionItemInfos(sectionItemInfos);
		sectionItemAdapter.notifyDataSetChanged();
		for (int i = 0; i < pro.length; i++) {
			View siteHead = list.get(i);
			initItem(siteHead, i);
		}
		myViewPageAdapter.notifyDataSetChanged();
	}

	@Override
	public void initView(View view) {
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		mScrollView = mPullRefreshScrollView.getRefreshableView();
		icon_user_head = (ImageView) view.findViewById(R.id.icon_user_head);
		initScrollLayout(view);
		initListView(view, sectionItemInfos);
	}

	private void initScrollLayout(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		for (int i = 0; i < pro.length; i++) {
			View siteHead = inflater.inflate(R.layout.site_head_item, null);
			initItem(siteHead, i);
			list.add(siteHead);
		}
		myViewPageAdapter = new MyViewPageAdapter(getActivity(), list);
		viewPager.setAdapter(myViewPageAdapter);
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
				if (currentPro != arg0) {
					currentPro = arg0;
					sectionInfo = sectionInfos.get(currentPro);
					sectionItemInfos = sectionInfo.getItems();
					sectionItemAdapter.setSectionItemInfos(sectionItemInfos);
					sectionItemAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void initItem(View siteHead, int position) {
		Log.i(TAG, "initItem" + position);
		TextView proName = (TextView) siteHead
				.findViewById(R.id.site_head_procedure_name);
		proName.setText(pro[position]);
		TextView proDate = (TextView) siteHead
				.findViewById(R.id.site_head_procedure_date);
		if (sectionInfos != null) {
			proDate.setText(DateFormatTool.covertLongToString(
					sectionInfos.get(position).getStart_at(), "M.dd")
					+ "-"
					+ DateFormatTool.covertLongToString(
							sectionInfos.get(position).getEnd_at(), "M.dd"));
		}
		ImageView icon = (ImageView) siteHead
				.findViewById(R.id.site_head_procedure_icon);
		icon.setImageResource(getResources().getIdentifier(
				"icon_home_normal" + (position + 1), "drawable",
				MyApplication.getInstance().getPackageName()));
	}

	private void initListView(View view,
			ArrayList<SectionItemInfo> sectionItemInfos) {
		detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
		// initCheck(detailNodeListView, sectionInfo);
		sectionItemAdapter = new SectionItemAdapter(getActivity(),
				sectionItemInfos);
		detailNodeListView.setAdapter(sectionItemAdapter);
		detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View lastClickItem = parent.getChildAt(sectionItemAdapter
						.getLastClickItem());
				if (position != sectionItemAdapter.getLastClickItem()) {
					/*lastClickItem.findViewById(
							R.id.site_listview_item_content_expand)
							.setVisibility(View.GONE);
					lastClickItem.findViewById(
							R.id.site_listview_item_content_small)
							.setVisibility(View.VISIBLE);
					view.findViewById(R.id.site_listview_item_content_expand)
							.setVisibility(View.VISIBLE);
					view.findViewById(R.id.site_listview_item_content_small)
							.setVisibility(View.GONE);*/
					sectionItemAdapter.setLastClickItem(position);
					sectionItemAdapter.notifyDataSetChanged();
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
		mPullRefreshScrollView.setOnRefreshListener(this);
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

	/*
	 * // 设置工序验收 private void initCheck(ListView detailNodeListView,
	 * ProcedureInfo procedure) { if (procedure.isProIsRequestCheck()) { View
	 * view = mLayoutInflater.inflate(R.layout.site_listview_head, null);
	 * TextView openCheckNode = (TextView) view
	 * .findViewById(R.id.site_list_item_content_expand_node_name); TextView
	 * closeCheckNode = (TextView) view
	 * .findViewById(R.id.site_list_item_content_small_node_name);
	 * openCheckNode.setText(procedure.getName() + "阶段验收");
	 * closeCheckNode.setText(procedure.getName() + "阶段验收");
	 * detailNodeListView.addHeaderView(view); } }
	 */

	/*
	 * private void setHead(final int position,View view,ProcedureInfo
	 * procedure){ if(procedure.isProIsFinish()){
	 * view.findViewById(R.id.site_head_procedure_icon
	 * ).setBackgroundResource(R.drawable.site_viewpager_item_selected_bg);
	 * }else{
	 * view.findViewById(R.id.site_head_procedure_icon).setBackgroundResource
	 * (R.drawable.site_viewpager_item_normal_bg); } TextView name =
	 * (TextView)view.findViewById(R.id.site_head_procedure_name);
	 * name.setText(procedure.getName()); TextView date =
	 * (TextView)view.findViewById(R.id.site_head_procedure_date);
	 * date.setText(procedure.getDate()); view.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * mViewPager.setCurrentItem(position); } }); }
	 * 
	 * //根据当前所在工序初始化显示的工序头列表，因为是循环显示的，所以有个归零的状态 private void
	 * initHeadShowList(int firstItem) { int temp = firstItem; for(int i=
	 * 0;i<4;i++){ if(temp >= procedureList.size()){ temp = 0; }
	 * headShowProcedure.add(procedureList.get(temp)); temp++; } }
	 */

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site_manage;
	}
}
