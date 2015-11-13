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
 * Description:替换设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ReplaceDesignerActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = ReplaceDesignerActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private LinearLayout marchRootview = null;
    private LinearLayout intentionRootview = null;
    private TextView moreText = null;
    private ListView marched_designer_listview = null;
    private ListView intention_designer_listview = null;
    private List<DesignerCanOrderInfo> rec_designer = new ArrayList<DesignerCanOrderInfo>();
    private List<DesignerCanOrderInfo> favorite_designer = new ArrayList<DesignerCanOrderInfo>();
    private DesignerByAppointAdapter designerByAppointAdapter = null;
    private DesignerByIntentionInfoAdapter designerByIntentionInfoAdapter = null;
    private String requestmentid = null;
    private String designerid = null;
    private int totalCount = 1;//总可预约数

    private String newDesignerid = null;
    private List<String> designerids = new ArrayList<String>();

    @Override
    public void initView() {
        initMainHeadView();
        marchRootview = (LinearLayout) findViewById(R.id.marchRootview);
        intentionRootview = (LinearLayout) findViewById(R.id.intentionRootview);
        moreText = (TextView) findViewById(R.id.moreText);
        marched_designer_listview = (ListView) findViewById(R.id.marched_designer_listview);
        intention_designer_listview = (ListView) findViewById(R.id.intention_designer_listview);
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
        mainHeadView.setRightTitle(getResources().getString(R.string.appointText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
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
            case R.id.moreText:
                startActivity(MyFavoriteDesignerActivity_.class);
                break;
            case R.id.head_right_title:
                LogTool.d(TAG, "designerids====" + designerids);
                if (null != designerids && designerids.size() > 0) {
                    newDesignerid = designerids.get(0);
                    LogTool.d(TAG, "newDesignerid:" + newDesignerid);
                    replaceDesignerDialog();
                } else {
                    makeTextLong("请选择设计师");
                }
                break;
            default:
                break;
        }
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

    private OnItemClickListener recDesignerClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DesignerCanOrderInfo info = rec_designer.get(position);
            String designerid = info.get_id();
            CheckBox ctb = (CheckBox) view.findViewById(R.id.list_item_check);
            ctb.toggle();
            designerByAppointAdapter.getIsSelected().put(position, ctb.isChecked());
            if (ctb.isChecked()) {
                if (designerids.isEmpty()) {
                    designerids.add(designerid);
                }
                totalCount--;
            } else {
                if (!designerids.isEmpty()) {
                    designerids.remove(designerid);
                }
                totalCount++;
            }
            mainHeadView.setMianTitle(totalCount + getResources().getString(R.string.appoint));
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
                if (designerids.isEmpty()) {
                    designerids.add(designerid);
                }
                totalCount--;
            } else {
                if (!designerids.isEmpty()) {
                    designerids.remove(designerid);
                }
                totalCount++;
            }
            mainHeadView.setMianTitle(totalCount + getResources().getString(R.string.appoint));
        }
    };

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
                designerByAppointAdapter = new DesignerByAppointAdapter(ReplaceDesignerActivity.this, rec_designer);
                marched_designer_listview.setAdapter(designerByAppointAdapter);
                designerByIntentionInfoAdapter = new DesignerByIntentionInfoAdapter(ReplaceDesignerActivity.this, favorite_designer);
                intention_designer_listview.setAdapter(designerByIntentionInfoAdapter);
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
            makeTextLong("替换成功");
            hideWaitDialog();
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
