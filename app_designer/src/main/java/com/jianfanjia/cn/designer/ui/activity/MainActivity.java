package com.jianfanjia.cn.designer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.cn.designer.AppManager;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.Event.MessageCountEvent;
import com.jianfanjia.cn.designer.ui.fragment.ManageFragment;
import com.jianfanjia.cn.designer.ui.fragment.MyNewFragment;
import com.jianfanjia.cn.designer.ui.fragment.MyOwnerFragment;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.sso.UMSsoHandler;
import de.greenrobot.event.EventBus;

/**
 * Description:主界面
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();

    public static final String TAB_POSITION = "tab_position";

    @Bind(R.id.badgeView)
    ImageView badgeView;

    @Bind(R.id.owner_layout)
    LinearLayout ownerLayout;

    @Bind(R.id.site_layout)
    LinearLayout siteLayout;

    @Bind(R.id.my_layout)
    LinearLayout myLayout;
    private MyOwnerFragment ownerFragment = null;
    private ManageFragment manageFragment = null;
    private MyNewFragment myFragment = null;
    private long mExitTime = 0L;
    private int tab = -1;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        tab = intent.getIntExtra(TAB_POSITION, Constant.OWNER);
    }

    public void initView() {
        badgeView.setVisibility(View.GONE);
        switchTab(tab);
    }

    @OnClick({R.id.owner_layout, R.id.site_layout, R.id.my_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.owner_layout:
                switchTab(Constant.OWNER);
                break;
            case R.id.site_layout:
                switchTab(Constant.MANAGE);
                break;
            case R.id.my_layout:
                switchTab(Constant.MORE);
                break;
        }
    }

    public void switchTab(int index) {
        switch (index) {
            case Constant.OWNER:
                ownerLayout.setSelected(true);
                siteLayout.setSelected(false);
                myLayout.setSelected(false);
                break;
            case Constant.MANAGE:
                ownerLayout.setSelected(false);
                siteLayout.setSelected(true);
                myLayout.setSelected(false);
                break;
            case Constant.MORE:
                ownerLayout.setSelected(false);
                siteLayout.setSelected(false);
                myLayout.setSelected(true);
                break;
        }
        setTabSelection(index);
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case Constant.OWNER:
                if (ownerFragment != null) {
                    transaction.show(ownerFragment);
                } else {
                    ownerFragment = new MyOwnerFragment();
                    transaction.add(R.id.tablayout, ownerFragment);
                }
                break;
            case Constant.MANAGE:
                if (manageFragment != null) {
                    transaction.show(manageFragment);
                } else {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.tablayout, manageFragment);
                }
                break;
            case Constant.MORE:
                if (myFragment != null) {
                    transaction.show(myFragment);
                } else {
                    myFragment = new MyNewFragment();
                    transaction.add(R.id.tablayout, myFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (ownerFragment != null) {
            ft.hide(ownerFragment);
        }
        if (manageFragment != null) {
            ft.hide(manageFragment);
        }
        if (myFragment != null) {
            ft.hide(myFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
            makeTextShort("再按一次退出简繁家");
            mExitTime = System.currentTimeMillis();// 更新mExitTime
        } else {
            AppManager.getAppManager().AppExit();
        }
    }

    public void onEventMainThread(MessageCountEvent messageCountEvent) {
        //为了让在当前屏能及时响应，所以每次收到提醒时刷新一下view
        UiHelper.getUnReadMessageCount(getMessageCountListener, Constant.searchMsgCountType1, Constant
                .searchMsgCountType2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiHelper.getUnReadMessageCount(getMessageCountListener, Constant.searchMsgCountType1, Constant
                .searchMsgCountType2);
    }

    private ApiCallback<ApiResponse<List<Integer>>> getMessageCountListener = new
            ApiCallback<ApiResponse<List<Integer>>>() {


                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {

                }

                @Override
                public void onSuccess(ApiResponse<List<Integer>> apiResponse) {
                    List<Integer> countList = apiResponse.getData();
                    if (countList != null) {
                        if (countList.get(0) > 0 || countList.get(1) > 0) {
                            badgeView.setVisibility(View.VISIBLE);
                        } else {
                            badgeView.setVisibility(View.GONE);
                        }
                        if (myFragment != null) {
                            if (countList.get(0) > 0) {
                                myFragment.noticeCountView.setVisibility(View.VISIBLE);
//                                myFragment.noticeCountView.setText(countList.get(0) + "");
                            } else {
                                myFragment.noticeCountView.setVisibility(View.GONE);
                            }
                            if (countList.get(1) > 0) {
                                myFragment.commentCountView.setVisibility(View.VISIBLE);
                                myFragment.commentCountView.setText(countList.get(1) + "");
                            } else {
                                myFragment.commentCountView.setVisibility(View.GONE);
                                myFragment.commentCountView.setText("");
                            }
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<List<Integer>> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                }
            };

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
