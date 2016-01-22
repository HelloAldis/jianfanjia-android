package com.jianfanjia.cn.designer.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.adapter.MyGridViewAdapter;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.CheckInfo.Imageid;
import com.jianfanjia.cn.designer.bean.GridItem;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.bean.SectionItemInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
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
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: CheckActivity
 * @Description: 验收
 * @date 2015-8-28 下午2:25:36
 */
public class CheckActivity extends BaseActivity implements OnClickListener,
        UploadListener, ItemClickCallBack, PopWindowCallBack {
    private static final String TAG = CheckActivity.class.getName();
    public static final int EDIT_STATUS = 0;
    public static final int FINISH_STATUS = 1;
    private MainHeadView mainHeadView = null;
    private RelativeLayout checkLayout = null;
    private GridView gridView = null;
    private TextView btn_confirm = null;
    private MyGridViewAdapter adapter = null;
    private List<GridItem> checkGridList = new ArrayList<>();//本页显示的griditem项
    private List<String> showSamplePic = new ArrayList<>();//示例照片
    private List<String> showProcessPic = new ArrayList<>();//工地验收照片
    private List<Imageid> imageids = null;
    private String processInfoId = null;// 工地id
    private String sectionInfoName = null;// 工序名称
    private String sectionInfoStatus = null;// 工序状态
    private int key;
    private File mTmpFile = null;
    private ProcessInfo processInfo;
    private List<SectionItemInfo> sectionItemInfos;
    private int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sectionInfoName = bundle.getString(Constant.PROCESS_NAME);
            sectionInfoStatus = bundle.getString(Constant.PROCESS_STATUS, Constant.DOING);
            processInfoId = bundle.getString(Global.PROCESS_ID);
            LogTool.d(TAG, "processInfoId:" + processInfoId + " sectionInfoName:" + sectionInfoName + " processInfoStatus:" + sectionInfoStatus);
            if (processInfoId != null) {
                loadCurrentProcess(processInfoId);
            }
        }
    }

    @Override
    public void initView() {
        initMainHeadView();
        checkLayout = (RelativeLayout) findViewById(R.id.checkLayout);
        gridView = (GridView) findViewById(R.id.mygridview);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_confirm.setText(this.getResources().getString(
                R.string.confirm_upload));
        currentState = FINISH_STATUS;
        gridView.setFocusable(false);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.check_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView.setRightTitle(getString(R.string.edit));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void loadCurrentProcess(String processid) {
        JianFanJiaClient.get_ProcessInfo_By_Id(this, processid,
                new ApiUiUpdateListener() {

                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        initData();
                    }

                    @Override
                    public void loadFailture(String errorMsg) {
                        hideWaitDialog();
                        makeTextShort(errorMsg);
                    }
                }, this);
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

    private void initData() {
        adapter = new MyGridViewAdapter(CheckActivity.this, checkGridList,
                this, this);
        gridView.setAdapter(adapter);
        processInfo = dataManager.getDefaultProcessInfo();
        if (processInfo != null) {
            sectionItemInfos = processInfo.getSectionInfoByName(sectionInfoName).getItems();
            refreshList();
        }
        mainHeadView.setMianTitle(MyApplication.getInstance().getStringById(sectionInfoName) + "阶段验收");
    }

    private void refreshList() {
        LogTool.d(TAG, "processInfo != null");
        checkGridList.clear();
        checkGridList = getCheckedImageById(sectionInfoName);
        processInfo = dataManager.getDefaultProcessInfo();
        imageids = processInfo.getImageidsByName(sectionInfoName);
        for (int i = 0; imageids != null && i < imageids.size(); i++) {
            String key = imageids.get(i).getKey();
            LogTool.d(TAG, imageids.get(i).getImageid());
            checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                    imageids.get(i).getImageid());
        }
        adapter.setList(checkGridList);
        adapter.notifyDataSetChanged();
        setConfimStatus();
        initShowList();
    }

    private void setConfimStatus() {
        int count = imageids.size();
        if (!sectionInfoStatus.equals(Constant.FINISHED)) {
            mainHeadView.setRightTitleVisable(View.VISIBLE);
            if (count < checkGridList.size() / 2) {
                //设计师图片没上传完，不能验收
                btn_confirm.setText(this.getResources().getString(
                        R.string.confirm_upload));
                btn_confirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                if (currentState == FINISH_STATUS) {
                    btn_confirm.setEnabled(true);
                } else {
                    btn_confirm.setEnabled(false);
                }
            } else {
                boolean isFinish = isSectionInfoFishish(sectionItemInfos);
                if (isFinish) {
                    //图片上传完了，可以进行验收
//                    btn_confirm.setEnabled(true);
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
     * @param sectionItemInfos
     * @return
     */
    private boolean isSectionInfoFishish(List<SectionItemInfo> sectionItemInfos) {
        boolean flag = true;
        for (SectionItemInfo sectionItemInfo : sectionItemInfos) {
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
    public void setListener() {

    }

    public void changeEditStatus() {
        if (!sectionInfoStatus.equals(Constant.FINISHED)) {
            if (currentState == FINISH_STATUS) {
                mainHeadView.setRightTitle(getString(R.string.finish));
                currentState = EDIT_STATUS;
                adapter.setCanDelete(true);
                adapter.notifyDataSetInvalidated();
            } else {
                mainHeadView.setRightTitle(getString(R.string.edit));
                currentState = FINISH_STATUS;
                adapter.setCanDelete(false);
                adapter.notifyDataSetInvalidated();
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
                processInfoId, sectionInfoName, key + "", new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        refreshList();
//                        changeEditStatus();
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
            startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
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
                    JianFanJiaClient.submitYanShouImage(CheckActivity.this, processInfoId, sectionInfoName,
                            key + "", dataManager.getCurrentUploadImageId(), new ApiUiUpdateListener() {

                                @Override
                                public void preLoad() {
                                    // TODO Auto-generated
                                    // method stub

                                }

                                @Override
                                public void loadSuccess(Object data) {
                                    hideWaitDialog();
                                    refreshList();
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
                                sectionInfoName);
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
