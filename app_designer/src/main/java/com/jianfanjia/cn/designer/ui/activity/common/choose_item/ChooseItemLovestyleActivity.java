package com.jianfanjia.cn.designer.ui.activity.common.choose_item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.adapter.RequirementItemLoveStyleAdapter;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.MainHeadView;


/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class ChooseItemLovestyleActivity extends BaseSwipeBackActivity {

    public static final String CURRENT_CHOOSED_VALUE = "current_choosed_value";
    public static final String CURRENT_CHOOSED_TYPE = "current_choosed_type";
    public static final int MAX_CHOOSE_THREE = 3;//最多选择三项

    public static final String TITLE = "title";

    public static final int CHOOSE_TYPE_SINGLE = 0;//单选，
    public static final int CHOOSE_TYPE_MULTIPLE = 1;//多选


    //用来记录是展示那个列表
    private int requestCode;

    private String currentChooseValue;
    private String title;
    private List<String> currentChooseValues;
    private List<ReqItemFinderImp.ItemMap> choosedItemMap;
    private int chooseType;

    @Bind(R.id.act_edit_req_lovestyle_gridview)
    GridView gridView;

    @Bind(R.id.act_edit_req_lovastyle_head)
    MainHeadView mMainHeadView;

    RequirementItemLoveStyleAdapter requirementItemLoveStyleAdapter;

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
            title = bundle.getString(TITLE, null);
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

        setMianHeadRightTitleEnable();
        requirementItemLoveStyleAdapter = new RequirementItemLoveStyleAdapter(this, chooseType);
        gridView.setAdapter(requirementItemLoveStyleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chooseType == CHOOSE_TYPE_SINGLE) {
                    Intent data = getIntent();
                    data.putExtra(Global.RESPONSE_DATA, requirementItemLoveStyleAdapter.getItemMaps().get(position));
                    setResult(RESULT_OK, data);
                    appManager.finishActivity(ChooseItemLovestyleActivity.this);
                } else {
                    String key = requirementItemLoveStyleAdapter.getItemMaps().get(position).key;
                    if (currentChooseValues.contains(key)) {
                        currentChooseValues.remove(key);
                    } else {
                        if (currentChooseValues.size() >= MAX_CHOOSE_THREE) {
                            makeTextShort(getString(R.string.all_more_three));
                        } else {
                            currentChooseValues.add(key);
                        }
                    }
                    setMianHeadRightTitleEnable();
                    requirementItemLoveStyleAdapter.notifyDataSetChanged();
                }
            }
        });
        requirementItemLoveStyleAdapter.changeShow(requestCode, currentChooseValues);
    }

    private void setMianHeadRightTitleEnable() {
        if (chooseType == CHOOSE_TYPE_MULTIPLE) {
            /*if (currentChooseValues.size() > 0) {
                mMainHeadView.setRigthTitleEnable(true);
            } else {
                mMainHeadView.setRigthTitleEnable(false);
            }*/
        }
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(title);
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
                for (ReqItemFinderImp.ItemMap itemMap : requirementItemLoveStyleAdapter.getItemMaps()) {
                    if (currentChooseValues.contains(itemMap.key)) {
                        choosedItemMap.add(itemMap);
                    }
                }

                Intent data = getIntent();
                data.putParcelableArrayListExtra(Global.RESPONSE_DATA, (ArrayList) choosedItemMap);
                setResult(RESULT_OK, data);
                appManager.finishActivity(ChooseItemLovestyleActivity.this);
            }
        });
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
        return R.layout.activity_edit_req_lovestyle;
    }
}
