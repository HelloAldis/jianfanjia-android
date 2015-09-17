package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
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
import com.jianfanjia.cn.activity.CommentActivity;
import com.jianfanjia.cn.activity.DesignerSiteActivity;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.OwnerSiteActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ShowPicActivity;
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
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName:SiteManageFragment
 * @Description:工地管理
 * @author fengliang
 * @date 2015-8-26 上午11:14:00
 * 
 */
public class SiteManageFragment extends BaseFragment implements
		OnRefreshListener2<ScrollView>, ItemClickCallBack, UploadImageListener,
		LoadDataListener {
	private static final String TAG = SiteManageFragment.class.getName();
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
	private TextView openDelay;// 延期按钮
	private TextView openCheck;// 对比验收按钮

	// private MainHeadView mainHeadView;
	private TextView titleCenter;
	private TextView titleRight;
	private ImageView titleImage;

	private boolean isOpen = false;

	private static final int CHANGE_PHOTO = 1;
	private static final int CHANGE_TIME = 5000;// 图片自动切换时间
	private static final int BANNER_ICON[] = { R.drawable.bg_home_banner1,
			R.drawable.bg_home_banner2, R.drawable.bg_home_banner3,
			R.drawable.bg_home_banner4 };

	private String processInfoId = null;

	private Handler bannerhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_PHOTO:
				int index = bannerViewPager.getCurrentItem();
				if (index == bannerList.size() - 1) {
					index = -1;
				}
				bannerViewPager.setCurrentItem(index + 1);
				bannerhandler
						.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			currentList = savedInstanceState.getInt(Constant.CURRENT_LIST, -1);
		}
		proTitle = getResources().getStringArray(R.array.site_procedure);
		checkSection = getResources().getStringArray(
				R.array.site_procedure_check);
	}

	private void initProcessInfo() {
		processInfo = dataManager.getDefaultProcessInfo();
	}

	private void refreshData() {
		if (dataManager.getDefaultProcessId() == null) {
			dataManager.requestProcessList(this);
		} else {
			Log.i(TAG, "proId = " + dataManager.getDefaultProcessId());
			dataManager.requestProcessInfoById(
					dataManager.getDefaultProcessId(), this);
		}
	}

	@Override
	public void initView(View view) {
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		initMainHead(view);
		initBannerView(view);
		initScrollLayout(view);
		initListView(view);
		initProcessInfo();
		if (processInfo != null) {
			initData();
		} else {
			refreshData();
		}
	}

	private void initMainHead(View view) {
		titleCenter = (TextView) view.findViewById(R.id.head_center_title);
		titleRight = (TextView) view.findViewById(R.id.head_right_title);
		titleImage = (ImageView) view.findViewById(R.id.icon_head);
		titleImage.setOnClickListener(this);
		titleRight.setOnClickListener(this);
		if (mUserType.equals(Constant.IDENTITY_OWNER)) {
			titleCenter.setText("");
			titleRight.setText("配置工地");
		} else if (mUserType.equals(Constant.IDENTITY_DESIGNER)) {
			titleCenter.setText("");
			titleRight.setText("切换工地");
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
					sectionItemInfos, this);
			sectionItemAdapter.setSection_status(sectionInfo.getStatus());
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
			layoutParams.bottomMargin = 10;
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
		bannerhandler.sendEmptyMessageDelayed(CHANGE_PHOTO, CHANGE_TIME);
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
			String checkText = getResources().getStringArray(
					R.array.site_check_name)[MyApplication.getInstance()
					.getPositionByItemName(name)];
			openCheckNode.setText(checkText);
			closeCheckNode.setText(checkText);
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
			viewPagerItem.setDate("");
			list.add(viewPagerItem);
		}
		myViewPageAdapter = new MyViewPageAdapter(getActivity(), list,
				new ViewPagerClickListener() {

					@Override
					public void onClickItem(int potition) {
						Log.i(TAG, "potition------->" + potition);
					}

				});
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
						sectionItemAdapter.setSection_status(sectionInfo.getStatus());
//						sectionItemAdapter.setPosition(currentList);
						sectionItemAdapter.setLastClickItem(-1, isOpen);
						sectionItemAdapter.notifyDataSetChanged();
					}
				}
			}

		});
	}

	private void setScrollHeadTime() {
		if (sectionInfos != null) {
			for (int i = 0; i < proTitle.length; i++) {
				ViewPagerItem viewPagerItem = myViewPageAdapter.getList()
						.get(i);
				Log.i(TAG,
						DateFormatTool.covertLongToString(sectionInfos.get(i)
								.getStart_at(), "M.dd")
								+ "-"
								+ DateFormatTool
										.covertLongToString(sectionInfos.get(i)
												.getEnd_at(), "M.dd"));
				viewPagerItem.setDate(DateFormatTool.covertLongToString(
						sectionInfos.get(i).getStart_at(), "M.dd")
						+ "-"
						+ DateFormatTool.covertLongToString(sectionInfos.get(i)
								.getEnd_at(), "M.dd"));
				if (i <= currentPro) {
					int drawableId = getResources().getIdentifier(
							"icon_home_checked" + (i + 1), "drawable",
							getApplication().getPackageName());
					viewPagerItem.setResId(drawableId);
				} else {
					int drawableId = getResources().getIdentifier(
							"icon_home_normal" + (i + 1), "drawable",
							getApplication().getPackageName());
					viewPagerItem.setResId(drawableId);
				}
			}
			infinitePagerAdapter.notifyDataSetChanged();
		}
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
					SectionItemInfo sectionItemInfo = sectionItemInfos
							.get(position - 1);
					processInfoId = sectionItemInfo.getName();
					LogTool.d(TAG, "processInfoId===" + processInfoId);
					if (isOpen) {
						isOpen = false;
					} else {
						isOpen = true;
					}
//					sectionItemAdapter.setCurrentClickItem(position -1);
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
		openCheck = (TextView) listHeadView
				.findViewById(R.id.site_list_head_check);
		openCheck.setOnClickListener(this);
		openDelay = (TextView) listHeadView
				.findViewById(R.id.site_list_head_delay);
		openDelay.setOnClickListener(this);
		smallHeadLayout = (RelativeLayout) listHeadView
				.findViewById(R.id.site_listview_item_content_small);
		expandHeadLayout = (RelativeLayout) listHeadView
				.findViewById(R.id.site_listview_item_content_expand);
		// 根据不同的用户类型显示不同的文字
		if (mUserType.equals(Constant.IDENTITY_DESIGNER)) {
			openCheck.setText(getString(R.string.upload_pic));
		} else if (mUserType.equals(Constant.IDENTITY_OWNER)) {
			openCheck.setText(getString(R.string.site_example_node_check));
		}
		listView.addHeaderView(view);
	}

	@Override
	public void setListener() {
		mPullRefreshScrollView.setOnRefreshListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon_head:
			((MainActivity) getActivity()).getSlidingPaneLayout().openPane();
			break;
		case R.id.head_right_title:
			if (mUserType.equals(Constant.IDENTITY_OWNER)) {
				Intent configIntent = new Intent(getActivity(),
						OwnerSiteActivity.class);
				startActivityForResult(configIntent,
						Constant.REQUESTCODE_CONFIG_SITE);
			} else if (mUserType.equals(Constant.IDENTITY_DESIGNER)) {
				Intent changeIntent = new Intent(getActivity(),
						DesignerSiteActivity.class);
				startActivityForResult(changeIntent,
						Constant.REQUESTCODE_CHANGE_SITE);
			}
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
							dialog.dismiss();
							String dateStr = StringUtils
									.getDateString(((DateWheelDialog) dialog)
											.getChooseCalendar().getTime());
							LogTool.d(TAG, "dateStr:" + dateStr);
							postReschedule(processInfo.get_id(),
									processInfo.getUserid(),
									processInfo.getFinal_designerid(),
									sectionInfo.getName(), dateStr);
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
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 下拉刷新(从第一页开始装载数据)
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		// 加载数据
		dataManager.requestProcessList(this);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 上拉加载更多(加载下一页数据)
		mPullRefreshScrollView.onRefreshComplete();
	}

	@Override
	public void click(int position, int itemType) {
		LogTool.d(TAG, "position:" + position + "itemType:" + itemType);
		switch (itemType) {
		case Constant.CONFIRM_ITEM:
			confirmDialog();
			break;
		case Constant.ADD_ITEM:
			showPopWindow(getView());
			break;
		case Constant.IMG_ITEM:

			break;
		case Constant.COMMENT_ITEM:
			Bundle bundle = new Bundle();
			bundle.putInt(Constant.CURRENT_LIST, currentList);
			bundle.putInt(Constant.CURRENT_ITEM, position);
			startActivity(CommentActivity.class, bundle);
			break;
		default:
			break;
		}
	}

	@Override
	public void loadSuccess() {
		mPullRefreshScrollView.onRefreshComplete();
		initProcessInfo();
		if (processInfo != null) {
			initData();
		} else {
			// loadempty
		}
	}

	@Override
	public void loadFailture() {
		mPullRefreshScrollView.onRefreshComplete();
	}

	@Override
	public void click(int position, int itemType, List<String> imageUrlList) {
		switch (itemType) {
		case Constant.IMG_ITEM:
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(Constant.IMAGE_LIST,
					(ArrayList<String>) imageUrlList);
			bundle.putInt(Constant.CURRENT_POSITION, position);
			startActivity(ShowPicActivity.class, bundle);
			break;
		default:
			break;
		}
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
		startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
	}

	private void confirmDialog() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(getActivity());
		dialog.setTitle("确认完工");
		dialog.setMessage("确认完工吗？");
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						confirmProcessItemDone(processInfo.get_id(),
								sectionInfo.getName(), processInfoId);
					}
				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
	}

	// 提交改期
	private void postReschedule(String processId, String userId,
			String designerId, String section, String newDate) {
		LogTool.d(TAG, "processId:" + processId + " userId:" + userId
				+ " designerId:" + designerId + " section:" + section
				+ " newDate:" + newDate);
		JianFanJiaApiClient.postReschedule(getActivity(), processId, userId,
				designerId, section, newDate, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						refreshData();
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

	// 确认完工装修流程小节点
	private void confirmProcessItemDone(String siteId, String section,
			String item) {
		LogTool.d(TAG, "siteId:" + siteId + " section:" + section + " item:"
				+ item);
		JianFanJiaApiClient.processItemDone(getActivity(), siteId, section,
				item, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						refreshData();
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								makeTextLong(response.get(Constant.SUCCESS_MSG)
										.toString());
								dataManager.getDesignerProcessLists();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.REQUESTCODE_CAMERA:// 拍照
			LogTool.d(TAG, "data:" + data);
			if (data != null) {
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				LogTool.d(TAG, "bitmap:" + bitmap);
				Uri mImageUri = null;
				if (null != data.getData()) {
					mImageUri = data.getData();
				} else {
					mImageUri = Uri.parse(MediaStore.Images.Media.insertImage(
							getActivity().getContentResolver(), bitmap, null,
							null));
				}
				LogTool.d(TAG, "mImageUri:" + mImageUri);
				startPhotoZoom(mImageUri);
			}
			break;
		case Constant.REQUESTCODE_LOCATION:// 本地选取
			if (data != null) {
				Uri uri = data.getData();
				LogTool.d(TAG, "uri:" + uri);
				if (null != uri) {
					startPhotoZoom(uri);
				}
			}
			break;
		case Constant.REQUESTCODE_CROP:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					// 得到返回来的数据，是bitmap类型的数据
					Bitmap bitmap = extras.getParcelable("data");
					LogTool.d(TAG, "avatar - bitmap = " + bitmap);
					String imgPath = PhotoUtils.savaPicture(bitmap);
					LogTool.d(TAG, "imgPath=============" + imgPath);
					if (!TextUtils.isEmpty(imgPath)) {
						uploadManager.uploadProcedureImage(imgPath,
								processInfo.get_id(), sectionInfo.getName(),
								processInfoId, this);
					}
				}
			}
			break;
		case Constant.REQUESTCODE_CONFIG_SITE:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String temStr = (String) bundle.get("Key");
					LogTool.d(TAG, "temStr" + temStr);
					if (null != temStr) {
						initProcessInfo();
						if (processInfo != null) {
							initData();
						} else {
							// loadempty
						}
					}
				}
			}
			break;
		case Constant.REQUESTCODE_CHANGE_SITE:
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String processId = (String) bundle.get("ProcessId");
					LogTool.d(TAG, "processId=" + processId);
					if (null != processId) {
						initProcessInfo();
						if (processInfo != null) {
							initData();
						} else {
							// loadempty
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onSuccess(String msg) {
		LogTool.d(TAG, "msg===========" + msg);
		if ("success".equals(msg)) {
			LogTool.d(TAG, "--------------------------------------------------");
			refreshData();
		}
	}

	@Override
	public void onFailure() {
		LogTool.d(TAG, "==============================================");
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site_manage;
	}

}
