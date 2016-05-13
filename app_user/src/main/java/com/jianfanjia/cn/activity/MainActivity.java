package com.jianfanjia.cn.activity;

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
import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.Event.MessageCountEvent;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.DecorationFragment;
import com.jianfanjia.cn.fragment.HomeNewFragment;
import com.jianfanjia.cn.fragment.MyNewFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.tools.UiHelper;
import de.greenrobot.event.EventBus;

/**
 * Description:主界面
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getName();

    @Bind(R.id.badgeView)
    ImageView badgeView;

    @Bind(R.id.home_layout)
    LinearLayout homeLayout;

    @Bind(R.id.img_layout)
    LinearLayout beautyLayout;

    @Bind(R.id.req_layout)
    LinearLayout reqLayout;

    @Bind(R.id.my_layout)
    LinearLayout myLayout;


    private HomeNewFragment homeFragment = null;
    private DecorationFragment decorationFragment = null;
    private XuQiuFragment xuqiuFragment = null;
    private MyNewFragment myFragment = null;
    private long mExitTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        badgeView.setVisibility(View.GONE);
        switchTab(Constant.HOME);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switchTab(Constant.MANAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次resume刷新一下消息条数
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
                                myFragment.noticeCountView.setText(countList.get(0) + "");
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

                }

                @Override
                public void onNetworkError(int code) {

                }
            };

    @OnClick({R.id.home_layout, R.id.img_layout, R.id.req_layout, R.id.my_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_layout:
                switchTab(Constant.HOME);
                break;
            case R.id.img_layout:
                switchTab(Constant.DECORATE);
                break;
            case R.id.req_layout:
                switchTab(Constant.MANAGE);
                break;
            case R.id.my_layout:
                switchTab(Constant.MY);
                break;
        }
    }

    private void switchTab(int index) {
        switch (index) {
            case Constant.HOME:
                homeLayout.setSelected(true);
                beautyLayout.setSelected(false);
                reqLayout.setSelected(false);
                myLayout.setSelected(false);
                break;
            case Constant.DECORATE:
                homeLayout.setSelected(false);
                beautyLayout.setSelected(true);
                reqLayout.setSelected(false);
                myLayout.setSelected(false);
                break;
            case Constant.MANAGE:
                homeLayout.setSelected(false);
                beautyLayout.setSelected(false);
                reqLayout.setSelected(true);
                myLayout.setSelected(false);
                break;
            case Constant.MY:
                homeLayout.setSelected(false);
                beautyLayout.setSelected(false);
                reqLayout.setSelected(false);
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
            case Constant.HOME:
                if (homeFragment != null) {
                    transaction.show(homeFragment);
                } else {
                    homeFragment = new HomeNewFragment();
                    transaction.add(R.id.tablayout, homeFragment);
                }
                break;
            case Constant.DECORATE:
                if (decorationFragment != null) {
                    transaction.show(decorationFragment);
                } else {
                    decorationFragment = new DecorationFragment();
                    transaction.add(R.id.tablayout, decorationFragment);
                }
                break;
            case Constant.MANAGE:
                if (xuqiuFragment != null) {
                    transaction.show(xuqiuFragment);
                } else {
                    xuqiuFragment = new XuQiuFragment();
                    transaction.add(R.id.tablayout, xuqiuFragment);
                }
                break;
            case Constant.MY:
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
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (decorationFragment != null) {
            ft.hide(decorationFragment);
        }
        if (xuqiuFragment != null) {
            ft.hide(xuqiuFragment);
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

}
