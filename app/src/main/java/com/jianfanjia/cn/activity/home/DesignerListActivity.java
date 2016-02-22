package com.jianfanjia.cn.activity.home;

import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerListAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.GetItemCallback;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.FilterPopWindow;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:海量设计师列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerListActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerListActivity.class.getName();
    private static final int DEC_TYPE = 1;
    private static final int DEC_HOUSE_TYPE = 2;
    private static final int DEC_STYLE = 3;
    private static final int DEC_FEE = 4;
    private static final int NOT = 5;
    private MainHeadView mainHeadView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private LinearLayout topLayout = null;
    private RelativeLayout decTypeLayout = null;
    private RelativeLayout decHouseTypeLayout = null;
    private RelativeLayout decStyleLayout = null;
    private RelativeLayout decFeeLayout = null;
    private TextView decType_item = null;
    private TextView decHouseType_item = null;
    private TextView decStyle_item = null;
    private TextView decFee_item = null;
    private PullToRefreshRecycleView designerListView = null;
    private DesignerListAdapter designerListAdapter = null;
    private List<DesignerInfo> designerList = new ArrayList<>();
    private FilterPopWindow window = null;
    private String decType = null;
    private String decHouseStyle = null;
    private String decStyle = null;
    private String decFee = null;

    private int FROM = 0;

    @Override
    public void initView() {
        initMainHeadView();
        emptyLayout = (RelativeLayout) findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) findViewById(R.id.error_include);
        topLayout = (LinearLayout) findViewById(R.id.topLayout);
        decTypeLayout = (RelativeLayout) findViewById(R.id.decTypeLayout);
        decHouseTypeLayout = (RelativeLayout) findViewById(R.id.dectHouseTypeLayout);
        decStyleLayout = (RelativeLayout) findViewById(R.id.decStyleLayout);
        decFeeLayout = (RelativeLayout) findViewById(R.id.decFeeLayout);
        decType_item = (TextView) findViewById(R.id.decType_item);
        decHouseType_item = (TextView) findViewById(R.id.decHouseType_item);
        decStyle_item = (TextView) findViewById(R.id.decStyle_item);
        decFee_item = (TextView) findViewById(R.id.decFee_item);
        designerListView = (PullToRefreshRecycleView) findViewById(R.id.designer_listview);
        designerListView.setMode(PullToRefreshBase.Mode.BOTH);
        designerListView.setLayoutManager(new LinearLayoutManager(DesignerListActivity.this));
        designerListView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        designerListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(DesignerListActivity.this).paint(paint).showLastDivider().build());
        searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullDownListener);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.designer_head);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.all_designer));
        mainHeadView.setBackgroundTransparent();
    }

    @Override
    public void setListener() {
        decTypeLayout.setOnClickListener(this);
        decHouseTypeLayout.setOnClickListener(this);
        decStyleLayout.setOnClickListener(this);
        decFeeLayout.setOnClickListener(this);
        designerListView.setOnRefreshListener(this);
    }

    @Override
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
                showWindow(R.array.arr_fee, DEC_FEE);
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

    private void searchDesigners(String decType, String decHouseType, String decStyle, String decFee, int from, ApiUiUpdateListener listener) {
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("dec_types", decType);
        conditionParam.put("dec_house_types", decHouseType);
        conditionParam.put("dec_styles", decStyle);
        conditionParam.put("design_fee_range", decFee);
        Map<String, Object> param = new HashMap<>();
        param.put("query", conditionParam);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesigner(new SearchDesignerRequest(DesignerListActivity.this, param), listener, this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullUpListener);
    }

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            designerListView.onRefreshComplete();
            MyFavoriteDesigner designer = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            LogTool.d(TAG, "designer:" + designer);
            if (null != designer) {
                designerList.clear();
                designerList.addAll(designer.getDesigners());
                if (null == designerListAdapter) {
                    designerListAdapter = new DesignerListAdapter(DesignerListActivity.this, designerList);
                    designerListView.setAdapter(designerListAdapter);
                } else {
                    designerListAdapter.notifyDataSetChanged();
                }
                FROM = designerList.size();
                LogTool.d(TAG, "FROM:" + FROM);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            designerListView.onRefreshComplete();
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            designerListView.onRefreshComplete();
            MyFavoriteDesigner designer = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            LogTool.d(TAG, "designer:" + designer);
            if (null != designer) {
                List<DesignerInfo> designers = designer.getDesigners();
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
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            designerListView.onRefreshComplete();
        }
    };

    private void showWindow(int resId, int type) {
        switch (type) {
            case DEC_TYPE:
                window = new FilterPopWindow(DesignerListActivity.this, resId, getDecTypeCallback, Global.DEC_TYPE_POSITION);
                break;
            case DEC_HOUSE_TYPE:
                window = new FilterPopWindow(DesignerListActivity.this, resId, getDecHouseTypeCallback, Global.DEC_HOUSE_TYPE_POSITION);
                break;
            case DEC_STYLE:
                window = new FilterPopWindow(DesignerListActivity.this, resId, getDecStyleCallback, Global.STYLE_POSITION);
                break;
            case DEC_FEE:
                window = new FilterPopWindow(DesignerListActivity.this, resId, getDecFeeCallback, Global.DEC_FEE_POSITION);
                break;
            default:
                break;
        }
        window.show(topLayout);
    }

    private GetItemCallback getDecTypeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.DEC_TYPE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decType_item.setText(title);
            } else {
                decType_item.setText(getResources().getString(R.string.dec_type_str));
            }
            FROM = 0;
            decType = BusinessManager.getDecTypeByText(title);
            searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };

    private GetItemCallback getDecHouseTypeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.DEC_HOUSE_TYPE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decHouseType_item.setText(title);
            } else {
                decHouseType_item.setText(getResources().getString(R.string.dec_house_type_str));
            }
            decHouseStyle = BusinessManager.getHouseTypeByText(title);
            FROM = 0;
            searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };

    private GetItemCallback getDecStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.STYLE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decStyle_item.setText(title);
            } else {
                decStyle_item.setText(getResources().getString(R.string.dec_style_str));
            }
            decStyle = BusinessManager.getDecStyleByText(title);
            FROM = 0;
            searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };

    private GetItemCallback getDecFeeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.DEC_FEE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decFee_item.setText(title);
            } else {
                decFee_item.setText(getResources().getString(R.string.dec_fee_str));
            }
            decFee = BusinessManager.getDecFeeByText(title);
            FROM = 0;
            searchDesigners(decType, decHouseStyle, decStyle, decFee, FROM, pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_list;
    }
}
