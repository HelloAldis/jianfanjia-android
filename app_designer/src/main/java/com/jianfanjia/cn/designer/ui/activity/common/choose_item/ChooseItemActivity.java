package com.jianfanjia.cn.designer.ui.activity.common.choose_item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.adapter.RequirementItemAdapter;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class ChooseItemActivity extends BaseSwipeBackActivity {

    public static final String CURRENT_CHOOSED_VALUE = "current_choosed_value";
    public static final String CURRENT_CHOOSED_TYPE = "current_choosed_type";

    public static final int CHOOSE_TYPE_SINGLE = 0;//单选，
    public static final int CHOOSE_TYPE_MULTIPLE = 1;//多选

    //用来记录是展示那个列表
    private int requestCode;

    private String currentChooseValue;
    private List<String> currentChooseValues;
    private List<ReqItemFinderImp.ItemMap> choosedItemMap;
    private int chooseType;

    @Bind(R.id.act_edit_req_item_head)
    protected MainHeadView mMainHeadView;

    @Bind(R.id.act_edit_req_item_listview)
    protected ListView edit_req_item_listview;

    @Bind(R.id.choose_all_toggle)
    ToggleButton toggleButton;

    @Bind(R.id.choose_all_toggle_layout)
    LinearLayout chooseAllToggleLayout;

    protected RequirementItemAdapter requirementItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            requestCode = bundle.getInt(Global.REQUIRE_DATA, 0);
            chooseType = bundle.getInt(CURRENT_CHOOSED_TYPE, CHOOSE_TYPE_SINGLE);
            if (chooseType == CHOOSE_TYPE_SINGLE) {
                currentChooseValues = new ArrayList<>();
                currentChooseValue = bundle.getString(CURRENT_CHOOSED_TYPE);
                if (currentChooseValue != null) {
                    currentChooseValues.add(bundle.getString(CURRENT_CHOOSED_VALUE));
                }
            } else {
                currentChooseValues = bundle.getStringArrayList(CURRENT_CHOOSED_VALUE);
                if (currentChooseValues == null) {
                    currentChooseValues = new ArrayList<>();
                }
            }
        }
    }

    public void initView() {
        initMainView();
        initListView();
        initToggle();
    }

    private void initToggle() {
        if (chooseType == CHOOSE_TYPE_SINGLE) {
            chooseAllToggleLayout.setVisibility(View.GONE);
        } else {
            chooseAllToggleLayout.setVisibility(View.VISIBLE);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (ReqItemFinderImp.ItemMap itemMap : requirementItemAdapter.getItemMaps()) {
                        if (!currentChooseValues.contains(itemMap.key)) {
                            currentChooseValues.add(itemMap.key);
                        }
                    }
                } else {
                    currentChooseValues.clear();
                }
                setMianHeadRightTitleEnable();
                requirementItemAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initListView() {
        setMianHeadRightTitleEnable();

        requirementItemAdapter = new RequirementItemAdapter(this);
        edit_req_item_listview.setAdapter(requirementItemAdapter);
        edit_req_item_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chooseType == CHOOSE_TYPE_SINGLE) {
                    Intent data = getIntent();
                    data.putExtra(Global.RESPONSE_DATA, requirementItemAdapter.getItemMaps().get(position));
                    setResult(RESULT_OK, data);
                    appManager.finishActivity(ChooseItemActivity.this);
                } else {
                    String key = requirementItemAdapter.getItemMaps().get(position).key;
                    if (currentChooseValues.contains(key)) {
                        currentChooseValues.remove(key);
                    } else {
                        currentChooseValues.add(key);
                    }
                    setMianHeadRightTitleEnable();
                    requirementItemAdapter.notifyDataSetChanged();
                }
            }
        });
        requirementItemAdapter.changeShow(requestCode, currentChooseValues);
    }

    private void setMianHeadRightTitleEnable() {
        if (chooseType == CHOOSE_TYPE_MULTIPLE) {
            if (currentChooseValues.size() > 0) {
                mMainHeadView.setRigthTitleEnable(true);
            } else {
                mMainHeadView.setRigthTitleEnable(false);
            }
        }
    }

    private void initMainView() {
        showHead(requestCode);
        if (chooseType == CHOOSE_TYPE_SINGLE) {
            mMainHeadView.setRightTitleVisable(View.GONE);
        } else {
            mMainHeadView.setRightTitle(getString(R.string.str_save));
            mMainHeadView.setRightTitleVisable(View.VISIBLE);
        }
        mMainHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedItemMap = new ArrayList<>();
                for (ReqItemFinderImp.ItemMap itemMap : requirementItemAdapter.getItemMaps()) {
                    if (currentChooseValues.contains(itemMap.key)) {
                        choosedItemMap.add(itemMap);
                    }
                }

                Intent data = getIntent();
                data.putParcelableArrayListExtra(Global.RESPONSE_DATA, (ArrayList) choosedItemMap);
                setResult(RESULT_OK, data);
                appManager.finishActivity(ChooseItemActivity.this);
            }
        });
    }

    /**
     * 根据requestcode动态展示头部显示的文字
     *
     * @param requestcode
     */
    private void showHead(int requestcode) {
        switch (requestcode) {
            case Constant.REQUIRECODE_CITY:
                mMainHeadView.setMianTitle(getString(R.string.str_district));
                break;
            case Constant.REQUIRECODE_HOUSETYPE:
                mMainHeadView.setMianTitle(getString(R.string.str_housetype));
                break;
            case Constant.REQUIRECODE_PERSONS:
                mMainHeadView.setMianTitle(getString(R.string.str_persons));
                break;
            case Constant.REQUIRECODE_LOVEDESISTYLE:
                mMainHeadView.setMianTitle(getString(R.string.receive_business_communication));
                break;
            case Constant.REQUIRECODE_BUSI_DECORATETYPE:
                mMainHeadView.setMianTitle(getString(R.string.str_businessdecoratetype));
                break;
            case Constant.REQUIRECODE_WORKTYPE:
                mMainHeadView.setMianTitle(getResources().getString(R.string.str_work_type));
                break;
            case Constant.REQUIRECODE_DESISEX:
                mMainHeadView.setMianTitle(getString(R.string.str_lovedesisex));
                break;
            case Constant.REQUIRECODE_DECTYPE:
                mMainHeadView.setMianTitle(getString(R.string.str_decoratetype));
                break;
            case Constant.REQUIRECODE_BANK:
                mMainHeadView.setMianTitle(getString(R.string.bank));
                break;
            case Constant.REQUIRECODE_GOODAT_WORKOFTYPE:
                mMainHeadView.setMianTitle(getString(R.string.goodat_type));
                break;
            case Constant.REQUIRECODE_DESIGN_FEE:
                mMainHeadView.setMianTitle(getString(R.string.str_receive_business_design_fee));
                break;
            case Constant.REQUIRECODE_DISTRICT:
                mMainHeadView.setMianTitle(getString(R.string.str_receive_district));
                break;
        }

    }

    @OnClick({R.id.head_back_layout})
    protected void back(View clickView) {
        int resId = clickView.getId();
        switch (resId) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_req_item;
    }
}