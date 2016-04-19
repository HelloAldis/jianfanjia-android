package com.jianfanjia.cn.supervisor.activity.requirement;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionYsImage;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.common.ShowPicActivity;
import com.jianfanjia.cn.supervisor.adapter.CheckGridViewAdapter;
import com.jianfanjia.cn.supervisor.application.MyApplication;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.bean.GridItem;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.interf.ItemClickCallBack;
import com.jianfanjia.cn.supervisor.view.MainHeadView;
import com.jianfanjia.cn.supervisor.view.baseview.ItemSpaceDecoration;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * @author fengliang
 * @ClassName: CheckActivity
 * @Description: 验收
 * @date 2015-8-28 下午2:25:36
 */
public class CheckActivity extends BaseSwipeBackActivity implements ItemClickCallBack {
    private static final String TAG = CheckActivity.class.getName();
    public static final String CHECK_INTENT_FLAG = "check_intent_flag";
    public static final int NOTICE_INTENT = 0;//通知进入的
    public static final int PROCESS_LIST_INTENT = 1;//工地
    private int flagIntent = -1;
    @Bind(R.id.check_pic_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.mygridview)
    protected RecyclerView gridView = null;

    private GridLayoutManager gridLayoutManager = null;
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
        mainHeadView.setMianTitle(sectionInfo.getLabel() + getResources()
                .getString(R.string.stage_check_text));
        checkGridList.clear();
        checkGridList = getCheckedImageById(sectionInfo.getName());
        imageids = sectionInfo.getYs().getImages();
        currentUploadCount = imageids.size();
        LogTool.d(TAG, "currentUploadCount=" + currentUploadCount);
        for (ProcessSectionYsImage ysImage : imageids) {
            String key = ysImage.getKey();
            LogTool.d(TAG, "key=" + key);
            checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
                    ysImage.getImageid());
        }
        adapter = new CheckGridViewAdapter(CheckActivity.this, checkGridList,
                this);
        gridView.setAdapter(adapter);
        initShowList();
    }


    @OnClick({R.id.head_back_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                setResult(RESULT_CANCELED);
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
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
