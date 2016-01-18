package com.jianfanjia.cn.designer.activity.beautifulpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.ShowPicPagerAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.BeautyImgInfo;
import com.jianfanjia.cn.designer.bean.Img;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.cn.designer.tools.ImageUtil;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.ShareUtil;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:预览装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDecorationActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = PreviewDecorationActivity.class.getName();
    private ShareUtil shareUtil = null;
    private Toolbar toolbar = null;
    private ImageView toolbar_collect = null;
    private ImageView toolbar_share = null;
    private ImageView btn_download = null;
    private RelativeLayout toolbar_collectLayout = null;
    private RelativeLayout toolbar_shareLayout = null;
    private RelativeLayout btn_downloadLayout = null;

    private ViewPager viewPager = null;
    private TextView pic_tip = null;
    private TextView pic_title = null;
    private TextView pic_des = null;
    private String decorationId = null;
    private List<String> imgList = new ArrayList<>();
    private int totalCount = 0;
    private int currentPosition = 0;
    private String picTitle = null;
    private String currentImgId = null;
    private String currentStyle = null;
    private String currentTag = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle decorationBundle = intent.getExtras();
        decorationId = decorationBundle.getString(Global.DECORATION_ID);
        LogTool.d(TAG, "decorationId=" + decorationId);
        shareUtil = new ShareUtil(this);
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
        viewPager = (ViewPager) findViewById(R.id.showpicPager);
        pic_tip = (TextView) findViewById(R.id.pic_tip);
        pic_title = (TextView) findViewById(R.id.pic_title);
        pic_des = (TextView) findViewById(R.id.pic_des);
        getDecorationImgInfo(decorationId);
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
        viewPager.setOnPageChangeListener(this);
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
                showPopwindow(getWindow().getDecorView());
                break;
            case R.id.btn_download_layout:
                UiHelper.imageButtonAnim(btn_download, null);
                downloadImg();
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
            showWaitDialog();
        }

        @Override
        public void loadSuccess(Object data) {
            hideWaitDialog();
            LogTool.d(TAG, "data:" + data.toString());
            BeautyImgInfo beautyImgInfo = JsonParser.jsonToBean(data.toString(), BeautyImgInfo.class);
            LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
            if (null != beautyImgInfo) {
                btn_downloadLayout.setVisibility(View.VISIBLE);
                toolbar_collectLayout.setVisibility(View.VISIBLE);
                toolbar_shareLayout.setVisibility(View.VISIBLE);
                if (beautyImgInfo.is_my_favorite()) {
                    toolbar_collect.setSelected(true);
                } else {
                    toolbar_collect.setSelected(false);
                }
                picTitle = beautyImgInfo.getTitle();
                currentStyle = beautyImgInfo.getDec_style();
                currentTag = beautyImgInfo.getSection();
                pic_title.setText(TextUtils.isEmpty(picTitle) ? "" : picTitle);
                String keyDes = BusinessManager.spilteKeyWord(beautyImgInfo.getKeywords());
                if (!TextUtils.isEmpty(keyDes)) {
                    pic_des.setText(keyDes);
                }
                List<Img> decorationImgs = beautyImgInfo.getImages();
                totalCount = decorationImgs.size();
                for (Img img : decorationImgs) {
                    imgList.add(img.getImageid());
                }
                currentImgId = imgList.get(currentPosition);
                pic_tip.setText((currentPosition + 1) + "/" + totalCount);
                final ShowPicPagerAdapter showPicPagerAdapter = new ShowPicPagerAdapter(PreviewDecorationActivity.this, imgList, new ViewPagerClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        LogTool.d(TAG, "pos:" + pos);
                        appManager.finishActivity(PreviewDecorationActivity.this);
                    }
                });
                viewPager.setAdapter(showPicPagerAdapter);
            }
        }


        @Override
        public void loadFailture(String error_msg) {
            hideWaitDialog();
            makeTextLong(error_msg);
            btn_downloadLayout.setVisibility(View.GONE);
            toolbar_collectLayout.setVisibility(View.GONE);
            toolbar_shareLayout.setVisibility(View.GONE);
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
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_BEAUTY_FRAGMENT));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
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
        currentImgId = imgList.get(currentPosition);
        LogTool.d(TAG, "currentImgId=" + currentImgId);
    }

    private void showPopwindow(View view) {
        shareUtil.shareImage(this, picTitle, currentStyle, currentTag, currentImgId, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                LogTool.d("onComplete","status =" + i);
            }
        });
       /* SharePopWindow window = new SharePopWindow(PreviewDecorationActivity.this, new ShowPopWindowCallBack() {
            @Override
            public void shareToWeiXin() {
                shareByPlatform(SHARE_MEDIA.WEIXIN);
            }

            @Override
            public void shareToWeiBo() {
                shareByPlatform(SHARE_MEDIA.SINA);
            }

            @Override
            public void shareToQQ() {
                shareByPlatform(SHARE_MEDIA.QQ);
            }

            @Override
            public void shareToCircle() {
                shareByPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
            }

            @Override
            public void shareToZone() {
                shareByPlatform(SHARE_MEDIA.QZONE);
            }
        }

        );
        window.show(view);*/
    }


    private void downloadImg() {
        ImageView photoView = (ImageView) viewPager.getChildAt(currentPosition).findViewById(R.id.image_item);
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
