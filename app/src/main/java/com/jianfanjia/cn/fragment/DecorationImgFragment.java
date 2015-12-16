package com.jianfanjia.cn.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;

/**
 * @author fengliang
 * @ClassName: DecorationImgFragment
 * @Description: 装修美图
 * @date 2015-8-26 下午1:07:52
 */
public class DecorationImgFragment extends BaseFragment {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView decoration_img_listview = null;

    @Override
    public void initView(View view) {
        decoration_img_listview = (RecyclerView) view.findViewById(R.id.decoration_img_listview);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        decoration_img_listview.addItemDecoration(decoration);
        getDecorationImgList(0, 8, getDecorationImgListListener);
    }

    @Override
    public void setListener() {

    }


    private void getDecorationImgList(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.getBeautyImgListByUser(getActivity(), from, limit, listener, this);
    }

    private ApiUiUpdateListener getDecorationImgListListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration_img;
    }

}
