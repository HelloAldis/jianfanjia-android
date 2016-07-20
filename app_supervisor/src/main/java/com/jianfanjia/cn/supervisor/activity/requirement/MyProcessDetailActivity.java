package com.jianfanjia.cn.supervisor.activity.requirement;

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
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.DeleteImageToProcessRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshListView;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.common.CommentActivity;
import com.jianfanjia.cn.supervisor.adapter.SectionItemAdapter;
import com.jianfanjia.cn.supervisor.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.supervisor.api.Api;
import com.jianfanjia.cn.supervisor.application.MyApplication;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.bean.ViewPagerItem;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.ProcessDetailHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
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
public class MyProcessDetailActivity extends BaseSwipeBackActivity{
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    @Bind(R.id.process_listview)
    PullToRefreshListView mPullToRefreshListView;

    @Bind(R.id.process_head_view_layout)
    ProcessDetailHeadView mProcessDetailHeadView;

    @Bind(R.id.process_head_layout)
    MainHeadView mainHeadView;

    private SectionItemAdapter sectionItemAdapter = null;
    private SectionViewPageAdapter sectionViewPageAdapter = null;
    private List<ViewPagerItem> processList = new ArrayList<>();
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


    private void initPullRefresh() {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (processId != Constant.DEFAULT_PROCESSINFO_ID) {
                    loadCurrentProcess(true);
                } else {
                    mPullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }

    private void initProcessInfo() {
        Intent intent = getIntent();
        processInfo = (Process) intent.getSerializableExtra(Global.PROCESS_INFO);
        LogTool.d("processInfo:" + processInfo);
        if (processInfo != null) {
            processId = processInfo.get_id();
            loadCurrentProcess(true);
        } else {
            processId = Constant.DEFAULT_PROCESSINFO_ID;
            processInfo = BusinessCovertUtil.getDefaultProcessInfo(this);
            initData(true);
        }
        LogTool.d("processId==" + processId);
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
                        mPullToRefreshListView.getRefreshableView().startLayoutAnimation();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadCurrentProcess(final boolean isResetCheckHead) {
        if (processId != null) {
            GetProcessInfoRequest getProcessInfoRequest = new GetProcessInfoRequest();
            getProcessInfoRequest.setProcessId(processId);
            Api.getProcessInfoDetail(getProcessInfoRequest, new ApiCallback<ApiResponse<Process>>() {
                @Override
                public void onPreLoad() {
                }

                @Override
                public void onHttpDone() {
                    if (mPullToRefreshListView != null) {
                        mPullToRefreshListView.onRefreshComplete();
                    }
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

    @OnClick({R.id.head_back_layout, R.id.site_list_head_delay, R.id
            .site_list_head_check, R.id.site_list_head_delay_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
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
                IntentUtil.startActivityForResult(this, CheckActivity.class, checkBundle, Constant
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
            LogTool.d("currentPro=" + currentPro);
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

    private void initListView() {
        sectionItemAdapter = new SectionItemAdapter(this,
                currentList, mProcessSections);
        sectionItemAdapter.setDeleteListener(new SectionItemAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                showDeletePicDialog(position);
            }
        });
        mPullToRefreshListView.setAdapter(sectionItemAdapter);
        UiHelper.setLayoutAnim(this, mPullToRefreshListView.getRefreshableView());
        mPullToRefreshListView.setFocusable(false);
        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position--;
                sectionItemAdapter.setCurrentOpenItem(position);
            }
        });
    }

    private void showDeletePicDialog(final int position) {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(MyProcessDetailActivity.this);
        dialog.setTitle("图片删除提示");
        dialog.setMessage("您确定删除该图片吗？");
        dialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteImage(position);
                    }
                });
        dialog.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void intentToComent(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Global.TOPIC_ID, processId);
        bundle.putString(Global.TO_DESIGNER, processInfo.getFinal_designerid());
        bundle.putString(Global.TO_USER, processInfo.getUserid());
        bundle.putString(Global.SECTION, mProcessSection.getName());
        bundle.putString(Global.ITEM, mProcessSection.getItems().get(position).getName());
        bundle.putString(Global.TOPICTYPE, Global.TOPIC_NODE);
        IntentUtil.startActivityForResult(this, CommentActivity.class, bundle, Constant
                .REQUESTCODE_GOTO_COMMENT);
    }

    public void deleteImage(int position) {
        DeleteImageToProcessRequest deleteImageToProcessRequest = new DeleteImageToProcessRequest();
        deleteImageToProcessRequest.set_id(processId);
        deleteImageToProcessRequest.setSection(mProcessSection.getName());
        deleteImageToProcessRequest.setItem(sectionItemAdapter.getCurrentItem());
        deleteImageToProcessRequest.setIndex(position);

        Api.deleteImageToProcess(deleteImageToProcessRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
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
                makeTextShort(HttpCode.getMsg(code));
            }
        }, this);

    }


    public void pickPicture(List<String> imageUrlList) {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(MyProcessDetailActivity.this);
        if (imageUrlList != null) {
            intent1.setPhotoCount(9 - imageUrlList.size());
        } else {
            intent1.setPhotoCount(9);
        }
        intent1.setShowGif(false);
        intent1.setShowCamera(true);
        startActivityForResult(intent1, Constant.REQUESTCODE_PICKER_PIC);
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
                        LogTool.d("dateStr:" + dateStr);
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
        dialog.show();
    }

    // 提交改期
    private void postReschedule(String processId, String userId,
                                String designerId, String section, String newDate) {
        LogTool.d("processId:" + processId + " userId:" + userId
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
        },this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_process_detail;
    }
}
