package com.jianfanjia.cn.activity.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.SearchDecorationImgFragment;
import com.jianfanjia.cn.fragment.SearchDesignerFragment;
import com.jianfanjia.cn.fragment.SearchProductFragment;
import com.jianfanjia.cn.tools.LogTool;

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

    @ViewById(R.id.tab_rb_1)
    protected RadioButton radioButton;

    private SearchDecorationImgFragment searchDecorationImgFragment;
    private SearchDesignerFragment searchDesignerFragment;
    private SearchProductFragment searchProductFragment;

    private String search;

    @AfterViews
    protected void initAnnotationView(){
        contentLayout.setVisibility(View.GONE);
        deleteView.setVisibility(View.GONE);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    resetView();
                    deleteView.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.GONE);
                } else {
                    deleteView.setVisibility(View.VISIBLE);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogTool.d(this.getClass().getName(), "checkId");
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

        searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //业务代码
                    if (!TextUtils.isEmpty(search = searchText.getEditableText().toString())) {
                        contentLayout.setVisibility(View.VISIBLE);
                        resetView();
                        radioGroup.check(R.id.tab_rb_1);
                        setTabSelection(DESIGNER);
//                        radioButton.setChecked(true);
                        hideSoftKeyBoard();
                    }
                }
                return true;
            }
        });
    }

    public void hideSoftKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(contentLayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void resetView(){
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        if(searchDesignerFragment != null){
            transaction.remove(searchDesignerFragment);
            searchDesignerFragment = null;
        }
        if(searchDecorationImgFragment != null){
            transaction.remove(searchDecorationImgFragment);
            searchDecorationImgFragment = null;
        }
        if(searchProductFragment != null){
            transaction.remove(searchProductFragment);
            searchProductFragment = null;
        }
        transaction.commit();
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case DESIGNER:
                if (searchDesignerFragment != null) {
                    transaction.show(searchDesignerFragment);
                } else {
                    searchDesignerFragment = new SearchDesignerFragment();
                    searchDesignerFragment.setArguments(getSearchBundle(search));
                    transaction.add(R.id.container_layout, searchDesignerFragment);
                }
                break;
            case PRODUCTCASE:
                if (searchProductFragment != null) {
                    transaction.show(searchProductFragment);
                } else {
                    searchProductFragment = new SearchProductFragment();
                    searchProductFragment.setArguments(getSearchBundle(search));
                    transaction.add(R.id.container_layout, searchProductFragment);
                }
                break;
            case BEAUTYIMAGE:
                if (searchDecorationImgFragment != null) {
                    transaction.show(searchDecorationImgFragment);
                } else {
                    searchDecorationImgFragment = new SearchDecorationImgFragment();
                    searchDecorationImgFragment.setArguments(getSearchBundle(search));
                    transaction.add(R.id.container_layout, searchDecorationImgFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    protected Bundle getSearchBundle(String search){
        Bundle bundle = new Bundle();
        bundle.putString(Global.SEARCH_TEXT,search);
        return bundle;
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (searchDesignerFragment != null) {
            ft.hide(searchDesignerFragment);
        }
        if (searchProductFragment != null) {
            ft.hide(searchProductFragment);
        }
        if (searchDecorationImgFragment != null) {
            ft.hide(searchDecorationImgFragment);
        }
    }

    @Click({R.id.act_search_cancel,R.id.act_search_delete})
    protected void click(View view){
        switch (view.getId()){
            case R.id.act_search_cancel:
                appManager.finishActivity(this);
                break;
            case R.id.act_search_delete:
                searchText.setText("");
                break;
        }
    }
}
