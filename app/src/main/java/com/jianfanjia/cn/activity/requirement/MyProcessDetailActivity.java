package com.jianfanjia.cn.activity.requirement;

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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.common.CommentActivity;
import com.jianfanjia.cn.activity.common.PhotoPickerActivity;
import com.jianfanjia.cn.activity.my.NoticeActivity;
import com.jianfanjia.cn.adapter.SectionItemAdapter;
import com.jianfanjia.cn.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoPickerIntent;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.AddPhotoDialog;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshListView;

/**
 * @author fengliang
 * @ClassName:SiteManageFragment
 * @Description:工地管理
 * @date 2015-8-26 上午11:14:00
 */
@EActivity(R.layout.activity_my_process_detail)
public class MyProcessDetailActivity extends SwipeBackActivity implements ItemClickCallBack, PopWindowCallBack {
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    @ViewById(R.id.process_viewpager)
    ViewPager processViewPager;
    @ViewById(R.id.process__listview)
    PullToRefreshListView detailNodeListView;
    @ViewById(R.id.process_head_layout)
    MainHeadView mainHeadView;

    @ViewById(R.id.head_notification_layout)
    RelativeLayout notificationLayout;
    @StringArrayRes(R.array.site_procedure)
    String[] proTitle = null;

    protected AddPhotoDialog popupWindow = null;
    private SectionItemAdapter sectionItemAdapter = null;
    private SectionViewPageAdapter sectionViewPageAdapter = null;
    private List<ViewPagerItem> processList = new ArrayList<ViewPagerItem>();
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

    /**
     * 拿到当前屏幕的工地id
     *
     * @return
     */
    public String getProcessId() {
        return processId;
    }

    @AfterViews
    public void initAnnotationView() {
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
                loadCurrentProcess(new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        detailNodeListView.onRefreshComplete();
                        if (data != null) {
                            processInfo = JsonParser.jsonToBean(data.toString(), ProcessInfo.class);
                            initData();
                        }
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        detailNodeListView.onRefreshComplete();
                        if (processId != Constant.DEFAULT_PROCESSINFO_ID) {
                            makeTextShort(error_msg);
                        }
                    }
                });
            }
        });
    }

    private void initProcessInfo() {
        Intent intent = getIntent();
        processInfo = (ProcessInfo) intent.getSerializableExtra(Global.PROCESS_INFO);
        if (processInfo != null) {
            LogTool.d(TAG, "processInfo:" + processInfo.get_id());
            processId = processInfo.get_id();
            loadCurrentProcess(this);
        } else {
            processId = Constant.DEFAULT_PROCESSINFO_ID;
            processInfo = BusinessManager.getDefaultProcessInfo(this);
            initData();
        }

    }

    private void loadCurrentProcess(ApiUiUpdateListener apiUiUpdateListener) {
        if (processId != null) {
            JianFanJiaClient.get_ProcessInfo_By_Id(this, processId, apiUiUpdateListener, this);
        }
    }

    private void initMainHead() {
        mainHeadView.setRightTitleVisable(View.GONE);
    }

    @Click(R.id.head_back_layout)
    public void comeback() {
        appManager.finishActivity(this);
    }

    @Click(R.id.head_notification_layout)
    protected void gotoNotifyActivity() {
        startActivity(NoticeActivity.class);
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
            sectionItemAdapter.setSectionInfoList(sectionInfos, currentList);
            processViewPager.setVisibility(View.VISIBLE);
            processViewPager.setCurrentItem(currentList);
        }
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
            viewPagerItem.setResId(R.mipmap.icon_process_no);
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
        processViewPager.setCurrentItem(processList.size() - 1);
        processViewPager.setVisibility(View.GONE);
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
                        detailNodeListView.getRefreshableView().startLayoutAnimation();
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
                if (sectionInfos.get(i).getStatus().equals(Constant.NO_START)) {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_normal" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else if (sectionInfos.get(i).getStatus().equals(Constant.FINISHED)) {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_checked" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_normal_" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                }
            }
            sectionViewPageAdapter.notifyDataSetChanged();
        }
    }

    private void initListView() {
        sectionItemAdapter = new SectionItemAdapter(getApplication(),
                currentList, sectionInfos, this);
        detailNodeListView.setAdapter(sectionItemAdapter);
        UiHelper.setLayoutAnim(this, detailNodeListView.getRefreshableView());
        detailNodeListView.setFocusable(false);
        detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position--;//因为Listview加了一个下拉头,所以第一个position为下拉头
                sectionItemAdapter.setCurrentOpenItem(position);
            }
        });
    }

    @Override
    public void click(int position, int itemType) {
        LogTool.d(TAG, "position:" + position + "  itemType:" + itemType);
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
                startActivityForResult(CommentActivity.class, bundle, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.DELAY_ITEM:
                delayDialog();
                break;
            case Constant.CHECK_ITEM:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.SECTION, sectionInfo.getName());
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                checkBundle.putSerializable(Constant.SECTION_INFO, sectionInfo);
                checkBundle.putInt(CheckActivity.CHECK_INTENT_FLAG, CheckActivity.PROCESS_LIST_INTENT);
                startActivityForResult(CheckActivity.class, checkBundle, Constant.REQUESTCODE_CHECK);
                break;
            default:
                break;
        }
    }

    @Override
    public void preLoad() {
        super.preLoad();
    }

    @Override
    public void loadSuccess(Object data) {
        hideWaitDialog();
        if (data != null) {
            processInfo = JsonParser.jsonToBean(data.toString(), ProcessInfo.class);
            initData();
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        makeTextShort(error_msg);
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
                startActivityForResult(ShowProcessPicActivity.class, bundle, Constant.REQUESTCODE_SHOW_PROCESS_PIC);
                break;
            case Constant.ADD_ITEM:
//                showPopWindow();
                PhotoPickerIntent intent1 = new PhotoPickerIntent(MyProcessDetailActivity.this);
                if (imageUrlList != null) {
                    intent1.setPhotoCount(9 - imageUrlList.size());
                } else {
                    intent1.setPhotoCount(9);
                }
                intent1.setShowGif(false);
                intent1.setShowCamera(true);
                startActivityForResult(intent1, Constant.REQUESTCODE_PICKER_PIC);
                break;
            default:
                break;
        }
    }

    protected void showPopWindow() {
        if (popupWindow == null) {
            popupWindow = new AddPhotoDialog(this, this);
        }
        popupWindow.show();
    }

    @Override
    public void firstItemClick() {
        mTmpFile = FileUtil.createTmpFile(this);
        if (mTmpFile != null) {
            Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
            if (cameraIntent != null) {
                startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
            } else {
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sectionInfo.getStart_at() + Constant.DELAY_TIME);
        DateWheelDialog dateWheelDialog = new DateWheelDialog(this,
                calendar);
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
                        loadCurrentProcess(MyProcessDetailActivity.this);
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        makeTextShort(error_msg);
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
            case Constant.REQUESTCODE_PICKER_PIC:
                if (data != null) {
                    List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    for (String path : photos) {
                        Bitmap imageBitmap = ImageUtil.getImage(path);
                        LogTool.d(TAG, "imageBitmap: path :" + path);
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
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
                        Bitmap imageBitmap = ImageUtil.getImage(ImageUtil
                                .getImagePath(this, uri));
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_SHOW_PROCESS_PIC:
            case Constant.REQUESTCODE_CHECK:
            case Constant.REQUESTCODE_GOTO_COMMENT:
                loadCurrentProcess(this);
                break;
            default:
                break;
        }
    }

    protected void upload_image(Bitmap bitmap) {
        JianFanJiaClient.uploadImage(this, bitmap, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                showWaitDialog();
            }

            @Override
            public void loadSuccess(Object data) {
                String itemName = sectionItemAdapter
                        .getCurrentItem();
                JianFanJiaClient.submitImageToProcess(MyProcessDetailActivity.this,
                        processInfo.get_id(),
                        sectionInfo.getName(),
                        itemName,
                        data.toString(), new ApiUiUpdateListener() {
                            @Override
                            public void preLoad() {

                            }

                            @Override
                            public void loadSuccess(Object data) {
                                loadCurrentProcess(MyProcessDetailActivity.this);
                                if (mTmpFile != null
                                        && mTmpFile
                                        .exists()) {
                                    mTmpFile.delete();
                                }
                            }

                            @Override
                            public void loadFailture(String error_msg) {
                                makeTextShort(error_msg);
                                hideWaitDialog();
                            }
                        }, this);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                hideWaitDialog();
            }
        }, this);
    }

}
