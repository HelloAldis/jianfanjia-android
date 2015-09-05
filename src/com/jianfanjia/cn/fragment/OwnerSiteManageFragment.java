package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.jianfanjia.cn.cache.CacheManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
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
 * @Description: ҵ�����ع���
 * @author fengliang
 * @date 2015-8-26 ����11:14:00
 * 
 */
public class OwnerSiteManageFragment extends BaseFragment implements
		OnRefreshListener2<ScrollView> {
	private static final String TAG = OwnerSiteManageFragment.class.getName();
	private SwitchFragmentListener listener;
	private PullToRefreshScrollView mPullRefreshScrollView = null;
	private ArrayList<SectionInfo> sectionInfos;
	private ArrayList<SectionItemInfo> sectionItemInfos;
	private SectionInfo sectionInfo;
	private ProcessInfo processInfo;
	private int currentPro = -1;//��ǰ���й���
	private int currentList = -1;//��ǰչ����һ������
	private ViewPager viewPager;
	private ImageView icon_user_head = null;
	private TextView head_right_title = null;
	private ListView detailNodeListView;
	private SectionItemAdapter sectionItemAdapter;
	private MyViewPageAdapter myViewPageAdapter;
	private String[] checkSection = null;
	private String[] pro = null;
	private List<View> list = new ArrayList<View>();

	private RelativeLayout listHeadView;// listͷ��ͼ
	private TextView openCheckNode;// �����սڵ�����
	private TextView closeCheckNode;// �۵����սڵ�����

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
		if(savedInstanceState != null){
			currentList = savedInstanceState.getInt(Constant.CURRENT_LIST,-1);
		}
		processInfo = (ProcessInfo) CacheManager.getObjectByFile(getActivity(),
				Constant.PROCESSINFO_CACHE);
		LogTool.d(TAG, "processInfo=" + processInfo);
		if (processInfo != null) {
			initData();
		} else {
			getOwnerProcess();
		}
		pro = getResources().getStringArray(R.array.site_procedure);
		checkSection = getResources().getStringArray(R.array.site_procedure_check);
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
						mPullRefreshScrollView.onRefreshComplete();
						LogTool.d(TAG, "response:" + response.toString());
						try {
							if (response.has(Constant.DATA)) {
								processInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										ProcessInfo.class);
								if (processInfo != null) {
									// ��������ɹ������ڻ�����
									CacheManager.saveObject(getActivity(),
											processInfo,
											Constant.PROCESSINFO_CACHE);
									// ����ҵ�������ʦid
									shared.setValue(Constant.FINAL_DESIGNER_ID,
											processInfo.getFinal_designerid());
									handlerSuccess();
								} else {
									// ����ɹ�û�����ݣ�����Ĭ������

								}
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
						mPullRefreshScrollView.onRefreshComplete();
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						mPullRefreshScrollView.onRefreshComplete();
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		//���浱ǰչ���ĵڼ�������
		outState.putInt(Constant.CURRENT_LIST, currentList);
		super.onSaveInstanceState(outState);
	}
	
	//��ʼ������
	private void initData() {
		if (processInfo != null) {
			currentPro = MyApplication.getInstance().getPositionByItemName(
					processInfo.getGoing_on());
			if(currentList == -1){
				currentList = currentPro;
			}
			sectionInfos = processInfo.getSections();
			sectionInfo = sectionInfos.get(currentList);
			sectionItemInfos = sectionInfo.getItems();
			
			boolean isHeadViewShow = false;
			for(String sectionName : checkSection){
				Log.i(TAG, sectionName+ "--"+ sectionInfo.getName());
				if(sectionName.equals(sectionInfo.getName())){
					isHeadViewShow = true;
					break;
				}
			};
			
			if(isHeadViewShow){
				detailNodeListView.addHeaderView(listHeadView);
			}else{
				detailNodeListView.removeHeaderView(listHeadView);
			}
			
			sectionItemAdapter = new SectionItemAdapter(getActivity(),
					sectionItemInfos);
			detailNodeListView.setAdapter(sectionItemAdapter);
		}
	}
	
	private void updateData(){
		
	}

	private void handlerSuccess() {
		updateData();
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
		icon_user_head = (ImageView) view.findViewById(R.id.icon_user_head);
		head_right_title = (TextView) view.findViewById(R.id.head_right_title);
		initScrollLayout(view);
		initListView(view);
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
				if (sectionInfos != null) {
					if (currentPro != arg0) {
						currentPro = arg0;
						sectionInfo = sectionInfos.get(currentPro);
						sectionItemInfos = sectionInfo.getItems();
						sectionItemAdapter
								.setSectionItemInfos(sectionItemInfos);
						sectionItemAdapter.setLastClickItem(-1);
						sectionItemAdapter.notifyDataSetChanged();
					}
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

	private void initListView(View view) {
		initHeadView();
		detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
		detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sectionItemAdapter.setLastClickItem(position);
				sectionItemAdapter.notifyDataSetChanged();
			}
		});

	}

	private void initHeadView() {
		listHeadView = (RelativeLayout) inflater.inflate(
				R.layout.site_listview_head, null);
		openCheckNode = (TextView) listHeadView
				.findViewById(R.id.site_list_item_content_expand_node_name);
		closeCheckNode = (TextView) listHeadView
				.findViewById(R.id.site_list_item_content_small_node_name);
		listHeadView.findViewById(R.id.site_list_head_check)
				.setOnClickListener(this);
		listHeadView.findViewById(R.id.site_list_head_delay)
				.setOnClickListener(this);

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
		case R.id.site_list_head_check:
			break;
		case R.id.site_list_head_delay:
			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// ����ˢ��(�ӵ�һҳ��ʼװ������)
		// ��������
		getOwnerProcess();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// �������ظ���(������һҳ����)
		mPullRefreshScrollView.onRefreshComplete();
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site_manage;
	}

}
