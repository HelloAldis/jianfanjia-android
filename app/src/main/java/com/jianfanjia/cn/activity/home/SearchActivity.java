package com.jianfanjia.cn.activity.home;

import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.fragment.SearchDecorationImgFragment;
import com.jianfanjia.cn.fragment.SearchDesignerFragment;
import com.jianfanjia.cn.fragment.SearchProductFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity.home
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-20 09:12
 */
@EActivity(R.layout.activity_search)
public class SearchActivity extends SwipeBackActivity{

    public static final int DESIGNER = 0x00;
    public static final int PRODUCTCASE = 0x01;
    public static final int BEAUTYIMAGE = 0x02;

    @ViewById(R.id.act_search_input)
    protected EditText searchText;

    @ViewById(R.id.act_search_cancel)
    protected TextView cancelView;

    @ViewById(R.id.act_search_delete)
    protected ImageView deleteView;

    @ViewById(R.id.act_search_content_layout)
    protected LinearLayout contentLayout;

    @ViewById(R.id.tab_rg_menu)
    protected RadioGroup radioGroup;

    private SearchDecorationImgFragment searchDecorationImgFragment;
    private SearchDesignerFragment searchDesignerFragment;
    private SearchProductFragment searchProductFragment;

    @AfterViews
    protected void initAnnotationView(){
//        contentLayout.setVisibility(View.GONE);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setTabSelection(DESIGNER);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_rb_1:
                        setTabSelection(DESIGNER);
                        break;
                    case R.id.tab_rb_2:
                        setTabSelection(PRODUCTCASE);


                        break;
                    case R.id.tab_rb_3:
                        setTabSelection(BEAUTYIMAGE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case DESIGNER:
                if (searchDesignerFragment != null) {
                    transaction.attach(searchDesignerFragment);
                } else {
                    searchDesignerFragment = new SearchDesignerFragment();
                    transaction.replace(R.id.container_layout, searchDesignerFragment);
                }
                break;
            case PRODUCTCASE:
                if (searchProductFragment != null) {
                    transaction.attach(searchProductFragment);
                } else {
                    searchProductFragment = new SearchProductFragment();
                    transaction.replace(R.id.container_layout, searchProductFragment);
                }
                break;
            case BEAUTYIMAGE:
                if (searchDecorationImgFragment != null) {
                    transaction.attach(searchDecorationImgFragment);
                } else {
                    searchDecorationImgFragment = new SearchDecorationImgFragment();
                    transaction.replace(R.id.container_layout, searchDecorationImgFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (searchDesignerFragment != null) {
            ft.detach(searchDesignerFragment);
        }
        if (searchProductFragment != null) {
            ft.detach(searchProductFragment);
        }
        if (searchDecorationImgFragment != null) {
            ft.detach(searchDecorationImgFragment);
        }
    }

    @Click({R.id.act_search_cancel})
    protected void click(View view){
        switch (view.getId()){
            case R.id.act_search_cancel:
                appManager.finishActivity(this);
                break;
        }
    }
}
