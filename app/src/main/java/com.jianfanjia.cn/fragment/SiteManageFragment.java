package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.CheckActivity;
import com.jianfanjia.cn.activity.CommentActivity;
import com.jianfanjia.cn.activity.DesignerSiteActivity;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ShowProcessPicActivity;
import com.jianfanjia.cn.adapter.MyViewPageAdapter;
import com.jianfanjia.cn.adapter.SectionItemAdapterBack;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.ReceiveMsgListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.ImageUtils;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.view.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.view.library.PullToRefreshScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author fengliang
 * @ClassName:SiteManageFragment
 * @Description:工地管理
 * @date 2015-8-26 上午11:14:00
 */
public class SiteManageFragment extends BaseFragment implements
        OnRefreshListener2<ScrollView>, ItemClickCallBack,
        ApiUiUpdateListener, PopWindowCallBack, ReceiveMsgListener {
    private static final String TAG = SiteManageFragment.class.getName();
    private PullToRefreshScrollView mPullRefreshScrollView = null;
    private static final int TOTAL_PROCESS = 7;// 7道工序
    private List<SectionInfo> sectionInfos;
    private SectionInfo sectionInfo = null;
    private ProcessInfo processInfo = null;
    private String processId = null;// 默认的工地id
    private int currentPro = -1;// 当前进行工序
    private int currentList = -1;// 当前展开第一道工序
    private int lastPro = -1;// 上次进行的工序

    private ViewPager processViewPager = null;
    private ListView detailNodeListView = null;
    private SectionItemAdapterBack sectionItemAdapter = null;
    private MyViewPageAdapter myViewPageAdapter = null;
    private String[] proTitle = null;
    private List<ViewPagerItem> processList = new ArrayList<ViewPagerItem>();
    private List<String> imageList;
    private AddPhotoPopWindow popupWindow;

    private TextView titleCenter = null;
    private TextView titleRight = null;
    private ImageView titleImage = null;

    private File mTmpFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "----onCreate()----");
        if (currentList == -1) {
            currentList = dataManager.getCurrentList();
        }
        initProcessInfo();
    }

    private void initProcessInfo() {
        processInfo = dataManager.getDefaultProcessInfo();
        processId = dataManager.getDefaultProcessId();
        if (processInfo == null) {
            if (processId != null) {
                if (NetTool.isNetworkAvailable(getActivity())) {
                    loadCurrentProcess();
                } else {
                    processInfo = dataManager.getProcessInfoById(processId);
                }
            } else {
                processInfo = MyApplication.getDefaultProcessInfo(getActivity());
            }
        }

    }

    private void loadCurrentProcess() {
        if (processId != null) {
            JianFanJiaClient.get_ProcessInfo_By_Id(getActivity(), dataManager.getDefaultProcessId(), this, this);
        }
    }

    @Override
    public void initView(View view) {
        proTitle = getApplication().getResources().getStringArray(
                R.array.site_procedure);
        mPullRefreshScrollView = (PullToRefreshScrollView) view
                .findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
        initMainHead(view);
        initScrollLayout(view);
        initListView(view);
        initData();
    }

    private void initMainHead(View view) {
        titleCenter = (TextView) view.findViewById(R.id.head_center_title);
        titleRight = (TextView) view.findViewById(R.id.head_right_title);
        titleImage = (ImageView) view.findViewById(R.id.icon_head);
        titleImage.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        titleCenter.setText("");
        titleRight.setText("切换工地");
    }

    // 初始化数据
    private void initData() {
        if (processInfo != null) {
            titleCenter.setText(processInfo.getCell() == null ? ""
                    : processInfo.getCell());// 设置标题头
            currentPro = MyApplication.getInstance().getPositionByItemName(
                    processInfo.getGoing_on());
            if (currentList == -1 || lastPro != currentPro) {
                currentList = currentPro;
                lastPro = currentPro;
            }
            sectionInfos = processInfo.getSections();
            sectionInfo = sectionInfos.get(currentList);
            setScrollHeadTime();
            sectionItemAdapter = new SectionItemAdapterBack(getApplication(),
                    currentList, sectionInfos, this);
            detailNodeListView.setAdapter(sectionItemAdapter);
            processViewPager.setCurrentItem(currentList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserImageId.contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayLocalImage(mUserImageId, titleImage);
        } else {
            imageShow.displayImageHeadWidthThumnailImage(getActivity(), mUserImageId, titleImage);
        }
        LogTool.d(TAG, "---onResume()-----");
        if (sectionItemAdapter != null) {
            sectionItemAdapter.notifyDataSetChanged();
        }
        listenerManeger.addReceiveMsgListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogTool.d(TAG, "---onPause()-----");
        if (currentList != -1) {
            dataManager.setCurrentList(currentList);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogTool.d(TAG, "---onStop()-----");
        listenerManeger.removeReceiveMsgListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.d(TAG, "---onDestroy()--------");
    }

    private void initScrollLayout(View view) {
        processViewPager = (ViewPager) view.findViewById(R.id.processViewPager);
        for (int i = 0; i < proTitle.length; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem();
            viewPagerItem.setResId(getApplication().getResources()
                    .getIdentifier("icon_home_normal" + (i + 1), "drawable",
                            getApplication().getPackageName()));
            viewPagerItem.setTitle(proTitle[i]);
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        for (int i = 0; i < 3; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem();
            viewPagerItem.setResId(R.drawable.icon8_home_normal);
            viewPagerItem.setTitle("");
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        // --------------------------
        myViewPageAdapter = new MyViewPageAdapter(getActivity(), processList,
                new ViewPagerClickListener() {

                    @Override
                    public void onClickItem(int potition) {
                        Log.i(TAG, "potition=" + potition);
                        if (sectionInfos != null) {
                            if (potition < TOTAL_PROCESS) {
                                currentList = potition;
                                sectionInfo = sectionInfos.get(currentList);
                                sectionItemAdapter.setPosition(currentList);
                                processViewPager.setCurrentItem(potition);
                            }
                        }
                    }

                });
        processViewPager.setAdapter(myViewPageAdapter);
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
                    if (arg0 < TOTAL_PROCESS) {
                        currentList = arg0;
                        sectionInfo = sectionInfos.get(currentList);
                        Log.i(TAG, "sectionInfo=" + sectionInfo.getName());
                        sectionItemAdapter.setPosition(currentList);
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
                if (sectionInfos.get(i).getStart_at() > 0) {
                    viewPagerItem.setDate(DateFormatTool.covertLongToString(
                            sectionInfos.get(i).getStart_at(), "M.dd")
                            + "-"
                            + DateFormatTool.covertLongToString(sectionInfos
                            .get(i).getEnd_at(), "M.dd"));
                }
                if (sectionInfos.get(i).getStatus() != Constant.NOT_START) {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_checked" + (i + 1),
                                    "drawable",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_normal" + (i + 1),
                                    "drawable",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                }
            }
            myViewPageAdapter.notifyDataSetChanged();
        }
    }

    private void initListView(View view) {
        detailNodeListView = (ListView) view.findViewById(R.id.site__listview);
        detailNodeListView.setFocusable(false);
        detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                sectionItemAdapter.setCurrentOpenItem(position);
            }
        });

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
                Intent changeIntent = new Intent(getActivity(),
                        DesignerSiteActivity.class);
                startActivityForResult(changeIntent,
                        Constant.REQUESTCODE_CHANGE_SITE);
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
        // refreshData();
        processId = dataManager.getDefaultProcessId();
        if (processId != null) {
            loadCurrentProcess();
        } else {
            mPullRefreshScrollView.onRefreshComplete();
        }
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
                confirmFinishDialog();
                break;
            case Constant.IMG_ITEM:
                break;
            case Constant.COMMENT_ITEM:
                Bundle bundle = new Bundle();
                bundle.putString(Constant.TOPIC_ID, processId);
                bundle.putString(Constant.TO, processInfo.getUserid());
                bundle.putString(Constant.SECTION, sectionInfo.getName());
                bundle.putString(Constant.ITEM, sectionInfo.getItems().get(position).getName());
                bundle.putString(Global.TOPICTYPE, Global.TOPIC_NODE);
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.DELAY_ITEM:
                delayDialog();
                break;
            case Constant.CHECK_ITEM:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.PROCESS_NAME, sectionInfo.getName());
                checkBundle
                        .putInt(Constant.PROCESS_STATUS, sectionInfo.getStatus());
                startActivity(CheckActivity.class, checkBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadSuccess(Object data) {
        mPullRefreshScrollView.onRefreshComplete();
        processInfo = dataManager.getDefaultProcessInfo();
        processId = dataManager.getDefaultProcessId();
        initData();
    }

    @Override
    public void loadFailture(String errorMsg) {
        makeTextLong(getString(R.string.tip_error_internet));
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void preLoad() {
        // TODO Auto-generated method stub
    }

    @Override
    public void click(int position, int itemType, List<String> imageUrlList) {
        switch (itemType) {
            case Constant.IMG_ITEM:
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) imageUrlList);
                bundle.putInt(Constant.CURRENT_POSITION, position);
                bundle.putString(Global.PROCESS_ID, processId);
                bundle.putString(Global.SECTION, sectionInfo.getName());
                bundle.putString(Global.ITEM, sectionItemAdapter.getCurrentItem());
                Intent intent = new Intent(getActivity(), ShowProcessPicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.REQUESTCODE_SHOW_PROCESS_PIC);
                break;
            case Constant.ADD_ITEM:
                imageList = imageUrlList;
                showPopWindow(getView());
                break;
            default:
                break;
        }
    }

    protected void showPopWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new AddPhotoPopWindow(getActivity(), this);
        }
        popupWindow.show(view);
    }

    @Override
    public void takecamera() {
        mTmpFile = FileUtil.createTmpFile(getActivity());
        if (mTmpFile != null) {
            Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
            startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
        } else {
            makeTextLong("没有sd卡，无法打开相机");
        }
    }

    @Override
    public void takePhoto() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
    }

    private void delayDialog() {
        LogTool.d(TAG, "delayDialog");
        DateWheelDialog dateWheelDialog = new DateWheelDialog(getActivity(),
                Calendar.getInstance());
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
    }

    private void confirmFinishDialog() {
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
                                sectionInfo.getName(),
                                sectionItemAdapter.getCurrentItem());
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
        JianFanJiaClient.postReschedule(getActivity(), processId, userId,
                designerId, section, newDate, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        loadCurrentProcess();
                    }

                    @Override
                    public void loadFailture(String errorMsg) {

                    }
                }, this);
    }

    // 确认完工装修流程小节点
    private void confirmProcessItemDone(String siteId, String section,
                                        String item) {
        LogTool.d(TAG, "siteId:" + siteId + " section:" + section + " item:"
                + item);
        JianFanJiaClient.processItemDone(getActivity(), siteId, section,
                item, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        loadCurrentProcess();
                    }

                    @Override
                    public void loadFailture(String errorMsg) {
                        hideWaitDialog();
                    }
                }, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_CAMERA:// 拍照
                mTmpFile = new File(dataManager.getPicPath());
                if (mTmpFile != null) {
                    Bitmap imageBitmap = ImageUtil.getImage(mTmpFile.getPath());
                    LogTool.d(TAG, "imageBitmap:" + imageBitmap);
                    if (null != imageBitmap) {
                        upload_image(imageBitmap);
                    }
                }
                break;
            case Constant.REQUESTCODE_LOCATION:// 本地选取
                if (data != null) {
                    Uri uri = data.getData();
                    LogTool.d(TAG, "uri:" + uri);
                    if (null != uri) {
                        Bitmap imageBitmap = ImageUtil.getImage(ImageUtils
                                .getImagePath(uri, getActivity()));
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_SHOW_PROCESS_PIC:
                loadCurrentProcess();
                break;
            case Constant.REQUESTCODE_CHANGE_SITE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String processId = (String) bundle.get("ProcessId");
                        LogTool.d(TAG, "processId=" + processId);
                        if (null != processId
                                && dataManager.getDefaultProcessId() != processId) {
                            // loadempty
                            loadCurrentProcess();
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_GOTO_COMMENT:
                loadCurrentProcess();
            default:
                break;
        }
    }

    protected void upload_image(Bitmap bitmap) {
        JianFanJiaClient.uploadImage(getActivity(), bitmap, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
            }

            @Override
            public void loadSuccess(Object data) {
                String itemName = sectionItemAdapter
                        .getCurrentItem();
                JianFanJiaClient.submitImageToProcess(getActivity(),
                        processInfo.get_id(),
                        sectionInfo.getName(),
                        itemName,
                        dataManager
                                .getCurrentUploadImageId(), new ApiUiUpdateListener() {
                            @Override
                            public void preLoad() {

                            }

                            @Override
                            public void loadSuccess(Object data) {
                                loadCurrentProcess();
                                if (mTmpFile != null
                                        && mTmpFile
                                        .exists()) {
                                    mTmpFile.delete();
                                }
                            }

                            @Override
                            public void loadFailture(String error_msg) {

                            }
                        }, this);
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, this);
    }

    @Override
    public void onReceive(NotifyMessage message) {
        Log.i(TAG, "onReceive  message");
        if (null != message) {
            showNotifyDialog(message);
        }
    }

    private void showNotifyDialog(final NotifyMessage message) {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        String msgType = message.getType();
        if (msgType.equals(Constant.YANQI_NOTIFY)) {
            dialog.setTitle("改期提醒");
            dialog.setMessage(message.getContent());
            dialog.setPositiveButton(R.string.agree,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            agreeReschedule(message.getProcessid());
                        }
                    });
            dialog.setNegativeButton(R.string.refuse, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    refuseReschedule(message.getProcessid());
                }
            });
        } else if (msgType.equals(Constant.FUKUAN_NOTIFY)) {
            dialog.setTitle("付款提醒");
            dialog.setMessage("系统提示:您即将进入下一轮付款环节,请您及时与设计师联系");
            dialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else if (msgType.equals(Constant.CAIGOU_NOTIFY)) {
            dialog.setTitle("采购提醒");
            dialog.setMessage("系统提示:您即将进入下一轮建材购买阶段,您需要购买的是" + message.getContent());
            dialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else if (msgType.equals(Constant.CONFIRM_CHECK_NOTIFY)) {
            dialog.setTitle("验收提醒");
            dialog.setMessage("确定要进行验收吗？");
            dialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Bundle checkBundle = new Bundle();
                            checkBundle.putString(Constant.PROCESS_NAME, sectionInfo.getName());
                            checkBundle
                                    .putInt(Constant.PROCESS_STATUS, sectionInfo.getStatus());
                            checkBundle.putSerializable(Global.PROCESS_INFO, processInfo);
                            startActivity(CheckActivity.class, checkBundle);
                        }
                    });
        }
        dialog.show();
    }

    //同意改期
    private void agreeReschedule(String processid) {
        JianFanJiaClient.agreeReschedule(getActivity(), processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }

    // 拒绝改期
    private void refuseReschedule(String processid) {
        JianFanJiaClient.refuseReschedule(getActivity(), processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_owner_site_manage;
    }

}
