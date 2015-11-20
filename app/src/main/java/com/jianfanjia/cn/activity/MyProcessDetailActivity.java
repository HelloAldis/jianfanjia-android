package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.adapter.SectionItemAdapter;
import com.jianfanjia.cn.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.ImageUtils;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

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
@EActivity(R.layout.activity_my_process_detail)
public class MyProcessDetailActivity extends BaseAnnotationActivity implements ItemClickCallBack {
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    @ViewById(R.id.process_viewpager)
    ViewPager processViewPager;
    @ViewById(R.id.lineView)
    View lineView;
    @ViewById(R.id.process__listview)
    PullToRefreshListView detailNodeListView;
    @ViewById(R.id.process_head_layout)
    MainHeadView mainHeadView;
    @ViewById(R.id.head_notification_layout)
    RelativeLayout notificationLayout;
    @StringArrayRes(R.array.site_procedure)
    String[] proTitle = null;

    private SectionItemAdapter sectionItemAdapter = null;
    private SectionViewPageAdapter sectionViewPageAdapter = null;
    private List<ViewPagerItem> processList = new ArrayList<ViewPagerItem>();
    private List<String> imageList;
    private List<SectionInfo> sectionInfos;
    private SectionInfo sectionInfo = null;
    private ProcessInfo processInfo = null;
    private String processId = null;// 默认的工地id

    private int currentPro = -1;// 当前进行工序
    private int currentList = -1;// 当前展开第一道工序
    private int lastPro = -1;// 上次进行的工序

    private File mTmpFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currentList == -1) {
            currentList = dataManager.getCurrentList();
        }
    }

    @AfterViews
    public void initView() {
        initPullRefresh();
        initMainHead();
        initScrollLayout();
        initListView();
        initProcessInfo();
    }

    private void initPullRefresh() {
        detailNodeListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        detailNodeListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadCurrentProcess();
            }
        });
    }

    private void initProcessInfo() {
        Intent intent = getIntent();
        processInfo = (ProcessInfo) intent.getSerializableExtra(Global.PROCESS_INFO);
        if (processInfo != null) {
            LogTool.d(getClass().getName(), "processInfo:" + processInfo.get_id());
            processId = processInfo.get_id();
            loadCurrentProcess();
        } else {
            processId = Constant.DEFAULT_PROCESSINFO_ID;
            processInfo = dataManager.getProcessInfoById(processId);
            initData();
        }

    }

    private void loadCurrentProcess() {
        if (processId != null) {
            JianFanJiaClient.get_ProcessInfo_By_Id(this, processId, this, this);
        }
    }

    private void initMainHead() {
        mainHeadView.setRightTitleVisable(View.GONE);
    }

    @Click(R.id.head_back_layout)
    public void comeback() {
        finish();
    }

    @Click(R.id.head_notification_layout)
    protected void gotoNotifyActivity() {
        startActivity(NotifyActivity.class);
    }

    // 初始化数据
    private void initData() {
        if (processInfo != null) {
            mainHeadView.setMianTitle(processInfo.getCell() == null ? getString(R.string.process_example)
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
            LogTool.d(TAG, sectionInfos.size() + "--sectionInfos.size()");
            sectionItemAdapter = new SectionItemAdapter(getApplication(),
                    currentList, sectionInfos, this);
            detailNodeListView.setAdapter(sectionItemAdapter);
            processViewPager.setCurrentItem(currentList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentList != -1) {
            dataManager.setCurrentList(currentList);
        }
    }

    private void initScrollLayout() {
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
            viewPagerItem.setResId(R.mipmap.icon8_home_normal);
            viewPagerItem.setTitle("");
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        // --------------------------
        sectionViewPageAdapter = new SectionViewPageAdapter(this, processList,
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
        processViewPager.setAdapter(sectionViewPageAdapter);
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
                ViewPagerItem viewPagerItem = sectionViewPageAdapter.getList()
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
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_normal" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                }
            }
            sectionViewPageAdapter.notifyDataSetChanged();
        }
    }

    private void initListView() {
        detailNodeListView.setFocusable(false);
        detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position--;//因为Listview加了一个下拉头,所以第一个position为下拉头
                /*if (sectionItemAdapter.isHasCheck()) {
                    if (position == 0) {
                        boolean isCanClickYanshou = true;
                        for (SectionItemInfo sectionItemInfo : sectionInfo
                                .getItems()) {
                            if (Constant.FINISH != Integer
                                    .parseInt(sectionItemInfo.getStatus())) {
                                isCanClickYanshou = false;
                                break;
                            }
                        }
                        if (isCanClickYanshou) {
                            sectionItemAdapter.setCurrentOpenItem(position);
                        }
                    } else {
                        sectionItemAdapter.setCurrentOpenItem(position);
                    }
                } else {
                    sectionItemAdapter.setCurrentOpenItem(position);
                }*/
                sectionItemAdapter.setCurrentOpenItem(position);
            }
        });


    }

    @Override
    public void click(int position, int itemType) {
        LogTool.d(TAG, "position:" + position + "itemType:" + itemType);
        switch (itemType) {
            case Constant.IMG_ITEM:
                break;
            case Constant.COMMENT_ITEM:
                Bundle bundle = new Bundle();
                bundle.putString(Global.TOPIC_ID, processId);
                bundle.putString(Global.TO, processInfo.getFinal_designerid());
                bundle.putString(Global.SECTION, sectionInfo.getName());
                bundle.putString(Global.ITEM, sectionInfo.getItems().get(position).getName());
                bundle.putString(Global.TOPICTYPE, Global.TOPIC_NODE);
                startActivity(CommentActivity.class, bundle);
                break;
            case Constant.DELAY_ITEM:
                delayDialog();
                break;
            case Constant.CHECK_ITEM:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.PROCESS_NAME, sectionInfo.getName());
                checkBundle
                        .putInt(Constant.PROCESS_STATUS, sectionInfo.getStatus());
                checkBundle.putSerializable(Global.PROCESS_INFO, processInfo);
                startActivity(CheckActivity.class, checkBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void preLoad() {
        super.preLoad();
        lineView.setVisibility(View.GONE);
    }

    @Override
    public void loadSuccess(Object data) {
        hideWaitDialog();
        detailNodeListView.onRefreshComplete();
//        lineView.setVisibility(View.VISIBLE);
        if (data != null) {
            processInfo = JsonParser.jsonToBean(data.toString(), ProcessInfo.class);
            initData();
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
//        lineView.setVisibility(View.GONE);
        if (processId != Constant.DEFAULT_PROCESSINFO_ID) {
            makeTextShort(error_msg);
        }
        detailNodeListView.onRefreshComplete();
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
                Intent intent = new Intent(this, ShowProcessPicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.REQUESTCODE_SHOW_PROCESS_PIC);
                break;
            case Constant.ADD_ITEM:
                imageList = imageUrlList;
                showPopWindow(getWindow().getDecorView());
                break;
            default:
                break;
        }
    }

    @Override
    public void firstItemClick() {
        mTmpFile = FileUtil.createTmpFile(this);
        if (mTmpFile != null) {
            Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
            if(cameraIntent != null){
                startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
            }else{
//                makeTextShort(getString(R.string.tip_open_camera));
            }
        } else {
            makeTextLong(getString(R.string.tip_not_sdcard));
        }
    }

    @Override
    public void secondItemClick() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
    }

    private void delayDialog() {
        DateWheelDialog dateWheelDialog = new DateWheelDialog(this,
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

    // 提交改期
    private void postReschedule(String processId, String userId,
                                String designerId, String section, String newDate) {
        LogTool.d(TAG, "processId:" + processId + " userId:" + userId
                + " designerId:" + designerId + " section:" + section
                + " newDate:" + newDate);
        JianFanJiaClient.postReschedule(this, processId, userId,
                designerId, section, newDate, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        loadCurrentProcess();
                    }

                    @Override
                    public void loadFailture(String error_msg) {

                    }
                }, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
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
                                .getImagePath(uri, this));
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_SHOW_PROCESS_PIC:
                loadCurrentProcess();
                break;
            default:
                break;
        }
    }

    protected void upload_image(Bitmap bitmap) {
        JianFanJiaClient.uploadImage(this, bitmap, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                String itemName = sectionItemAdapter
                        .getCurrentItem();
                JianFanJiaClient.submitImageToProcess(MyProcessDetailActivity.this,
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

}
