package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerCanOrderList;
import com.jianfanjia.api.request.user.GetCanOrderDesignerListRequest;
import com.jianfanjia.api.request.user.OrderDesignerRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.DesignerByAppointOrReplaceAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.CheckListener;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description:预约设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AppointDesignerActivity extends BaseSwipeBackActivity {
    private static final String TAG = AppointDesignerActivity.class.getName();

    @Bind(R.id.my_appoint_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.appoint_designer_listview)
    RecyclerView appoint_designer_listview;

    private List<Map<String, Object>> mylist = new ArrayList<>();
    private List<Map<String, Object>> splitList = new ArrayList<>();
    private List<Designer> rec_designer = new ArrayList<>();
    private List<Designer> favorite_designer = new ArrayList<>();
    private DesignerByAppointOrReplaceAdapter designerByAppointOrReplaceAdapter = null;
    private String requestmentid = null;
    private int orderDesignerNum = 0;//已预约设计师数
    private int totalCount = 3;//总可预约数
    private int total = 0;
    private int checkedItemCount = 0;//已选数

    private int currentPos = -1;

    private List<String> designerIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getDataFromIntent();
        initView();
        initData();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        orderDesignerNum = intent.getIntExtra(IntentConstant.REQUIREMENT_DESIGNER_NUM, 0);
        requestmentid = intent.getStringExtra(IntentConstant.REQUIREMENT_ID);
        LogTool.d(TAG, "requestmentid:" + requestmentid + " orderDesignerNum:" + orderDesignerNum);
        total = totalCount - orderDesignerNum;
        LogTool.d(TAG, " total :" + total);
    }

    private void initView() {
        initMainHeadView();
        appoint_designer_listview.setLayoutManager(new LinearLayoutManager(AppointDesignerActivity.this));
        appoint_designer_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        appoint_designer_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint)
                .showLastDivider().build());
    }

    private void initData() {
        getOrderDesignerList(requestmentid);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.appoint) + total + getResources().getString(R
                .string.appointNum));
        mainHeadView.setMianTitleColor();
        mainHeadView.setRightTitle(getResources().getString(R.string.appointText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void setAppointDesignerList(List<Designer> rec_designerList, List<Designer>
            favorite_designerList) {
        mylist.clear();
        splitList.clear();
        Map<String, Object> mp = new HashMap<>();
        mp.put(Constant.KEY, getResources().getString(R.string.marchDesignerText));
        mp.put(Constant.TEXT_KEY, "");
        mylist.add(mp);
        splitList.add(mp);
        for (Designer designerCanOrderInfo : rec_designerList) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constant.KEY, designerCanOrderInfo);
            mylist.add(map);
        }
        //----------------------------------------------------
        mp = new HashMap<>();
        mp.put(Constant.KEY, getResources().getString(R.string.intentionDesignerText));
        mp.put(Constant.TEXT_KEY, getResources().getString(R.string.moreText));
        mylist.add(mp);
        splitList.add(mp);
        for (Designer designerCanOrderInfo : favorite_designerList) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constant.KEY, designerCanOrderInfo);
            mylist.add(map);
        }
    }

    public void onEventMainThread(MessageEvent messageEvent) {
        LogTool.d(TAG, "messageEvent:" + messageEvent.getEventType());
        switch (messageEvent.getEventType()) {
            case Constant.DELETE_ORDER_DESIGNER_ACTIVITY:
                designerByAppointOrReplaceAdapter.remove(currentPos);
                break;
            case Constant.UPDATE_ORDER_DESIGNER_ACTIVITY:
                getOrderDesignerList(requestmentid);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                if (dataManager.getAccount() != null) {//先判断一下，防止老数据出现异常
                    orderDesignerByUser(requestmentid, designerIds);
                }
                break;
            default:
                break;
        }
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
                            favorite_designer = designerCanOrderListInfo.getFavorite_designer();
                            setAppointDesignerList(rec_designer, favorite_designer);
                            designerByAppointOrReplaceAdapter = new DesignerByAppointOrReplaceAdapter
                                    (AppointDesignerActivity
                                            .this, mylist, splitList, total, new CheckListener() {

                                        @Override
                                        public void getItemData(int position, String designerid) {
                                            LogTool.d(TAG, "position=" + position + " designerid=" + designerid);
                                            currentPos = position;
                                            Bundle designerBundle = new Bundle();
                                            designerBundle.putString(IntentConstant.DESIGNER_ID, designerid);
                                            startActivity(DesignerInfoActivity.class, designerBundle);
                                        }

                                        @Override
                                        public void getCheckedData(List<String> designerids) {
                                            int checkNum = designerids.size();
                                            LogTool.d(TAG, "checkNum=" + checkNum);
                                            if (null != designerids && designerids.size() > 0) {
                                                mainHeadView.setRigthTitleEnable(true);
                                                designerIds = designerids;
                                            } else {
                                                mainHeadView.setRigthTitleEnable(false);
                                            }
                                            mainHeadView.setMianTitle(getResources().getString(R.string.appoint) +
                                                    (total -
                                                            checkNum) +
                                                    getResources().getString(R.string.appointNum));
                                            mainHeadView.setMianTitleColor();
                                        }
                                    });
                            appoint_designer_listview.setAdapter(designerByAppointOrReplaceAdapter);
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

    //业主预约设计师
    private void orderDesignerByUser(String requestmentid, List<String> designerids) {
        OrderDesignerRequest orderDesignerRequest = new OrderDesignerRequest();
        orderDesignerRequest.setRequirementid(requestmentid);
        orderDesignerRequest.setDesignerids(designerids);
        Api.orderDesigner(orderDesignerRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                appManager.finishActivity(AppointDesignerActivity.this);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_appoint_designer;
    }
}
