package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerByAppointAdapter;
import com.jianfanjia.cn.adapter.DesignerByIntentionInfoAdapter;
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
import java.util.List;

/**
 * Description:预约设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class AppointDesignerActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = AppointDesignerActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private LinearLayout marchRootview = null;
    private LinearLayout intentionRootview = null;
    private TextView allText = null;
    private TextView cancelText = null;
    private TextView moreText = null;
    private ListView marched_designer_listview = null;
    private ListView intention_designer_listview = null;
    private List<DesignerCanOrderInfo> rec_designer = new ArrayList<DesignerCanOrderInfo>();
    private List<DesignerCanOrderInfo> favorite_designer = new ArrayList<DesignerCanOrderInfo>();
    private DesignerByAppointAdapter designerByAppointAdapter = null;
    private DesignerByIntentionInfoAdapter designerByIntentionInfoAdapter = null;
    private String requestmentid = null;
    private int totalCount = 3;//总可预约数

    private List<String> designerids = new ArrayList<String>();

    @Override
    public void initView() {
        initMainHeadView();
        marchRootview = (LinearLayout) findViewById(R.id.marchRootview);
        intentionRootview = (LinearLayout) findViewById(R.id.intentionRootview);
        allText = (TextView) findViewById(R.id.allText);
        cancelText = (TextView) findViewById(R.id.cancelText);
        moreText = (TextView) findViewById(R.id.moreText);
        marched_designer_listview = (ListView) findViewById(R.id.marched_designer_listview);
        intention_designer_listview = (ListView) findViewById(R.id.intention_designer_listview);
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


    @Override
    public void setListener() {
        allText.setOnClickListener(this);
        cancelText.setOnClickListener(this);
        moreText.setOnClickListener(this);
        marched_designer_listview.setOnItemClickListener(recDesignerClickListener);
        intention_designer_listview.setOnItemClickListener(favoriteDesignerClickListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.allText:
                allText.setVisibility(View.GONE);
                cancelText.setVisibility(View.VISIBLE);
                selectAll();
                break;
            case R.id.cancelText:
                allText.setVisibility(View.VISIBLE);
                cancelText.setVisibility(View.GONE);
                deSelectAll();
                break;
            case R.id.moreText:
                startActivity(MyFavoriteDesignerActivity_.class);
                break;
            case R.id.head_right_title:
                LogTool.d(TAG, "designerids=" + designerids);
                if (designerids.size() > 0) {
                    appointDesignerDialog();
                } else {
                    makeTextLong("请选择设计师");
                }
                break;
            default:
                break;
        }
    }

    private void appointDesignerDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(AppointDesignerActivity.this);
        dialog.setTitle("预约设计师？");
        dialog.setMessage("确定预约设计师吗？");
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

    private OnItemClickListener recDesignerClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DesignerCanOrderInfo info = rec_designer.get(position);
            String designerid = info.get_id();
            CheckBox ctb = (CheckBox) view.findViewById(R.id.list_item_check);
            ctb.toggle();
            designerByAppointAdapter.getIsSelected().put(position, ctb.isChecked());
            if (ctb.isChecked()) {
                designerids.add(designerid);
                totalCount--;
            } else {
                designerids.remove(designerid);
                totalCount++;
            }
            dataChanged();
        }
    };

    private OnItemClickListener favoriteDesignerClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DesignerCanOrderInfo info = favorite_designer.get(position);
            String designerid = info.get_id();
            CheckBox ctb = (CheckBox) view.findViewById(R.id.list_item_check);
            ctb.toggle();
            designerByIntentionInfoAdapter.getIsSelected().put(position, ctb.isChecked());
            if (ctb.isChecked()) {
                designerids.add(designerid);
                totalCount--;
            } else {
                designerids.remove(designerid);
                totalCount++;
            }
            dataNotifyChanged();
        }
    };


    //全选
    private void selectAll() {
        for (int i = 0; i < rec_designer.size(); i++) {
            designerByAppointAdapter.getIsSelected().put(i, true);
        }
        totalCount = rec_designer.size();
        dataChanged();
    }

    //取消
    private void deSelectAll() {
        for (int i = 0; i < rec_designer.size(); i++) {
            if (designerByAppointAdapter.getIsSelected().get(i)) {
                designerByAppointAdapter.getIsSelected().put(i, false);
                totalCount--;// 数量减1
            }
        }
        dataChanged();
    }

    private void dataNotifyChanged() {
        designerByIntentionInfoAdapter.notifyDataSetChanged();
        LogTool.d(TAG, "totalCount======" + totalCount);
        mainHeadView
                .setMianTitle(totalCount + getResources().getString(R.string.appoint));
    }

    private void dataChanged() {
        designerByAppointAdapter.notifyDataSetChanged();
        LogTool.d(TAG, "  totalCount==========================" + totalCount);
        mainHeadView
                .setMianTitle(totalCount + getResources().getString(R.string.appoint));
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
                designerByAppointAdapter = new DesignerByAppointAdapter(AppointDesignerActivity.this, rec_designer);
                marched_designer_listview.setAdapter(designerByAppointAdapter);
                designerByIntentionInfoAdapter = new DesignerByIntentionInfoAdapter(AppointDesignerActivity.this, favorite_designer);
                intention_designer_listview.setAdapter(designerByIntentionInfoAdapter);
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
            makeTextLong("预约成功");
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
