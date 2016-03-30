package com.jianfanjia.cn.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.api.model.ProcessSectionYsImage;
import com.jianfanjia.api.request.user.ConfirmCheckRequest;
import com.jianfanjia.cn.Event.CheckEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.common.ShowPicActivity;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.adapter.CheckGridViewAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.ItemSpaceDecoration;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: CheckActivity
 * @Description: 验收
 * @date 2015-8-28 下午2:25:36
 */
public class CheckActivity extends BaseSwipeBackActivity implements OnClickListener, ItemClickCallBack {
    private static final String TAG = CheckActivity.class.getName();
    public static final String CHECK_INTENT_FLAG = "check_intent_flag";
    public static final int NOTICE_INTENT = 0;//通知进入的
    public static final int PROCESS_LIST_INTENT = 1;//工地
    private int flagIntent = -1;
    @Bind(R.id.check_pic_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.mygridview)
    protected RecyclerView gridView = null;
    @Bind(R.id.btn_confirm)
    protected TextView btn_confirm = null;
    protected GridLayoutManager gridLayoutManager = null;
    private CheckGridViewAdapter adapter = null;
    private List<GridItem> checkGridList = new ArrayList<>();
    private List<String> showSamplePic = new ArrayList<>();
    private List<String> showProcessPic = new ArrayList<>();
    private List<ProcessSectionYsImage> imageids = new ArrayList<>();
    private String processInfoId = null;// 工地id
    private Process processInfo = null;
    private String sectionName = null;//工序名称
    private ProcessSection sectionInfo = null;

    private int currentUploadCount = 0;//当前已上传图片个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
        initData();
    }

    public void initView() {
        mainHeadView.setBackListener(this);
        gridLayoutManager = new GridLayoutManager(CheckActivity.this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        gridView.setLayoutManager(gridLayoutManager);
        gridView.setHasFixedSize(true);
        gridView.setItemAnimator(new DefaultItemAnimator());
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(MyApplication.dip2px(getApplicationContext(), 5));
        gridView.addItemDecoration(decoration);
    }

    protected void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            sectionName = bundle.getString(Constant.SECTION);
            processInfo = (Process) bundle.getSerializable(Constant.PROCESS_INFO);
            processInfoId = processInfo.get_id();
            flagIntent = bundle.getInt(CheckActivity.CHECK_INTENT_FLAG);
            LogTool.d(TAG, "sectionName:" + sectionName + " processInfo:" + processInfo + " processInfoId:" +
                    processInfoId + " flagIntent:" + flagIntent);
            sectionInfo = (ProcessSection) bundle.getSerializable(Constant.SECTION_INFO);
        }
    }

    private void initData() {
        LogTool.d(TAG, "sectionInfo:" + sectionInfo.get_id());
        mainHeadView.setMianTitle(MyApplication.getInstance().getStringById(sectionInfo.getName()) + "阶段验收");
        checkGridList.clear();
        checkGridList = getCheckedImageById(sectionInfo.getName());
        imageids = sectionInfo.getYs().getImages();
        currentUploadCount = imageids.size();
        LogTool.d(TAG, "currentUploadCount=" + currentUploadCount);
        for (int i = 0; imageids != null && i < imageids.size(); i++) {
            String key = imageids.get(i).getKey();
            LogTool.d(TAG, "key=" + key);
            checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                    imageids.get(i).getImageid());
        }
        adapter = new CheckGridViewAdapter(CheckActivity.this, checkGridList,
                this);
        gridView.setAdapter(adapter);
        setConfimStatus();
        initShowList();
    }

    private void setConfimStatus() {
        if (!sectionInfo.getStatus().equals(Constant.FINISHED)) {
            if (currentUploadCount < BusinessCovertUtil
                    .getCheckPicCountBySection(sectionInfo.getName())) {
                //设计师图片没上传完，不能验收
                btn_confirm.setEnabled(false);
                btn_confirm.setText(this.getResources().getString(
                        R.string.confirm_done));
            } else {
                boolean isFinish = isSectionInfoFishish(sectionInfo.getItems());
                if (isFinish) {
                    //图片上传完了，可以进行验收
                    btn_confirm.setEnabled(true);
                    btn_confirm.setText(this.getResources().getString(
                            R.string.confirm_done));
                } else {
                    //图片上传完了，但是工序的某些节点还没有完工
                    btn_confirm.setEnabled(false);
                    btn_confirm.setText(this.getResources().getString(
                            R.string.confirm_not_finish));
                }
            }
        } else {
            //已经验收过了
            btn_confirm.setEnabled(false);
            btn_confirm.setText(this.getResources().getString(
                    R.string.confirm_finish));
        }
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickCheckDone();
            }
        });
    }

    /**
     * 判断是否所有节点都已经完工了
     *
     * @param sectionItemInfos
     * @return
     */
    private boolean isSectionInfoFishish(List<ProcessSectionItem> sectionItemInfos) {
        boolean flag = true;
        for (ProcessSectionItem sectionItemInfo : sectionItemInfos) {
            LogTool.d(TAG, "sectionitem name =" + sectionItemInfo.getName());
            LogTool.d(TAG, "sectionitem status =" + sectionItemInfo.getStatus());
            if (!sectionItemInfo.getStatus().equals(Constant.FINISHED)) {
                LogTool.d(TAG, "sectionitem not finish");
                flag = false;
                return flag;
            }
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                setResult(RESULT_CANCELED);
                appManager.finishActivity(this);
                break;
            case R.id.btn_confirm:
                onClickCheckDone();
                break;
            default:
                break;
        }
    }

    private void onClickCheckDone() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(CheckActivity.this);
        dialog.setTitle("确认验收");
        dialog.setMessage("确定验收吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmCheckDoneByOwner(processInfoId, sectionName);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    //初始化放大显示的list
    private void initShowList() {
        showProcessPic.clear();
        showSamplePic.clear();
        for (int i = 0; i < checkGridList.size(); i = i + 2) {
            showSamplePic.add(checkGridList.get(i).getImgId());
        }
        for (int i = 1; i < checkGridList.size(); i = i + 2) {
            if (!checkGridList.get(i).getImgId().contains(Constant.DEFALUT_PIC_HEAD)) {
                showProcessPic.add(checkGridList.get(i).getImgId());
            }
        }
    }

    // 业主确认对比验收完成
    private void confirmCheckDoneByOwner(String processid, String section) {
        ConfirmCheckRequest confirmCheckRequest = new ConfirmCheckRequest();
        confirmCheckRequest.set_id(processid);
        confirmCheckRequest.setSection(section);

        Api.confirmSectionCheck(confirmCheckRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                btn_confirm.setEnabled(false);
                checkSuccess();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void checkSuccess() {
        switch (flagIntent) {
            case NOTICE_INTENT:
                EventBus.getDefault().post(new CheckEvent());
                break;
            case PROCESS_LIST_INTENT:
                setResult(RESULT_OK);
                appManager.finishActivity(CheckActivity.this);
                break;
            default:
                break;
        }
    }

    /**
     * 根据工序名获取验收图片
     *
     * @param sectionName
     * @return
     */
    private List<GridItem> getCheckedImageById(String sectionName) {
        try {
            List<GridItem> gridList = new ArrayList<>();
            int arrId = getResources().getIdentifier(sectionName, "array",
                    MyApplication.getInstance().getPackageName());
            TypedArray ta = getResources().obtainTypedArray(arrId);
            LogTool.d(TAG, "res length:" + ta.length());
            for (int i = 0; i < ta.length(); i++) {
                LogTool.d(TAG, "res id:" + ta.getResourceId(i, 0));
                GridItem item = new GridItem();
                item.setImgId(Constant.DEFALUT_PIC_HEAD + ta.getResourceId(i, 0));
                gridList.add(item);
            }
            ta.recycle();
            return gridList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startShowActivity(int arg2) {
        LogTool.d(TAG, "arg2=" + arg2);
        Bundle bundle = new Bundle();
        if (arg2 % 2 == 0) {
            bundle.putStringArrayList(Constant.IMAGE_LIST,
                    (ArrayList<String>) showSamplePic);
            bundle.putInt(Constant.CURRENT_POSITION, arg2 / 2);
        } else {
            String currentImageId = checkGridList.get(arg2).getImgId();
            for (int i = 0; i < showProcessPic.size(); i++) {
                if (currentImageId.equals(showProcessPic.get(i))) {
                    arg2 = i;
                }
            }
            bundle.putStringArrayList(Constant.IMAGE_LIST,
                    (ArrayList<String>) showProcessPic);
            bundle.putInt(Constant.CURRENT_POSITION, arg2);

        }
        startActivity(ShowPicActivity.class, bundle);
    }

    @Override
    public void click(int position, int itemType) {
        LogTool.d(TAG, "position = " + position);
        startShowActivity(position);
    }

    @Override
    public void click(int position, int itemType, List<String> imageUrlList) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_pic;
    }

}
