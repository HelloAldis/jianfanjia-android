package com.jianfanjia.cn.ui.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.user.UpdateOwnerInfoRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.activity.MainActivity;
import com.jianfanjia.cn.ui.adapter.CollectPersonViewPageAdapter;
import com.jianfanjia.cn.ui.interf.OnItemClickListener;

/**
 * Description: com.jianfanjia.cn
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 11:08
 */
public class NewUserCollectPersonActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager loveStyleViewPager;

    @Bind(R.id.act_reg_collect_content)
    TextView contentView;

    @Bind(R.id.act_reg_collect_title)
    TextView titleView;

    @Bind(R.id.btn_next)
    Button buttonNext;

    List<String> personList = new ArrayList<>();

    User ownerInfo;

    CollectPersonViewPageAdapter collectPersonViewPageAdapter;

    List<CollectPersonViewPageAdapter.LoveStyleItemInfo> loveStyleItemInfoList = new ArrayList<>();

    int lastSelectorPos = -1;
    int currentSelcetorPos = -1;

    protected String[] persons;

    protected int[] personImageIds = new int[]{
            R.mipmap.img_danshen, R.mipmap.img_twopeople, R.mipmap.img_threepeople,
            R.mipmap.img_threegenerations
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    private void initView() {
        persons = getResources().getStringArray(R.array.arr_person);

        Intent intent = getIntent();
        ownerInfo = (User) intent.getSerializableExtra(IntentConstant.OWNERINFO);

        titleView.setText(getString(R.string.collect_person_title));
        contentView.setText(getString(R.string.collect_person_content));
        buttonNext.setText(getString(R.string.finish));
        buttonNext.setEnabled(false);

        for (int imageid : personImageIds) {
            CollectPersonViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = new CollectPersonViewPageAdapter
                    .LoveStyleItemInfo();
            loveStyleItemInfo.setResId(imageid);
            loveStyleItemInfoList.add(loveStyleItemInfo);
        }

        collectPersonViewPageAdapter = new CollectPersonViewPageAdapter(this, loveStyleItemInfoList, new
                OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        currentSelcetorPos = position;
                        CollectPersonViewPageAdapter.LoveStyleItemInfo loveStyleItemInfo = loveStyleItemInfoList.get
                                (position);
                        if (lastSelectorPos != -1) {//已经选择了一项
                            if (lastSelectorPos != currentSelcetorPos) {//现在选择的是另外一项
                                loveStyleItemInfo.setIsSelector(true);
                                loveStyleItemInfoList.get(lastSelectorPos).setIsSelector(false);
                                personList.remove(persons[lastSelectorPos]);
                                buttonNext.setEnabled(true);
                                personList.add(persons[position]);
                                contentView.setText(personList.toString().substring(1, personList.toString().length()
                                        - 1));
                                contentView.setTextColor(getResources().getColor(R.color.orange_color));

                                lastSelectorPos = currentSelcetorPos;
                            } else {
                                //现在选择的是同一项，取消选中
                                loveStyleItemInfo.setIsSelector(false);
                                buttonNext.setEnabled(false);
                                contentView.setText(getString(R.string.collect_person_content));
                                personList.remove(persons[position]);
                                contentView.setTextColor(getResources().getColor(R.color.light_black_color));

                                currentSelcetorPos = lastSelectorPos = -1;
                            }
                        } else {//还没有选择任何项
                            loveStyleItemInfo.setIsSelector(true);
                            buttonNext.setEnabled(true);
                            personList.add(persons[position]);
                            contentView.setText(personList.toString().substring(1, personList.toString().length() - 1));
                            contentView.setTextColor(getResources().getColor(R.color.orange_color));
                            lastSelectorPos = position;
                        }
                        collectPersonViewPageAdapter.notifyDataSetChanged();
                    }
                });
        loveStyleViewPager.setAdapter(collectPersonViewPageAdapter);
    }

    @OnClick({R.id.head_back_layout, R.id.btn_next})
    protected void back(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                postCollectOwnerInfo();
//                startActivity(MainActivity.class);
//                finish();
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    private void postCollectOwnerInfo() {
        if (ownerInfo == null) {
            ownerInfo = new User();
        }
        ownerInfo.setFamily_description(persons[currentSelcetorPos]);

        UpdateOwnerInfoRequest updateOwnerInfoRequest = new UpdateOwnerInfoRequest();
        updateOwnerInfoRequest.setUser(ownerInfo);
        Api.updateUserInfo(updateOwnerInfoRequest, new ApiCallback<ApiResponse<String>>() {
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
                startActivity(MainActivity.class);
                appManager.finishAllActivity();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));

            }
        },this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_collect_req;
    }
}
