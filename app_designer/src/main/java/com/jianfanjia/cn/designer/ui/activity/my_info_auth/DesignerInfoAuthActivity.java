package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.bean.AuthCenterItem;
import com.jianfanjia.cn.designer.business.ProductBusiness;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info.BaseInfoAuthActicity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.identity_info.DesignerIdentityAuthActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.DesignerProductAuthActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info.DesignerTeamAuthActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-17 15:22
 */
public class DesignerInfoAuthActivity extends BaseSwipeBackActivity {

    @Bind(R.id.designer_auth_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.gridview)
    GridView mGridView;

    private AuthCenterItem[] mAuthCenterItems = new AuthCenterItem[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initGridView();
    }

    private void initGridView() {
        initGridViewData();
        mGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mAuthCenterItems.length;
            }

            @Override
            public Object getItem(int position) {
                return mAuthCenterItems[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = inflater.inflate(R.layout.grid_item_designer_auth_center, null, true);
                AuthCenterItem authCenterItem = mAuthCenterItems[position];
                ImageView authImg = (ImageView) view.findViewById(R.id.auth_img);
                TextView authTitle = (TextView) view.findViewById(R.id.auth_text);
                TextView authStatus = (TextView) view.findViewById(R.id.auth_status);
                authImg.setBackgroundResource(authCenterItem.getIconResId());
                authTitle.setText(authCenterItem.getTitle());
                switch (authCenterItem.getStatus()){
                    case ProductBusiness.PRODUCT_AUTH_SUCCESS:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_green);
                        authStatus.setText(getString(R.string.auth_success));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_green);
                        break;
                    case ProductBusiness.PRODUCT_AUTH_FAILURE:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_red);
                        authStatus.setText(getString(R.string.authorize_fail));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_red);
                        break;
                    case ProductBusiness.PRODUCT_AUTH_VIOLATION:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_red);
                        authStatus.setText(getString(R.string.authorize_violation));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_red);
                        break;
                    case ProductBusiness.PRODUCT_NOT_AUTH:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_blue);
                        authStatus.setText(getString(R.string.auth_going));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_blue);
                        break;
                }
                return view;
            }
        });
    }

    private void initGridViewData() {
        mAuthCenterItems[0] = new AuthCenterItem(R.mipmap.icon_my_product, getString(R.string.base_info_auth), "1");
        mAuthCenterItems[1] = new AuthCenterItem(R.mipmap.icon_my_product, getString(R.string.identity_auth), "2");
        mAuthCenterItems[2] = new AuthCenterItem(R.mipmap.icon_my_product, getString(R.string.product_auth), "2");
        mAuthCenterItems[3] = new AuthCenterItem(R.mipmap.icon_my_product, getString(R.string.process_team_auth), "3");
        mAuthCenterItems[4] = new AuthCenterItem(R.mipmap.icon_my_product, getString(R.string.email_auth), "1");
    }

    private void initView() {
        initMainView();
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.designer_auth));
    }

    @OnClick({R.id.head_back_layout, R.id.base_info_auth_layout, R.id.identity_auth_layout, R.id.product_auth_layout,
            R.id.process_team_auth_layout, R.id.email_auth_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.base_info_auth_layout:
                startActivity(BaseInfoAuthActicity.class);
                break;
            case R.id.identity_auth_layout:
                startActivity(DesignerIdentityAuthActivity.class);
                break;
            case R.id.product_auth_layout:
                startActivity(DesignerProductAuthActivity.class);
                break;
            case R.id.process_team_auth_layout:
                startActivity(DesignerTeamAuthActivity.class);
                break;
            case R.id.email_auth_layout:
//                startActivity();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info_auth_list;
    }
}
