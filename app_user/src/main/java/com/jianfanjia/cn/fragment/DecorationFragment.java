package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageList;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.GetItemCallback;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.view.FilterPopWindow;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import de.greenrobot.event.EventBus;

/**
 * Description:装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DecorationFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = DecorationFragment.class.getName();
    private static final int SECTION = 1;
    private static final int HOUSETYPE = 2;
    private static final int DECSTYLE = 3;
    private static final int NOT = 4;

    @Bind(R.id.dec_head)
    MainHeadView mainHeadView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    @Bind(R.id.topLayout)
    LinearLayout topLayout;

    @Bind(R.id.sectionLayout)
    RelativeLayout sectionLayout;

    @Bind(R.id.houseTypeLayout)
    RelativeLayout houseTypeLayout;

    @Bind(R.id.decStyleLayout)
    RelativeLayout decStyleLayout;

    @Bind(R.id.section_item)
    TextView section_item;

    @Bind(R.id.houseType_item)
    TextView houseType_item;

    @Bind(R.id.decStyle_item)
    TextView decStyle_item;

    @Bind(R.id.decoration_listview)
    PullToRefreshRecycleView decoration_listview;

    private DecorationAdapter decorationAdapter = null;
    private FilterPopWindow window = null;
    private List<BeautifulImage> beautyImgList = new ArrayList<>();
    private String section = null;
    private String houseStyle = null;
    private String decStyle = null;
    private int FROM = 0;
    private boolean isFirst = true;
    private int total = 0;

    private int currentPos = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        initMainHeadView();
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.error_view_no_img_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_img);
        decoration_listview.setMode(PullToRefreshBase.Mode.BOTH);
        decoration_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_listview.setHasFixedSize(true);
        decoration_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(TDevice.dip2px(getContext()
                .getApplicationContext(), 5));
        decoration_listview.addItemDecoration(decoration);
        decoration_listview.setOnRefreshListener(this);
        getDecorationImgInfo(pullDownListener);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.decoration_img));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @OnClick({R.id.sectionLayout, R.id.houseTypeLayout, R.id.decStyleLayout, R.id.error_include})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sectionLayout:
                setSelectState(SECTION);
                break;
            case R.id.houseTypeLayout:
                setSelectState(HOUSETYPE);
                break;
            case R.id.decStyleLayout:
                setSelectState(DECSTYLE);
                break;
            case R.id.error_include:
                getDecorationImgInfo(pullDownListener);
                break;
            default:
                break;
        }
    }

    private void setSelectState(int type) {
        switch (type) {
            case SECTION:
                showWindow(R.array.arr_section, SECTION);
                sectionLayout.setSelected(true);
                houseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
                break;
            case HOUSETYPE:
                showWindow(R.array.arr_housetype, HOUSETYPE);
                sectionLayout.setSelected(false);
                houseTypeLayout.setSelected(true);
                decStyleLayout.setSelected(false);
                break;
            case DECSTYLE:
                showWindow(R.array.arr_decstyle, DECSTYLE);
                sectionLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(true);
                break;
            case NOT:
                sectionLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
            default:
                break;
        }
    }

    private void getDecorationImgInfo(ApiCallback<ApiResponse<BeautifulImageList>> listener) {
        SearchDecorationImgRequest request = new SearchDecorationImgRequest();
        Map<String, Object> param = new HashMap<>();
        param.put("section", section);
        param.put("house_type", houseStyle);
        param.put("dec_style", decStyle);
        request.setQuery(param);
        request.setFrom(FROM);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchDecorationImg(request, listener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getDecorationImgInfo(pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDecorationImgInfo(pullUpListener);
    }

    private ApiCallback<ApiResponse<BeautifulImageList>> pullDownListener = new
            ApiCallback<ApiResponse<BeautifulImageList>>() {

                @Override
                public void onPreLoad() {
                    if (isFirst) {
                        showWaitDialog();
                    }
                }

                @Override
                public void onHttpDone() {
                    hideWaitDialog();
                }

                @Override
                public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
                    decoration_listview.onRefreshComplete();
                    BeautifulImageList decorationItemInfo = apiResponse.getData();
                    LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
                    if (null != decorationItemInfo) {
                        total = decorationItemInfo.getTotal();
                        LogTool.d(TAG, "total:" + total);
                        beautyImgList.clear();
                        beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                        LogTool.d(TAG, "beautyImgList=" + beautyImgList);
                        if (null != beautyImgList && beautyImgList.size() > 0) {
                            if (null == decorationAdapter) {
                                LogTool.d(TAG, "decorationAdapter is null");
                                decorationAdapter = new DecorationAdapter(getActivity(), beautyImgList, new
                                        OnItemClickListener() {
                                            @Override
                                            public void OnItemClick(int position) {
                                                LogTool.d(TAG, "position=" + position);
                                                currentPos = position;
                                                LogTool.d(TAG, "currentPos-----" + currentPos);
                                                BeautifulImage beautyImgInfo = beautyImgList.get(currentPos);
                                                LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
                                                Bundle decorationBundle = new Bundle();
                                                decorationBundle.putString(Global.DECORATION_ID, beautyImgInfo.get_id
                                                        ());
                                                decorationBundle.putInt(Global.POSITION, position);
                                                decorationBundle.putSerializable(Global.IMG_LIST,
                                                        (ArrayList<BeautifulImage>)
                                                                beautyImgList);
                                                decorationBundle.putString(Global.HOUSE_SECTION, section);
                                                decorationBundle.putString(Global.HOUSE_STYLE, houseStyle);
                                                decorationBundle.putString(Global.DEC_STYLE, decStyle);
                                                decorationBundle.putInt(Global.TOTAL_COUNT, total);
                                                decorationBundle.putInt(Global.VIEW_TYPE, Constant.BEAUTY_FRAGMENT);
                                                startActivity(PreviewDecorationActivity.class, decorationBundle);
                                            }
                                        });
                                decoration_listview.setAdapter(decorationAdapter);
                            } else {
                                LogTool.d(TAG, "decorationAdapter is not null");
                                decoration_listview.scrollToPosition(0);
                                decorationAdapter.notifyDataSetChanged();
                            }
                            decoration_listview.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                            isFirst = false;
                        } else {
                            decoration_listview.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                        }
                        errorLayout.setVisibility(View.GONE);
                        FROM = beautyImgList.size();
                        LogTool.d(TAG, "FROM:" + FROM);
                    }
                }

                @Override
                public void onFailed(ApiResponse<BeautifulImageList> apiResponse) {
                    decoration_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    decoration_listview.onRefreshComplete();
                }

                @Override
                public void onNetworkError(int code) {

                }

            };

    private ApiCallback<ApiResponse<BeautifulImageList>> pullUpListener = new
            ApiCallback<ApiResponse<BeautifulImageList>>() {


                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {

                }

                @Override
                public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
                    decoration_listview.onRefreshComplete();
                    BeautifulImageList decorationItemInfo = apiResponse.getData();
                    LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
                    if (null != decorationItemInfo) {
                        List<BeautifulImage> beautyImgs = decorationItemInfo.getBeautiful_images();
                        LogTool.d(TAG, "beautyImgs=" + beautyImgs);
                        if (null != beautyImgs && beautyImgs.size() > 0) {
                            decorationAdapter.add(FROM, beautyImgs);
                            FROM += Constant.HOME_PAGE_LIMIT;
                            LogTool.d(TAG, "FROM=" + FROM);
                        } else {
                            makeTextShort(getResources().getString(R.string.no_more_data));
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<BeautifulImageList> apiResponse) {
                    decoration_listview.onRefreshComplete();
                }

                @Override
                public void onNetworkError(int code) {

                }
            };

    private void showWindow(int resId, int type) {
        switch (type) {
            case SECTION:
                window = new FilterPopWindow(getActivity(), resId, getSectionCallback, Global.SECTION_POSITION);
                break;
            case HOUSETYPE:
                window = new FilterPopWindow(getActivity(), resId, getHouseStyleCallback, Global.HOUSE_TYPE_POSITION);
                break;
            case DECSTYLE:
                window = new FilterPopWindow(getActivity(), resId, getDecStyleCallback, Global.DEC_STYLE_POSITION);
                break;
            default:
                break;
        }
        window.show(topLayout);
    }

    private GetItemCallback getSectionCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.SECTION_POSITION = position;
            isFirst = true;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                section = title;
                section_item.setText(title);
            } else {
                section = null;
                section_item.setText(getResources().getString(R.string.dec_section_str));
            }
            FROM = 0;
            getDecorationImgInfo(pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };
    private GetItemCallback getHouseStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.HOUSE_TYPE_POSITION = position;
            isFirst = true;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                houseType_item.setText(title);
            } else {
                houseType_item.setText(getResources().getString(R.string.dec_house_type_str));
            }
            houseStyle = BusinessCovertUtil.getHouseTypeByText(title);
            FROM = 0;
            getDecorationImgInfo(pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };
    private GetItemCallback getDecStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.DEC_STYLE_POSITION = position;
            isFirst = true;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decStyle_item.setText(title);
            } else {
                decStyle_item.setText(getResources().getString(R.string.dec_style_str));
            }
            decStyle = BusinessCovertUtil.getDecStyleByText(title);
            FROM = 0;
            getDecorationImgInfo(pullDownListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };

    public void onEventMainThread(MessageEvent event) {
        LogTool.d(TAG, "event=" + event.getEventType());
        switch (event.getEventType()) {
            case Constant.UPDATE_BEAUTY_IMG_FRAGMENT:
                notifyChangeItemState(true);
                break;
            case Constant.UPDATE_BEAUTY_FRAGMENT:
                notifyChangeItemState(false);
                break;
            default:
                break;
        }
    }

    private void notifyChangeItemState(boolean isSelect) {
        BeautifulImage beautyImgInfo = decorationAdapter.getBeautyImgList().get(currentPos);
        beautyImgInfo.setIs_my_favorite(isSelect);
        decorationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration;
    }

}
