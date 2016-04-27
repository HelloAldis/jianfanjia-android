package com.jianfanjia.cn.activity.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.fragment.SearchDecorationImgFragment;
import com.jianfanjia.cn.fragment.SearchDesignerFragment;
import com.jianfanjia.cn.fragment.SearchProductFragment;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.activity.home
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-20 09:12
 */
public class SearchActivity extends BaseSwipeBackActivity {
    private static final String TAG = SearchActivity.class.getName();

    public static final int DESIGNER = 0x00;
    public static final int PRODUCTCASE = 0x01;
    public static final int BEAUTYIMAGE = 0x02;

    @Bind(R.id.act_search_input)
    protected EditText searchText;

    @Bind(R.id.act_search_cancel)
    protected TextView cancelView;

    @Bind(R.id.act_search_delete)
    protected ImageView deleteView;

    @Bind(R.id.act_search_content_layout)
    protected LinearLayout contentLayout;

    @Bind(R.id.tab_rg_menu)
    protected RadioGroup radioGroup;

    private SearchDecorationImgFragment searchDecorationImgFragment;
    private SearchDesignerFragment searchDesignerFragment;
    private SearchProductFragment searchProductFragment;

    private String search;

    private int currentTab = DESIGNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        contentLayout.setVisibility(View.GONE);
        deleteView.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogTool.d(TAG, "checkId");
                switch (checkedId) {
                    case R.id.tab_rb_1:
                        setTabSelection(DESIGNER);
                        currentTab = DESIGNER;
                        break;
                    case R.id.tab_rb_2:
                        setTabSelection(PRODUCTCASE);
                        currentTab = PRODUCTCASE;
                        break;
                    case R.id.tab_rb_3:
                        setTabSelection(BEAUTYIMAGE);
                        currentTab = BEAUTYIMAGE;
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
                        switch (currentTab){
                            case DESIGNER:
                                radioGroup.check(R.id.tab_rb_1);
                                break;
                            case PRODUCTCASE:
                                radioGroup.check(R.id.tab_rb_2);
                                break;
                            case BEAUTYIMAGE:
                                radioGroup.check(R.id.tab_rb_3);
                                break;
                        }
                        setTabSelection(currentTab);
//                        radioButton.setChecked(true);
                        hideSoftKeyBoard();
                    }
                }
                return true;
            }
        });
    }

    public void hideSoftKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(contentLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void resetView() {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        if (searchDesignerFragment != null) {
            transaction.remove(searchDesignerFragment);
            searchDesignerFragment = null;
        }
        if (searchDecorationImgFragment != null) {
            transaction.remove(searchDecorationImgFragment);
            searchDecorationImgFragment = null;
        }
        if (searchProductFragment != null) {
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

    protected Bundle getSearchBundle(String search) {
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstant.SEARCH_TEXT, search);
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

    @OnTextChanged(R.id.act_search_input)
    public void onTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            resetView();
            deleteView.setVisibility(View.GONE);
            contentLayout.setVisibility(View.GONE);
        } else {
            deleteView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.act_search_cancel, R.id.act_search_delete})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.act_search_cancel:
                appManager.finishActivity(this);
                break;
            case R.id.act_search_delete:
                searchText.setText("");
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

}
