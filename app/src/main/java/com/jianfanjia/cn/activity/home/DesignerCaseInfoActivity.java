package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.common.ShowPicActivity;
import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.bean.ImageInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:设计师作品案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends SwipeBackActivity implements OnClickListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();
    private RelativeLayout head_back_layout = null;
    private RelativeLayout toolbar_collect_layout = null;
    private TextView tv_title = null;
    private ImageView toolbar_collect = null;
    private LinearLayout activity_case_info_top_layout = null;
    private RecyclerView designer_case_listview = null;
    private LinearLayoutManager mLayoutManager = null;
    private ImageView head_img = null;
    private TextView nameText = null;
    private List<String> imgs = new ArrayList<String>();

    private String productid = null;
    private String designertid = null;

    @Override
    public void initView() {
        head_back_layout = (RelativeLayout) findViewById(R.id.head_back_layout);
        toolbar_collect_layout = (RelativeLayout) findViewById(R.id.toolbar_collect_layout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        toolbar_collect = (ImageView) findViewById(R.id.toolbar_collect);
        activity_case_info_top_layout = (LinearLayout) findViewById(R.id.top_info_layout);
        designer_case_listview = (RecyclerView) findViewById(R.id.designer_case_listview);
        mLayoutManager = new LinearLayoutManager(DesignerCaseInfoActivity.this);
        designer_case_listview.setLayoutManager(mLayoutManager);
        designer_case_listview.setItemAnimator(new DefaultItemAnimator());
        designer_case_listview.setHasFixedSize(true);
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        designer_case_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(DesignerCaseInfoActivity.this).paint(paint).showLastDivider().build());
        head_img = (ImageView) findViewById(R.id.head_img);
        head_img = (ImageView) findViewById(R.id.head_img);
        nameText = (TextView) findViewById(R.id.name_text);
        //---------------------------------------------
        initData(this.getIntent());
    }

    private void initData(Intent intent) {
        Bundle productBundle = intent.getExtras();
        productid = productBundle.getString(Global.PRODUCT_ID);
        LogTool.d(TAG, "productid=" + productid);
        getProductHomePageInfo(productid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        initData(intent);
    }

    @Override
    public void setListener() {
        head_back_layout.setOnClickListener(this);
        toolbar_collect_layout.setOnClickListener(this);
        activity_case_info_top_layout.setOnClickListener(this);
        designer_case_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mScrollY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mScrollY += dy;
                if (mScrollY > 30) {
                    activity_case_info_top_layout.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                } else {
                    activity_case_info_top_layout.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.toolbar_collect_layout:
                UiHelper.imageButtonAnim(toolbar_collect, null);
                if (toolbar_collect.isSelected()) {
                    deleteProductDesigner(productid);
                } else {
                    addProductHomePageInfo(productid);
                }
                break;
            case R.id.top_info_layout:
                startDesignerInfoActivity(designertid);
                break;
            default:
                break;
        }
    }

    private void startDesignerInfoActivity(String designertid) {
        Bundle designerInfoBundle = new Bundle();
        designerInfoBundle.putString(Global.DESIGNER_ID, designertid);
        startActivity(DesignerInfoActivity.class, designerInfoBundle);
    }

    private void getProductHomePageInfo(String productid) {
        JianFanJiaClient.getProductHomePage(DesignerCaseInfoActivity.this, productid, getProductHomePageInfoListener, this);
    }

    private void addProductHomePageInfo(String productid) {
        JianFanJiaClient.addCollectionByUser(DesignerCaseInfoActivity.this, productid, addProductHomePageInfoListener, this);
    }

    private void deleteProductDesigner(String productid) {
        JianFanJiaClient.deleteCollectionByUser(DesignerCaseInfoActivity.this, productid, deleteProductListener, this);
    }

    private ApiUiUpdateListener getProductHomePageInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loding);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            DesignerCaseInfo designerCaseInfo = JsonParser.jsonToBean(data.toString(), DesignerCaseInfo.class);
            LogTool.d(TAG, "designerCaseInfo" + designerCaseInfo);
            if (null != designerCaseInfo) {
                toolbar_collect_layout.setVisibility(View.VISIBLE);
                if (designerCaseInfo.is_my_favorite()) {
                    toolbar_collect.setSelected(true);
                } else {
                    toolbar_collect.setSelected(false);
                }
                designertid = designerCaseInfo.getDesigner().get_id();
                tv_title.setText(designerCaseInfo.getCell());
                nameText.setText(designerCaseInfo.getDesigner().getUsername());
                imageShow.displayImageHeadWidthThumnailImage(DesignerCaseInfoActivity.this, designerCaseInfo.getDesigner().getImageid(), head_img);
                List<ImageInfo> imgList = designerCaseInfo.getImages();
                imgs.clear();
                for (ImageInfo info : imgList) {
                    imgs.add(info.getImageid());
                }
                DesignerCaseAdapter adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, imgList, designerCaseInfo, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        LogTool.d(TAG, "position:" + position);
                        Intent showPicIntent = new Intent(DesignerCaseInfoActivity.this, ShowPicActivity.class);
                        Bundle showPicBundle = new Bundle();
                        showPicBundle.putInt(Constant.CURRENT_POSITION, position);
                        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                                (ArrayList<String>) imgs);
                        showPicIntent.putExtras(showPicBundle);
                        startActivity(showPicIntent);
                    }

                    @Override
                    public void OnViewClick(int position) {
                        LogTool.d(TAG, "position=" + position);
                        startDesignerInfoActivity(designertid);
                    }
                });
                designer_case_listview.setAdapter(adapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            hideWaitDialog();
            makeTextLong(error_msg);
            toolbar_collect.setVisibility(View.GONE);
        }
    };

    private ApiUiUpdateListener addProductHomePageInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            toolbar_collect.setSelected(true);
            makeTextShort(getString(R.string.str_collect_success));
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_PRODUCT_FRAGMENT));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };
    private ApiUiUpdateListener deleteProductListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            toolbar_collect.setSelected(false);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_PRODUCT_FRAGMENT));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_info;
    }

}
