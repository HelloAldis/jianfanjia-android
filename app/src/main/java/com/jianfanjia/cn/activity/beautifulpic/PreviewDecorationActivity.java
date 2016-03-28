package com.jianfanjia.cn.activity.beautifulpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageList;
import com.jianfanjia.api.request.common.AddBeautyImgRequest;
import com.jianfanjia.api.request.common.DeleteBeautyImgRequest;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.PreImgPagerAdapter;

import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.ImageUtil;
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

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:预览装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDecorationActivity extends SwipeBackActivity implements ViewPager
        .OnPageChangeListener, PullToRefreshBase.OnRefreshListener<ViewPager> {
    private static final String TAG = PreviewDecorationActivity.class.getName();

    @Bind(R.id.showpicPager)
    PullToRefreshViewPager mPullToRefreshViewPager;

    @Bind(R.id.pic_tip)
    TextView picTip;

    @Bind(R.id.toolbar_collect)
    ImageView toolbarCollect;

    @Bind(R.id.toolbar_collect_layout)
    RelativeLayout toolbarCollectLayout;

    @Bind(R.id.toolbar_share)
    ImageView toolbarShare;

    @Bind(R.id.toolbar_share_layout)
    RelativeLayout toolbarShareLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.btn_download)
    ImageView btnDownload;

    @Bind(R.id.btn_download_layout)
    RelativeLayout btnDownloadLayout;

    private ShareUtil shareUtil = null;
    private boolean isFirst = true;
    private ViewPager imgViewPager = null;
    private String decorationId = null;
    private List<BeautifulImage> beautiful_images = new ArrayList<>();
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
    private String search = null;
    private int FROM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getDataFromIntent(getIntent());
        initData();
        setListener();
    }

    private void initView() {
        shareUtil = new ShareUtil(this);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgViewPager = mPullToRefreshViewPager.getRefreshableView();
        btnDownloadLayout.setVisibility(View.VISIBLE);
        toolbarCollectLayout.setVisibility(View.VISIBLE);
        toolbarShareLayout.setVisibility(View.VISIBLE);
    }

    private void getDataFromIntent(Intent intent) {
        Bundle decorationBundle = intent.getExtras();
        viewType = decorationBundle.getInt(Global.VIEW_TYPE, 0);
        LogTool.d(TAG, "viewType==" + viewType);
        search = decorationBundle.getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search==" + search);
        decorationId = decorationBundle.getString(Global.DECORATION_ID);
        currentPosition = decorationBundle.getInt(Global.POSITION, 0);
        totalCount = decorationBundle.getInt(Global.TOTAL_COUNT, 0);
        beautiful_images = (List<BeautifulImage>) decorationBundle.getSerializable(Global.IMG_LIST);
        section = decorationBundle.getString(Global.HOUSE_SECTION);
        houseStyle = decorationBundle.getString(Global.HOUSE_STYLE);
        decStyle = decorationBundle.getString(Global.DEC_STYLE);
        LogTool.d(TAG, "section:" + section + " houseStyle:" + houseStyle + " decStyle:" + decStyle);
        LogTool.d(TAG, "decorationId=" + decorationId + " currentPosition=" + currentPosition + "  totalCount=" +
                totalCount + "  beautiful_images.size()=" + beautiful_images.size());
        FROM = beautiful_images.size();
        LogTool.d(TAG, "FROM:" + FROM);
    }

    private void initData() {
        showPicPagerAdapter = new PreImgPagerAdapter(PreviewDecorationActivity.this, beautiful_images, new
                ViewPagerClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        appManager.finishActivity(PreviewDecorationActivity.this);
                    }
                });
        imgViewPager.setAdapter(showPicPagerAdapter);
        imgViewPager.setCurrentItem(currentPosition);
        setPreviewImgInfo(currentPosition);
    }

    public void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(PreviewDecorationActivity.this);
            }
        });
        mPullToRefreshViewPager.setOnRefreshListener(this);
        imgViewPager.setOnPageChangeListener(this);
    }

    @OnClick({R.id.toolbar_collect_layout, R.id.toolbar_share_layout, R.id.btn_download_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_collect_layout:
                UiHelper.imageButtonAnim(toolbarCollect, null);
                if (toolbarCollect.isSelected()) {
                    deleteDecorationImg(decorationId);
                } else {
                    addDecorationImgInfo(decorationId);
                }
                break;
            case R.id.toolbar_share_layout:
                UiHelper.imageButtonAnim(toolbarShare, null);
                showPopwindow();
                break;
            case R.id.btn_download_layout:
                UiHelper.imageButtonAnim(btnDownload, null);
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
                getDecorationImgInfo(FROM);
                break;
            default:
                break;
        }
    }

    private void getDecorationImgInfo(int from) {
        Map<String, Object> query = new HashMap<>();
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("section", section);
        conditionParam.put("house_type", houseStyle);
        conditionParam.put("dec_style", decStyle);
        query.put("query", conditionParam);
        query.put("search_word", search);
        query.put("from", from);
        query.put("limit", Constant.HOME_PAGE_LIMIT);

        SearchDecorationImgRequest request = new SearchDecorationImgRequest();
        request.setQuery(query);
        Api.searchDecorationImg(request, this.getDecorationImgInfoCallback);
    }

    private void getCollectedDecorationImgInfo(int from, int limit) {
        GetBeautyImgListRequest request = new GetBeautyImgListRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.getBeautyImgListByUser(request, this.getDecorationImgInfoCallback);
    }

    private void addDecorationImgInfo(String decorationId) {
        AddBeautyImgRequest request = new AddBeautyImgRequest();
        request.set_id(decorationId);

        Api.addBeautyImgByUser(request, this.addDecorationImgInfoCallback);
    }

    private void deleteDecorationImg(String decorationId) {
        DeleteBeautyImgRequest request = new DeleteBeautyImgRequest();
        request.set_id(decorationId);

        Api.deleteBeautyImgByUser(request, this.deleteDecorationImgCallback);
    }

    private ApiCallback<ApiResponse<BeautifulImageList>> getDecorationImgInfoCallback = new ApiCallback<ApiResponse<BeautifulImageList>>() {
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
            mPullToRefreshViewPager.onRefreshComplete();
            BeautifulImageList decorationItemInfo = apiResponse.getData();
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                List<BeautifulImage> beautyImages = decorationItemInfo.getBeautiful_images();
                LogTool.d(TAG, "beautyImages:" + beautyImages.size());
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
        public void onFailed(ApiResponse<BeautifulImageList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
            mPullToRefreshViewPager.onRefreshComplete();
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<Object>> addDecorationImgInfoCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            makeTextShort(getString(R.string.str_collect_success));
            toolbarCollect.setSelected(true);
            notifyChangeState(true);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_BEAUTY_IMG_FRAGMENT));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {

        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<Object>> deleteDecorationImgCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            toolbarCollect.setSelected(false);
            notifyChangeState(false);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_BEAUTY_FRAGMENT));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private void notifyChangeState(boolean isSelect) {
        BeautifulImage BeautifulImage = showPicPagerAdapter.getBeautyImagesList().get(currentPosition);
        LogTool.d(TAG, "BeautifulImage=" + BeautifulImage);
        BeautifulImage.setIs_my_favorite(isSelect);
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
        picTip.setText((position + 1) + "/" + totalCount);
        BeautifulImage BeautifulImage = beautiful_images.get(position);
        currentImgId = BeautifulImage.getImages().get(0).getImageid();
        LogTool.d(TAG, "  currentImgId=" + currentImgId);
        picTitle = BeautifulImage.getTitle();
        currentStyle = BeautifulImage.getDec_style();
        currentTag = BeautifulImage.getSection();
        LogTool.d(TAG, "picTitle:" + picTitle + " currentStyle:" + currentStyle + " currentTag:" + currentTag);
        decorationId = BeautifulImage.get_id();
        if (BeautifulImage.is_my_favorite()) {
            toolbarCollect.setSelected(true);
        } else {
            toolbarCollect.setSelected(false);
        }
    }

    private void showPopwindow() {
        shareUtil.shareImage(this, picTitle, currentStyle, currentTag, currentImgId, new SocializeListeners
                .SnsPostListener() {
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
