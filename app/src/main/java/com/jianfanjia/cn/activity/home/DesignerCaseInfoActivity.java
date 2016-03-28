package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.common.AddCollectionRequest;
import com.jianfanjia.api.request.common.DeleteCollectionRequest;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.common.ShowPicActivity;
import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:设计师作品案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends SwipeBackActivity implements OnClickListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();
    private static final int SCROLL_Y = 100;
    private int mScrollY = 0;

    @Bind(R.id.head_back_layout)
    protected RelativeLayout head_back_layout = null;

    @Bind(R.id.toolbar_collect_layout)
    protected RelativeLayout toolbar_collect_layout = null;

    @Bind(R.id.tv_title)
    protected TextView tv_title = null;

    @Bind(R.id.toolbar_collect)
    protected ImageView toolbar_collect = null;

    @Bind(R.id.top_info_layout)
    protected LinearLayout activity_case_info_top_layout = null;

    @Bind(R.id.designer_case_listview)
    protected RecyclerView designer_case_listview = null;

    private LinearLayoutManager mLayoutManager = null;

    @Bind(R.id.head_img)
    protected ImageView head_img = null;

    @Bind(R.id.name_text)
    protected TextView nameText = null;

    private List<String> imgs = new ArrayList<>();

    private String productid = null;
    private String designertid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.getDataFromIntent(this.getIntent());
        this.setListener();
    }

    public void initView() {
        mLayoutManager = new LinearLayoutManager(DesignerCaseInfoActivity.this);
        designer_case_listview.setLayoutManager(mLayoutManager);
        designer_case_listview.setItemAnimator(new DefaultItemAnimator());
        designer_case_listview.setHasFixedSize(true);
        designer_case_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
        setListener();
    }

    private void getDataFromIntent(Intent intent) {
        Bundle productBundle = intent.getExtras();
        productid = productBundle.getString(Global.PRODUCT_ID);
        LogTool.d(TAG, "productid=" + productid);
        getProductHomePageInfo(productid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        mScrollY = 0;
        getDataFromIntent(intent);
    }

    private void setListener() {
        designer_case_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mScrollY += dy;
                if (mScrollY > SCROLL_Y) {
                    activity_case_info_top_layout.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                } else {
                    activity_case_info_top_layout.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.head_back_layout, R.id.toolbar_collect_layout, R.id.top_info_layout})
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
        GetProductHomePageRequest request = new GetProductHomePageRequest();
        request.setProductid(productid);

        Api.getProductHomePage(request, this.getProductHomePageInfoCallback);
    }

    private void addProductHomePageInfo(String productid) {
        AddCollectionRequest request = new AddCollectionRequest();
        request.set_id(productid);

        Api.addCollectionByUser(request, this.addProductHomePageInfoCallback);
    }

    private void deleteProductDesigner(String productid) {
        DeleteCollectionRequest request = new DeleteCollectionRequest();
        request.set_id(productid);
        Api.deleteCollectionByUser(request, this.deleteProductCallback);
    }

    private ApiCallback<ApiResponse<DesignerCaseInfo>> getProductHomePageInfoCallback = new ApiCallback<ApiResponse<DesignerCaseInfo>>() {
        @Override
        public void onPreLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<DesignerCaseInfo> apiResponse) {
            DesignerCaseInfo designerCaseInfo = apiResponse.getData();
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
                imageShow.displayImageHeadWidthThumnailImage(DesignerCaseInfoActivity.this, designerCaseInfo
                        .getDesigner().getImageid(), head_img);
                List<ImageInfo> imgList = designerCaseInfo.getImages();
                imgs.clear();
                for (ImageInfo info : imgList) {
                    imgs.add(info.getImageid());
                }
                DesignerCaseAdapter adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, imgList,
                        designerCaseInfo, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        LogTool.d(TAG, "position:" + position);
                        Bundle showPicBundle = new Bundle();
                        showPicBundle.putInt(Constant.CURRENT_POSITION, position);
                        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                                (ArrayList<String>) imgs);
                        startActivity(ShowPicActivity.class, showPicBundle);
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
        public void onFailed(ApiResponse<DesignerCaseInfo> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
            toolbar_collect.setVisibility(View.GONE);
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<Object>> addProductHomePageInfoCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            toolbar_collect.setSelected(true);
            makeTextShort(getString(R.string.str_collect_success));
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_PRODUCT_FRAGMENT));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<Object>> deleteProductCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            toolbar_collect.setSelected(false);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_PRODUCT_FRAGMENT));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

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
