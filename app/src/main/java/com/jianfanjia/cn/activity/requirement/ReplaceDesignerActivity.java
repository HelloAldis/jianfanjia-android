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
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerCanOrderList;
import com.jianfanjia.api.request.user.GetCanOrderDesignerListRequest;
import com.jianfanjia.api.request.user.ReplaceOrderedDesignerRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.DesignerByAppointOrReplaceAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.CheckListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import de.greenrobot.event.EventBus;

/**
 * Description:替换设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ReplaceDesignerActivity extends SwipeBackActivity {
    private static final String TAG = ReplaceDesignerActivity.class.getName();

    @Bind(R.id.my_appoint_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.replace_designer_listview)
    RecyclerView replace_designer_listview;

    private List<Map<String, Object>> mylist = new ArrayList<>();
    private List<Map<String, Object>> splitList = new ArrayList<>();
    private List<Designer> rec_designer = new ArrayList<>();
    private List<Designer> favorite_designer = new ArrayList<>();
    private DesignerByAppointOrReplaceAdapter designerByAppointOrReplaceAdapter = null;
    private String requestmentid = null;
    private String designerid = null;
    private int totalCount = 1;//总可预约数

    private int currentPos = -1;

    private String newDesignerid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        initMainHeadView();
        replace_designer_listview.setLayoutManager(new LinearLayoutManager(ReplaceDesignerActivity.this));
        replace_designer_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        replace_designer_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint)
                .showLastDivider().build());
        Intent intent = this.getIntent();
        requestmentid = intent.getStringExtra(Global.REQUIREMENT_ID);
        designerid = intent.getStringExtra(Global.DESIGNER_ID);
        LogTool.d(TAG, "requestmentid:" + requestmentid + " designerid:" + designerid);
        getOrderDesignerList(requestmentid);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.appoint) + totalCount + getResources().getString
                (R.string.appointNum));
        mainHeadView.setMianTitleColor();
        mainHeadView.setRightTitle(getResources().getString(R.string.replaceText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void setReplaceDesignerList(List<Designer> rec_designerList, List<Designer>
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

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                replaceDesignerByUser(requestmentid, designerid, newDesignerid);
                break;
            default:
                break;
        }
    }

    //获取自己可以预约的设计师
    private void getOrderDesignerList(String requestmentid) {

        GetCanOrderDesignerListRequest getCanOrderDesignerListRequest = new GetCanOrderDesignerListRequest();
        getCanOrderDesignerListRequest.setRequirementis(requestmentid);

        Api.getCanOrderDesigner(getCanOrderDesignerListRequest, new ApiCallback<ApiResponse<DesignerCanOrderList>>() {


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
                LogTool.d(TAG, "designerCanOrderListInfo:" + designerCanOrderListInfo);
                if (null != designerCanOrderListInfo) {
                    rec_designer = designerCanOrderListInfo.getRec_designer();
                    favorite_designer = designerCanOrderListInfo.getFavorite_designer();
                    setReplaceDesignerList(rec_designer, favorite_designer);
                    designerByAppointOrReplaceAdapter = new DesignerByAppointOrReplaceAdapter(ReplaceDesignerActivity
                            .this, mylist, splitList, totalCount, new CheckListener() {
                        @Override
                        public void getItemData(int position, String designerid) {
                            LogTool.d(TAG, "position=" + position + " designerid=" + designerid);
                            currentPos = position;
                            Bundle designerBundle = new Bundle();
                            designerBundle.putString(Global.DESIGNER_ID, designerid);
                            startActivity(DesignerInfoActivity.class, designerBundle);
                        }

                        @Override
                        public void getCheckedData(List<String> designerids) {
                            int checkNum = designerids.size();
                            LogTool.d(TAG, "checkNum:" + checkNum);
                            if (null != designerids && designerids.size() > 0) {
                                mainHeadView.setRigthTitleEnable(true);
                                newDesignerid = designerids.get(0);
                            } else {
                                mainHeadView.setRigthTitleEnable(false);
                            }
                            mainHeadView.setMianTitle(getResources().getString(R.string.appoint) + (totalCount -
                                    checkNum) + getResources().getString(R.string.appointNum));
                            mainHeadView.setMianTitleColor();
                        }
                    });
                    replace_designer_listview.setAdapter(designerByAppointOrReplaceAdapter);
                }
            }

            @Override
            public void onFailed(ApiResponse<DesignerCanOrderList> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    //替换设计师
    private void replaceDesignerByUser(String requirementid, String old_designerid, String new_designerid) {
        ReplaceOrderedDesignerRequest replaceOrderedDesignerRequest = new ReplaceOrderedDesignerRequest();
        replaceOrderedDesignerRequest.setRequirementid(requirementid);
        replaceOrderedDesignerRequest.setOld_designserid(old_designerid);
        replaceOrderedDesignerRequest.setNew_designerid(new_designerid);

        Api.replaceOrderedDesigner(replaceOrderedDesignerRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                appManager.finishActivity(ReplaceDesignerActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_replace_designer;
    }
}
