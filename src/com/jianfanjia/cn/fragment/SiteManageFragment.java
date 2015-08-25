package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.NoteListAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.ProcedureInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.view.ScrollLayout;
import com.jianfanjia.cn.view.ScrollLayout.OnViewChangeListener;

public class SiteManageFragment extends BaseFragment {
	private static final String TAG = SiteManageFragment.class.getClass()
			.getName();
	private ArrayList<ProcedureInfo> procedureList;
	private SiteInfo site;
	private int currentPro;
	private LayoutInflater mLayoutInflater;
	private ListView detailNodeListView;
	private NoteListAdapter mNoteListAdapter;
	private ScrollLayout scrollLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		site = JianFanJiaApiClient.getAllSites(getActivity(), "18107218595",
				"1").get(0);// 默认拿到第一个工地
		currentPro = site.getCurrentPro();
		procedureList = site.getProcedures();
		Log.i(TAG, "pro =" + procedureList.get(currentPro).getNodeList().size());
	}

	@Override
	public void initView(View view) {
		scrollLayout = (ScrollLayout) view
				.findViewById(R.id.site_scroller_layout);
		scrollLayout.setmCurScreen(currentPro);
		scrollLayout.SetOnViewChangeListener(new OnViewChangeListener() {

			@Override
			public void OnViewChange(int view) {
				mNoteListAdapter.setProcedureInfo(procedureList.get(view));
				mNoteListAdapter.notifyDataSetChanged();
			}
		});

		initListView(view, procedureList.get(currentPro));
	}

	private void initListView(View view, ProcedureInfo procedure) {
		detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
		initCheck(detailNodeListView, procedure);
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

		/*
		 * final SwipeRefreshLayout mSuperSwipeRefreshLayout =
		 * (SwipeRefreshLayout)view.findViewById(R.id.site_viewpager_refresh);
		 * mSuperSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener()
		 * {
		 * 
		 * @Override public void onRefresh() {
		 * mSuperSwipeRefreshLayout.setRefreshing(true); new
		 * Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() {
		 * mSuperSwipeRefreshLayout.setRefreshing(false); makeTextShort("刷新成功");
		 * } },2000);
		 * 
		 * } });
		 */

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	// 设置工序验收
	private void initCheck(ListView detailNodeListView, ProcedureInfo procedure) {
		if (procedure.isProIsRequestCheck()) {
			View view = mLayoutInflater.inflate(R.layout.site_listview_head,
					null);
			TextView openCheckNode = (TextView) view
					.findViewById(R.id.site_list_item_content_expand_node_name);
			TextView closeCheckNode = (TextView) view
					.findViewById(R.id.site_list_item_content_small_node_name);
			openCheckNode.setText(procedure.getName() + "阶段验收");
			closeCheckNode.setText(procedure.getName() + "阶段验收");
			detailNodeListView.addHeaderView(view);
		}
	}

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
		// TODO Auto-generated method stub
		return R.layout.fragment_site;
	}

}
