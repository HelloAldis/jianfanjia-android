package com.jianfanjia.cn.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.FavoriteDesignerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @author fengliang
 * @ClassName: MyFavoriteDesignerFragment
 * @Description: 我的意向设计师
 * @date 2015-8-26 下午1:07:52
 */
public class MyFavoriteDesignerFragment extends BaseFragment implements ApiUiUpdateListener {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView my_favorite_designer_listview = null;

    @Override
    public void initView(View view) {
        my_favorite_designer_listview = (RecyclerView) view.findViewById(R.id.my_favorite_designer_listview);
        my_favorite_designer_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_favorite_designer_listview.setItemAnimator(new DefaultItemAnimator());
        JianFanJiaClient.get_MyFavoriteDesignerList(getActivity(), 0, 100, this, this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data=" + data.toString());
        MyFavoriteDesigner myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
        LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
        if (myFavoriteDesigner != null) {
            FavoriteDesignerAdapter adapter = new FavoriteDesignerAdapter(getActivity(), myFavoriteDesigner.getDesigners());
            my_favorite_designer_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_favorite_designer;
    }


}
