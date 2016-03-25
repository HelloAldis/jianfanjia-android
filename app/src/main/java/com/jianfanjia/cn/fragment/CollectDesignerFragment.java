package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.FavoriteDesignerAdapter;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * @author fengliang
 * @ClassName: CollectDesignerFragment
 * @Description: 我的意向设计师
 * @date 2015-8-26 下午1:07:52
 */
public class CollectDesignerFragment extends BaseAnnotationFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView>, View.OnClickListener {
    private static final String TAG = CollectDesignerFragment.class.getName();
    private PullToRefreshRecycleView my_favorite_designer_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private FavoriteDesignerAdapter designAdapter = null;
    private MyFavoriteDesigner myFavoriteDesigner = null;
    private List<DesignerInfo> designers = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int FROM = 0;
    private int currentPos = -1;

    public static CollectDesignerFragment newInstance() {
        CollectDesignerFragment designerFragment = new CollectDesignerFragment();
        return designerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_designer, container, false);
        init(view);
        isPrepared = true;
        load();
        return view;
    }

    public void init(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.emtpy_view_no_designer_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        my_favorite_designer_listview = (PullToRefreshRecycleView) view.findViewById(R.id
                .my_favorite_designer_listview);
        my_favorite_designer_listview.setMode(PullToRefreshBase.Mode.BOTH);
        my_favorite_designer_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_favorite_designer_listview.setHasFixedSize(true);
        my_favorite_designer_listview.setItemAnimator(new DefaultItemAnimator());
        my_favorite_designer_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
    }

    public void setListener() {
        errorLayout.setOnClickListener(this);
        my_favorite_designer_listview.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
                break;
            default:
                break;
        }
    }

    @Override
    protected void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getUpMyFavoriteDesignerListener);
    }

    private void getMyFavoriteDesignerList(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.get_MyFavoriteDesignerList(getActivity(), from, limit, listener, this);
    }

    private ApiUiUpdateListener getDownMyFavoriteDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            hideWaitDialog();
            mHasLoadedOnce = true;
            my_favorite_designer_listview.onRefreshComplete();
            myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
            if (myFavoriteDesigner != null) {
                designers.clear();
                designers.addAll(myFavoriteDesigner.getDesigners());
                if (null != designers && designers.size() > 0) {
                    designAdapter = new FavoriteDesignerAdapter(getActivity(), designers, new
                            RecyclerViewOnItemClickListener() {
                                @Override
                                public void OnItemClick(View view, int position) {
                                    LogTool.d(TAG, "position=" + position);
                                    currentPos = position;
                                    LogTool.d(TAG, "currentPos========" + currentPos);
                                    String designerId = designers.get(currentPos).get_id();
                                    LogTool.d(TAG, "designerId:" + designerId);
                                    Bundle designerBundle = new Bundle();
                                    designerBundle.putString(Global.DESIGNER_ID, designerId);
                                    startActivity(DesignerInfoActivity.class, designerBundle);
                                }

                                @Override
                                public void OnViewClick(int position) {

                                }
                            });
                    my_favorite_designer_listview.setAdapter(designAdapter);
                    my_favorite_designer_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    isFirst = false;
                } else {
                    my_favorite_designer_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
                FROM = designers.size();
                LogTool.d(TAG, "FROM:" + FROM);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
            my_favorite_designer_listview.onRefreshComplete();
            my_favorite_designer_listview.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    };

    private ApiUiUpdateListener getUpMyFavoriteDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            my_favorite_designer_listview.onRefreshComplete();
            myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
            if (myFavoriteDesigner != null) {
                List<DesignerInfo> designerList = myFavoriteDesigner.getDesigners();
                if (null != designerList && designerList.size() > 0) {
                    designAdapter.add(FROM, designerList);
                    FROM += Constant.HOME_PAGE_LIMIT;
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            my_favorite_designer_listview.onRefreshComplete();
        }
    };

    public void onEventMainThread(MessageEvent event) {
        LogTool.d(TAG, "event:" + event.getEventType());
        switch (event.getEventType()) {
            case Constant.DELETE_FAVORITE_DESIGNER_FRAGMENT:
                designAdapter.remove(currentPos);
                if (designers.size() == 0) {
                    my_favorite_designer_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
