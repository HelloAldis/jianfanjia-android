package com.jianfanjia.cn.designer.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.designer.adapter.FavoriteDesignerAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.DesignerInfo;
import com.jianfanjia.cn.designer.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: MyFavoriteDesignerFragment
 * @Description: 我的意向设计师
 * @date 2015-8-26 下午1:07:52
 */
public class MyFavoriteDesignerFragment extends BaseFragment {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView my_favorite_designer_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private FavoriteDesignerAdapter designAdapter = null;
    private MyFavoriteDesigner myFavoriteDesigner = null;
    private List<DesignerInfo> designers = new ArrayList<DesignerInfo>();
    private int currentPos = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        my_favorite_designer_listview = (RecyclerView) view.findViewById(R.id.my_favorite_designer_listview);
        my_favorite_designer_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_favorite_designer_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        my_favorite_designer_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // fragment可见时加载数据
            LogTool.d(TAG, "MyFavoriteDesignerFragment 可见");
            getMyFavoriteDesignerList();
        } else {
            // 不可见时不执行操作
            LogTool.d(TAG, "MyFavoriteDesignerFragment 不可见");
        }
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getMyFavoriteDesignerList();
                break;
            default:
                break;
        }
    }

    private void getMyFavoriteDesignerList() {
        JianFanJiaClient.get_MyFavoriteDesignerList(getActivity(), 0, 100, getMyFavoriteDesignerListener, this);
    }

    private ApiUiUpdateListener getMyFavoriteDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
            if (myFavoriteDesigner != null) {
                designers = myFavoriteDesigner.getDesigners();
                if (null != designers && designers.size() > 0) {
                    designAdapter = new FavoriteDesignerAdapter(getActivity(), designers, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            LogTool.d(TAG, "position=" + position);
                            currentPos = position;
                            String designerId = myFavoriteDesigner.getDesigners().get(position).get_id();
                            LogTool.d(this.getClass().getName(), designerId);
                            Intent designerIntent = new Intent(getActivity(), DesignerInfoActivity.class);
                            Bundle designerBundle = new Bundle();
                            designerBundle.putString(Global.DESIGNER_ID, designerId);
                            designerIntent.putExtras(designerBundle);
                            startActivity(designerIntent);
                        }

                        @Override
                        public void OnViewClick(int position) {

                        }
                    });
                    my_favorite_designer_listview.setAdapter(designAdapter);
                    my_favorite_designer_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    my_favorite_designer_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            my_favorite_designer_listview.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    };

    public void onEventMainThread(MessageEvent event) {
        switch (event.getEventType()) {
            case Constant.UPDATE_FAVORITE_FRAGMENT:
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_favorite_designer;
    }


}