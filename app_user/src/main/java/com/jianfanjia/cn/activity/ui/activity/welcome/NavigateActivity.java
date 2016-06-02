package com.jianfanjia.cn.activity.ui.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.base.BaseActivity;
import com.jianfanjia.cn.activity.business.DataManagerNew;
import com.jianfanjia.cn.activity.ui.activity.MainActivity;
import com.jianfanjia.cn.activity.ui.activity.loginandreg.LoginNewActivity;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.guest.WeiXinRegisterRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.adapter.ViewPageAdapter;
import com.jianfanjia.cn.activity.api.Api;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.cn.activity.tools.AuthUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.viewpager_indicator.CircleIndicator;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * @author fengliang
 * @ClassName: NavigateActivity
 * @Description: 引导
 * @date 2015-8-28 下午3:23:37
 */
public class NavigateActivity extends BaseActivity {
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.register_login_text)
    TextView register_login_text;

    @Bind(R.id.registerText)
    TextView registerText;

    @Bind(R.id.dot_indicator)
    CircleIndicator mCircleIndicator;

    private AuthUtil authUtil;

    private List<View> list = new ArrayList<>();
    private ViewPageAdapter adapter = null;
    private RelativeLayout imageLayout = null;

    private int imgId[] = {R.mipmap.img_guide1, R.mipmap.img_guide2,
            R.mipmap.img_guide3, R.mipmap.img_guide4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        authUtil = AuthUtil.getInstance(this);
        initView();
    }

    private void initView() {
        for (int i = 0; i < imgId.length; i++) {
            imageLayout = (RelativeLayout) inflater.inflate(R.layout.viewpager_item_navigate, null, false);
            ImageView view = (ImageView) (imageLayout.findViewById(R.id.viewpager_navigate_item_pic));
            view.setImageResource(imgId[i]);
            list.add(view);
        }
        adapter = new ViewPageAdapter(NavigateActivity.this, list);
        viewPager.setAdapter(adapter);
        mCircleIndicator.setViewPager(viewPager);
    }

    @OnClick({R.id.register_login_text, R.id.registerText, R.id.btnWeixinLayout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_login_text:
                Bundle loginBundle = new Bundle();
                loginBundle.putInt(IntentConstant.LOGIN_REGIISTER_TYPE, IntentConstant.LOGIN_FRAGMENT);
                startActivity(LoginNewActivity.class, loginBundle);
                break;
            case R.id.registerText:
                Bundle registerBundle = new Bundle();
                registerBundle.putInt(IntentConstant.LOGIN_REGIISTER_TYPE, IntentConstant.REGISTER_FRAGMENT);
                startActivity(LoginNewActivity.class, registerBundle);
                break;
            case R.id.btnWeixinLayout:
                SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                authUtil.doOauthVerify(this, platform, umDataListener);
                break;
            default:
                break;
        }
    }

    private SocializeListeners.UMDataListener umDataListener = new SocializeListeners.UMDataListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(int i, Map<String, Object> data) {
            if (i == 200 && data != null) {
                LogTool.d(this.getClass().getName(), data.toString());
                WeiXinRegisterRequest weiXinRegisterRequest = new WeiXinRegisterRequest();
                weiXinRegisterRequest.setUsername(data.get("nickname").toString());
                weiXinRegisterRequest.setImage_url((String) data.get("headimgurl").toString());
                String sex = null;
                if ((sex = data.get("sex").toString()) != null) {
                    weiXinRegisterRequest.setSex(sex.equals(Constant.SEX_MAN) ? Constant.SEX_WOMEN : Constant.SEX_MAN);
                    //系统的性别和微信的性别要转换
                }
                weiXinRegisterRequest.setWechat_openid(data.get("openid").toString());
                weiXinRegisterRequest.setWechat_unionid(data.get("unionid").toString());
                Api.weiXinRegister(weiXinRegisterRequest, new ApiCallback<ApiResponse<User>>() {
                    @Override
                    public void onPreLoad() {

                    }

                    @Override
                    public void onHttpDone() {

                    }

                    @Override
                    public void onSuccess(ApiResponse<User> apiResponse) {
                        User loginUserBean = apiResponse.getData();
                        loginUserBean.setWechat_openid(loginUserBean.getWechat_openid());
                        loginUserBean.setWechat_unionid(loginUserBean.getWechat_unionid());
                        DataManagerNew.loginSuccess(loginUserBean);
                        if (dataManager.getWeixinFisrtLogin()) {
                            startActivity(NewUserCollectDecStageActivity.class);
                        } else {
                            startActivity(MainActivity.class);
                        }
                        appManager.finishActivity(NavigateActivity.this);
                    }

                    @Override
                    public void onFailed(ApiResponse<User> apiResponse) {
                        makeTextShort(apiResponse.getErr_msg());
                    }

                    @Override
                    public void onNetworkError(int code) {
                        makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    }
                });
            } else {
                hideWaitDialog();
                makeTextShort(getString(R.string.authorize_fail));
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.d(this.getClass().getName(), "onActivityResult");
        UMSsoHandler ssoHandler = authUtil.getUmSocialService().getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigate;
    }

}
