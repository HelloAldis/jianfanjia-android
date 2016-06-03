package com.jianfanjia.cn.designer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.designer.AppManager;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.activity.common.EditCityActivity;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemActivity;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemLovestyleActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.UploadProductActivity;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 10:05
 */
public class UploadProduct1Fragment extends BaseFragment {

    private static final String TAG = UploadProduct1Fragment.class.getName();

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_worktype;
    private String[] arr_dectype;

    @Bind(R.id.upload_product_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.act_edit_req_city_content)
    protected TextView act_edit_req_city_content;//所在城市

    @Bind(R.id.act_edit_req_cell_content)
    protected EditText act_edit_req_cell_content;//小区

    @Bind(R.id.act_edit_req_dectype_content)
    protected TextView act_edit_req_dectype_content;//装修类型

    @Bind(R.id.act_edit_req_housetype_content)
    protected TextView act_edit_req_housetype_content;//户型

    @Bind(R.id.act_edit_req_lovestyle_content)
    protected TextView act_edit_req_lovestyle_content;//风格喜好

    @Bind(R.id.act_edit_req_work_type_content)
    protected TextView act_edit_req_work_type_content;//包工类型

    @Bind(R.id.act_edit_req_housearea_content)
    protected EditText act_edit_req_housearea_content;//装修面积

    @Bind(R.id.act_edit_req_decoratebudget_content)
    protected EditText act_edit_req_decoratebudget_content;//装修预算

    private Product mProduct;
    private UploadProductActivity mUploadProductActivity;

    public static UploadProduct1Fragment getInstance(Product product) {
        UploadProduct1Fragment uploadProduct1Fragment = new UploadProduct1Fragment();
        uploadProduct1Fragment.setProduct(product);
        return uploadProduct1Fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initStringArray() {
        arr_lovestyle = getResources().getStringArray(R.array.arr_decstyle);
        arr_housetype = getResources().getStringArray(R.array.arr_housetype);
        arr_worktype = getResources().getStringArray(R.array.arr_worktype);
        arr_dectype = getResources().getStringArray(R.array.arr_dectype);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUploadProductActivity = (UploadProductActivity) getActivity();
    }

    private void initView() {
        initMainView();
        initStringArray();
        initData();
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(Html.fromHtml("上传作品<font color=\"#fe7003\">（1/2）</font>"));
        mMainHeadView.setRightTitle(getString(R.string.next));
        mMainHeadView.setRigthTitleEnable(false);
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                if (mUploadProductActivity != null) {
                    AppManager.getAppManager().finishActivity(getActivity());
                }
                break;
            case R.id.head_right_title:
                if (mUploadProductActivity != null) {
                    mUploadProductActivity.nextFragment();
                }
                break;
        }
    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    @OnTextChanged(value = R.id.act_edit_req_cell_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void cellAfterChanged(CharSequence charSequence) {
        mProduct.setCell(charSequence.toString());
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_decoratebudget_content, callback = OnTextChanged.Callback
            .AFTER_TEXT_CHANGED)
    protected void decoratebudgetAfterChanged(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence.toString())) {
            mProduct.setTotal_price(Float.parseFloat(charSequence.toString()));
        } else {
            mProduct.setTotal_price(0.0f);
        }
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_housearea_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void houseareaAfterChanged(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence.toString())) {
            mProduct.setHouse_area(Integer.parseInt(charSequence.toString()));
        } else {
            mProduct.setHouse_area(0);
        }
        isAllInput();
    }

    @OnClick({R.id.act_edit_req_city, R.id.act_edit_req_housetype,
            R.id.act_edit_req_lovestyle,
            R.id.act_edit_req_work_type, R.id.act_edit_req_dectype})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.act_edit_req_city:
                Bundle address = new Bundle();
                address.putString(Constant.EDIT_PROVICE, "湖北省");
                address.putString(Constant.EDIT_CITY, "武汉市");
                if (!TextUtils.isEmpty(mProduct.getDistrict())) {
                    address.putString(Constant.EDIT_DISTRICT, mProduct.getDistrict());
                }
                address.putInt(EditCityActivity.PAGE, EditCityActivity.EDIT_REQUIREMENT_ADRESS);
                startActivityForResult(EditCityActivity.class, address, Constant.REQUIRECODE_CITY);
                break;
            case R.id.act_edit_req_lovestyle:
                Bundle loveStyleBundle = new Bundle();
                loveStyleBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVESTYLE);
                if (!TextUtils.isEmpty(mProduct.getDec_style())) {
                    loveStyleBundle.putString(ChooseItemLovestyleActivity.CURRENT_CHOOSED_VALUE, mProduct.getDec_style());
                }
                loveStyleBundle.putString(ChooseItemLovestyleActivity.TITLE,getString(R.string.str_lovestyle));
                startActivityForResult(ChooseItemLovestyleActivity.class, loveStyleBundle, Constant
                        .REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_dectype:
                Bundle personBundle = new Bundle();
                personBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_DECTYPE);
                if (!TextUtils.isEmpty(mProduct.getDec_type())) {
                    personBundle.putString(ChooseItemActivity.CURRENT_CHOOSED_VALUE, mProduct.getDec_type());
                }
                personBundle.putString(ChooseItemActivity.TITLE,getString(R.string.str_decoratetype));
                startActivityForResult(ChooseItemActivity.class, personBundle, Constant.REQUIRECODE_DECTYPE);
                break;
            case R.id.act_edit_req_housetype:
                Bundle houseTypeBundle = new Bundle();
                houseTypeBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_HOUSETYPE);
                if (!TextUtils.isEmpty(mProduct.getHouse_type())) {
                    houseTypeBundle.putString(ChooseItemActivity.CURRENT_CHOOSED_VALUE, mProduct.getHouse_type());
                }
                houseTypeBundle.putString(ChooseItemActivity.TITLE,getString(R.string.str_dec_housetype));
                startActivityForResult(ChooseItemActivity.class, houseTypeBundle, Constant
                        .REQUIRECODE_HOUSETYPE);
                break;
            case R.id.act_edit_req_work_type:
                Bundle workTypeBundle = new Bundle();
                workTypeBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_WORKTYPE);
                if (!TextUtils.isEmpty(mProduct.getWork_type())) {
                    workTypeBundle.putString(ChooseItemActivity.CURRENT_CHOOSED_VALUE, mProduct.getWork_type());
                }
                workTypeBundle.putString(ChooseItemActivity.TITLE,getString(R.string.str_work_type));
                startActivityForResult(ChooseItemActivity.class, workTypeBundle, Constant
                        .REQUIRECODE_WORKTYPE);
                break;
            default:
                break;
        }
    }

    /**
     * 是否所有的字段都已经输入
     *
     * @return
     */
    public void isAllInput() {
        if (!TextUtils.isEmpty(mProduct.getDistrict()) && !TextUtils.isEmpty(mProduct.getCell())
                && !TextUtils.isEmpty(mProduct.getHouse_type()) && mProduct.getHouse_area() > 0 &&
                !TextUtils.isEmpty(mProduct.getDec_style()) && !TextUtils.isEmpty(mProduct.getDec_type())
                && !TextUtils.isEmpty(mProduct.getWork_type()) && mProduct.getTotal_price() > 0
                ) {

            LogTool.d(TAG, "isFisish =" + true);
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            LogTool.d(TAG, "isFisish =" + false);
            mMainHeadView.setRigthTitleEnable(false);
        }
    }

    private void initData() {
        LogTool.d(this.getClass().getName(), "initData");
        if (mProduct != null) {
            if (!TextUtils.isEmpty(mProduct.getProvince()) && !TextUtils.isEmpty(mProduct.getCity()
            ) && !TextUtils.isEmpty(mProduct.getDistrict())) {
                act_edit_req_city_content.setText(mProduct.getProvince() + " " + mProduct.getCity() + " " +
                        mProduct.getDistrict());
            }
            if (mProduct.getHouse_area() != 0) {
                act_edit_req_housearea_content.setText(mProduct.getHouse_area() + "");
            }
            if (mProduct.getTotal_price() > 0) {
                act_edit_req_decoratebudget_content.setText(mProduct.getTotal_price() + "");
            }
            if (!TextUtils.isEmpty(mProduct.getCell())) {
                act_edit_req_cell_content.setText(mProduct.getCell());
            }
            act_edit_req_housetype_content.setText(TextUtils.isEmpty(mProduct.getHouse_type()) ? "" :
                    arr_housetype[Integer.parseInt(mProduct.getHouse_type())]);
            act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(mProduct.getDec_style()) ? "" :
                    arr_lovestyle[Integer.parseInt(mProduct.getDec_style())]);
            act_edit_req_work_type_content.setText(TextUtils.isEmpty(mProduct.getWork_type()) ? "" :
                    arr_worktype[Integer.parseInt(mProduct.getWork_type())]);
            act_edit_req_dectype_content.setText(TextUtils.isEmpty(mProduct.getDec_type()) ? "" :
                    arr_dectype[Integer.parseInt(mProduct.getDec_type())]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
            ReqItemFinderImp.ItemMap itemMap = data.getParcelableExtra(Global
                    .RESPONSE_DATA);
            switch (requestCode) {
                case Constant.REQUIRECODE_CITY:
                    String provice = data.getStringExtra(Constant.EDIT_PROVICE);
                    String city = data.getStringExtra(Constant.EDIT_CITY);
                    String district = data.getStringExtra(Constant.EDIT_DISTRICT);
                    if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                        act_edit_req_city_content.setText(provice + city + district);
                        mProduct.setProvince(provice);
                        mProduct.setCity(city);
                        mProduct.setDistrict(district);
                    }
                    break;
                case Constant.REQUIRECODE_DECTYPE:
                    act_edit_req_dectype_content.setText(itemMap.value);
                    mProduct.setDec_type(itemMap.key);
                    break;
                case Constant.REQUIRECODE_LOVESTYLE:
                    act_edit_req_lovestyle_content.setText(itemMap.value);
                    mProduct.setDec_style(itemMap.key);
                    break;
                case Constant.REQUIRECODE_HOUSETYPE:
                    act_edit_req_housetype_content.setText(itemMap.value);
                    mProduct.setHouse_type(itemMap.key);
                    break;
                case Constant.REQUIRECODE_WORKTYPE:
                    act_edit_req_work_type_content.setText(arr_worktype[Integer.parseInt(itemMap.key)]);
                    mProduct.setWork_type(itemMap.key);
                    break;
            }
            isAllInput();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_upload_product1;
    }
}
