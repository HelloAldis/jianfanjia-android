package com.jianfanjia.cn.activity;

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

import com.jianfanjia.cn.adapter.MyGridViewAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.CheckInfo.Imageid;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.UploadListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.ImageUtils;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

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

    private RelativeLayout checkLayout = null;
    private TextView backView = null;// 返回视图
    private TextView check_pic_title = null;
    private TextView check_pic_edit = null;
    private GridView gridView = null;
    private TextView btn_confirm = null;
    private MyGridViewAdapter adapter = null;
    private List<GridItem> checkGridList = new ArrayList<>();
    private List<String> showSamplePic = new ArrayList<>();
    private List<String> showProcessPic = new ArrayList<>();
    private List<Imageid> imageids = null;
    private String processInfoId = null;// 工地id
    private String sectionInfoName = null;// 工序名称
    private String sectionInfoStatus = null;// 工序状态
    private String key = null;
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
            sectionInfoStatus = bundle.getString(Constant.PROCESS_STATUS, "0");
            LogTool.d(TAG, "processInfoId:" + processInfoId
                    + " sectionInfoName:" + sectionInfoName
                    + " processInfoStatus:" + sectionInfoStatus);
        }
        if (NetTool.isNetworkAvailable(this)) {
            loadCurrentProcess();
        } else {
            initProcessInfo();
        }
    }

    private void initProcessInfo() {
        processInfo = dataManager.getDefaultProcessInfo();
        LogTool.d(TAG, "processInfo:" + processInfo);
        if (processInfo != null) {
            processInfoId = processInfo.get_id();
            sectionItemInfos = processInfo.getSectionInfoByName(sectionInfoName).getItems();
            initData();
        } else {
            btn_confirm.setEnabled(false);
        }
    }

    private void loadCurrentProcess() {
        JianFanJiaClient.get_ProcessInfo_By_Id(this, dataManager.getDefaultProcessId(),
                new ApiUiUpdateListener() {

                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
                        initProcessInfo();
                    }

                    @Override
                    public void loadFailture(String errorMsg) {
                        hideWaitDialog();
                        initProcessInfo();
                    }
                }, this);
    }

    private void initShowList() {
        showProcessPic.clear();
        showSamplePic.clear();
        for (int i = 0; i < checkGridList.size(); i = i + 2) {
            showSamplePic.add(checkGridList.get(i).getImgId());
        }
        for (int i = 1; i < checkGridList.size(); i = i + 2) {
            if (checkGridList.get(i).getImgId() != null) {
                showProcessPic.add(checkGridList.get(i).getImgId());
            }
        }
    }

    @Override
    public void initView() {
        checkLayout = (RelativeLayout) findViewById(R.id.checkLayout);
        backView = (TextView) findViewById(R.id.check_pic_back);
        check_pic_title = (TextView) findViewById(R.id.check_pic_title);
        check_pic_edit = (TextView) findViewById(R.id.check_pic_edit);
        gridView = (GridView) findViewById(R.id.mygridview);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);

        btn_confirm.setText(this.getResources().getString(
                R.string.confirm_upload));
        check_pic_edit.setVisibility(View.VISIBLE);
        check_pic_edit.setText("编辑");
        currentState = EDIT_STATUS;
        gridView.setFocusable(false);
    }

    private void initData() {
        check_pic_title.setText(MyApplication.getInstance().getStringById(
                sectionInfoName)
                + "阶段验收");
        switch (sectionInfoStatus) {
            case Constant.NO_START:
                break;
            case Constant.DOING:
                break;
            case Constant.FINISHED:
                btn_confirm.setEnabled(false);
                break;
            default:
                break;
        }
        adapter = new MyGridViewAdapter(CheckActivity.this, checkGridList,
                this, this);
        gridView.setAdapter(adapter);
        initList();
    }

    private void initList() {
        checkGridList.clear();
        checkGridList = getCheckedImageById(sectionInfoName);
        processInfo = dataManager.getDefaultProcessInfo();
        if (processInfo == null) {
            processInfo = dataManager.getProcessInfoById(processInfoId);
        }
        if (processInfo != null) {
            dataManager.setCurrentProcessInfo(processInfo);
            LogTool.d(TAG, "processInfo != null");
            imageids = processInfo.getImageidsByName(sectionInfoName);
            int imagecount = 0;
            for (int i = 0; imageids != null && i < imageids.size(); i++) {
                String key = imageids.get(i).getKey();
                LogTool.d(TAG, imageids.get(i).getImageid());
                checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                        imageids.get(i).getImageid());
                imagecount++;
            }
            setConfimStatus(imagecount);
            adapter.setList(checkGridList);
        }
        initShowList();
    }

    private void setConfimStatus(int count) {
        /*btn_confirm.setText(this.getResources().getString(
                R.string.confirm_upload));
        if (count < BusinessManager
                .getCheckPicCountBySection(sectionInfoName)) {
            btn_confirm.setText(this.getResources().getString(
                    R.string.confirm_upload));
            btn_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            btn_confirm.setText(this.getResources().getString(
                    R.string.confirm_tip));
            btn_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickCheckConfirm();
                }
            });
        }
        if (sectionInfoStatus == Constant.FINISH) {
            btn_confirm.setEnabled(false);
        }*/

        if (!sectionInfoStatus.equals(Constant.FINISHED)) {
            if (count < BusinessManager
                    .getCheckPicCountBySection(sectionInfoName)) {
                //设计师图片没上传完，不能验收
                btn_confirm.setEnabled(true);
                btn_confirm.setText(this.getResources().getString(
                        R.string.confirm_upload));
                btn_confirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } else {
                boolean isFinish = isSectionInfoFishish(sectionItemInfos);
                if (isFinish) {
                    //图片上传完了，可以进行验收
                    btn_confirm.setEnabled(true);
                    btn_confirm.setText(this.getResources().getString(
                            R.string.confirm_tip));
                    btn_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onClickCheckConfirm();
                        }
                    });
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
        backView.setOnClickListener(this);
        check_pic_edit.setOnClickListener(this);
    }

    public void changeEditStatus() {
        if (!sectionInfoStatus.equals(Constant.FINISHED)) {
            if (currentState == FINISH_STATUS) {
                check_pic_edit.setText("编辑");
                currentState = EDIT_STATUS;
                adapter.setCanDelete(false);
                btn_confirm.setEnabled(true);
                adapter.notifyDataSetInvalidated();
            } else {
                btn_confirm.setEnabled(false);
                check_pic_edit.setText("完成");
                currentState = FINISH_STATUS;
                adapter.setCanDelete(true);
                adapter.notifyDataSetInvalidated();
            }
        } else {
            check_pic_edit.setEnabled(false);
            btn_confirm.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_pic_back:
                finish();
                break;
            case R.id.check_pic_edit:
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
        key = position + "";
        LogTool.d(TAG, "key:" + key);
        showPopWindow(checkLayout);
    }

    protected void showPopWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new AddPhotoPopWindow(this, this);
        }
        popupWindow.show(view);
    }

    @Override
    public void delete(int position) {
        LogTool.d(TAG, "position:" + position);
        key = position + "";
        LogTool.d(TAG, "key:" + key);
        JianFanJiaClient.deleteYanshouImgByDesigner(this,
                processInfoId, sectionInfoName, key, this, this);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        initList();
        changeEditStatus();
    }

    @Override
    public void takecamera() {
        mTmpFile = FileUtil.createTmpFile(this);
        if (mTmpFile != null) {
            Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
            startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
        } else {
            makeTextLong("没有sd卡，无法打开相机");
        }
    }

    @Override
    public void takePhoto() {
        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
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
                    Uri uri = Uri.fromFile(mTmpFile);
                    LogTool.d(TAG, "uri:" + uri);
                    uploadImage(mTmpFile.getPath());
                }
                break;
            case Constant.REQUESTCODE_LOCATION:// 本地选取
                if (data != null) {
                    Uri uri = data.getData();
                    LogTool.d(TAG, "uri:" + uri);
                    if (null != uri) {
                        uploadImage(ImageUtils.getImagePath(uri, this));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void uploadImage(String imagePath) {
        Bitmap imageBitmap = ImageUtil.getImage(imagePath);
        if (null != imageBitmap) {
            JianFanJiaClient.uploadImage(this, imageBitmap, new ApiUiUpdateListener() {

                @Override
                public void preLoad() {
                    showWaitDialog();
                }

                @Override
                public void loadSuccess(Object data) {
                    JianFanJiaClient.submitYanShouImage(CheckActivity.this, processInfoId, sectionInfoName,
                            key, dataManager.getCurrentUploadImageId(), new ApiUiUpdateListener() {

                                @Override
                                public void preLoad() {
                                    // TODO Auto-generated
                                    // method stub

                                }

                                @Override
                                public void loadSuccess(Object data) {
                                    hideWaitDialog();
                                    initList();
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void loadFailture(String errorMsg) {
                                    hideWaitDialog();
                                    makeTextLong(getString(R.string.tip_error_internet));
                                }
                            }, this);
                }

                @Override
                public void loadFailture(String errorMsg) {
                    hideWaitDialog();
                    makeTextLong(getString(R.string.tip_error_internet));
                }
            }, this);
        }
    }

    private void onClickCheckConfirm() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(CheckActivity.this);
        dialog.setTitle("提交验收");
        dialog.setMessage("确定提交验收吗？");
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
            List<GridItem> gridList = new ArrayList<GridItem>();
            int arrId = getResources().getIdentifier(sectionName, "array",
                    MyApplication.getInstance().getPackageName());
            TypedArray ta = getResources().obtainTypedArray(arrId);
            for (int i = 0; i < ta.length(); i++) {
                LogTool.d(TAG, "res id:" + ta.getResourceId(i, 0));
                GridItem item = new GridItem();
                item.setImgId("drawable://" + ta.getResourceId(i, 0));
                gridList.add(item);
            }
            return gridList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_pic;
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

}
