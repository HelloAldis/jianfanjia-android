package com.jianfanjia.cn.designer.activity.requirement;

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
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.adapter.CheckGridViewAdapter;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.GridItem;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.bean.ProcessSection;
import com.jianfanjia.cn.designer.bean.ProcessSectionItem;
import com.jianfanjia.cn.designer.bean.ProcessSectionYsImage;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ItemClickCallBack;
import com.jianfanjia.cn.designer.interf.PopWindowCallBack;
import com.jianfanjia.cn.designer.interf.UploadListener;
import com.jianfanjia.cn.designer.tools.FileUtil;
import com.jianfanjia.cn.designer.tools.ImageUtil;
import com.jianfanjia.cn.designer.tools.ImageUtils;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.ItemSpaceDecoration;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;

/**
 * @author fengliang
 * @ClassName: CheckActivity
 * @Description: 验收
 * @date 2015-8-28 下午2:25:36
 */
public class CheckActivity extends BaseActivity implements OnClickListener,
        UploadListener, ItemClickCallBack, PopWindowCallBack {
    private static final String TAG = CheckActivity.class.getName();
    public static final String CHECK_INTENT_FLAG = "check_intent_flag";
    public static final int NOTICE_INTENT = 0;//通知进入的
    public static final int PROCESS_LIST_INTENT = 1;//工地
    private int flagIntent = -1;
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
    private ProcessInfo processInfo = null;
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
        initView();
        initData();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            sectionName = bundle.getString(Constant.SECTION);
            processInfo = (ProcessInfo) bundle.getSerializable(Constant.PROCESS_INFO);
            processInfoId = processInfo.get_id();
            LogTool.d(TAG, "sectionName:" + sectionName + " processInfo:" + processInfo + " processInfoId:" +
                    processInfoId);
            processSection = BusinessManager.getSectionInfoByName(processInfo.getSections(), sectionName);
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
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(MyApplication.dip2px(getApplicationContext(), 5));
        gridView.addItemDecoration(decoration);
        btn_confirm.setText(this.getResources().getString(
                R.string.confirm_upload));
        currentState = FINISH_STATUS;
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.check_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView.setRightTitle(getString(R.string.edit));
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void initData() {
        LogTool.d(TAG, "processSection:" + processSection.get_id());
        mainHeadView.setMianTitle(MyApplication.getInstance().getStringById(processSection.getName()) + "阶段验收");
        checkGridList.clear();
        checkGridList = getCheckedImageById(processSection.getName());
        imageids = processSection.getYs().getImages();
        LogTool.d(TAG, "imageids=" + imageids);
        currentUploadCount = imageids.size();
        LogTool.d(TAG, "currentUploadCount=======" + currentUploadCount);
        for (int i = 0; imageids != null && i < imageids.size(); i++) {
            String key = imageids.get(i).getKey();
            LogTool.d(TAG, "key=" + key);
            checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                    imageids.get(i).getImageid());
        }
        adapter = new CheckGridViewAdapter(CheckActivity.this, checkGridList,
                this, this);
        gridView.setAdapter(adapter);
        setConfimStatus();
        initShowList();

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
        if (!processSection.getStatus().equals(Constant.FINISHED)) {
            mainHeadView.setRightTitleVisable(View.VISIBLE);
            mainHeadView.setRigthTitleEnable(true);
            if (currentUploadCount < BusinessManager
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
        } else {
            mainHeadView.setRightTitleVisable(View.GONE);
            //已经验收过了
            btn_confirm.setEnabled(false);
            btn_confirm.setText(this.getResources().getString(
                    R.string.confirm_finish));
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

    public void changeEditStatus() {
        if (!processSection.getStatus().equals(Constant.FINISHED)) {
            if (currentState == FINISH_STATUS) {
                mainHeadView.setRightTitle(getString(R.string.finish));
                currentState = EDIT_STATUS;
                adapter.setCanDelete(true);
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            } else {
                mainHeadView.setRightTitle(getString(R.string.edit));
                currentState = FINISH_STATUS;
                adapter.setCanDelete(false);
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            }
        } else {
            mainHeadView.setRigthTitleEnable(false);
            btn_confirm.setEnabled(false);
        }
        setConfimStatus();
    }

    @Override
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
        LogTool.d(TAG, "position:" + position);
        key = position;
        LogTool.d(TAG, "key:" + key);
        showPopWindow(checkLayout);
    }

    @Override
    public void delete(int position) {
        LogTool.d(TAG, "position:" + position);
        key = position;
        LogTool.d(TAG, "key:" + key);
        JianFanJiaClient.deleteYanshouImgByDesigner(this,
                processInfoId, sectionName, key + "", new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        updateList(Constant.HOME_ADD_PIC);
                        currentUploadCount--;
                        LogTool.d(TAG, "currentUploadCount--" + currentUploadCount);
                        changeEditStatus();
                    }

                    @Override
                    public void loadFailture(String errorMsg) {
                        hideWaitDialog();
                        makeTextShort(errorMsg);
                    }
                }, this);
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
                        Bitmap imageBitmap = ImageUtil.getImage(ImageUtils
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
        if (null != imageBitmap) {
            JianFanJiaClient.uploadImage(this, imageBitmap, new ApiUiUpdateListener() {

                @Override
                public void preLoad() {
                    showWaitDialog();
                }

                @Override
                public void loadSuccess(Object data) {
                    JianFanJiaClient.submitYanShouImage(CheckActivity.this, processInfoId, sectionName,
                            key + "", dataManager.getCurrentUploadImageId(), new ApiUiUpdateListener() {

                                @Override
                                public void preLoad() {
                                    // TODO Auto-generated
                                    // method stub
                                }

                                @Override
                                public void loadSuccess(Object data) {
                                    LogTool.d(TAG, "submitYanShouImage  data:" + data.toString());
                                    hideWaitDialog();
                                    updateList(dataManager.getCurrentUploadImageId());
                                    currentUploadCount++;
                                    LogTool.d(TAG, "currentUploadCount:" + currentUploadCount);
                                    setConfimStatus();
                                }

                                @Override
                                public void loadFailture(String errorMsg) {
                                    hideWaitDialog();
                                    makeTextShort(errorMsg);
                                }
                            }, this);
                }

                @Override
                public void loadFailture(String errorMsg) {
                    hideWaitDialog();
                    makeTextShort(errorMsg);
                }
            }, this);
        }
    }

    private void updateList(String imgid) {
        GridItem gridItem = checkGridList.get(key * 2 + 1);
        gridItem.setImgId(imgid);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
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
        JianFanJiaClient.confirm_canCheckBydesigner(this, processid, section, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data.toString());
                btn_confirm.setEnabled(false);
            }

            @Override
            public void loadFailture(String errorMsg) {
                LogTool.d(TAG, "errorMsg:" + errorMsg);
                btn_confirm.setEnabled(false);
            }
        }, this);
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
