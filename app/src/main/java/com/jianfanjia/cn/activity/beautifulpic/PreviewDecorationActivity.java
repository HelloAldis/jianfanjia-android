package com.jianfanjia.cn.activity.beautifulpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PreviewAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.Img;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.SharePopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:预览装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDecorationActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = PreviewDecorationActivity.class.getName();
    private Toolbar toolbar = null;
    private ImageButton toolbar_collect = null;
    private ImageButton toolbar_share = null;
    private ImageButton btn_download = null;
    private ViewPager viewPager = null;
    private TextView pic_tip = null;
    private TextView pic_title = null;
    private TextView pic_des = null;
    private String decorationId = null;
    private List<String> imgList = new ArrayList<String>();
    private int totalCount = 0;
    private int currentPosition = 0;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle decorationBundle = intent.getExtras();
        decorationId = decorationBundle.getString(Global.DECORATION_ID);
        LogTool.d(TAG, "decorationId=" + decorationId);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_collect = (ImageButton) findViewById(R.id.toolbar_collect);
        toolbar_share = (ImageButton) findViewById(R.id.toolbar_share);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.showpicPager);
        pic_tip = (TextView) findViewById(R.id.pic_tip);
        pic_title = (TextView) findViewById(R.id.pic_title);
        pic_des = (TextView) findViewById(R.id.pic_des);
        btn_download = (ImageButton) findViewById(R.id.btn_download);
        getDecorationImgInfo(decorationId);
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_collect.setOnClickListener(this);
        toolbar_share.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_collect:
                if (toolbar_collect.isSelected()) {
                    deleteDecorationImg(decorationId);
                } else {
                    addDecorationImgInfo(decorationId);
                }
                break;
            case R.id.toolbar_share:
                showPopwindow(getWindow().getDecorView());
                break;
            case R.id.btn_download:

                break;
            default:
                break;
        }
    }

    private void getDecorationImgInfo(String decorationId) {
        JianFanJiaClient.getDecorationImgInfo(PreviewDecorationActivity.this, decorationId, getDecorationImgInfoListener, this);
    }

    private void addDecorationImgInfo(String decorationId) {
        JianFanJiaClient.addBeautyImgByUser(PreviewDecorationActivity.this, decorationId, AddDecorationImgInfoListener, this);
    }

    private void deleteDecorationImg(String decorationId) {
        JianFanJiaClient.deleteBeautyImgByUser(PreviewDecorationActivity.this, decorationId, deleteDecorationImgListener, this);
    }

    private ApiUiUpdateListener getDecorationImgInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            BeautyImgInfo beautyImgInfo = JsonParser.jsonToBean(data.toString(), BeautyImgInfo.class);
            LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
            if (null != beautyImgInfo) {
                if (beautyImgInfo.is_my_favorite()) {
                    toolbar_collect.setSelected(true);
                } else {
                    toolbar_collect.setSelected(false);
                }
                pic_title.setText(beautyImgInfo.getTitle());
                pic_des.setText("#" + beautyImgInfo.getDescription() + "  #" + getHouseType(beautyImgInfo.getHouse_type()) + "  #" + getDecStyle(beautyImgInfo.getDec_type()));
                List<Img> decorationImgs = beautyImgInfo.getImages();
                totalCount = decorationImgs.size();
                for (Img img : decorationImgs) {
                    imgList.add(img.getImageid());
                }
                pic_tip.setText((currentPosition + 1) + "/" + totalCount);
                PreviewAdapter adapter = new PreviewAdapter(PreviewDecorationActivity.this, imgList, new ViewPagerClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        LogTool.d(TAG, "pos:" + pos);
                    }
                });
                viewPager.setAdapter(adapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener AddDecorationImgInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            toolbar_collect.setSelected(true);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener deleteDecorationImgListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            toolbar_collect.setSelected(false);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        currentPosition = arg0;
        pic_tip.setText((currentPosition + 1) + "/" + totalCount);
    }


    private void showPopwindow(View view) {
        SharePopWindow window = new SharePopWindow(PreviewDecorationActivity.this, new PopWindowCallBack() {
            @Override
            public void firstItemClick() {

            }

            @Override
            public void secondItemClick() {

            }
        });
        window.show(view);
    }


    private void downloadImg(String url, String filePath,
                             String fileName) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_decoration;
    }
}
