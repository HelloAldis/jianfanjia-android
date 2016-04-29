package com.jianfanjia.cn.supervisor.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.request.supervisor.UpdateSuperVisorRequest;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.api.Api;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.tools.CityFormatTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-09 13:06
 */
public class EditCityActivity extends BaseSwipeBackActivity {
    private static final String TAG = EditCityActivity.class.getName();
    public static final String PAGE = "page";
    public static final int EDIT_USER_ADRESS = 0;
    public static final int EDIT_REQUIREMENT_ADRESS = 1;

    @Bind(R.id.spinner_pro)
    Spinner spinner_pro;

    @Bind(R.id.spinner_city)
    Spinner spinner_city;

    @Bind(R.id.spinner_district)
    Spinner spinner_district;

    @Bind(R.id.btn_confirm)
    Button btn_confirm;

    @Bind(R.id.cityedit_head_layout)
    MainHeadView mainHeadView;

    ArrayAdapter spinnerProAdapter;
    ArrayAdapter spinnerCityAdapter;
    ArrayAdapter spinnerDistrictAdapter;

    private String provice;
    private String city;
    private String district;
    private int page;

    private int currentPro = 0;
    private int currentCity = 0;
    private int currentDistrict = 0;

    private boolean isInit = false;

    private List<String> provinces;
    private List<String> citys;
    private List<String> districts;
    private Map<String, List<String>> cityMap;
    private Map<String, List<String>> districtMap;

    private Intent intent;
    private SuperVisor user = new SuperVisor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initData();
        initView();
    }

    private void getDataFromIntent() {
        intent = getIntent();
        provice = intent.getStringExtra(Constant.EDIT_PROVICE);
        city = intent.getStringExtra(Constant.EDIT_CITY);
        district = intent.getStringExtra(Constant.EDIT_DISTRICT);
        page = intent.getIntExtra(PAGE, 0);
    }

    private void initView() {
        mainHeadView.setMianTitle(getString(R.string.user_address));
        initSpinner();
    }

    private void initSpinner() {
        spinnerProAdapter = new ArrayAdapter(this, R.layout.spinner_city_item, provinces);
//        spinner_pro.setDropDownVerticalOffset(2);
        spinnerProAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pro.setAdapter(spinnerProAdapter);
        spinner_pro.setSelection(currentPro);
        spinner_pro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogTool.d(TAG, " position " + position);
                currentPro = position;
                provice = provinces.get(position);
                citys = cityMap.get(provice);
                LogTool.d(TAG, city + citys.size());

                city = citys.get(currentCity);
                spinnerCityAdapter = new ArrayAdapter(EditCityActivity.this, R.layout.spinner_city_item, citys);
                spinnerCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_city.setAdapter(spinnerCityAdapter);
                if (!isInit) {
                    currentCity = 0;
                } else {
                    spinner_city.setSelection(currentCity);
                }
                spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentCity = position;
                        city = citys.get(position);
                        districts = districtMap.get(city);
                        district = districts.get(currentDistrict);
                        spinnerDistrictAdapter = new ArrayAdapter(EditCityActivity.this, R.layout.spinner_city_item,
                                districts);
                        spinnerDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_district.setAdapter(spinnerDistrictAdapter);
                        if (!isInit) {
                            currentDistrict = 0;
                        } else {
                            spinner_district.setSelection(currentDistrict);
                        }
                        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                currentDistrict = position;
                                district = districts.get(position);
                                if (isInit == true) {
                                    isInit = false;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        provinces = CityFormatTool.getProviceList();
        cityMap = CityFormatTool.getCityMap();
        districtMap = CityFormatTool.getDistrictMap();

        switch (page) {
            case EDIT_REQUIREMENT_ADRESS:
                spinner_pro.setEnabled(false);
                spinner_city.setEnabled(false);
                break;
            case EDIT_USER_ADRESS:
                spinner_pro.setEnabled(true);
                spinner_city.setEnabled(true);
                break;
        }
        if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city)) {
            currentPro = provinces.indexOf(provice);
            citys = cityMap.get(provice);
            currentCity = citys.indexOf(city);
            districts = districtMap.get(city);
            if (!TextUtils.isEmpty(district)) {
                currentDistrict = districts.indexOf(district);
            } else {
                currentDistrict = 0;
            }
            district = districts.get(currentDistrict);
            isInit = true;
        } else {
            provice = provinces.get(currentPro);
            citys = cityMap.get(provice);
            city = citys.get(currentCity);
            districts = districtMap.get(city);
            district = districts.get(currentDistrict);
        }


        LogTool.d(TAG, provice + "= " + city + "= " + district);

    }

    // 修改设计师个人资料
    private void put_Owner_Info() {
        UpdateSuperVisorRequest request = new UpdateSuperVisorRequest();
        request.setSupervisor(user);

        Api.updateSuperVisorInfo(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(EditCityActivity.this);
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                setResultTo();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    protected void setResultTo() {
        intent.putExtra(Constant.EDIT_PROVICE, provice);
        intent.putExtra(Constant.EDIT_CITY, city);
        intent.putExtra(Constant.EDIT_DISTRICT, district);
        LogTool.d(TAG, provice + city + district);
        setResult(RESULT_OK, intent);
        appManager.finishActivity(EditCityActivity.this);
    }

    @OnClick({R.id.head_back_layout, R.id.btn_confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_confirm:
                switch (page) {
                    case EDIT_USER_ADRESS:
                        user.setProvince(provice);
                        user.setCity(city);
                        user.setDistrict(district);
                        put_Owner_Info();
                        break;
                    case EDIT_REQUIREMENT_ADRESS:
                        setResultTo();
                        break;
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cityedit;
    }
}
