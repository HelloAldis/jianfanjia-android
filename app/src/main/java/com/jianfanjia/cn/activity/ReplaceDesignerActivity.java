package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.DesignerByAppointOrReplaceAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCanOrderInfo;
import com.jianfanjia.cn.bean.DesignerCanOrderListInfo;
import com.jianfanjia.cn.config.Global;
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
 * Description:替换设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ReplaceDesignerActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final String TAG = ReplaceDesignerActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ListView replace_designer_listview = null;
    private List<Map<String, String>> mylist = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> splitList = new ArrayList<Map<String, String>>();
    private List<DesignerCanOrderInfo> rec_designer = new ArrayList<DesignerCanOrderInfo>();
    private List<DesignerCanOrderInfo> favorite_designer = new ArrayList<DesignerCanOrderInfo>();
    private DesignerByAppointOrReplaceAdapter designerByAppointOrReplaceAdapter = null;
    private String requestmentid = null;
    private String designerid = null;
    private int totalCount = 1;//总可预约数

    private String newDesignerid = null;

    @Override
    public void initView() {
        initMainHeadView();
        replace_designer_listview = (ListView) findViewById(R.id.replace_designer_listview);
        Intent intent = this.getIntent();
        requestmentid = intent.getStringExtra(Global.REQUIREMENT_ID);
        designerid = intent.getStringExtra(Global.DESIGNER_ID);
        LogTool.d(TAG, "requestmentid:" + requestmentid + " designerid:" + designerid);
        getOrderDesignerList(requestmentid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_appoint_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(totalCount + getResources().getString(R.string.appoint));
        mainHeadView.setRightTitle(getResources().getString(R.string.replaceText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    private void setReplaceDesignerList(List<DesignerCanOrderInfo> rec_designerList, List<DesignerCanOrderInfo> favorite_designerList) {
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
        replace_designer_listview.setOnItemClickListener(this);
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
                if (null != newDesignerid) {
                    replaceDesignerDialog();
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
        newDesignerid = mylist.get(position).get("itemId");
        LogTool.d(TAG, "newDesignerid=" + newDesignerid);
        if (totalCount > 0) {
            totalCount--;
        }
        mainHeadView.setMianTitle(totalCount + getResources().getString(R.string.appoint));
    }

    private void replaceDesignerDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(ReplaceDesignerActivity.this);
        dialog.setTitle("替换设计师");
        dialog.setMessage("确定要替换设计师吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        replaceDesignerByUser(requestmentid, designerid, newDesignerid);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    //获取自己可以预约的设计师
    private void getOrderDesignerList(String requestmentid) {
        JianFanJiaClient.getOrderDesignerListByUser(ReplaceDesignerActivity.this, requestmentid, getOrderDesignerListener, this);
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
                setReplaceDesignerList(rec_designer, favorite_designer);
                designerByAppointOrReplaceAdapter = new DesignerByAppointOrReplaceAdapter(ReplaceDesignerActivity.this, mylist, splitList);
                replace_designer_listview.setAdapter(designerByAppointOrReplaceAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    //替换设计师
    private void replaceDesignerByUser(String requirementid, String old_designerid, String new_designerid) {
        JianFanJiaClient.ChangeOrderedDesignerByUser(ReplaceDesignerActivity.this, requirementid, old_designerid, new_designerid, replaceDesignerListener, this);
    }

    private ApiUiUpdateListener replaceDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.submiting);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            hideWaitDialog();
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_replace_designer;
    }
}
