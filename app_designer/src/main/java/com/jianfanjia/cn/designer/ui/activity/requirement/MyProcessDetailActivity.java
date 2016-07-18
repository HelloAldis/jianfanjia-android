package com.jianfanjia.cn.designer.ui.activity.requirement;

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
import com.jianfanjia.api.request.designer.FinishSectionItemRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.activity.common.CommentActivity;
import com.jianfanjia.cn.designer.ui.activity.my.NoticeActivity;
import com.jianfanjia.cn.designer.ui.adapter.SectionItemAdapter;
import com.jianfanjia.cn.designer.ui.interf.ItemClickCallBack;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.ProcessDetailHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
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
                if (processSections != null) {
                    if (position < TOTAL_PROCESS) {
                        currentList = position;
                        processSection = processSections.get(currentList);
                        Log.i(TAG, "processSection=" + processSection.getName());
                        mProcessDetailHeadView.changeProcessStateShow(processSection, true);
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
                    LogTool.d("degfault process refresh finish");
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
        processId = intent.getStringExtra(Global.PROCESS_ID);
        LogTool.d("processId :" + processId);
        if (processId != null) {
            loadCurrentProcess(true);
        } else {
            processId = Constant.DEFAULT_PROCESSINFO_ID;
            processInfo = BusinessCovertUtil.getDefaultProcessInfo(this);
            initData(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            },this);
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
                checkBundle.putString(Constant.SECTION, processSection.getName());
                checkBundle.putSerializable(Constant.PROCESS_INFO, processInfo);
                Intent checkIntent = new Intent(MyProcessDetailActivity.this,
                        CheckActivity.class);
                checkIntent.putExtras(checkBundle);
                startActivityForResult(checkIntent, Constant.REQUESTCODE_CHECK);
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
            if (currentList == -1 || lastPro != currentPro) {
                currentList = currentPro;
                lastPro = currentPro;
            }
            processSections = processInfo.getSections();
            processSection = processSections.get(currentList);
            sectionItemAdapter.setSectionInfoList(processSections, currentList);
            mProcessDetailHeadView.setScrollHeadTime(processSections);
            mProcessDetailHeadView.setCurrentItem(currentList);
            mProcessDetailHeadView.changeProcessStateShow(processSection, isResetCheckHead);
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
                position--;
                sectionItemAdapter.setCurrentOpenItem(position);
            }
        });
    }

    @Override
    public void click(int position, int itemType) {
        LogTool.d("position:" + position + "  itemType:" + itemType);
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
                        String dateStr = DateFormatTool.longToString(((DateWheelDialog) dialog)
                                .getChooseCalendar().getTimeInMillis());
                        LogTool.d("dateStr:" + dateStr);
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
    }

    // 提交改期
    private void postReschedule(String processId, String userId,
                                String designerId, String section, String newDate) {
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
        },this);
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
        },this);
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
        },this);
    }

    // 确认完工装修流程小节点
    private void confirmProcessItemDone(String siteId, String section,
                                        String item) {
        LogTool.d("siteId:" + siteId + " section:" + section + " item:" + item);
        FinishSectionItemRequest finishSectionItemRequest = new FinishSectionItemRequest();
        finishSectionItemRequest.set_id(siteId);
        finishSectionItemRequest.setSection(section);
        finishSectionItemRequest.setItem(item);
        Api.finishSectionItem(finishSectionItemRequest, new ApiCallback<ApiResponse<String>>() {
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
        },this);
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
                        LogTool.d("imageBitmap: path :" + path);
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
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                String itemName = sectionItemAdapter
                        .getCurrentItem();
                SubmitImageToProcessRequest submitImageToProcessRequest = new SubmitImageToProcessRequest();
                submitImageToProcessRequest.set_id(processId);
                submitImageToProcessRequest.setSection(processSection.getName());
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
                },MyProcessDetailActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        },MyProcessDetailActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_process_detail;
    }
}
