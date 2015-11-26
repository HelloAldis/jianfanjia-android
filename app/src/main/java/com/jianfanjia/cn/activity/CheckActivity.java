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
    public static final int EDIT_STATUS = 0;
    public static final int FINISH_STATUS = 1;
    public static final String SHOW_LIST = "show_list";
    public static final String POSITION = "position";

    private RelativeLayout checkLayout = null;
    private MainHeadView mainHeadView = null;
    private TextView backView = null;// 返回视图
    private TextView check_pic_title = null;
    private GridView gridView = null;
    private TextView btn_confirm = null;
    private MyGridViewAdapter adapter = null;
    private List<GridItem> checkGridList = new ArrayList<GridItem>();
    private List<String> showSamplePic = new ArrayList<String>();
    private List<String> showProcessPic = new ArrayList<String>();
    private List<Imageid> imageids = null;
    private String processInfoId = null;// 工地id
    private String sectionInfoName = null;// 工序名称
    private int sectionInfoStatus = -1;// 工序状态
    private ProcessInfo processInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sectionInfoName = bundle.getString(Constant.PROCESS_NAME);
            sectionInfoStatus = bundle.getInt(Constant.PROCESS_STATUS, 0);
            processInfo = (ProcessInfo) bundle.getSerializable(Global.PROCESS_INFO);
            LogTool.d(TAG, "processInfoId:" + processInfoId
                    + " sectionInfoName:" + sectionInfoName
                    + " processInfoStatus:" + sectionInfoStatus);
        }

        initProcessInfo();
    }

    private void initProcessInfo() {
        if (processInfo != null) {
            processInfoId = processInfo.get_id();
            initData();
        }
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
        mainHeadView = (MainHeadView) findViewById(R.id.check_pic_head_layout);
        mainHeadView.setBackListener(this);
        checkLayout = (RelativeLayout) findViewById(R.id.checkLayout);
        gridView = (GridView) findViewById(R.id.mygridview);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        gridView.setFocusable(false);
    }

    private void initData() {
        mainHeadView.setMianTitle(MyApplication.getInstance().getStringById(
                sectionInfoName)
                + getString(R.string.check_head));
        switch (sectionInfoStatus) {
            case Constant.NOT_START:
                break;
            case Constant.WORKING:
                break;
            case Constant.FINISH:
                btn_confirm.setEnabled(false);
                break;
            case Constant.OWNER_APPLY_DELAY:
                break;
            case Constant.DESIGNER_APPLY_DELAY:
                break;
            default:
                break;
        }
        adapter = new MyGridViewAdapter(CheckActivity.this, checkGridList,
                this);
        gridView.setAdapter(adapter);
        initList();
    }

    private void initList() {
        checkGridList = getCheckedImageById(sectionInfoName);
        if (processInfo != null) {
            LogTool.d(TAG, "processInfo != null");
            imageids = processInfo.getImageidsByName(sectionInfoName);
            int imagecount = 0;
            for (int i = 0; imageids != null && i < imageids.size(); i++) {
                String key = imageids.get(i).getKey();
                LogTool.d(TAG, imageids.get(i).getImageid());
                checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                        imageids.get(i).getImageid());
                imagecount++;
                /*if (imageids.get(i).getImageid() != null) {

                }*/
            }
            setConfimStatus(imagecount);
            adapter.setList(checkGridList);
            adapter.notifyDataSetChanged();
        }
        initShowList();
    }

    private void setConfimStatus(int count) {
        btn_confirm.setText(this.getResources().getString(
                R.string.confirm_done));
        if (count < BusinessManager
                .getCheckPicCountBySection(sectionInfoName)) {
            btn_confirm.setEnabled(false);
        } else {
            btn_confirm.setEnabled(true);
            btn_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickCheckDone();
                }
            });
        }
        if (sectionInfoStatus == Constant.FINISH) {
            btn_confirm.setEnabled(false);
        }

        if (sectionInfoStatus == Constant.FINISH) {
            btn_confirm.setEnabled(false);
        }
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
