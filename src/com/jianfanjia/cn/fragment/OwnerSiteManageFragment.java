package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.jianfanjia.cn.cache.CacheManager;
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
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
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
		OnRefreshListener2<ScrollView>, ItemClickCallBack, PopWindowCallBack {
	private static final String TAG = OwnerSiteManageFragment.class.getName();
	private SwitchFragmentListener listener;
	private LinearLayout layoutAll = null;
	private PullToRefreshScrollView mPullRefreshScrollView = null;
	private ArrayList<SectionInfo> sectionInfos;
	private ArrayList<SectionItemInfo> sectionItemInfos;
	private SectionInfo sectionInfo;
	private ProcessInfo processInfo;
	private int currentPro = -1;// ��ǰ���й���
	private int currentList = -1;// ��ǰչ����һ������
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

	private RelativeLayout listHeadView;// listͷ��ͼ
	private RelativeLayout smallHeadLayout;// �۵�layout
	private RelativeLayout expandHeadLayout;// ��layout
	private TextView openCheckNode;// �����սڵ�����
	private TextView closeCheckNode;// �۵����սڵ�����

	private static final int CHANGE_PHOTO = 1;
	private static final int CHANGE_TIME = 5000;// ͼƬ�Զ��л�ʱ��
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
		if (savedInstanceState != null) {
			currentList = savedInstanceState.getInt(Constant.CURRENT_LIST, -1);
		}
		proTitle = getResources().getStringArray(R.array.site_procedure);
		checkSection = getResources().getStringArray(
				R.array.site_procedure_check);
		DataManager.getInstance().addObserver(this);
		String ownerProcessid = shared.getValue(Constant.PROCESSINFO_ID, null);
		if (ownerProcessid != null) {
			processInfo = DataManager.getInstance().getProcessInfo(
					ownerProcessid);
		}
		LogTool.d(TAG, "processInfo=" + processInfo);

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
		if (processInfo != null) {
			initData();
		} else {
			// getOwnerProcess();
			DataManager.getInstance().requestOwnerProcessInfo();
		}
	}

	public void update(Observable observable, Object data) {
		if (data != null) {
			processInfo = (ProcessInfo) data;
			initData();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// ���浱ǰչ���ĵڼ�������
		outState.putInt(Constant.CURRENT_LIST, currentList);
		super.onSaveInstanceState(outState);
	}

	// ��ʼ������
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
			setHeadView(sectionInfo.getName());
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
		// �������뵽ViewGroup��
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
	 * ����ѡ�е������ı���
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

	private void setHeadView(String name) {
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

	private void updateData() {
		initData();
	}

	private void handlerSuccess() {
		updateData();
		// for (int i = 0; i < pro.length; i++) {
		// View siteHead = list.get(i);
		// initItem(siteHead, i);
		// }
		myViewPageAdapter.notifyDataSetChanged();
	}

	private void initScrollLayout(View view) {
		processViewPager = (ViewPager) view.findViewById(R.id.processViewPager);
		for (int i = 0; i < proTitle.length; i++) {
			ViewPagerItem viewPagerItem = new ViewPagerItem();
			viewPagerItem.setResId(getResources().getIdentifier(
					"icon_home_normal" + (i + 1), "drawable",
					MyApplication.getInstance().getPackageName()));
			viewPagerItem.setTitle(proTitle[i]);
			if (sectionInfos != null) {
				viewPagerItem.setDate(DateFormatTool.covertLongToString(
						sectionInfos.get(i).getStart_at(), "M.dd")
						+ "-"
						+ DateFormatTool.covertLongToString(sectionInfos.get(i)
								.getEnd_at(), "M.dd"));
			} else {

			}
			// initItem(siteHead, i);
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
						setHeadView(sectionInfo.getName());
						sectionItemInfos = sectionInfo.getItems();
						sectionItemAdapter
								.setSectionItemInfos(sectionItemInfos);
						sectionItemAdapter.setLastClickItem(-1);
						sectionItemAdapter.setCurrentPro(currentList);
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
		proName.setText(proTitle[position]);
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
		detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
		detailNodeListView.setFocusable(false);
		initHeadView(detailNodeListView);
		detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					// ���listview headͷ��ͼ�����ж�ͷ��ͼ�Ƿ���ʾ���Ƿ��жԱ�����
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
					// ���listview item��
					sectionItemAdapter.setLastClickItem(position - 1);
					sectionItemAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	// ��ʼ��listview head��ͼ
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
			dateWheelDialog.setTitle("ѡ��ʱ��");
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
		LogTool.d(TAG, "position=================" + position);
		AddPhotoPopWindow addPhotoPopWindow = new AddPhotoPopWindow(
				getActivity(), this);
		addPhotoPopWindow.showAtLocation(layoutAll, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
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

	private void getOwnerProcess() {
		JianFanJiaApiClient.get_Owner_Process(getApplication(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						showWaitDialog();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						mPullRefreshScrollView.onRefreshComplete();
						LogTool.d(TAG, "response:" + response.toString());
						try {
							if (response.has(Constant.DATA)) {
								hideWaitDialog();
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
								hideWaitDialog();
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getApplication().getString(
									R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						hideWaitDialog();
						mPullRefreshScrollView.onRefreshComplete();
						makeTextLong(getApplication().getString(
								R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						hideWaitDialog();
						mPullRefreshScrollView.onRefreshComplete();
						makeTextLong(getApplication().getString(
								R.string.tip_login_error_for_network));
					};
				});
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
		case Constant.REQUESTCODE_CAMERA:// ����
			LogTool.d(TAG, "data:" + data);
			if (data != null) {
				Uri mImageUri = data.getData();
				LogTool.d(TAG, "mImageUri:" + mImageUri);

			}
			break;
		case Constant.REQUESTCODE__LOCATION:// ����ѡȡ
			LogTool.d(TAG, "data:" + data);
			if (data != null) {
				Uri uri = data.getData();
				if (null != uri) {

				}
			}
			break;
		case Constant.REQUESTCODE__CROP:

			break;
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site_manage;
	}

}
