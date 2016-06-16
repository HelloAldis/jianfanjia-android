package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 11:25
 */
public class AddDiaryActivity extends BaseSwipeBackActivity {
    private static final String TAG = AddDiaryActivity.class.getClass().getName();

    public static final String CURRENT_DIARYSET_TITLE = "current_diaryset_title";

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.et_add_diary_content)
    EditText etAddDiaryContent;

    @Bind(R.id.gv_add_diary_pic)
    GridView gvAddDiaryPic;

    @Bind(R.id.tv_add_diary_dec_stage_content)
    TextView tvAddDiaryDecStageContent;

    @Bind(R.id.tv_add_diary_now_diaryset_content)
    TextView tvAddDiaryNowDiarySetContent;

    public List<DiarySetInfo> mDiarySetInfoList;

    private List<String> mDiarySetTitleList;

    private String currentDiarySetTitle;

    private DiarySetInfo currentDiarySet;

    private DiaryInfo mDiaryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
        initData();
    }


    private void initView() {
        initMianView();

    }

    private void initMianView() {
        mMainHeadView.setMianTitle(getString(R.string.add_diary));
        mMainHeadView.setRightTitle(getString(R.string.save));
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(AddDiaryActivity.class);
            }
        });
    }

    private void setMianHeadRightTitleEnable() {
        if (true) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }
    }

    private void initData() {
        setMianHeadRightTitleEnable();

        etAddDiaryContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiaryInfo.setContent(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setNowDiarySetTitle();
        setDecStage();
    }

    private void setNowDiarySetTitle() {
        tvAddDiaryNowDiarySetContent.setText(currentDiarySetTitle);
    }

    private void setDecStage() {
        tvAddDiaryDecStageContent.setText(mDiaryInfo.getSection_label());
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentDiarySetTitle = bundle.getString(CURRENT_DIARYSET_TITLE);
            mDiarySetInfoList = (ArrayList<DiarySetInfo>) bundle.getSerializable(IntentConstant.DIARYSET_INFO_LIST);

            mDiarySetTitleList = new ArrayList<>();
            for (DiarySetInfo diarySetInfo : mDiarySetInfoList) {
                mDiarySetTitleList.add(diarySetInfo.getTitle());
            }
            if (currentDiarySetTitle == null) {//没有传当前的日记本，默认选择最后一个日记本
                currentDiarySet = mDiarySetInfoList.get(mDiarySetInfoList.size());
                currentDiarySetTitle = currentDiarySet.getTitle();
            } else {
                for (DiarySetInfo diarySetInfo : mDiarySetInfoList) {
                    if (currentDiarySetTitle.equals(diarySetInfo.getTitle())) {
                        currentDiarySet = diarySetInfo;
                        break;
                    }
                }
            }
            LogTool.d(TAG, "currentDiarySetTitle =" + currentDiarySetTitle);

            mDiaryInfo = new DiaryInfo();
            mDiaryInfo.setSection_label(currentDiarySet.getLatest_section_label());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != RESULT_OK){
            return;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diary_add;
    }
}
