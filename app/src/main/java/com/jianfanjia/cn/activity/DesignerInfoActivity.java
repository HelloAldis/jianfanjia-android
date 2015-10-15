package com.jianfanjia.cn.activity;

import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.fragment.DesignerInfoFragment;
import com.jianfanjia.cn.fragment.DesignerWorksFragment;

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends BaseActivity implements
        OnCheckedChangeListener {
    private static final String TAG = DesignerInfoActivity.class.getName();
    private RadioGroup mTabRadioGroup = null;
    private static final int DESIGNER_INFO = 0;
    private static final int DESIGNER_WORK = 1;
    private DesignerInfoFragment infoFragment = null;
    private DesignerWorksFragment workFragment = null;

    @Override
    public void initView() {
        mTabRadioGroup = (RadioGroup) findViewById(R.id.tab_rg_layout);
        setTabSelection(DESIGNER_INFO);
    }

    @Override
    public void setListener() {
        mTabRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_designer_info:
                setTabSelection(DESIGNER_INFO);
                break;
            case R.id.tab_designer_works:
                setTabSelection(DESIGNER_WORK);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case DESIGNER_INFO:
                if (infoFragment != null) {
                    transaction.show(infoFragment);
                } else {
                    infoFragment = new DesignerInfoFragment();
                    transaction.add(R.id.contentLayout, infoFragment);
                }
                break;
            case DESIGNER_WORK:
                if (workFragment != null) {
                    transaction.show(workFragment);
                } else {
                    workFragment = new DesignerWorksFragment();
                    transaction.add(R.id.contentLayout, workFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (infoFragment != null) {
            ft.hide(infoFragment);
        }
        if (workFragment != null) {
            ft.hide(workFragment);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info;
    }
}
