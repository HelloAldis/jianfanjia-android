package com.jianfanjia.cn.designer.activity.requirement;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.common.CommentActivity;
import com.jianfanjia.cn.designer.activity.my.NoticeActivity;
import com.jianfanjia.cn.designer.adapter.SectionItemAdapter;
import com.jianfanjia.cn.designer.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.ViewPagerItem;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ItemClickCallBack;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.cn.designer.tools.DateFormatTool;
import com.jianfanjia.cn.designer.tools.FileUtil;
import com.jianfanjia.cn.designer.tools.ImageUtil;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshListView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;


/**
 * @author fengliang
 * @ClassName:SiteManageFragment
 * @Description:工地管理
 * @date 2015-8-26 上午11:14:00
 */
public class MyProcessDetailActivity extends BaseActivity implements ItemClickCallBack {
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    @Bind(R.id.process_viewpager)
    ViewPager processViewPager;
    @Bind(R.id.process__listview)
    PullToRefreshListView detailNodeListView;
    @Bind(R.id.process_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.head_notification_layout)
    RelativeLayout notificationLayout;

    String[] proTitle = null;

    private SectionItemAdapter sectionItemAdapter = null;
    private SectionViewPageAdapter sectionViewPageAdapter = null;
    private List<ViewPagerItem> processList = new ArrayList<ViewPagerItem>();
    private List<ProcessSection> processSections;
    private ProcessSection processSection = null;
    private Process processInfo = null;
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
        initView();
    }

    private void initView() {
        initStringArray();
        initPullRefresh();
        initMainHead();
        initScrollLayout();
        initListView();
        initProcessInfo();
    }

    private void initStringArray() {
        proTitle = getResources().getStringArray(R.array.site_procedure);
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
                            processInfo = JsonParser.jsonToBean(data.toString(), Process.class);
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
        processId = intent.getStringExtra(Global.PROCESS_ID);
        LogTool.d(TAG, "processId :" + processId);
        if (processId != null) {
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

    @OnClick(R.id.head_back_layout)
    public void comeback() {
        appManager.finishActivity(this);
    }

    @OnClick(R.id.head_notification_layout)
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
            processSections = processInfo.getSections();
            processSection = processSections.get(currentList);
            setScrollHeadTime();
            LogTool.d(TAG, processSections.size() + "--processSections.size()");
            sectionItemAdapter.setSectionInfoList(processSections, currentList);
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
                        if (processSections != null) {
                            if (potition < TOTAL_PROCESS) {
                                currentList = potition;
                                processSection = processSections.get(currentList);
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
                if (processSections != null) {
                    if (arg0 < TOTAL_PROCESS) {
                        currentList = arg0;
                        processSection = processSections.get(currentList);
                        Log.i(TAG, "processSection=" + processSection.getName());
                        sectionItemAdapter.setPosition(currentList);
                        detailNodeListView.getRefreshableView().startLayoutAnimation();
                    }
                }
            }
        });

    }

    private void setScrollHeadTime() {
        if (processSections != null) {
            for (int i = 0; i < proTitle.length; i++) {
                ViewPagerItem viewPagerItem = sectionViewPageAdapter.getList()
                        .get(i);
                if (processSections.get(i).getStart_at() > 0) {
                    viewPagerItem.setDate(DateFormatTool.covertLongToString(
                            processSections.get(i).getStart_at(), "M.dd")
                            + "-"
                            + DateFormatTool.covertLongToString(processSections
                            .get(i).getEnd_at(), "M.dd"));
                }
                if (processSections.get(i).getStatus().equals(Constant.NO_START)) {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_normal" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else if (processSections.get(i).getStatus().equals(Constant.FINISHED)) {
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
                currentList, processSections, this);
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
            case Constant.CONFIRM_ITEM:
                confirmFinishDialog();
                break;
            case Constant.IMG_ITEM:
                break;
            case Constant.COMMENT_ITEM:
                Bundle bundle = new Bundle();
                bundle.putString(Global.TOPIC_ID, processId);
                bundle.putString(Global.TO, processInfo.getUserid());
                bundle.putString(Global.SECTION, processSection.getName());
                bundle.putString(Global.ITEM, processSection.getItems().get(position).getName());
                bundle.putString(Global.TOPICTYPE, Global.TOPIC_NODE);
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.DELAY_ITEM:
                delayDialog();
                break;
            case Constant.CHECK_ITEM:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.SECTION, processSection.getName());
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                Intent checkIntent = new Intent(MyProcessDetailActivity.this, CheckActivity.class);
                checkIntent.putExtras(checkBundle);
                startActivityForResult(checkIntent, Constant.REQUESTCODE_CHECK);
                break;
            case Constant.OPERATE_ITEM:
                showDelayDialog();
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
            processInfo = JsonParser.jsonToBean(data.toString(), Process.class);
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
                bundle.putString(Global.SECTION, processSection.getName());
                bundle.putString(Global.ITEM, sectionItemAdapter.getCurrentItem());
                Intent intent = new Intent(this, ShowProcessPicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.REQUESTCODE_SHOW_PROCESS_PIC);
                break;
            case Constant.ADD_ITEM:
//                showPopWindow(getWindow().getDecorView());
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
            makeTextShort(getString(R.string.tip_not_sdcard));
        }
    }

    @Override
    public void secondItemClick() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
    }

    private void confirmFinishDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(MyProcessDetailActivity.this);
        dialog.setTitle("确认完工");
        dialog.setMessage("确认完工吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmProcessItemDone(processInfo.get_id(),
                                processSection.getName(),
                                sectionItemAdapter.getCurrentItem());
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    private void delayDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(processSection.getStart_at() + Constant.DELAY_TIME);
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
                                processSection.getName(), dateStr);
                    }
                });
        dateWheelDialog.setNegativeButton(R.string.no, null);
        dateWheelDialog.show();
    }

    private void showDelayDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(MyProcessDetailActivity.this);
        dialog.setTitle("改期提醒");
        dialog.setMessage("对方申请改期至   " + DateFormatTool.longToString(processSection.getReschedule().getNew_date()));
        dialog.setPositiveButton(R.string.agree,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        agreeReschedule(processInfo.get_id());
                    }
                });
        dialog.setNegativeButton(R.string.refuse, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                refuseReschedule(processInfo.get_id());
            }
        });
        dialog.show();
        dialog.show();
    }

    //同意改期
    private void agreeReschedule(String processid) {
        JianFanJiaClient.agreeReschedule(MyProcessDetailActivity.this, processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                loadCurrentProcess(MyProcessDetailActivity.this);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, this);
    }

    // 拒绝改期
    private void refuseReschedule(String processid) {
        JianFanJiaClient.refuseReschedule(MyProcessDetailActivity.this, processid, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                loadCurrentProcess(MyProcessDetailActivity.this);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
            }
        }, this);
    }


    // 确认完工装修流程小节点
    private void confirmProcessItemDone(String siteId, String section,
                                        String item) {
        LogTool.d(TAG, "siteId:" + siteId + " section:" + section + " item:" + item);
        JianFanJiaClient.processItemDone(this, siteId, section,
                item, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        loadCurrentProcess(MyProcessDetailActivity.this);
                    }

                    @Override
                    public void loadFailture(String errorMsg) {
                        makeTextShort(errorMsg);
                        hideWaitDialog();
                    }
                }, this);
    }

    // 提交改期
    private void postReschedule(String processId, String userId,
                                String designerId, String section, String newDate) {
        LogTool.d(TAG, "processId:" + processId + " userId:" + userId + " designerId:" + designerId + " section:" +
                section + " newDate:" + newDate);
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
                        processSection.getName(),
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_process_detail;
    }
}