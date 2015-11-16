package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerByAppointOrReplaceAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCanOrderInfo;
import com.jianfanjia.cn.bean.DesignerCanOrderListInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:预约设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AppointDesignerActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final String TAG = AppointDesignerActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TextView moreText = null;
    private ListView appoint_designer_listview = null;
    private List<Map<String, String>> mylist = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> splitList = new ArrayList<Map<String, String>>();
    private List<DesignerCanOrderInfo> rec_designer = new ArrayList<DesignerCanOrderInfo>();
    private List<DesignerCanOrderInfo> favorite_designer = new ArrayList<DesignerCanOrderInfo>();
    private DesignerByAppointOrReplaceAdapter designerByAppointOrReplaceAdapter = null;
    private String requestmentid = null;
    private int totalCount = 3;//总可预约数
    private int checkedItemCount = 0;//已选数

    private List<String> designerids = new ArrayList<String>();

    @Override
    public void initView() {
        initMainHeadView();
        appoint_designer_listview = (ListView) findViewById(R.id.appoint_designer_listview);
        Intent intent = this.getIntent();
        requestmentid = intent.getStringExtra(Global.REQUIREMENT_ID);
        LogTool.d(TAG, "requestmentid:" + requestmentid);
        getOrderDesignerList(requestmentid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_appoint_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(totalCount + getResources().getString(R.string.appoint));
        mainHeadView.setRightTitle(getResources().getString(R.string.appointText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    private void setAppointDesignerList(List<DesignerCanOrderInfo> rec_designerList, List<DesignerCanOrderInfo> favorite_designerList) {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("itemTitle", "匹配设计师");
        mylist.add(mp);
        splitList.add(mp);
        for (DesignerCanOrderInfo info : rec_designerList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("itemId", info.get_id());
            map.put("itemTitle", info.getUsername());
            map.put("itemImg", info.getImageid());
            map.put("itemMatch", "" + info.getMatch());
            mylist.add(map);
        }
        //----------------------------------------------------
        mp = new HashMap<String, String>();
        mp.put("itemTitle", "意向设计师");
        mylist.add(mp);
        splitList.add(mp);
        for (DesignerCanOrderInfo info : favorite_designerList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("itemId", info.get_id());
            map.put("itemTitle", info.getUsername());
            map.put("itemImg", info.getImageid());
            map.put("itemMatch", "" + info.getMatch());
            mylist.add(map);
        }
    }


    @Override
    public void setListener() {
        appoint_designer_listview.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
//            case R.id.moreText:
//                startActivity(MyFavoriteDesignerActivity_.class);
//                break;
            case R.id.head_right_title:
                if (checkedItemCount != 0) {
                    appointDesignerDialog();
                } else {
                    makeTextLong("请选择设计师");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkedItemCount = appoint_designer_listview.getCheckedItemCount();
        if (checkedItemCount > totalCount) {
            makeTextLong("最多可选3名设计师");
            return;
        }
        boolean checked = appoint_designer_listview.getCheckedItemPositions().get(position);
        LogTool.d(TAG, "checked=" + checked);
        String designerid = mylist.get(position).get("itemId");
        LogTool.d(TAG, "designerid=" + designerid);
        if (checked) {
            designerids.add(designerid);
        } else {
            designerids.remove(designerid);
        }
        mainHeadView.setMianTitle((totalCount - checkedItemCount) + getResources().getString(R.string.appoint));
    }

    private void appointDesignerDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(AppointDesignerActivity.this);
        dialog.setTitle("预约设计师");
        dialog.setMessage("确定要预约设计师吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        orderDesignerByUser(requestmentid, designerids);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
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
                designerByAppointOrReplaceAdapter = new DesignerByAppointOrReplaceAdapter(AppointDesignerActivity.this, mylist, splitList);
                appoint_designer_listview.setAdapter(designerByAppointOrReplaceAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
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
            setResult(XuQiuFragment.REQUESTCODE_FRESH_REQUIREMENT);
            finish();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_appoint_designer;
    }
}
