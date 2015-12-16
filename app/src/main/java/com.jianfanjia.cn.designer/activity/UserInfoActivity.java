package com.jianfanjia.cn.designer.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.bean.DesignerInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author fengliang
 * @ClassName: UserInfoActivity
 * @Description:用户个人信息(业主)
 * @date 2015-8-18 下午12:11:49
 */
@EActivity(R.layout.activity_owner_info)
public class UserInfoActivity extends BaseAnnotationActivity {
    private static final String TAG = UserInfoActivity.class.getName();
    @ViewById(R.id.head_layout)
    protected RelativeLayout headLayout = null;
    @ViewById(R.id.ownerinfoLayout)
    protected RelativeLayout ownerInfoLayout = null;
    @ViewById(R.id.ownerinfo_scrollview)
    protected ScrollView scrollView = null;
    @ViewById(R.id.nameText)
    protected TextView nameText = null;
    @ViewById(R.id.sexText)
    protected TextView sexText = null;
    @ViewById(R.id.phoneText)
    protected TextView phoneText = null;
    @ViewById(R.id.addressText)
    protected TextView addressText = null;
    @ViewById(R.id.homeText)
    protected TextView homeText = null;
    @ViewById(R.id.head_icon)
    protected ImageView headImageView = null;
    @ViewById(R.id.btn_confirm)
    protected Button btn_confirm = null;
    @ViewById(R.id.ownerinfo_head_layout)
    protected MainHeadView mainHeadView;
    @ViewById(R.id.error_include)
    protected RelativeLayout error_Layout;
    private DesignerInfo designerInfo = null;
    private String sex = null;

    @AfterViews
    public void afterView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.userinfo));
        initData();
    }

    private void setData() {

        scrollView.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(designerInfo.getImageid())) {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, headImageView);
        } else {
            imageShow.displayImageHeadWidthThumnailImage(this, designerInfo.getImageid(), headImageView);
        }
        nameText.setText(TextUtils.isEmpty(designerInfo.getUsername()) ? getString(R.string.ower)
                : designerInfo.getUsername());
        String sexInfo = designerInfo.getSex();
        if (!TextUtils.isEmpty(sexInfo)) {
            if (sexInfo.equals(Constant.SEX_MAN)) {
                sexText.setText("男");
            } else if (sexInfo.equals(Constant.SEX_WOMEN)) {
                sexText.setText("女");
            }
        } else {
            sexText.setText(getString(R.string.not_edit));
        }
        phoneText
                .setText(TextUtils.isEmpty(designerInfo.getPhone()) ? getString(R.string.not_edit)
                        : designerInfo.getPhone());
        String city = designerInfo.getCity();
        if (TextUtils.isEmpty(city)) {
            addressText
                    .setText(getString(R.string.not_edit));
        } else {
            String province = designerInfo.getProvince();
            String district = designerInfo.getDistrict();
            addressText
                    .setText(province + city + (TextUtils.isEmpty(district) ? ""
                            : district));
        }
        homeText.setText(TextUtils.isEmpty(designerInfo.getAddress()) ? getString(R.string.not_edit)
                : designerInfo.getAddress());
    }

    @Click({R.id.error_include, R.id.head_back_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                initData();
                break;
            case R.id.head_back_layout:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        if (data.toString() != null) {
            designerInfo = JsonParser.jsonToBean(data.toString(), DesignerInfo.class);
            setData();
            error_Layout.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.GONE);
            error_Layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
        scrollView.setVisibility(View.GONE);
        error_Layout.setVisibility(View.VISIBLE);
    }


    private void initData() {
        JianFanJiaClient.get_Designer_Info(this, this, this);
    }


}
