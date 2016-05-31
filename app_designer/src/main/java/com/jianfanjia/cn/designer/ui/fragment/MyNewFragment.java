package com.jianfanjia.cn.designer.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.designer.GetDesignerInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.business.DesignerBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.ShareUtil;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.activity.common.CommentListActivity;
import com.jianfanjia.cn.designer.ui.activity.my.CustomerServiceActivity;
import com.jianfanjia.cn.designer.ui.activity.my.FeedBackActivity;
import com.jianfanjia.cn.designer.ui.activity.my.NoticeActivity;
import com.jianfanjia.cn.designer.ui.activity.my.SettingActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.DesignerInfoAuthActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info.BaseInfoAuthActicity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.DesignerProductAuthActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.receive_business_info.DesignerReceiveInfoActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info.DesignerTeamAuthActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.layout.BadgeView;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

/**
 * Description:我的
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyNewFragment extends BaseFragment {
    private static final String TAG = MyNewFragment.class.getName();

    @Bind(R.id.my_head_layout)
    protected MainHeadView mainHeadView;

    @Bind(R.id.setting_scrollview)
    ScrollView scrollView;

    @Bind(R.id.user_head_img)
    ImageView user_head_img;

    @Bind(R.id.frag_my_name)
    TextView my_name;

    @Bind(R.id.frag_my_account)
    TextView my_account;

    @Bind(R.id.product_badgeview)
    public BadgeView noticeCountView = null;

    @Bind(R.id.designer_auth_center_stage)
    TextView authProductText;

    @Bind(R.id.comment_count_text)
    public BadgeView commentCountView = null;

    private ShareUtil mShareUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        setListener();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShareUtil = new ShareUtil(getActivity());
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    private void initView() {
        initMainHeadView();
        commentCountView.setVisibility(View.GONE);
        noticeCountView.setVisibility(View.GONE);

        setMyHeadInfo();
        setBaseInfoLayout();

        //动态计算imageview的宽高
        getUnReadMessageCount(Constant.searchMsgCountType1, Constant.searchMsgCountType2);
    }

    private void setMyHeadInfo() {
        String imgPath = dataManager.getUserImagePath();
        LogTool.d(TAG, "imgPath=" + imgPath);
        if (!imgPath.contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayImageHeadWidthThumnailImage(getActivity(), imgPath, user_head_img);
        } else {
            user_head_img.setImageResource(R.mipmap.icon_default_head);
        }
    }

    private void setBaseInfoLayout() {
        my_name.setText(TextUtils.isEmpty(dataManager.getUserName()) ? getResources().getString(R.string.ower) :
                dataManager.getUserName());
        my_account.setText(TextUtils.isEmpty(dataManager.getAccount()) ? "" : "手机号：" + dataManager.getAccount());

        if (dataManager.getDesigner() != null) {
            if (DesignerBusiness.getAuthProcessPercent(dataManager.getDesigner()) == 0) {
                authProductText.setText(getString(R.string.going_auth));
            } else {
                authProductText.setText("已完成认证：" + DesignerBusiness.getAuthProcessPercent(dataManager.getDesigner())
                        + "%");
            }
        }
    }

    private void getDesignerInfo() {
        GetDesignerInfoRequest getDesignerInfoRequest = new GetDesignerInfoRequest();

        Api.getDesignerInfo(getDesignerInfoRequest, new ApiCallback<ApiResponse<Designer>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<Designer> apiResponse) {
                Designer designer = apiResponse.getData();
                dataManager.setDesigner(designer);
                setMyHeadInfo();
                setBaseInfoLayout();
            }

            @Override
            public void onFailed(ApiResponse<Designer> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void setListener() {
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @OnClick({R.id.frag_my_info_layout, R.id.kefu_layout, R.id.setting_layout, R.id.feedback_layout, R.id
            .call_layout, R.id.comment_layout, R.id.designer_auth_center_layout, R.id.head_notification_layout, R.id
            .product_layout, R.id.team_layout, R.id.receive_business_info_layout, R.id.invite_friends_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_my_info_layout:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Global.DESIGNER_INFO, dataManager.getDesigner());
                startActivity(BaseInfoAuthActicity.class, bundle);
                break;
            case R.id.head_notification_layout:
                startActivity(NoticeActivity.class);
                break;
            case R.id.kefu_layout:
                startActivity(CustomerServiceActivity.class);
                break;
            case R.id.setting_layout:
                startActivity(SettingActivity.class);
                break;
            case R.id.feedback_layout:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.call_layout:
                UiHelper.callPhoneIntent(getContext(), getString(R.string.app_phone));
                break;
            case R.id.comment_layout:
                startActivity(CommentListActivity.class);
                break;
            case R.id.designer_auth_center_layout:
                startActivity(DesignerInfoAuthActivity.class);
                break;
            case R.id.product_layout:
                startActivity(DesignerProductAuthActivity.class);
                break;
            case R.id.team_layout:
                startActivity(DesignerTeamAuthActivity.class);
                break;
            case R.id.receive_business_info_layout:
                Bundle receiveBundle = new Bundle();
                receiveBundle.putSerializable(Global.DESIGNER_INFO, dataManager.getDesigner());
                startActivity(DesignerReceiveInfoActivity.class, receiveBundle);
                break;
            case R.id.invite_friends_layout:
                mShareUtil.shareApp(getActivity(), new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        LogTool.d("onComplete", "status =" + i);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDesignerInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogTool.d(TAG, "isHidden =" + hidden);
        if (!hidden) {

        }
    }

    private void getUnReadMessageCount(String[]... selectLists) {
        UiHelper.getUnReadMessageCount(new ApiCallback<ApiResponse<List<Integer>>>() {
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
                    if (countList.get(0) > 0) {
                        noticeCountView.setVisibility(View.VISIBLE);
                        noticeCountView.setText(countList.get(0) + "");
                    } else {
                        noticeCountView.setVisibility(View.GONE);
                    }
                    if (countList.get(1) > 0) {
                        commentCountView.setVisibility(View.VISIBLE);
                        commentCountView.setText(countList.get(1) + "");
                    } else {
                        commentCountView.setVisibility(View.GONE);
                        commentCountView.setText("");
                    }
                }
            }

            @Override
            public void onFailed(ApiResponse<List<Integer>> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        }, selectLists);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_new;
    }

}
