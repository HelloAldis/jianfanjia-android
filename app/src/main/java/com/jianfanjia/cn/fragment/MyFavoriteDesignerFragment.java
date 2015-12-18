package com.jianfanjia.cn.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.activity.DesignerInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.FavoriteDesignerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: MyFavoriteDesignerFragment
 * @Description: 我的意向设计师
 * @date 2015-8-26 下午1:07:52
 */
public class MyFavoriteDesignerFragment extends BaseFragment {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView my_favorite_designer_listview = null;
    private FavoriteDesignerAdapter designAdapter = null;
    private MyFavoriteDesigner myFavoriteDesigner = null;
    private List<DesignerInfo> designers = new ArrayList<DesignerInfo>();

    @Override
    public void initView(View view) {
        my_favorite_designer_listview = (RecyclerView) view.findViewById(R.id.my_favorite_designer_listview);
        my_favorite_designer_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_favorite_designer_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        my_favorite_designer_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        getMyFavoriteDesignerList();
    }

    @Override
    public void setListener() {

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
                designAdapter = new FavoriteDesignerAdapter(getActivity(), designers, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        String designerId = myFavoriteDesigner.getDesigners().get(position).get_id();
                        LogTool.d(this.getClass().getName(), designerId);
                        Bundle designerBundle = new Bundle();
                        designerBundle.putString(Global.DESIGNER_ID, designerId);
                        startActivity(DesignerInfoActivity.class, designerBundle);
                    }

                    @Override
                    public void OnViewClick(int position) {

                    }
                });
                my_favorite_designer_listview.setAdapter(designAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_favorite_designer;
    }


}
