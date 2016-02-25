package com.jianfanjia.cn.activity.beautifulpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.PreImgPagerAdapter;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDecorationImgRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ShareUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshViewPager;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Description:预览装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDecorationActivity extends SwipeBackActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, PullToRefreshBase.OnRefreshListener<ViewPager> {
    private static final String TAG = PreviewDecorationActivity.class.getName();
    private ShareUtil shareUtil = null;
    private Toolbar toolbar = null;
    private ImageView toolbar_collect = null;
    private ImageView toolbar_share = null;
    private ImageView btn_download = null;
    private RelativeLayout toolbar_collectLayout = null;
    private RelativeLayout toolbar_shareLayout = null;
    private RelativeLayout btn_downloadLayout = null;
    private boolean isFirst = true;
    private PullToRefreshViewPager mPullToRefreshViewPager = null;
    private ViewPager imgViewPager = null;
    private TextView pic_tip = null;
    private TextView pic_title = null;
    private TextView pic_des = null;
    private String decorationId = null;
    private List<BeautyImgInfo> beautiful_images = new ArrayList<>();
    private List<String> imgIdList = new ArrayList<>();
    private List<String> imgList = new ArrayList<>();
    private PreImgPagerAdapter showPicPagerAdapter = null;
    private int viewType = -1;
    private String section = null;
    private String houseStyle = null;
    private String decStyle = null;
    private int currentPosition = 0;
    private String picTitle = null;
    private String currentImgId = null;
    private String currentStyle = null;
    private String currentTag = null;
    private int totalCount = 0;
    private int FROM = 0;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle decorationBundle = intent.getExtras();
        viewType = decorationBundle.getInt(Global.VIEW_TYPE, 0);
        LogTool.d(TAG, "viewType==" + viewType);
        decorationId = decorationBundle.getString(Global.DECORATION_ID);
        currentPosition = decorationBundle.getInt(Global.POSITION, 0);
        totalCount = decorationBundle.getInt(Global.TOTAL_COUNT, 0);
        beautiful_images = (List<BeautyImgInfo>) decorationBundle.getSerializable(Global.IMG_LIST);
        section = decorationBundle.getString(Global.HOUSE_SECTION);
        houseStyle = decorationBundle.getString(Global.HOUSE_STYLE);
        decStyle = decorationBundle.getString(Global.DEC_STYLE);
        LogTool.d(TAG, "section:" + section + " houseStyle:" + houseStyle + " decStyle:" + decStyle);
        shareUtil = new ShareUtil(this);
        LogTool.d(TAG, "decorationId=" + decorationId + " currentPosition=" + currentPosition + "  totalCount=" + totalCount + "  beautiful_images.size()=" + beautiful_images.size());
        FROM = beautiful_images.size();
        LogTool.d(TAG, "FROM:" + FROM);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_collect = (ImageView) findViewById(R.id.toolbar_collect);
        toolbar_share = (ImageView) findViewById(R.id.toolbar_share);
        btn_download = (ImageView) findViewById(R.id.btn_download);
        toolbar_collectLayout = (RelativeLayout) findViewById(R.id.toolbar_collect_layout);
        toolbar_shareLayout = (RelativeLayout) findViewById(R.id.toolbar_share_layout);
        btn_downloadLayout = (RelativeLayout) findViewById(R.id.btn_download_layout);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPullToRefreshViewPager = (PullToRefreshViewPager) findViewById(R.id.showpicPager);
        imgViewPager = mPullToRefreshViewPager.getRefreshableView();
        pic_tip = (TextView) findViewById(R.id.pic_tip);
        pic_title = (TextView) findViewById(R.id.pic_title);
        pic_des = (TextView) findViewById(R.id.pic_des);
        btn_downloadLayout.setVisibility(View.VISIBLE);
        toolbar_collectLayout.setVisibility(View.VISIBLE);
        toolbar_shareLayout.setVisibility(View.VISIBLE);
        initViewPager(beautiful_images);
    }

    private void initViewPager(List<BeautyImgInfo> beautyImagesList) {
        showPicPagerAdapter = new PreImgPagerAdapter(PreviewDecorationActivity.this, beautyImagesList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {

            }
        });
        imgViewPager.setAdapter(showPicPagerAdapter);
        imgViewPager.setCurrentItem(currentPosition);
        setPreviewImgInfo(currentPosition);
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(PreviewDecorationActivity.this);
            }
        });
        toolbar_shareLayout.setOnClickListener(this);
        toolbar_collectLayout.setOnClickListener(this);
        btn_downloadLayout.setOnClickListener(this);
        mPullToRefreshViewPager.setOnRefreshListener(this);
        imgViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_collect_layout:
                UiHelper.imageButtonAnim(toolbar_collect, null);
                if (toolbar_collect.isSelected()) {
                    deleteDecorationImg(decorationId);
                } else {
                    addDecorationImgInfo(decorationId);
                }
                break;
            case R.id.toolbar_share_layout:
                UiHelper.imageButtonAnim(toolbar_share, null);
                showPopwindow();
                break;
            case R.id.btn_download_layout:
                UiHelper.imageButtonAnim(btn_download, null);
                downloadImg();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ViewPager> refreshView) {
        isFirst = false;
        switch (viewType) {
            case Constant.BEAUTY_FRAGMENT:
                getDecorationImgInfo(FROM);
                break;
            case Constant.COLLECT_BEAUTY_FRAGMENT:
                getCollectedDecorationImgInfo(FROM, Constant.HOME_PAGE_LIMIT);
                break;
            case Constant.SEARCH_BEAUTY_FRAGMENT:
                break;
            default:
                break;
        }
    }

    private void getDecorationImgInfo(int from) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("section", section);
        conditionParam.put("house_type", houseStyle);
        conditionParam.put("dec_style", decStyle);
        param.put("query", conditionParam);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDecorationImg(new SearchDecorationImgRequest(PreviewDecorationActivity.this, param), getDecorationImgInfoListener, this);
    }

    private void getCollectedDecorationImgInfo(int from, int limit) {
        JianFanJiaClient.getBeautyImgListByUser(PreviewDecorationActivity.this, from, limit, getDecorationImgInfoListener, this);
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
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            hideWaitDialog();
            mPullToRefreshViewPager.onRefreshComplete();
            LogTool.d(TAG, "data:" + data.toString());
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                List<BeautyImgInfo> beautyImages = decorationItemInfo.getBeautiful_images();
                LogTool.d(TAG, "beautyImages=" + beautyImages.size());
                if (null != beautyImages && beautyImages.size() > 0) {
                    showPicPagerAdapter.addItem(beautyImages);
                    FROM += Constant.HOME_PAGE_LIMIT;
//                    EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_BEAUTY_IMG_FRAGMENT));
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            hideWaitDialog();
            makeTextShort(error_msg);
            mPullToRefreshViewPager.onRefreshComplete();
        }
    };

    private ApiUiUpdateListener AddDecorationImgInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            makeTextShort(getString(R.string.str_collect_success));
            toolbar_collect.setSelected(true);
            notifyChangeState(true);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_BEAUTY_FRAGMENT));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
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
            notifyChangeState(false);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_BEAUTY_FRAGMENT));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
        }
    };

    private void notifyChangeState(boolean isSelect) {
        BeautyImgInfo beautyImgInfo = showPicPagerAdapter.getBeautyImagesList().get(currentPosition);
        LogTool.d(TAG, "beautyImgInfo=====>" + beautyImgInfo.get_id());
        beautyImgInfo.setIs_my_favorite(isSelect);
        showPicPagerAdapter.notifyDataSetChanged();
    }

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
        LogTool.d(TAG, "currentPosition=" + currentPosition);
        setPreviewImgInfo(currentPosition);
    }

    private void setPreviewImgInfo(int position) {
        LogTool.d(TAG, "position===" + position);
        pic_tip.setText((position + 1) + "/" + totalCount);
        BeautyImgInfo beautyImgInfo = beautiful_images.get(position);
        currentImgId = beautyImgInfo.getImages().get(0).getImageid();
        LogTool.d(TAG, "  currentImgId=" + currentImgId);
        picTitle = beautyImgInfo.getTitle();
        currentStyle = beautyImgInfo.getDec_style();
        currentTag = beautyImgInfo.getSection();
        LogTool.d(TAG, "picTitle:" + picTitle + " currentStyle:" + currentStyle + " currentTag:" + currentTag);
        decorationId = beautyImgInfo.get_id();
        if (beautyImgInfo.is_my_favorite()) {
            toolbar_collect.setSelected(true);
        } else {
            toolbar_collect.setSelected(false);
        }
    }

    private void showPopwindow() {
        shareUtil.shareImage(this, picTitle, currentStyle, currentTag, currentImgId, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                LogTool.d(TAG, "status =" + i);
            }
        });
    }

    private void downloadImg() {
        View view = imgViewPager.getChildAt(0).findViewById(R.id.viewPagerLayout);
        ImageView photoView = (ImageView) view.findViewById(R.id.image_item);
        try {
            boolean isSuccess = ImageUtil.snapshot(this, photoView, 100);
            if (isSuccess) {
                makeTextShort(getResources().getString(R.string.save_image_success));
            } else {
                makeTextShort(getResources().getString(R.string.save_image_failure));
            }
        } catch (Exception e) {
            makeTextShort(getResources().getString(R.string.save_image_failure));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must icon_add this**/
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_decoration;
    }
}
