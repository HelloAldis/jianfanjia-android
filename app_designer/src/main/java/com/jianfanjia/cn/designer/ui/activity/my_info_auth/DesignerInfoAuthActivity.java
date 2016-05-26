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
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.bean.AuthCenterItem;
import com.jianfanjia.cn.designer.business.DesignerBusiness;
import com.jianfanjia.cn.designer.business.ProductBusiness;
import com.jianfanjia.cn.designer.config.Global;
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

    private static final int BASE_INFO_AUTH_POSITION = 0;
    private static final int IDENTITY_AUTH_POSITION = 1;
    private static final int PRODUCT_AUTH_POSITION = 2;
    private static final int TEAM_AUTH_POSITION = 3;
    private static final int EMAIL_AUTH_POSITION = 4;

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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = inflater.inflate(R.layout.grid_item_designer_auth_center, null, true);
                AuthCenterItem authCenterItem = mAuthCenterItems[position];
                ImageView authImg = (ImageView) view.findViewById(R.id.auth_img);
                TextView authTitle = (TextView) view.findViewById(R.id.auth_text);
                TextView authStatus = (TextView) view.findViewById(R.id.auth_status);
                authImg.setImageResource(authCenterItem.getIconResId());
                authTitle.setText(authCenterItem.getTitle());
                if (position == PRODUCT_AUTH_POSITION) {
                    setProductAuthStatus(authImg, authStatus);
                } else {
                    setAuthStatus(authCenterItem, authImg, authStatus);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intentTo(position);
                    }
                });
                return view;
            }

            private void setProductAuthStatus(ImageView authImg, TextView authStatus) {
                if (dataManager.getDesigner().getProduct_count() == 0) {
                    authImg.setBackgroundResource(R.drawable.bg_auth_oval_grey);
                    authStatus.setText(getString(R.string.auth_not_apply));
                    authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_grey);
                } else {
                    if (dataManager.getDesigner().getAuthed_product_count() > ProductBusiness
                            .PRODUCT_AUTH_SUCCESS_MIN_COUNT) {
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_green);
                        authStatus.setText(getString(R.string.view_detail));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_green);
                    } else {
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_blue);
                        authStatus.setText(getString(R.string.view_detail));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_blue);
                    }
                }
            }

            private void setAuthStatus(AuthCenterItem authCenterItem, ImageView authImg, TextView authStatus) {
                switch (authCenterItem.getStatus()) {
                    case DesignerBusiness.DESIGNER_NOT_APPLY:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_grey);
                        authStatus.setText(getString(R.string.auth_not_apply));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_grey);
                        break;
                    case DesignerBusiness.DESIGNER_AUTH_SUCCESS:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_green);
                        authStatus.setText(getString(R.string.auth_success));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_green);
                        break;
                    case DesignerBusiness.DESIGNER_AUTH_FAILURE:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_red);
                        authStatus.setText(getString(R.string.authorize_fail));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_red);
                        break;
                    case DesignerBusiness.DESIGNER_AUTH_VIOLATION:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_red);
                        authStatus.setText(getString(R.string.authorize_violation));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_red);
                        break;
                    case DesignerBusiness.DESIGNER_NOT_AUTH:
                        authImg.setBackgroundResource(R.drawable.bg_auth_oval_blue);
                        authStatus.setText(getString(R.string.auth_going));
                        authStatus.setBackgroundResource(R.drawable.bg_auth_rectangle_blue);
                        break;
                }
            }
        });
    }

    private void intentTo(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.DESIGNER_INFO, dataManager.getDesigner());
        switch (position) {
            case BASE_INFO_AUTH_POSITION:
                startActivity(BaseInfoAuthActicity.class, bundle);
                break;
            case IDENTITY_AUTH_POSITION:
                startActivity(DesignerIdentityAuthActivity.class,bundle);
                break;
            case PRODUCT_AUTH_POSITION:
                startActivity(DesignerProductAuthActivity.class);
                break;
            case TEAM_AUTH_POSITION:
                startActivity(DesignerTeamAuthActivity.class);
                break;
            case EMAIL_AUTH_POSITION:
                startActivity(EmailAuthActivity.class);
                break;
        }
    }

    private void initGridViewData() {
        Designer designer = dataManager.getDesigner();
        mAuthCenterItems[BASE_INFO_AUTH_POSITION] = new AuthCenterItem(R.mipmap.icon_auth_baseinfo, getString(R
                .string.base_info_auth), designer.getAuth_type());
        mAuthCenterItems[IDENTITY_AUTH_POSITION] = new AuthCenterItem(R.mipmap.icon_auth_identity, getString(R.string
                .identity_auth), designer.getUid_auth_type());
        mAuthCenterItems[PRODUCT_AUTH_POSITION] = new AuthCenterItem(R.mipmap.icon_auth_product, getString(R.string
                .product_auth), "-1");
        mAuthCenterItems[TEAM_AUTH_POSITION] = new AuthCenterItem(R.mipmap.icon_auth_team, getString(R.string
                .process_team_auth), designer.getWork_auth_type());
        mAuthCenterItems[EMAIL_AUTH_POSITION] = new AuthCenterItem(R.mipmap.icon_auth_email, getString(R.string
                .email_auth), designer.getEmail_auth_type());
    }

    private void initView() {
        initMainView();
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.designer_auth_center));
    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info_auth_list;
    }
}
