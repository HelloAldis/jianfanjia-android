package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.ShowPicPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.DeletePicPopWindow;

import java.util.ArrayList;
import java.util.List;

public class ShowProcessPicActivity extends BaseActivity implements
        ViewPagerClickListener, OnPageChangeListener, View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = ShowProcessPicActivity.class.getName();
    private ViewPager viewPager;
    private ShowPicPagerAdapter showPicPagerAdapter;
    private TextView tipView;
    private List<String> imageList = new ArrayList<String>();
    private int currentPosition;// 当前第几张照片
    private int totalCount = 0;
    private String processid;
    private String section;
    private String item;
    private String tipText = null;
    private DeletePicPopWindow deletePicPopWindow = null;
    private boolean isDeletePic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            processid = bundle.getString(Global.PROCESS_ID, null);
            section = bundle.getString(Global.SECTION, null);
            item = bundle.getString(Global.ITEM, null);
            currentPosition = bundle.getInt(Constant.CURRENT_POSITION, 0);
            imageList = bundle.getStringArrayList(Constant.IMAGE_LIST);
            for (String str : imageList) {
                LogTool.d(this.getClass().getName(), " str:" + str);
            }
            totalCount = imageList.size();
        }
        showPicPagerAdapter = new ShowPicPagerAdapter(this, imageList, this);
        showPicPagerAdapter.setOnLongClickListener(this);
        viewPager.setAdapter(showPicPagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.setOnPageChangeListener(this);
        setTipText();
        deletePicPopWindow = new DeletePicPopWindow(this, this);
    }

    private void setTipText() {
        tipText = (currentPosition + 1) + "/" + totalCount;
        tipView.setText(tipText);
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.showpicPager);
        tipView = (TextView) findViewById(R.id.pic_tip);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_delete_pic:
                JianFanJiaClient.deleteImageToProcess(this, processid, section, item, currentPosition, this, this);
                break;
        }
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        if (showPicPagerAdapter.getCount() == 1) {
            setResult(RESULT_OK);
            appManager.finishActivity(this);
        } else {
            deletePicPopWindow.dismiss();
            totalCount--;
            setTipText();
            showPicPagerAdapter.deleteItem(currentPosition);
            isDeletePic = true;
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
    }

    @Override
    public void setListener() {

    }

    @Override
    public boolean onLongClick(View v) {
        if (processid != null) {
            deletePicPopWindow.show(v);
            return true;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_showpic;
    }

    @Override
    public void onClickItem(int potition) {
        if(isDeletePic){
            setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
        }
        appManager.finishActivity(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        currentPosition = arg0;
        setTipText();
    }

}
