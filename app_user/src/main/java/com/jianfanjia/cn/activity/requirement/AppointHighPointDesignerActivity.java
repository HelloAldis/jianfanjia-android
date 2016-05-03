package com.jianfanjia.cn.activity.requirement;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerCanOrderList;
import com.jianfanjia.api.request.user.GetCanOrderDesignerListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.AppointDesignerViewPageAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.viewpager.transfrom.ScaleInOutTransfromer;
import com.jianfanjia.viewpager_indicator.CircleIndicator;

/**
 * Description: com.jianfanjia.cn.activity.requirement
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-03 14:38
 */
public class AppointHighPointDesignerActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Bind(R.id.appoint_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.dot_indicator)
    CircleIndicator mCircleIndicator;

    AppointDesignerViewPageAdapter mViewPageAdapter;

    List<View> mViews = new ArrayList<>();

    private List<Designer> rec_designer;

    protected int[] loveStyleImageIds = new int[]{
            R.mipmap.img_req_ou_shi, R.mipmap.img_req_zhong_shi, R.mipmap.img_req_xian_dai,
            R.mipmap.img_req_di_zhonghai, R.mipmap.img_req_mei_shi, R.mipmap.img_req_dong_nanya,
            R.mipmap.img_req_tian_yuan
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        initMainView();
        initViewPager();
    }

    private void initViewPager(){
        for (int imageid : loveStyleImageIds) {
            View view = inflater.inflate(R.layout.viewpager_item_appoint_designer,null,true);
            ImageView imageView = (ImageView)view.findViewById(R.id.designer_case_background);
            imageView.setImageResource(imageid);
            mViews.add(view);
        }

        mViewPageAdapter = new AppointDesignerViewPageAdapter(this);

        mViewPager.setAdapter(mViewPageAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ScaleInOutTransfromer());
        mCircleIndicator.setViewPager(mViewPager);
    }

    //获取自己可以预约的设计师
    private void getOrderDesignerList(String requestmentid) {
        GetCanOrderDesignerListRequest getCanOrderDesignerListRequest = new GetCanOrderDesignerListRequest();
        getCanOrderDesignerListRequest.setRequirementid(requestmentid);
        Api.getCanOrderDesigner(getCanOrderDesignerListRequest, new
                ApiCallback<ApiResponse<DesignerCanOrderList>>() {

                    @Override
                    public void onPreLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void onHttpDone() {
                        hideWaitDialog();
                    }

                    @Override
                    public void onSuccess(ApiResponse<DesignerCanOrderList> apiResponse) {
                        DesignerCanOrderList designerCanOrderListInfo = apiResponse.getData();
                        if (null != designerCanOrderListInfo) {
                            rec_designer = designerCanOrderListInfo.getRec_designer();

                        }
                    }

                    @Override
                    public void onFailed(ApiResponse<DesignerCanOrderList> apiResponse) {
                        makeTextShort(apiResponse.getErr_msg());
                    }

                    @Override
                    public void onNetworkError(int code) {
                        makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    }
                });
    }


    private void initMainView(){
        mMainHeadView.setMianTitle(getString(R.string.high_point));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_appoint_highpoint_designer;
    }
}
