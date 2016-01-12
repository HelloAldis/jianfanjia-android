package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.Event.BindingPhoneEvent;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.activity.my.BindingPhoneActivity_;
import com.jianfanjia.cn.adapter.DesignerByAppointOrReplaceAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCanOrderInfo;
import com.jianfanjia.cn.bean.DesignerCanOrderListInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.CheckListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Description:预约设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AppointDesignerActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = AppointDesignerActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private RecyclerView appoint_designer_listview = null;
    private List<Map<String, Object>> mylist = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> splitList = new ArrayList<Map<String, Object>>();
    private List<DesignerCanOrderInfo> rec_designer = new ArrayList<DesignerCanOrderInfo>();
    private List<DesignerCanOrderInfo> favorite_designer = new ArrayList<DesignerCanOrderInfo>();
    private DesignerByAppointOrReplaceAdapter designerByAppointOrReplaceAdapter = null;
    private String requestmentid = null;
    private int orderDesignerNum = 0;//已预约设计师数
    private int totalCount = 3;//总可预约数
    private int total = 0;
    private int checkedItemCount = 0;//已选数

    private int currentPos = -1;

    private List<String> designerIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        orderDesignerNum = intent.getIntExtra(Global.REQUIREMENT_DESIGNER_NUM, 0);
        requestmentid = intent.getStringExtra(Global.REQUIREMENT_ID);
        LogTool.d(TAG, "requestmentid:" + requestmentid + " orderDesignerNum:" + orderDesignerNum);
        total = totalCount - orderDesignerNum;
        LogTool.d(TAG, " total :" + total);
        initMainHeadView();
        appoint_designer_listview = (RecyclerView) findViewById(R.id.appoint_designer_listview);
        appoint_designer_listview.setLayoutManager(new LinearLayoutManager(AppointDesignerActivity.this));
        appoint_designer_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        appoint_designer_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).showLastDivider().build());
        getOrderDesignerList(requestmentid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_appoint_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(total + getResources().getString(R.string.appoint));
        mainHeadView.setMianTitleColor();
        mainHeadView.setRightTitle(getResources().getString(R.string.appointText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void setAppointDesignerList(List<DesignerCanOrderInfo> rec_designerList, List<DesignerCanOrderInfo> favorite_designerList) {
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put(Constant.KEY, getResources().getString(R.string.marchDesignerText));
        mp.put(Constant.TEXT_KEY, "");
        mylist.add(mp);
        splitList.add(mp);
        for (DesignerCanOrderInfo designerCanOrderInfo : rec_designerList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.KEY, designerCanOrderInfo);
            mylist.add(map);
        }
        //----------------------------------------------------
        mp = new HashMap<String, Object>();
        mp.put(Constant.KEY, getResources().getString(R.string.intentionDesignerText));
        mp.put(Constant.TEXT_KEY, "");
        mylist.add(mp);
        splitList.add(mp);
        for (DesignerCanOrderInfo designerCanOrderInfo : favorite_designerList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.KEY, designerCanOrderInfo);
            mylist.add(map);
        }
    }

    public void onEventMainThread(BindingPhoneEvent bindingPhoneEvent) {
        if (TextUtils.isEmpty(bindingPhoneEvent.getPhone())) return;
        LogTool.d(TAG, "event:" + bindingPhoneEvent.getPhone());
        orderDesignerByUser(requestmentid, designerIds);
    }

    public void onEventMainThread(MessageEvent messageEvent) {
        LogTool.d(TAG, "messageEvent:" + messageEvent.getEventType());
        switch (messageEvent.getEventType()) {
            case Constant.UPDATE_ORDER_DESIGNER_ACTIVITY:
                designerByAppointOrReplaceAdapter.remove(currentPos);
                break;
            default:
                break;
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                if (dataManager.getAccount() != null) {
                    orderDesignerByUser(requestmentid, designerIds);
                } else {
                    startActivity(BindingPhoneActivity_.class);
                    overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                }
                break;
            default:
                break;
        }
    }

    //获取自己可以预约的设计师
    private void getOrderDesignerList(String requestmentid) {
        JianFanJiaClient.getOrderDesignerListByUser(AppointDesignerActivity.this, requestmentid, getOrderDesignerListener, this);
    }

    private ApiUiUpdateListener getOrderDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loding);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            DesignerCanOrderListInfo designerCanOrderListInfo = JsonParser.jsonToBean(data.toString(), DesignerCanOrderListInfo.class);
            LogTool.d(TAG, "designerCanOrderListInfo:" + designerCanOrderListInfo);
            if (null != designerCanOrderListInfo) {
                rec_designer = designerCanOrderListInfo.getRec_designer();
                favorite_designer = designerCanOrderListInfo.getFavorite_designer();
                setAppointDesignerList(rec_designer, favorite_designer);
                designerByAppointOrReplaceAdapter = new DesignerByAppointOrReplaceAdapter(AppointDesignerActivity.this, mylist, splitList, total, new CheckListener() {

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
                        LogTool.d(TAG, "checkNum=" + checkNum);
                        if (null != designerids && designerids.size() > 0) {
                            mainHeadView.setRigthTitleEnable(true);
                            designerIds = designerids;
                        } else {
                            mainHeadView.setRigthTitleEnable(false);
                        }
                        mainHeadView.setMianTitle((total - checkNum) + getResources().getString(R.string.appoint));
                        mainHeadView.setMianTitleColor();
                    }
                });
                appoint_designer_listview.setAdapter(designerByAppointOrReplaceAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            hideWaitDialog();
        }
    };

    //业主预约设计师
    private void orderDesignerByUser(String requestmentid, List<String> designerids) {
        JianFanJiaClient.orderDesignerByUser(AppointDesignerActivity.this, requestmentid, designerids, orderDesignerListener, this);
    }

    private ApiUiUpdateListener orderDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            //刷新Xuqiufragmet
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_HOME_FRAGMENT));
            UiHelper.sendUpdateBroast(AppointDesignerActivity.this);
            appManager.finishActivity(AppointDesignerActivity.this);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

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
