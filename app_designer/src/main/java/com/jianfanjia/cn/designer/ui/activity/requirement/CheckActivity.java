package com.jianfanjia.cn.designer.ui.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.api.model.ProcessSectionYsImage;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.designer.AddImageToCheckRequest;
import com.jianfanjia.api.request.designer.DeleteCheckImgRequest;
import com.jianfanjia.api.request.designer.NotifyOwnerCheckRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.ui.adapter.CheckGridViewAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.bean.GridItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.ui.interf.ItemClickCallBack;
import com.jianfanjia.cn.designer.ui.interf.PopWindowCallBack;
import com.jianfanjia.cn.designer.ui.interf.UploadListener;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.ItemSpaceDecoration;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.cn.designer.view.AddPhotoDialog;
import com.jianfanjia.common.tool.FileUtil;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * @author fengliang
 * @ClassName: CheckActivity
 * @Description: 验收
 * @date 2015-8-28 下午2:25:36
 */
public class CheckActivity extends BaseSwipeBackActivity implements
        UploadListener, ItemClickCallBack, PopWindowCallBack {
    private static final String TAG = CheckActivity.class.getName();
    public static final int EDIT_STATUS = 0;
    public static final int FINISH_STATUS = 1;
    @Bind(R.id.check_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.mygridview)
    protected RecyclerView gridView;
    @Bind(R.id.btn_confirm)
    protected TextView btn_confirm;
    @Bind(R.id.checkLayout)
    protected RelativeLayout checkLayout;

    private GridLayoutManager gridLayoutManager = null;
    private CheckGridViewAdapter adapter = null;
    private List<GridItem> checkGridList = new ArrayList<>();//本页显示的griditem项
    private List<String> showSamplePic = new ArrayList<>();//示例照片
    private List<String> showProcessPic = new ArrayList<>();//工地验收照片
    private List<ProcessSectionYsImage> imageids = new ArrayList<>();
    private String processInfoId = null;// 工地id
    private Process processInfo = null;
    private String sectionName = null;//工序名称
    private ProcessSection processSection = null;
    private int key = -1;
    private File mTmpFile = null;
    private List<ProcessSectionItem> processSectionItems;
    private int currentState;

    private int uploadCount = 0;//要上传图片个数
    private int currentUploadCount = 0;//当前已上传图片个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            sectionName = bundle.getString(Constant.SECTION);
            processInfo = (Process) bundle.getSerializable(Constant.PROCESS_INFO);
            processInfoId = processInfo.get_id();
            LogTool.d(TAG, "sectionName:" + sectionName + " processInfo:" + processInfo + " processInfoId:" +
                    processInfoId);
            processSection = BusinessCovertUtil.getSectionInfoByName(processInfo.getSections(), sectionName);

            initView();
            initData();
        }
    }

    public void initView() {
        initMainHeadView();
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
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(TDevice.dip2px(getApplicationContext(), 5));
        gridView.addItemDecoration(decoration);
        btn_confirm.setText(this.getResources().getString(
                R.string.confirm_upload));
        currentState = FINISH_STATUS;
    }

    private void initMainHeadView() {
        mainHeadView.setRightTitle(getString(R.string.edit));
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void initData() {
        LogTool.d(TAG, "processSection.get_id():" + processSection.get_id() + " processSection.getStatus():" +
                processSection.getStatus());
        mainHeadView.setMianTitle(processSection.getLabel() + getResources()
                .getString(R.string.stage_check_text));
        checkGridList.clear();
        checkGridList = getCheckedImageById(processSection.getName());
        LogTool.d(TAG, "checkGridList:" + checkGridList.size());
        imageids = processSection.getYs().getImages();
        LogTool.d(TAG, "imageids=" + imageids);
        currentUploadCount = imageids.size();
        LogTool.d(TAG, "currentUploadCount  " + currentUploadCount);
        initCheckGridList();
        adapter = new CheckGridViewAdapter(CheckActivity.this, checkGridList,
                this, this);
        gridView.setAdapter(adapter);
        setConfimStatus();
        initShowList();
    }

    private void initCheckGridList() {
        for (ProcessSectionYsImage ysImage : imageids) {
            String key = ysImage.getKey();
            LogTool.d(TAG, "key=" + key);
            checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                    ysImage.getImageid());
        }
    }

    //初始化放大显示的list
    private void initShowList() {
        showProcessPic.clear();
        showSamplePic.clear();
        for (int i = 0; i < checkGridList.size(); i += 2) {
            showSamplePic.add(checkGridList.get(i).getImgId());
        }
        for (int i = 1; i < checkGridList.size(); i += 2) {
            if (!checkGridList.get(i).getImgId().contains(Constant.DEFALUT_PIC_HEAD)) {
                showProcessPic.add(checkGridList.get(i).getImgId());
            }
        }
    }

    private void setConfimStatus() {
        switch (processSection.getStatus()) {
            case Constant.NO_START:
                mainHeadView.setRightTitleVisable(View.GONE);
                btn_confirm.setEnabled(false);
                break;
            case Constant.DOING:
            case Constant.YANQI_BE_DOING:
            case Constant.YANQI_AGREE:
            case Constant.YANQI_REFUSE:
                mainHeadView.setRightTitleVisable(View.VISIBLE);
                mainHeadView.setRigthTitleEnable(true);
                if (currentUploadCount < BusinessCovertUtil
                        .getCheckPicCountBySection(processSection.getName())) {
                    //设计师图片没上传完，不能验收
                    btn_confirm.setText(this.getResources().getString(
                            R.string.confirm_upload));
                    btn_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_OK);
                            appManager.finishActivity(CheckActivity.this);
                        }
                    });
                    if (currentState == FINISH_STATUS) {
                        btn_confirm.setEnabled(true);
                    } else {
                        btn_confirm.setEnabled(false);
                    }
                } else {
                    boolean isFinish = isSectionInfoFishish(processSection.getItems());
                    LogTool.d(TAG, "isFinish=" + isFinish);
                    if (isFinish) {
                        //图片上传完了，可以进行验收
                        btn_confirm.setText(this.getResources().getString(
                                R.string.confirm_tip));
                        btn_confirm.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                onClickCheckConfirm();
                            }
                        });
                        if (currentState == FINISH_STATUS) {
                            btn_confirm.setEnabled(true);
                        } else {
                            btn_confirm.setEnabled(false);
                        }
                    } else {
                        //图片上传完了，但是工序的某些节点还没有完工
                        btn_confirm.setEnabled(false);
                        btn_confirm.setText(this.getResources().getString(
                                R.string.confirm_not_finish));
                    }
                }
                break;
            case Constant.FINISHED:
                mainHeadView.setRightTitleVisable(View.GONE);
                btn_confirm.setEnabled(false);
                btn_confirm.setText(this.getResources().getString(R.string.confirm_finish));
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否所有节点都已经完工了
     *
     * @param processSectionItems
     * @return
     */
    private boolean isSectionInfoFishish(List<ProcessSectionItem> processSectionItems) {
        boolean flag = true;
        for (ProcessSectionItem processSectionItem : processSectionItems) {
            LogTool.d(TAG, "sectionitem name =" + processSectionItem.getName());
            LogTool.d(TAG, "sectionitem status =" + processSectionItem.getStatus());
            if (!processSectionItem.getStatus().equals(Constant.FINISHED)) {
                LogTool.d(TAG, "sectionitem not finish");
                flag = false;
                return flag;
            }
        }
        return flag;
    }

    private void changeEditStatus() {
        if (!processSection.getStatus().equals(Constant.FINISHED)) {
            if (currentState == FINISH_STATUS) {
                mainHeadView.setRightTitle(getString(R.string.finish));
                currentState = EDIT_STATUS;
                adapter.setCanDelete(true);
            } else {
                mainHeadView.setRightTitle(getString(R.string.edit));
                currentState = FINISH_STATUS;
                adapter.setCanDelete(false);
            }
        } else {
            mainHeadView.setRigthTitleEnable(false);
            btn_confirm.setEnabled(false);
        }
        setConfimStatus();
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                setResult(RESULT_OK);
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                changeEditStatus();
                break;
            case R.id.btn_confirm:
                onClickCheckConfirm();
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpload(int position) {
        if (processSection.getStatus().equals(Constant.NO_START)) {
            return;
        }
        LogTool.d(TAG, "position:" + position);
        key = position;
        LogTool.d(TAG, "key:" + key);
        showPopWindow();
    }

    protected void showPopWindow() {
        if (popupWindow == null) {
            popupWindow = new AddPhotoDialog(this, this);
        }
        popupWindow.show();
    }

    @Override
    public void delete(int position) {
        LogTool.d(TAG, "position:" + position);
        key = position;
        LogTool.d(TAG, "key:" + key);
        deleteCheckImage(key + "");
    }

    private void deleteCheckImage(String key) {
        DeleteCheckImgRequest deleteCheckImgRequest = new DeleteCheckImgRequest();
        deleteCheckImgRequest.set_id(processInfoId);
        deleteCheckImgRequest.setSection(sectionName);
        deleteCheckImgRequest.setKey(key);
        Api.deleteCheckImg(deleteCheckImgRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                currentUploadCount--;
                LogTool.d(TAG, "currentUploadCount=" + currentUploadCount);
                updateList(Constant.HOME_ADD_PIC);
                changeEditStatus();
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
            makeTextLong("没有sd卡，无法打开相机");
        }
    }

    @Override
    public void secondItemClick() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
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
                        uploadImage(imageBitmap);
                    }
                }
                break;
            case Constant.REQUESTCODE_LOCATION:// 本地选取
                if (data != null) {
                    Uri uri = data.getData();
                    LogTool.d(TAG, "uri:" + uri);
                    if (null != uri) {
                        Bitmap imageBitmap = ImageUtil.getImage(ImageUtil
                                .getImagePath(uri, this));
                        if (null != imageBitmap) {
                            uploadImage(imageBitmap);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void uploadImage(Bitmap imageBitmap) {
        if (imageBitmap == null) return;
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(ImageUtil.transformBitmapToBytes(imageBitmap));
        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                addImageToCheck(apiResponse.getData());
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

    private void addImageToCheck(final String imageid) {
        final AddImageToCheckRequest addImageToCheckRequest = new AddImageToCheckRequest();
        addImageToCheckRequest.set_id(processInfoId);
        addImageToCheckRequest.setSection(sectionName);
        addImageToCheckRequest.setKey(key + "");
        addImageToCheckRequest.setImageid(imageid);
        Api.addImageToCheck(addImageToCheckRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                currentUploadCount++;
                LogTool.d(TAG, "currentUploadCount:" + currentUploadCount);
                updateList(imageid);
                setConfimStatus();
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

    private void updateList(String imgid) {
        adapter.updateItem(key * 2 + 1, imgid);
        showProcessPic.clear();
        for (int i = 1; i < checkGridList.size(); i += 2) {
            if (!checkGridList.get(i).getImgId().contains(Constant.DEFALUT_PIC_HEAD)) {
                showProcessPic.add(checkGridList.get(i).getImgId());
            }
        }
    }

    private void onClickCheckConfirm() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(CheckActivity.this);
        dialog.setTitle("提醒业主验收");
        dialog.setMessage("确定提醒业主验收吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmCanCheckByDesigner(processInfoId,
                                sectionName);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    // 设计师确认可以开始验收
    private void confirmCanCheckByDesigner(String processid, String section) {
        NotifyOwnerCheckRequest notifyOwnerCheckRequest = new NotifyOwnerCheckRequest();
        notifyOwnerCheckRequest.set_id(processid);
        notifyOwnerCheckRequest.setSection(section);
        Api.notifyOwnerCheck(notifyOwnerCheckRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                btn_confirm.setEnabled(false);
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
            for (int i = 0; i < ta.length(); i++) {
                LogTool.d(TAG, "res id:" + ta.getResourceId(i, 0));
                GridItem item = new GridItem("drawable://" + ta.getResourceId(i, 0));
                gridList.add(item);
            }
            return gridList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startShowActivity(int arg2) {
        Bundle bundle = new Bundle();
        if (arg2 % 2 == 0) {
            bundle.putStringArrayList(Constant.IMAGE_LIST,
                    (ArrayList<String>) showSamplePic);
            bundle.putInt(Constant.CURRENT_POSITION, arg2 / 2);
        } else {
            String currentImageId = checkGridList.get(arg2).getImgId();
            LogTool.d(TAG, "currentImageId:" + currentImageId);
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
    public void onBackPressed() {
        setResult(RESULT_OK);
        appManager.finishActivity(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_pic;
    }

}
