package com.jianfanjia.cn.ui.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.adapter.ChooseDiarySetTitleAdapter;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Created by asus on 2016/6/19.
 */
public class ChooseNowDiarySetActivity extends BaseSwipeBackActivity {

    public static final String CURRENT_CHOOSE_VALUE = "current_choosed_value";

    public static final String ALL_CAN_CHOOSE_VALUES = "all_can_choose_values";

    private String currentChoosedValue;
    private List<String> mDiarySetTitleList;

    private ChooseDiarySetTitleAdapter chooseDiarySetTitleAdapter;

    @Bind(R.id.mainhv_diary)
    protected MainHeadView mMainHeadView;

    @Bind(R.id.act_edit_req_item_listview)
    protected ListView edit_req_item_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void initView() {
        mMainHeadView.setMianTitle(getString(R.string.str_now_diaryset));

        chooseDiarySetTitleAdapter = new ChooseDiarySetTitleAdapter(this, mDiarySetTitleList, currentChoosedValue);
        edit_req_item_listview.setAdapter(chooseDiarySetTitleAdapter);
        edit_req_item_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = getIntent();
                data.putExtra(IntentConstant.RESPONSE_DATA, mDiarySetTitleList.get(position));
                setResult(RESULT_OK, data);
                appManager.finishActivity(ChooseNowDiarySetActivity.class);
            }
        });
    }

    private void getDataFromIntent() {
        currentChoosedValue = getIntent().getStringExtra(CURRENT_CHOOSE_VALUE);
        mDiarySetTitleList = getIntent().getStringArrayListExtra(ALL_CAN_CHOOSE_VALUES);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_now_diaryset;
    }
}
