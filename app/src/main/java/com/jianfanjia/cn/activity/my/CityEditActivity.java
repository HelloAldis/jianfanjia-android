package com.jianfanjia.cn.activity.my;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.CityFormatTool;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-09 13:06
 */
@EActivity(R.layout.activity_cityedit)
public class CityEditActivity extends BaseAnnotationActivity {

    private static final String TAG = "CityEditActivity";
    @ViewById(R.id.spinner_pro)
    Spinner spinner_pro;

    @ViewById(R.id.spinner_city)
    Spinner spinner_city;

    @ViewById(R.id.spinner_district)
    Spinner spinner_district;

    @ViewById(R.id.btn_confirm)
    Button btn_confirm;

    @ViewById(R.id.cityedit_head_layout)
    MainHeadView mainHeadView;

    ArrayAdapter spinnerProAdapter;
    ArrayAdapter spinnerCityAdapter;
    ArrayAdapter spinnerDistrictAdapter;

    private String provice;
    private String city;
    private String district;

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

    @AfterViews
    public void init() {
        mainHeadView.setMianTitle(getString(R.string.user_address));

        initData();
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
                spinnerCityAdapter = new ArrayAdapter(CityEditActivity.this, R.layout.spinner_city_item, citys);
                spinnerCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_city.setAdapter(spinnerCityAdapter);
                if(!isInit){
                    currentCity = 0;
                }else{
                    spinner_city.setSelection(currentCity);
                }
                spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentCity = position;
                        city = citys.get(position);
                        districts = districtMap.get(city);
                        district = districts.get(currentDistrict);
                        spinnerDistrictAdapter = new ArrayAdapter(CityEditActivity.this, R.layout.spinner_city_item, districts);
                        spinnerDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_district.setAdapter(spinnerDistrictAdapter);
                        if(!isInit){
                            currentDistrict = 0;
                        }else{
                            spinner_district.setSelection(currentDistrict);
                        }
                        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                currentDistrict = position;
                                district = districts.get(position);
                                if(isInit == true){
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

        intent = getIntent();
        provice = intent.getStringExtra(Constant.EDIT_PROVICE);
        city = intent.getStringExtra(Constant.EDIT_CITY);
        district = intent.getStringExtra(Constant.EDIT_DISTRICT);
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

    @Click({R.id.head_back_layout, R.id.btn_confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_confirm:
                intent.putExtra(Constant.EDIT_PROVICE, provice);
                intent.putExtra(Constant.EDIT_CITY, city);
                intent.putExtra(Constant.EDIT_DISTRICT, district);
                LogTool.d(TAG, provice + city + district);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

}
