package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.CheckActivity;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.InfinitePagerAdapter;
import com.jianfanjia.cn.adapter.MyViewPageAdapter;
import com.jianfanjia.cn.adapter.SectionItemAdapter;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
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
		OnRefreshListener2<ScrollView>, ItemClickCallBack, PopWindowCallBack {
	private static final String TAG = OwnerSiteManageFragment.class.getName();
	private SwitchFragmentListener listener;
	private LinearLayout layoutAll = null;
	private PullToRefreshScrollView mPullRefreshScrollView = null;
	private ArrayList<SectionInfo> sectionInfos;
	private ArrayList<SectionItemInfo> sectionItemInfos;
	private SectionInfo sectionInfo;
	private ProcessInfo processInfo;
	private int currentPro = -1;// 当前进行工序
	private int currentList = -1;// 当前展开第一道工序
	private ViewPager bannerViewPager;
	private ViewGroup group = null;
	private ImageView[] tips;
	private List<View> bannerList = new ArrayList<View>();
	private ViewPager processViewPager;
	private ImageView icon_user_head = null;
	private TextView head_right_title = null;
	private ListView detailNodeListView;
	private SectionItemAdapter sectionItemAdapter;
	private InfinitePagerAdapter infinitePagerAdapter = null;
	private MyViewPageAdapter myViewPageAdapter = null;
	private String[] checkSection = null;
	private String[] proTitle = null;
	private List<ViewPagerItem> list = new ArrayList<ViewPagerItem>();

	private RelativeLayout listHeadView;// list头视图
	private RelativeLayout smallHeadLayout;// 折叠layout
	private RelativeLayout expandHeadLayout;// 打开layout
	private TextView openCheckNode;// 打开验收节点名称
	private TextView closeCheckNode;// 折叠验收节点名称
	private TextView openDelay;//延期按钮
	private TextView openCheck;//对比验收按钮

	private boolean isOpen = false;

	private static final int CHANGE_PHOTO = 1;
	private static final int CHANGE_TIME = 5000;// 图片自动切换时间
	private static final int BANNER_ICON[] = { R.drawable.bg_home_banner1,
			R.drawable.bg_home_banner2, R.drawable.bg_home_banner3,
			R.drawable.bg_home_banner4 };

	private String processInfoId = null;

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
		if (savedInstanceState != null) {
			currentList = savedInstanceState.getInt(Constant.CURRENT_LIST, -1);
		}
		proTitle = getResources().getStringArray(R.array.site_procedure);
		checkSection = getResources().getStringArray(
				R.array.site_procedure_check);
		DataManager.getInstance().addObserver(this);
	}
	
	@Override
	public void initView(View view) {
		layoutAll = (LinearLayout) view.findViewById(R.id.layoutAll);
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		icon_user_head = (ImageView) view.findViewById(R.id.icon_user_head);
		head_right_title = (TextView) view.findViewById(R.id.head_right_title);
		initBannerView(view);
		initScrollLayout(view);
		initListView(view);
		initProcessInfo();
		if (processInfo != null) {
			initData();
		} else {
			DataManager.getInstance().requestProcessInfo();
		}
	}
	
	private void initProcessInfo(){
		processInfo = DataManager.getInstance().getDefaultProcessInfo();
	}

	public void update(Observable observable, Object data) {
		mPullRefreshScrollView.onRefreshComplete();
		if (DataManager.SUCCESS.equals(data)) {
			initProcessInfo();
			if (processInfo != null) {
				initData();
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// 保存当前展开的第几道工序
		outState.putInt(Constant.CURRENT_LIST, currentList);
		super.onSaveInstanceState(outState);
	}

	// 初始化数据
	private void initData() {
		if (processInfo != null) {
			currentPro = MyApplication.getInstance().getPositionByItemName(
					processInfo.getGoing_on());
			if (currentList == -1) {
				currentList = currentPro;
			}
			sectionInfos = processInfo.getSections();
			sectionInfo = sectionInfos.get(currentList);
			sectionItemInfos = sectionInfo.getItems();
			setCheckHeadView(sectionInfo.getName());
			setScrollHeadTime();
			sectionItemAdapter = new SectionItemAdapter(getActivity(),
					sectionItemInfos, currentList, this);
			detailNodeListView.setAdapter(sectionItemAdapter);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		processViewPager.setCurrentItem(currentList);
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

	private void setCheckHeadView(String name) {
		boolean isHeadViewShow = false;
		for (String sectionName : checkSection) {
			if (sectionName.equals(sectionInfo.getName())) {
				isHeadViewShow = true;
				break;
			}
		}
		if (isHeadViewShow) {
			listHeadView.setVisibility(View.VISIBLE);
		} else {
			listHeadView.setVisibility(View.GONE);
		}
	}

	private void initScrollLayout(View view) {
		processViewPager = (ViewPager) view.findViewById(R.id.processViewPager);
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
		processViewPager.setAdapter(infinitePagerAdapter);
		processViewPager.setOnPageChangeListener(new OnPageChangeListener() {
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
					if (currentList != arg0 % 7) {
						currentList = arg0 % 7;
						sectionInfo = sectionInfos.get(currentList);
						setCheckHeadView(sectionInfo.getName());
						sectionItemInfos = sectionInfo.getItems();
						sectionItemAdapter
								.setSectionItemInfos(sectionItemInfos);
						sectionItemAdapter.setLastClickItem(-1, isOpen);
						sectionItemAdapter.setCurrentPro(currentList);
						sectionItemAdapter.notifyDataSetChanged();
					}
				}
			}

			
		});
	}
	
	private void setScrollHeadTime() {
		/*if (sectionInfos != null) {
			for (int i = 0; i < proTitle.length; i++) {
				ViewPagerItem viewPagerItem = list.get(i);
				Log.i(TAG, DateFormatTool.covertLongToString(sectionInfos.get(i).getStart_at(), "M.dd")
						+ "-"
						+ DateFormatTool.covertLongToString(sectionInfos.get(i)
								.getEnd_at(), "M.dd"));
				viewPagerItem.setDate(DateFormatTool.covertLongToString(
						sectionInfos.get(i).getStart_at(), "M.dd")
						+ "-"
						+ DateFormatTool.covertLongToString(sectionInfos.get(i)
								.getEnd_at(), "M.dd"));
			}
			myViewPageAdapter.notifyDataSetChanged();
			infinitePagerAdapter.notifyDataSetChanged();
		} */
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
					// sectionItemAdapter.setLastClickItem(position - 1);
					// sectionItemAdapter.notifyDataSetChanged();
					SectionItemInfo sectionItemInfo = sectionItemInfos
							.get(position - 1);
					processInfoId = sectionItemInfo.getName();
					LogTool.d(TAG,
							"=============================================="
									+ sectionItemInfo.getName());
					if (isOpen) {
						isOpen = false;
					} else {
						isOpen = true;
					}
					sectionItemAdapter.setLastClickItem(position - 1, isOpen);
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
		openCheck = (TextView) listHeadView.findViewById(R.id.site_list_head_check);
		openCheck.setOnClickListener(this);
		openDelay = (TextView) listHeadView.findViewById(R.id.site_list_head_delay);
		openDelay.setOnClickListener(this);
		smallHeadLayout = (RelativeLayout) listHeadView
				.findViewById(R.id.site_listview_item_content_small);
		expandHeadLayout = (RelativeLayout) listHeadView
				.findViewById(R.id.site_listview_item_content_expand);
		//根据不同的用户类型显示不同的文字
		if(mUserType.equals(Constant.IDENTITY_DESIGNER)){
			openCheck.setText(getString(R.string.upload_pic));
		}else if(mUserType.equals(Constant.IDENTITY_OWNER)){
			openCheck.setText(getString(R.string.site_example_node_check));
		}
		listView.addHeaderView(view);
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
			Bundle bundle = new Bundle();
			bundle.putInt(Constant.CURRENT_LIST, currentList);
			startActivity(CheckActivity.class, bundle);
			break;
		case R.id.site_list_head_delay:
			DateWheelDialog dateWheelDialog = new DateWheelDialog(
					getActivity(), Calendar.getInstance());
			dateWheelDialog.setTitle("选择时间");
			dateWheelDialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							makeTextLong(StringUtils
									.getDateString(((DateWheelDialog) dialog)
											.getChooseCalendar().getTime()));
							dialog.dismiss();
						}
					});
			dateWheelDialog.setNegativeButton(R.string.no, null);
			dateWheelDialog.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void click(int position) {
		AddPhotoPopWindow addPhotoPopWindow = new AddPhotoPopWindow(
				getActivity(), this);
		addPhotoPopWindow.showAtLocation(layoutAll, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 下拉刷新(从第一页开始装载数据)
		// 加载数据
		DataManager.getInstance().requestProcessInfo();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 上拉加载更多(加载下一页数据)
		mPullRefreshScrollView.onRefreshComplete();
	}

	@Override
	public void takecamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
	}

	@Override
	public void takePhoto() {
		Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		albumIntent.setType("image/*");
		startActivityForResult(albumIntent, Constant.REQUESTCODE__LOCATION);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.REQUESTCODE_CAMERA:// 拍照
			LogTool.d(TAG, "data:" + data);
			if (data != null) {
				Uri mImageUri = data.getData();
				LogTool.d(TAG, "mImageUri:" + mImageUri);
				if (mImageUri != null) {

				}
			}
			break;
		case Constant.REQUESTCODE__LOCATION:// 本地选取
			if (data != null) {
				Uri uri = data.getData();
				LogTool.d(TAG, "uri:" + uri);
				if (null != uri) {
					String path = getPicture(uri);
					LogTool.d(TAG, "path:" + path);
					upload(path);//
				}
			}
			break;
		case Constant.REQUESTCODE__CROP:

			break;
		default:
			break;
		}
	}

	// 获取相册图片路径
	private String getPicture(Uri uri) {
		try {
			ContentResolver resolver = getActivity().getContentResolver();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = resolver.query(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);
			return path;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void upload(String imgPath) {
		JianFanJiaApiClient.uploadImage(getActivity(), imgPath,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String imageid = obj.getString("data");
								LogTool.d(TAG, "imageid:" + imageid);
								submitImageToProgress(processInfo.get_id(),
										sectionInfo.getName(), processInfoId,
										imageid);
							} else if (response.has(Constant.ERROR_MSG)) {

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
					};
				});
	}

	private void submitImageToProgress(String siteId, String processId,
			String processInfoId, String imageid) {
		LogTool.d(TAG, "siteId:" + siteId + " processId:" + processId
				+ " processInfoId:" + processInfoId + " imageid:" + imageid);
		JianFanJiaApiClient.submitImageToProcess(getActivity(), siteId,
				processId, processInfoId, imageid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site_manage;
	}

}
