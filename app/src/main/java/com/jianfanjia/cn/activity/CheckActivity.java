package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
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
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: CheckActivity
 * @Description: 验收
 * @date 2015-8-28 下午2:25:36
 */
public class CheckActivity extends BaseActivity implements OnClickListener, ItemClickCallBack {
    private static final String TAG = CheckActivity.class.getName();
    private RelativeLayout checkLayout = null;
    private MainHeadView mainHeadView = null;
    private GridView gridView = null;
    private TextView btn_confirm = null;
    private MyGridViewAdapter adapter = null;
    private List<GridItem> checkGridList = new ArrayList<GridItem>();
    private List<String> showSamplePic = new ArrayList<String>();
    private List<String> showProcessPic = new ArrayList<String>();
    private List<Imageid> imageids = null;
    private String processInfoId = null;// 工地id
    private String sectionInfoName = null;// 工序名称
    private String sectionInfoStatus = null;// 工序状态
    private ProcessInfo processInfo;
    private List<SectionItemInfo> sectionItemInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sectionInfoName = bundle.getString(Constant.PROCESS_NAME);
            sectionInfoStatus = bundle.getString(Constant.PROCESS_STATUS, Constant.DOING);
            processInfoId = bundle.getString(Global.PROCESS_ID);
            LogTool.d(TAG, "processInfoId:" + processInfoId
                    + " sectionInfoName:" + sectionInfoName
                    + " processInfoStatus:" + sectionInfoStatus);
            if (processInfoId != null) {
                loadCurrentProcess();
            }
        }
    }

    private void loadCurrentProcess() {
        JianFanJiaClient.get_ProcessInfo_By_Id(this, processInfoId,
                new ApiUiUpdateListener() {

                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        hideWaitDialog();
//                        initProcessInfo();
                        initData();
                    }

                    @Override
                    public void loadFailture(String errorMsg) {
                        hideWaitDialog();
                        makeTextShort(errorMsg);
//                        initProcessInfo();
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

    @Override
    public void initView() {
        mainHeadView = (MainHeadView) findViewById(R.id.check_pic_head_layout);
        mainHeadView.setBackListener(this);
        checkLayout = (RelativeLayout) findViewById(R.id.checkLayout);
        gridView = (GridView) findViewById(R.id.mygridview);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        gridView.setFocusable(false);
    }

    private void initData() {
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
                this);
        gridView.setAdapter(adapter);
        processInfo = dataManager.getDefaultProcessInfo();
        if (processInfo != null) {
            sectionItemInfos = processInfo.getSectionInfoByName(sectionInfoName).getItems();
            refreshList();
        }
        mainHeadView.setMianTitle(MyApplication.getInstance().getStringById(
                sectionInfoName)
                + "阶段验收");
    }

    private void refreshList() {
        LogTool.d(TAG, "processInfo != null");
        checkGridList.clear();
        checkGridList = getCheckedImageById(sectionInfoName);
        processInfo = dataManager.getDefaultProcessInfo();
        imageids = processInfo.getImageidsByName(sectionInfoName);
        int imagecount = imageids.size();
        for (int i = 0; imageids != null && i < imageids.size(); i++) {
            String key = imageids.get(i).getKey();
            LogTool.d(TAG, imageids.get(i).getImageid());
            checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                    imageids.get(i).getImageid());
        }
        adapter.setList(checkGridList);
        adapter.notifyDataSetChanged();
        setConfimStatus(imagecount);
        initShowList();
    }

    private void setConfimStatus(int count) {
        if (!sectionInfoStatus.equals(Constant.FINISHED)) {
            if (count < BusinessManager
                    .getCheckPicCountBySection(sectionInfoName)) {
                //设计师图片没上传完，不能验收
                btn_confirm.setEnabled(false);
                btn_confirm.setText(this.getResources().getString(
                        R.string.confirm_done));
            } else {
                boolean isFinish = isSectionInfoFishish(sectionItemInfos);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_confirm:
                onClickCheckDone();
                break;
            default:
                break;
        }
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
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
                        confirmCheckDoneByOwner(processInfoId, sectionInfoName);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    // 业主确认对比验收完成
    private void confirmCheckDoneByOwner(String processid, String section) {
        JianFanJiaClient.confirm_CheckDoneByOwner(this, processid, section, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                btn_confirm.setEnabled(false);
                btn_confirm.setText(getResources().getString(
                        R.string.confirm_finish));
            }

            @Override
            public void loadFailture(String err_msg) {

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
