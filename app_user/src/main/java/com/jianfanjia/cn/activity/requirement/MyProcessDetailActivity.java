package com.jianfanjia.cn.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnPageChange;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.Reschedule;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.common.CommentActivity;
import com.jianfanjia.cn.activity.my.NoticeActivity;
import com.jianfanjia.cn.adapter.SectionItemAdapter;
import com.jianfanjia.cn.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshListView;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.FileUtil;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * @author fengliang
 * @ClassName:SiteManageFragment
 * @Description:工地管理
 * @date 2015-8-26 上午11:14:00
 */
public class MyProcessDetailActivity extends BaseSwipeBackActivity implements ItemClickCallBack, PopWindowCallBack {
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    @Bind(R.id.process_head_view_layout)
    RelativeLayout process_head_view_layout;

    @Bind(R.id.checkLayout)
    LinearLayout checkLayout;

    @Bind(R.id.site_list_head_checkbutton_layout)
    LinearLayout site_list_head_checkbutton_layout;

    @Bind(R.id.site_list_head_delay_layout)
    LinearLayout site_list_head_delay_layout;

    @Bind(R.id.site_list_head_delay)
    TextView openDelay;

    @Bind(R.id.site_list_head_check)
    TextView openCheck;

    @Bind(R.id.site_list_head_delay_text)
    TextView site_list_head_delay_text;

    @Bind(R.id.rowBtnUp)
    ImageView rowBtnUp;

    @Bind(R.id.rowBtnDown)
    ImageView rowBtnDown;

    @Bind(R.id.process_viewpager)
    ViewPager processViewPager;

    @Bind(R.id.process__listview)
    PullToRefreshListView detailNodeListView;

    @Bind(R.id.process_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.head_notification_layout)
    RelativeLayout notificationLayout;

    private String[] proTitle = null;

    private SectionItemAdapter sectionItemAdapter = null;
    private SectionViewPageAdapter sectionViewPageAdapter = null;
    private List<ViewPagerItem> processList = new ArrayList<>();
    private List<ProcessSection> sectionInfos;
    private ProcessSection sectionInfo = null;
    private Process processInfo = null;
    private String processId = null;// 默认的工地id

    private int currentPro = -1;// 当前进行工序
    private int currentList = -1;// 当前展开第一道工序
    private int lastPro = -1;// 上次进行的工序

    private File mTmpFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (processId != Constant.DEFAULT_PROCESSINFO_ID) {
                    loadCurrentProcess();
                } else {
                    detailNodeListView.onRefreshComplete();
                }
            }
        });
    }

    private void initProcessInfo() {
        Intent intent = getIntent();
        processInfo = (Process) intent.getSerializableExtra(IntentConstant.PROCESS_INFO);
        LogTool.d(TAG, "processInfo:" + processInfo);
        if (processInfo != null) {
            processId = processInfo.get_id();
            loadCurrentProcess();
        } else {
            processId = Constant.DEFAULT_PROCESSINFO_ID;
            processInfo = BusinessCovertUtil.getDefaultProcessInfo(this);
            initData();
        }
        LogTool.d(TAG, "processId==" + processId);
    }

    private void loadCurrentProcess() {
        if (processId != null) {
            GetProcessInfoRequest getProcessInfoRequest = new GetProcessInfoRequest();
            getProcessInfoRequest.setProcessId(processId);
            Api.getProcessInfoDetail(getProcessInfoRequest, new ApiCallback<ApiResponse<Process>>() {
                @Override
                public void onPreLoad() {
                    showWaitDialog();
                }

                @Override
                public void onHttpDone() {
                    hideWaitDialog();
                    detailNodeListView.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<Process> apiResponse) {
                    processInfo = apiResponse.getData();
                    initData();
                }

                @Override
                public void onFailed(ApiResponse<Process> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                }
            });
        }
    }

    private void initMainHead() {
        mainHeadView.setRightTitleVisable(View.GONE);
    }

    @OnClick({R.id.head_back_layout, R.id.head_notification_layout, R.id.site_list_head_delay, R.id
            .site_list_head_check, R.id.site_list_head_delay_text, R.id.rowBtnUp, R.id.rowBtnDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_notification_layout:
                Bundle noticeBundle = new Bundle();
                noticeBundle.putInt(NoticeActivity.TAB_TYPE,NoticeActivity.TAB_TYPE_PROCESS);
                startActivity(NoticeActivity.class,noticeBundle);
                break;
            case R.id.site_list_head_delay:
                delayDialog();
                break;
            case R.id.site_list_head_check:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.SECTION, sectionInfo.getName());
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                checkBundle.putSerializable(Constant.SECTION_INFO, sectionInfo);
                checkBundle.putInt(CheckActivity.CHECK_INTENT_FLAG, CheckActivity
                        .PROCESS_LIST_INTENT);
                startActivityForResult(CheckActivity.class, checkBundle, Constant
                        .REQUESTCODE_CHECK);
                break;
            case R.id.site_list_head_delay_text:
                showDelayDialog();
                break;
            case R.id.rowBtnUp:
                rowBtnUp.setVisibility(View.GONE);
                rowBtnDown.setVisibility(View.VISIBLE);
                site_list_head_checkbutton_layout.setVisibility(View.GONE);
                break;
            case R.id.rowBtnDown:
                rowBtnUp.setVisibility(View.VISIBLE);
                rowBtnDown.setVisibility(View.GONE);
                site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @OnPageChange(R.id.process_viewpager)
    public void onPageSelected(int position) {
        if (sectionInfos != null) {
            if (position < TOTAL_PROCESS) {
                currentList = position;
                sectionInfo = sectionInfos.get(currentList);
                Log.i(TAG, "sectionInfo=" + sectionInfo.getName());
                setCheckLayoutState();
                sectionItemAdapter.setPosition(currentList);
                detailNodeListView.getRefreshableView().startLayoutAnimation();
            }
        }
    }

    // 初始化数据
    private void initData() {
        if (processInfo != null) {
            mainHeadView.setMianTitle(processInfo.getBasic_address() == null ? getString(R.string.process_example)
                    : processInfo.getBasic_address());// 设置标题头
            currentPro = MyApplication.getInstance().getPositionByItemName(
                    processInfo.getGoing_on());
            LogTool.d(TAG, "currentPro=" + currentPro);
            if (currentList == -1 || lastPro != currentPro) {
                currentList = currentPro;
                lastPro = currentPro;
            }
            sectionInfos = processInfo.getSections();
            sectionInfo = sectionInfos.get(currentList);
            setScrollHeadTime();
            setCheckLayoutState();
            sectionItemAdapter.setSectionInfoList(sectionInfos, currentList);
            processViewPager.setVisibility(View.VISIBLE);
            processViewPager.setCurrentItem(currentList);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
        sectionViewPageAdapter = new SectionViewPageAdapter(this, processList,
                new ViewPagerClickListener() {

                    @Override
                    public void onClickItem(int potition) {
                        Log.i(TAG, "potition=" + potition);
                        if (sectionInfos != null) {
                            if (potition < TOTAL_PROCESS) {
                                currentList = potition;
                                processViewPager.setCurrentItem(currentList);
                                sectionViewPageAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                });
        processViewPager.setAdapter(sectionViewPageAdapter);
        processViewPager.setVisibility(View.GONE);
    }

    private void setCheckLayoutState() {
        if (!sectionInfo.getName().equals("kai_gong")
                && !sectionInfo.getName().equals("chai_gai")) {
            String section_status = sectionInfo.getStatus();
            Log.i(TAG, "section_status=" + section_status);
            switch (section_status) {
                case Constant.FINISHED:
                    checkLayout.setVisibility(View.VISIBLE);
                    rowBtnUp.setVisibility(View.VISIBLE);
                    rowBtnDown.setVisibility(View.GONE);
                    site_list_head_delay_layout.setVisibility(View.GONE);
                    site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                    openDelay.setEnabled(false);
                    openDelay.setTextColor(getResources().getColor(R.color.grey_color));
                    openDelay.setText(getResources().getText(R.string
                            .site_example_node_delay_no));
                    break;
                case Constant.NO_START:
                    checkLayout.setVisibility(View.VISIBLE);
                    rowBtnUp.setVisibility(View.VISIBLE);
                    rowBtnDown.setVisibility(View.GONE);
                    site_list_head_delay_layout.setVisibility(View.GONE);
                    site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                    openDelay.setEnabled(false);
                    openDelay.setTextColor(getResources().getColor(R.color.grey_color));
                    openDelay.setText(getResources().getText(R.string.site_example_node_delay));
                    break;
                case Constant.YANQI_AGREE:
                case Constant.YANQI_REFUSE:
                case Constant.DOING:
                    checkLayout.setVisibility(View.VISIBLE);
                    rowBtnUp.setVisibility(View.VISIBLE);
                    rowBtnDown.setVisibility(View.GONE);
                    site_list_head_delay_layout.setVisibility(View.GONE);
                    site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                    openDelay.setEnabled(true);
                    openDelay.setTextColor(getResources().getColor(R.color.orange_color));
                    openDelay.setText(getResources().getText(R.string.site_example_node_delay));
                    break;
                case Constant.YANQI_BE_DOING:
                    Reschedule rescheduleInfo = sectionInfo.getReschedule();
                    if (null != rescheduleInfo) {
                        String role = rescheduleInfo.getRequest_role();
                        if (role.equals(Constant.IDENTITY_OWNER)) {
                            checkLayout.setVisibility(View.VISIBLE);
                            rowBtnUp.setVisibility(View.VISIBLE);
                            rowBtnDown.setVisibility(View.GONE);
                            site_list_head_delay_layout.setVisibility(View.GONE);
                            site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                            openDelay.setTextColor(getResources().getColor(R.color.grey_color));
                            openDelay.setText(getResources().getText(R.string
                                    .site_example_node_delay_doing));
                            openDelay.setEnabled(false);
                        } else {
                            checkLayout.setVisibility(View.VISIBLE);
                            rowBtnUp.setVisibility(View.VISIBLE);
                            rowBtnDown.setVisibility(View.GONE);
                            site_list_head_delay_layout.setVisibility(View.VISIBLE);
                            site_list_head_checkbutton_layout.setVisibility(View.GONE);
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            checkLayout.setVisibility(View.GONE);
        }
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
                position--;
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
                bundle.putString(IntentConstant.TOPIC_ID, processId);
                bundle.putString(IntentConstant.TO, processInfo.getFinal_designerid());
                bundle.putString(IntentConstant.SECTION, sectionInfo.getName());
                bundle.putString(IntentConstant.ITEM, sectionInfo.getItems().get(position).getName());
                bundle.putString(IntentConstant.TOPICTYPE, Global.TOPIC_NODE);
                startActivityForResult(CommentActivity.class, bundle, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void click(int position, int itemType, List<String> imageUrlList) {
        switch (itemType) {
            case Constant.IMG_ITEM:
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) imageUrlList);
                bundle.putInt(Constant.CURRENT_POSITION, position);
                bundle.putString(IntentConstant.PROCESS_ID, processId);
                bundle.putString(IntentConstant.SECTION, sectionInfo.getName());
                bundle.putString(IntentConstant.ITEM, sectionItemAdapter.getCurrentItem());
                startActivityForResult(ShowProcessPicActivity.class, bundle, Constant.REQUESTCODE_SHOW_PROCESS_PIC);
                break;
            case Constant.ADD_ITEM:
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
        mTmpFile = FileUtil.createTimeStampTmpFile();
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

    private void showDelayDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(MyProcessDetailActivity.this);
        dialog.setTitle("改期提醒");
        dialog.setMessage("对方申请改期至   " + DateFormatTool.longToString(sectionInfo.getReschedule().getNew_date()));
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
    }

    // 提交改期
    private void postReschedule(String processId, String userId,
                                String designerId, String section, String newDate) {
        LogTool.d(TAG, "processId:" + processId + " userId:" + userId
                + " designerId:" + designerId + " section:" + section
                + " newDate:" + newDate);
        ApplyRescheduleRequest applyRescheduleRequest = new ApplyRescheduleRequest();
        applyRescheduleRequest.setProcessid(processId);
        applyRescheduleRequest.setUserid(userId);
        applyRescheduleRequest.setDesignerid(designerId);
        applyRescheduleRequest.setNew_date(DateFormatTool.covertStringToLong(newDate));
        applyRescheduleRequest.setSection(section);
        Api.applyReschedule(applyRescheduleRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                loadCurrentProcess();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    //同意改期
    private void agreeReschedule(String processid) {
        AgreeRescheduleRequest agreeRescheduleRequest = new AgreeRescheduleRequest();
        agreeRescheduleRequest.setProcessid(processid);
        Api.agreeReschedule(agreeRescheduleRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                loadCurrentProcess();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    // 拒绝改期
    private void refuseReschedule(String processid) {
        RefuseRescheduleRequest refuseRescheduleRequest = new RefuseRescheduleRequest();
        refuseRescheduleRequest.setProcessid(processid);
        Api.refuseReschedule(refuseRescheduleRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                loadCurrentProcess();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
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
            case Constant.REQUESTCODE_SHOW_PROCESS_PIC:
            case Constant.REQUESTCODE_CHECK:
            case Constant.REQUESTCODE_GOTO_COMMENT:
                loadCurrentProcess();
                break;
            default:
                break;
        }
    }

    private void upload_image(Bitmap bitmap) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                String itemName = sectionItemAdapter.getCurrentItem();
                SubmitImageToProcessRequest submitImageToProcessRequest = new SubmitImageToProcessRequest();
                submitImageToProcessRequest.set_id(processId);
                submitImageToProcessRequest.setSection(sectionInfo.getName());
                submitImageToProcessRequest.setItem(itemName);
                submitImageToProcessRequest.setImageid(apiResponse.getData());
                Api.submitImageToProcess(submitImageToProcessRequest, new ApiCallback<ApiResponse<String>>() {
                    @Override
                    public void onPreLoad() {

                    }

                    @Override
                    public void onHttpDone() {

                    }

                    @Override
                    public void onSuccess(ApiResponse<String> apiResponse) {
                        loadCurrentProcess();
                        if (mTmpFile != null
                                && mTmpFile
                                .exists()) {
                            mTmpFile.delete();
                        }
                    }

                    @Override
                    public void onFailed(ApiResponse<String> apiResponse) {
                        makeTextShort(apiResponse.getErr_msg());
                    }

                    @Override
                    public void onNetworkError(int code) {
                        makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    }
                });
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_process_detail;
    }
}
