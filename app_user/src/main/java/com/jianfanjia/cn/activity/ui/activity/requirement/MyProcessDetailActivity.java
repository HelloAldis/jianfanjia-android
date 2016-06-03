package com.jianfanjia.cn.activity.ui.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.api.Api;
import com.jianfanjia.cn.activity.application.MyApplication;
import com.jianfanjia.cn.activity.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.config.Global;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.cn.activity.tools.BusinessCovertUtil;
import com.jianfanjia.cn.activity.tools.UiHelper;
import com.jianfanjia.cn.activity.ui.activity.common.CommentActivity;
import com.jianfanjia.cn.activity.ui.activity.my.NoticeActivity;
import com.jianfanjia.cn.activity.ui.adapter.SectionItemAdapter;
import com.jianfanjia.cn.activity.ui.interf.ItemClickCallBack;
import com.jianfanjia.cn.activity.view.MainHeadView;
import com.jianfanjia.cn.activity.view.ProcessDetailHeadView;
import com.jianfanjia.cn.activity.view.dialog.CommonDialog;
import com.jianfanjia.cn.activity.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.activity.view.dialog.DialogHelper;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshListView;
import com.jianfanjia.common.tool.DateFormatTool;
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
public class MyProcessDetailActivity extends BaseSwipeBackActivity implements ItemClickCallBack {
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    @Bind(R.id.process__listview)
    PullToRefreshListView detailNodeListView;

    @Bind(R.id.process_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.process_head_view_layout)
    ProcessDetailHeadView mProcessDetailHeadView;

    private SectionItemAdapter sectionItemAdapter = null;
    private List<ProcessSection> mProcessSections;
    private ProcessSection mProcessSection = null;
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
        initPullRefresh();
        initMainHead();
        initProcessHead();
        initListView();
        initProcessInfo();
    }

    private void initProcessHead() {
        mProcessDetailHeadView.setOnPageScrollListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mProcessSections != null) {
                    if (position < TOTAL_PROCESS) {
                        currentList = position;
                        mProcessSection = mProcessSections.get(currentList);
                        Log.i(TAG, "processSection=" + mProcessSection.getName());
                        mProcessDetailHeadView.changeProcessStateShow(mProcessSection, true);
                        sectionItemAdapter.setPosition(currentList);
                        detailNodeListView.getRefreshableView().startLayoutAnimation();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initPullRefresh() {
        detailNodeListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        detailNodeListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (processId != Constant.DEFAULT_PROCESSINFO_ID) {
                    loadCurrentProcess(true);
                } else {
                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            detailNodeListView.onRefreshComplete();
                        }
                    });
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
            loadCurrentProcess(true);
        } else {
            processId = Constant.DEFAULT_PROCESSINFO_ID;
            processInfo = BusinessCovertUtil.getDefaultProcessInfo(this);
            initData(true);
        }
        LogTool.d(TAG, "processId==" + processId);
    }

    private void loadCurrentProcess(final boolean isResetCheckHead) {
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
                    initData(isResetCheckHead);
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
            .site_list_head_check, R.id.site_list_head_delay_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_notification_layout:
                Bundle noticeBundle = new Bundle();
                noticeBundle.putInt(NoticeActivity.TAB_TYPE, NoticeActivity.TAB_TYPE_PROCESS);
                startActivity(NoticeActivity.class, noticeBundle);
                break;
            case R.id.site_list_head_delay:
                delayDialog();
                break;
            case R.id.site_list_head_check:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.SECTION, mProcessSection.getName());
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                checkBundle.putSerializable(Constant.SECTION_INFO, mProcessSection);
                checkBundle.putInt(CheckActivity.CHECK_INTENT_FLAG, CheckActivity
                        .PROCESS_LIST_INTENT);
                startActivityForResult(CheckActivity.class, checkBundle, Constant
                        .REQUESTCODE_CHECK);
                break;
            case R.id.site_list_head_delay_text:
                showDelayDialog();
                break;
            default:
                break;
        }
    }

    // 初始化数据
    private void initData(boolean isResetCheckHead) {
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
            mProcessSections = processInfo.getSections();
            mProcessSection = mProcessSections.get(currentList);
            sectionItemAdapter.setSectionInfoList(mProcessSections, currentList);
            mProcessDetailHeadView.setScrollHeadTime(mProcessSections);
            mProcessDetailHeadView.setCurrentItem(currentList);
            mProcessDetailHeadView.changeProcessStateShow(mProcessSection, isResetCheckHead);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initListView() {
        sectionItemAdapter = new SectionItemAdapter(getApplication(),
                currentList, mProcessSections, this);
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
                bundle.putString(IntentConstant.SECTION, mProcessSection.getName());
                bundle.putString(IntentConstant.ITEM, mProcessSection.getItems().get(position).getName());
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
                bundle.putString(IntentConstant.SECTION, mProcessSection.getName());
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

    private void delayDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mProcessSection.getStart_at() + Constant.DELAY_TIME);
        DateWheelDialog dateWheelDialog = new DateWheelDialog(this,
                calendar);
        dateWheelDialog.setTitle("选择时间");
        dateWheelDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String dateStr = DateFormatTool.longToString(((DateWheelDialog) dialog)
                                .getChooseCalendar().getTimeInMillis());
                        LogTool.d(TAG, "dateStr:" + dateStr);
                        postReschedule(processInfo.get_id(),
                                processInfo.getUserid(),
                                processInfo.getFinal_designerid(),
                                mProcessSection.getName(), dateStr);
                    }
                });
        dateWheelDialog.setNegativeButton(R.string.no, null);
        dateWheelDialog.show();
    }

    private void showDelayDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(MyProcessDetailActivity.this);
        dialog.setTitle("改期提醒");
        dialog.setMessage("对方申请改期至   " + DateFormatTool.longToString(mProcessSection.getReschedule().getNew_date()));
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
                loadCurrentProcess(false);
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
                loadCurrentProcess(false);
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
                loadCurrentProcess(false);
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
                loadCurrentProcess(false);
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
                submitImageToProcessRequest.setSection(mProcessSection.getName());
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
                        loadCurrentProcess(false);
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
