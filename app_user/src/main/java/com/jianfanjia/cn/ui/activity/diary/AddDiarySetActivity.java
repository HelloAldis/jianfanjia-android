package com.jianfanjia.cn.ui.activity.diary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.common.AddDiarySetRequest;
import com.jianfanjia.api.request.common.UpdateDiarySetRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.ui.activity.common.choose_item.ChooseItemIntent;
import com.jianfanjia.cn.ui.activity.common.choose_item.ChooseItemLoveStyleIntent;
import com.jianfanjia.cn.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 11:39
 */
public class AddDiarySetActivity extends BaseSwipeBackActivity {

    public static final int CURRENT_STATUS_ADD = 0;
    public static final int CURRENT_STATUS_UPDATE = 1;

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.et_add_dairy_title_content)
    EditText etAddDiaryTitleContent;

    @Bind(R.id.et_add_dairy_area_content)
    EditText etAddDiaryAreaContent;

    @Bind(R.id.tv_add_diary_housetype_content)
    TextView tvAddDiaryHousetypeContent;

    @Bind(R.id.tv_add_diary_style_content)
    TextView tvAddDiaryStyleContent;

    @Bind(R.id.tv_add_diary_work_type_content)
    TextView tvAddDiaryWorkTypeContent;

    private DiarySetInfo mDiarySetInfo;
    private int currentStatus = CURRENT_STATUS_UPDATE;

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

    private void initData() {
        setMianHeadRightTitleEnable();

        setTitle();

        setHouseArea();

        setHouseType();

        setDecStyle();

        setWorkType();

    }

    private void setWorkType() {
        if (!TextUtils.isEmpty(mDiarySetInfo.getWork_type())) {
            tvAddDiaryWorkTypeContent.setText(BusinessCovertUtil.convertWorkTypeToShow(mDiarySetInfo.getWork_type()));
        } else {
            tvAddDiaryWorkTypeContent.setText(null);
        }
        setMianHeadRightTitleEnable();
    }

    private void setDecStyle() {
        if (!TextUtils.isEmpty(mDiarySetInfo.getDec_style())) {
            tvAddDiaryStyleContent.setText(BusinessCovertUtil.convertDecStyleToShow(mDiarySetInfo.getDec_style()));
        } else {
            tvAddDiaryStyleContent.setText(null);
        }
        setMianHeadRightTitleEnable();
    }

    private void setHouseType() {
        if (!TextUtils.isEmpty(mDiarySetInfo.getHouse_type())) {
            tvAddDiaryHousetypeContent.setText(BusinessCovertUtil.convertHouseTypeToShow(mDiarySetInfo.getHouse_type
                    ()));
        } else {
            tvAddDiaryHousetypeContent.setText(null);
        }
        setMianHeadRightTitleEnable();
    }

    private void setHouseArea() {
        if (mDiarySetInfo.getHouse_area() != 0) {
            etAddDiaryAreaContent.setText(mDiarySetInfo.getHouse_area() + "");
        }
        etAddDiaryAreaContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mDiarySetInfo.setHouse_area(Integer.parseInt(s.toString()));
                } else {
                    mDiarySetInfo.setHouse_area(0);
                }
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setTitle() {
        etAddDiaryTitleContent.setText(mDiarySetInfo.getTitle());
        etAddDiaryTitleContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiarySetInfo.setTitle(s.toString());
                setMianHeadRightTitleEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initMianView() {
        mMainHeadView.setMianTitle(getString(R.string.diaryset_info));
        mMainHeadView.setRightTitle(getString(R.string.save));
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStatus == CURRENT_STATUS_ADD) {
                    addDiarySetInfo(mDiarySetInfo);
                } else {
                    updateDiarySetInfo(mDiarySetInfo);
                }
            }
        });
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStatus == CURRENT_STATUS_ADD) {
                    if (!TextUtils.isEmpty(mDiarySetInfo.getTitle()) || !TextUtils.isEmpty(mDiarySetInfo.getWork_type
                            ()) || !TextUtils.isEmpty(mDiarySetInfo.getHouse_type()) || !TextUtils.isEmpty(mDiarySetInfo
                            .getDec_style())
                            || mDiarySetInfo.getHouse_area() > 0) {
                        showTipDialog();
                    }else {
                        appManager.finishActivity(AddDiarySetActivity.this);
                    }
                } else {
                    appManager.finishActivity(AddDiarySetActivity.this);
                }
            }
        });
    }


    private void setMianHeadRightTitleEnable() {
        if (!TextUtils.isEmpty(mDiarySetInfo.getTitle()) && !TextUtils.isEmpty(mDiarySetInfo.getWork_type()) &&
                !TextUtils.isEmpty(mDiarySetInfo.getHouse_type()) && !TextUtils.isEmpty(mDiarySetInfo.getDec_style())
                && mDiarySetInfo.getHouse_area() > 0) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }
    }

    protected void showTipDialog() {
        CommonDialog commonDialog = DialogHelper.getPinterestDialogCancelable(this);
        commonDialog.setTitle(R.string.tip_confirm);
        commonDialog.setMessage(getString(R.string.abandon_diaryset_write));
        commonDialog.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commonDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                appManager.finishActivity(AddDiarySetActivity.this);
            }
        });
        commonDialog.show();
    }

    @OnClick({R.id.rl_add_diary_housetype, R.id.rl_add_diary_style, R.id.rl_add_diary_work_type})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_diary_housetype:
                ChooseItemIntent houseTypeIntent = new ChooseItemIntent(this);
                houseTypeIntent.setSingleChoose(Constant.REQUIRECODE_HOUSETYPE, mDiarySetInfo.getHouse_type(),
                        getString(R.string.str_housetype));
                startActivityForResult(houseTypeIntent, Constant.REQUIRECODE_HOUSETYPE);
                break;
            case R.id.rl_add_diary_style:
                ChooseItemLoveStyleIntent loveStyleIntent = new ChooseItemLoveStyleIntent(this);
                loveStyleIntent.setSingleChoose(Constant.REQUIRECODE_LOVESTYLE, mDiarySetInfo.getDec_style(),
                        getString(R.string.str_decorate_style));
                startActivityForResult(loveStyleIntent, Constant.REQUIRECODE_LOVESTYLE);
                break;
            case R.id.rl_add_diary_work_type:
                ChooseItemIntent workTypeIntent = new ChooseItemIntent(this);
                workTypeIntent.setSingleChoose(Constant.REQUIRECODE_WORKTYPE, mDiarySetInfo.getWork_type(),
                        getString(R.string.str_work_type));
                startActivityForResult(workTypeIntent, Constant.REQUIRECODE_WORKTYPE);
                break;
        }
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        mDiarySetInfo = (DiarySetInfo) intent.getSerializableExtra(IntentConstant.DIARYSET_INFO);
        if (mDiarySetInfo == null) {
            mDiarySetInfo = new DiarySetInfo();
            currentStatus = CURRENT_STATUS_ADD;
        }
    }

    private void addDiarySetInfo(DiarySetInfo diarySetInfo) {
        AddDiarySetRequest addDiarySetRequest = new AddDiarySetRequest();
        addDiarySetRequest.setDiary_set(diarySetInfo);

        Api.addDiarySetInfo(addDiarySetRequest, new ApiCallback<ApiResponse<DiarySetInfo>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<DiarySetInfo> apiResponse) {
                gotoDiarySetInfo(apiResponse.getData());
            }

            @Override
            public void onFailed(ApiResponse<DiarySetInfo> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void gotoDiarySetInfo(DiarySetInfo data) {
        DiarySetInfoActivity.intentToDiarySet(this, data);
        appManager.finishActivity(this);
    }

    private void updateDiarySetInfo(DiarySetInfo diarySetInfo) {
        UpdateDiarySetRequest updateDiarySetRequest = new UpdateDiarySetRequest();
        updateDiarySetRequest.setDiary_set(diarySetInfo);

        Api.updateDiarySetInfo(updateDiarySetRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                appManager.finishActivity(AddDiarySetActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diaryset_add;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
            ReqItemFinderImp.ItemMap itemMap = data.getParcelableExtra(IntentConstant
                    .RESPONSE_DATA);
            switch (requestCode) {
                case Constant.REQUIRECODE_LOVESTYLE:
                    mDiarySetInfo.setDec_style(itemMap.key);
                    setDecStyle();
                    break;
                case Constant.REQUIRECODE_HOUSETYPE:
                    mDiarySetInfo.setHouse_type(itemMap.key);
                    setHouseType();
                    break;
                case Constant.REQUIRECODE_WORKTYPE:
                    mDiarySetInfo.setWork_type(itemMap.key);
                    setWorkType();
                    break;
            }
        }
    }
}
