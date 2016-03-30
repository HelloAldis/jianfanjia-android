package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.AppManager;
import com.jianfanjia.cn.designer.Event.MessageCountEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.fragment.ManageFragment;
import com.jianfanjia.cn.designer.fragment.MyNewFragment;
import com.jianfanjia.cn.designer.fragment.MyOwnerFragment;
import com.jianfanjia.cn.designer.fragment.MyOwnerFragment_;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import de.greenrobot.event.EventBus;

/**
 * Description:主界面
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private ImageView badgeView = null;
    private LinearLayout ownerLayout = null;
    private LinearLayout siteLayout = null;
    private LinearLayout myLayout = null;
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
    }

    @Override
    public void initView() {
        badgeView = (ImageView) findViewById(R.id.badgeView);
        badgeView.setVisibility(View.GONE);
        ownerLayout = (LinearLayout) findViewById(R.id.owner_layout);
        siteLayout = (LinearLayout) findViewById(R.id.site_layout);
        myLayout = (LinearLayout) findViewById(R.id.my_layout);
        setTabSelection(Constant.OWNER);
    }

    @Override
    public void setListener() {
        ownerLayout.setOnClickListener(this);
        siteLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);
    }

    @Override
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
                    ownerFragment = MyOwnerFragment_.builder().build();
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
        UiHelper.getUnReadMessageCount(this, getMessageCountListener, this, Constant.searchMsgCountType1, Constant
                .searchMsgCountType2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiHelper.getUnReadMessageCount(this, getMessageCountListener, this, Constant.searchMsgCountType1,
                Constant.searchMsgCountType2);
    }

    private ApiUiUpdateListener getMessageCountListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            List<Integer> countList = JsonParser.jsonToList(data.toString(), new TypeToken<List<Integer>>() {
            }.getType());
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
        public void loadFailture(String error_msg) {

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
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
