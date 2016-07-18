package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-17 15:00
 */
public class ChooseDiaryStageActivity extends BaseSwipeBackActivity {

    public static final String CURRENT_CHOOSE_VALUE = "current_choose_item";

    @Bind(R.id.mainhv_diary)
    MainHeadView mainHeadView;

    @Bind(R.id.ll_choose_stage)
    LinearLayout llChooseStage;

    private String currentChooseValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        currentChooseValue = getIntent().getStringExtra(CURRENT_CHOOSE_VALUE);
        if (currentChooseValue != null) {
            LogTool.d("initChooseVulue =" + currentChooseValue);
            setInitSelectedValue();
        }
    }

    private void setInitSelectedValue() {
        for (int i = 0; i < llChooseStage.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llChooseStage.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                View view = linearLayout.getChildAt(j);
                if (view instanceof TextView) {
                    if (((TextView) view).getText().equals(currentChooseValue)) {
                        LogTool.d("chooseTextView");
                        view.setSelected(true);
                    }
                }
            }
        }
    }


    @OnClick({R.id.tv_zhunbei, R.id.tv_kaigong, R.id.tv_chaigai, R.id.tv_shuidian, R.id.tv_nimu, R.id.tv_youqi, R.id
            .tv_anzhuang, R.id.tv_jungong, R.id.tv_ruanzhuang, R.id.tv_ruzhu})
    protected void click(View view) {
        String chooseValue = ((TextView) view).getText().toString();
        LogTool.d("chooseVulue =" + chooseValue);
        Intent intent = getIntent();
        intent.putExtra(IntentConstant.RESPONSE_DATA, chooseValue);
        setResult(RESULT_OK, intent);
        appManager.finishActivity(this);
    }

    private void initView() {
        mainHeadView.setMianTitle(getString(R.string.str_decorate_stage_choose));
        mainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(ChooseDiaryStageActivity.this);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_diary_stage;
    }
}
