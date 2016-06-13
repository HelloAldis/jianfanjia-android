package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.DailyInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 11:39
 */
public class AddDiaryActivity extends BaseSwipeBackActivity {

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.et_add_dairy_title_content)
    EditText etAddDiaryTitleContent;

    @Bind(R.id.et_add_dairy_area_content)
    EditText etAddDiaryAreaContent;

    @Bind(R.id.tv_add_diary_housetype_content)
    TextView tvAddDiaryHousetyoeContent;

    @Bind(R.id.tv_add_diary_style_content)
    TextView tvAddDiaryStyleContent;

    private DailyInfo mDailyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void initView() {
        initMianView();

        setMianHeadRightTitleEnable();

    }

    private void initMianView() {
        mMainHeadView.setMianTitle(getString(R.string.diary_info));
        mMainHeadView.setRightTitle(getString(R.string.save));
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(AddDiaryActivity.this);
            }
        });
    }


    private void setMianHeadRightTitleEnable() {
        /*if (mWorkFeeInfo.getWork_fee_half_min() > 0 && mWorkFeeInfo.getWork_fee_all_min() > 0) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }*/
    }

    @OnClick({R.id.rl_add_diary_housetype,R.id.rl_add_diary_style})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.rl_add_diary_housetype:
                break;
            case R.id.rl_add_diary_style:
                break;
        }
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        mDailyInfo = (DailyInfo)intent.getSerializableExtra(IntentConstant.DIARY_INFO);
        if(mDailyInfo == null){
            mDailyInfo = new DailyInfo();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_diary;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
