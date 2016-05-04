package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.jianfanjia.api.model.DesignerList;
import com.jianfanjia.api.request.guest.SearchDesignerRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerListAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.GetItemCallback;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.SelectPopupWindowUtil;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:海量设计师列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerListActivity extends BaseSwipeBackActivity implements View.OnClickListener, PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerListActivity.class.getName();
    private static final int DEC_TYPE = 1;
    private static final int DEC_HOUSE_TYPE = 2;
    private static final int DEC_STYLE = 3;
    private static final int DEC_FEE = 4;
    private static final int NOT = 5;

    @Bind(R.id.designer_head)
    protected MainHeadView mainHeadView = null;

    @Bind(R.id.empty_include)
    protected RelativeLayout emptyLayout = null;

    @Bind(R.id.error_include)
    protected RelativeLayout errorLayout = null;

    @Bind(R.id.topLayout)
    protected LinearLayout topLayout = null;

    @Bind(R.id.decTypeLayout)
    protected RelativeLayout decTypeLayout = null;

    @Bind(R.id.dectHouseTypeLayout)
    protected RelativeLayout decHouseTypeLayout = null;

    @Bind(R.id.decStyleLayout)
    protected RelativeLayout decStyleLayout = null;

    @Bind(R.id.decFeeLayout)
    protected RelativeLayout decFeeLayout = null;

    @Bind(R.id.decType_item)
    protected TextView decType_item = null;

    @Bind(R.id.decHouseType_item)
    protected TextView decHouseType_item = null;

    @Bind(R.id.decStyle_item)
    protected TextView decStyle_item = null;

    @Bind(R.id.decFee_item)
    protected TextView decFee_item = null;

    @Bind(R.id.recycleview)
    protected PullToRefreshRecycleView designerListView = null;

    private DesignerListAdapter designerListAdapter = null;
    private List<Designer> designerList = new ArrayList<>();
    private SelectPopupWindowUtil mSelectPopupWindowUtil = null;
    private String decType = null;
    private String decHouseStyle = null;
    private String decStyle = null;
    private String decFeeList = null;
    private boolean isFirst = true;
    private int FROM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.setListener();
    }

    public void initView() {
        initMainHeadView();
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_designer));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);

        designerListView.setMode(PullToRefreshBase.Mode.BOTH);
        designerListView.setLayoutManager(new LinearLayoutManager(DesignerListActivity.this));
        designerListView.setHasFixedSize(true);
        designerListView.setItemAnimator(new DefaultItemAnimator());
        designerListView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));

        mSelectPopupWindowUtil = new SelectPopupWindowUtil(this);

        searchDesigners(FROM, this.pullDownCallback);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.all_designer));
        mainHeadView.setBackgroundTransparent();
    }

    private void setListener() {
        designerListView.setOnRefreshListener(this);
    }

    @OnClick({R.id.head_back_layout, R.id.decTypeLayout, R.id.dectHouseTypeLayout, R.id.decStyleLayout,
            R.id.decFeeLayout, R.id.error_include})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.decTypeLayout:
                setSelectState(DEC_TYPE);
                break;
            case R.id.dectHouseTypeLayout:
                setSelectState(DEC_HOUSE_TYPE);
                break;
            case R.id.decStyleLayout:
                setSelectState(DEC_STYLE);
                break;
            case R.id.decFeeLayout:
                setSelectState(DEC_FEE);
                break;
            case R.id.error_include:
                searchDesigners(FROM, this.pullUpCallback);
                break;
            default:
                break;
        }
    }

    private void setSelectState(int type) {
        switch (type) {
            case DEC_TYPE:
                showWindow(R.array.arr_dectype, DEC_TYPE);
                decTypeLayout.setSelected(true);
                decHouseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
                decFeeLayout.setSelected(false);
                break;
            case DEC_HOUSE_TYPE:
                showWindow(R.array.arr_housetype, DEC_HOUSE_TYPE);
                decTypeLayout.setSelected(false);
                decHouseTypeLayout.setSelected(true);
                decStyleLayout.setSelected(false);
                decFeeLayout.setSelected(false);
                break;
            case DEC_STYLE:
                showWindow(R.array.arr_decstyle, DEC_STYLE);
                decTypeLayout.setSelected(false);
                decHouseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(true);
                decFeeLayout.setSelected(false);
                break;
            case DEC_FEE:
                showWindow(R.array.arr_grade, DEC_FEE);
                decTypeLayout.setSelected(false);
                decHouseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
                decFeeLayout.setSelected(true);
                break;
            case NOT:
                decTypeLayout.setSelected(false);
                decHouseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
                decFeeLayout.setSelected(false);
            default:
                break;
        }
    }

    private void searchDesigners(int from, ApiCallback<ApiResponse<DesignerList>> apiCallback) {
        Map<String, Object> query = new HashMap<>();
        query.put("dec_types", decType);
        query.put("dec_house_types", decHouseStyle);
        query.put("dec_styles", decStyle);
        if (!TextUtils.isEmpty(decFeeList)) {
            query.put("tags", decFeeList);
        }
        SearchDesignerRequest request = new SearchDesignerRequest();
        request.setQuery(query);
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);

        Api.searchDesigner(request, apiCallback);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        searchDesigners(FROM, this.pullDownCallback);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        searchDesigners(FROM, this.pullUpCallback);
    }

    private ApiCallback<ApiResponse<DesignerList>> pullDownCallback = new ApiCallback<ApiResponse<DesignerList>>() {
        @Override
        public void onPreLoad() {
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void onHttpDone() {
            designerListView.onRefreshComplete();
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<DesignerList> apiResponse) {
            DesignerList designer = apiResponse.getData();
            LogTool.d(TAG, "designer:" + designer);
            if (null != designer) {
                designerList.clear();
                designerList.addAll(designer.getDesigners());
                if (null != designerList && designerList.size() > 0) {
                    if (null == designerListAdapter) {
                        designerListAdapter = new DesignerListAdapter(DesignerListActivity.this, designerList, new
                                RecyclerViewOnItemClickListener() {
                                    @Override
                                    public void OnItemClick(View view, int position) {

                                    }

                                    @Override
                                    public void OnViewClick(int position) {
                                        String designerId = designerList.get(position).get_id();
                                        LogTool.d(TAG, "designerId:" + designerId);
                                        Intent designerIntent = new Intent(DesignerListActivity.this,
                                                DesignerInfoActivity
                                                        .class);
                                        Bundle designerBundle = new Bundle();
                                        designerBundle.putString(IntentConstant.DESIGNER_ID, designerId);
                                        designerIntent.putExtras(designerBundle);
                                        startActivity(designerIntent);
                                    }
                                });
                        designerListView.setAdapter(designerListAdapter);
                    } else {
                        designerListView.scrollToPosition(0);
                        designerListAdapter.notifyDataSetChanged();
                    }
                    FROM = designerList.size();
                    LogTool.d(TAG, "FROM:" + FROM);
                    isFirst = false;
                    designerListView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    designerListView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<DesignerList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());

        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            if (isFirst) {
                designerListView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private ApiCallback<ApiResponse<DesignerList>> pullUpCallback = new ApiCallback<ApiResponse<DesignerList>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {
            designerListView.onRefreshComplete();
        }

        @Override
        public void onSuccess(ApiResponse<DesignerList> apiResponse) {
            designerListView.onRefreshComplete();
            DesignerList designer = apiResponse.getData();
            LogTool.d(TAG, "designer:" + designer);
            if (null != designer) {
                List<Designer> designers = designer.getDesigners();
                if (null != designers && designers.size() > 0) {
                    designerListAdapter.add(FROM, designers);
                    FROM += Constant.HOME_PAGE_LIMIT;
                    LogTool.d(TAG, "FROM=" + FROM);
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<DesignerList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };


    private void showWindow(int resId, int type) {
        switch (type) {
            case DEC_TYPE:
                mSelectPopupWindowUtil.showAsDropDown(topLayout, resId, getDecTypeCallback, Global
                        .DEC_TYPE_POSITION);
                break;
            case DEC_HOUSE_TYPE:
                mSelectPopupWindowUtil.showAsDropDown(topLayout, resId, getDecHouseTypeCallback, Global
                        .DEC_HOUSE_TYPE_POSITION);
                break;
            case DEC_STYLE:
                mSelectPopupWindowUtil.showAsDropDown(topLayout, resId, getDecStyleCallback, Global
                        .STYLE_POSITION);
                break;
            case DEC_FEE:
                mSelectPopupWindowUtil.showAsDropDown(topLayout, resId, getDecFeeCallback, Global
                        .DEC_FEE_POSITION);
                break;
            default:
                break;
        }
    }

    private GetItemCallback getDecTypeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.DEC_TYPE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decType_item.setText(title);
            } else {
                decType_item.setText(getResources().getString(R.string.dec_type_str));
            }
            FROM = 0;
            decType = BusinessCovertUtil.getDecTypeByText(title);
            searchDesigners(FROM, DesignerListActivity.this.pullDownCallback);
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
        }
    };

    private GetItemCallback getDecHouseTypeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.DEC_HOUSE_TYPE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decHouseType_item.setText(title);
            } else {
                decHouseType_item.setText(getResources().getString(R.string.dec_house_type_str));
            }
            decHouseStyle = BusinessCovertUtil.getHouseTypeByText(title);
            FROM = 0;
            searchDesigners(FROM, DesignerListActivity.this.pullDownCallback);
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
        }
    };

    private GetItemCallback getDecStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.STYLE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decStyle_item.setText(title);
            } else {
                decStyle_item.setText(getResources().getString(R.string.dec_style_str));
            }
            decStyle = BusinessCovertUtil.getDecStyleByText(title);
            FROM = 0;
            searchDesigners(FROM, DesignerListActivity.this.pullDownCallback);
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
        }
    };

    private GetItemCallback getDecFeeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.DEC_FEE_POSITION = position;
            if (!TextUtils.isEmpty(title)) {
                if (!title.equals(Constant.KEY_WORD)) {
                    decFeeList = title;
                    decFee_item.setText(title);
                } else {
                    decFeeList = null;
                    decFee_item.setText(getResources().getString(R.string.dec_grade));
                }
            }
            FROM = 0;
            searchDesigners(FROM, DesignerListActivity.this.pullDownCallback);
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.DEC_TYPE_POSITION = 0;
        Global.DEC_HOUSE_TYPE_POSITION = 0;
        Global.STYLE_POSITION = 0;
        Global.DEC_FEE_POSITION = 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_list;
    }
}
