package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.common.CommentListActivity_;
import com.jianfanjia.cn.activity.my.BindingAccountActivity_;
import com.jianfanjia.cn.activity.my.CollectActivity;
import com.jianfanjia.cn.activity.my.CustomerServiceActivity;
import com.jianfanjia.cn.activity.my.FeedBackActivity;
import com.jianfanjia.cn.activity.my.NotifyActivity;
import com.jianfanjia.cn.activity.my.SettingActivity;
import com.jianfanjia.cn.activity.my.UserInfoActivity_;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * Description:我的
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyNewFragment extends BaseFragment {
    private static final String TAG = MyNewFragment.class.getName();
    public static final int REQUESTCODE_USERINFO = 0;
    private RelativeLayout notifyLayout = null;
    private RelativeLayout my_collect_layout = null;
    private RelativeLayout setting_layout = null;
    private RelativeLayout kefu_layout = null;
    private RelativeLayout my_info_layout = null;
    private RelativeLayout feedback_layout = null;
    private RelativeLayout clearCacheLayout = null;
    private RelativeLayout callPhoneLayout = null;
    private RelativeLayout commentLayout = null;
    private RelativeLayout bindingAccountLayout = null;
    private TextView cacheSizeView = null;
    private ScrollView scrollView = null;
    private ImageView head_img = null;
    private ImageView user_head_img = null;
    private TextView my_name = null;
    private TextView my_account = null;

    @Override
    public void initView(View view) {
        notifyLayout = (RelativeLayout) view.findViewById(R.id.notify_layout);
        my_collect_layout = (RelativeLayout) view.findViewById(R.id.collect_layout);
        setting_layout = (RelativeLayout) view.findViewById(R.id.setting_layout);
        my_info_layout = (RelativeLayout) view.findViewById(R.id.frag_my_info_layout);
        kefu_layout = (RelativeLayout) view.findViewById(R.id.kefu_layout);
        feedback_layout = (RelativeLayout) view.findViewById(R.id.feedback_layout);
        clearCacheLayout = (RelativeLayout) view.findViewById(R.id.clear_cache_layout);
        cacheSizeView = (TextView) view.findViewById(R.id.cache_size);
        callPhoneLayout = (RelativeLayout) view.findViewById(R.id.call_layout);
        commentLayout = (RelativeLayout) view.findViewById(R.id.comment_layout);
        bindingAccountLayout = (RelativeLayout) view.findViewById(R.id.binding_account_layout);
        head_img = (ImageView) view.findViewById(R.id.head_img);
        user_head_img = (ImageView) view.findViewById(R.id.user_head_img);
        my_account = (TextView) view.findViewById(R.id.frag_my_account);
        my_name = (TextView) view.findViewById(R.id.frag_my_name);

        scrollView = (ScrollView)view.findViewById(R.id.setting_scrollview);

        cacheSizeView.setText(UiHelper.caculateCacheSize());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void initMyInfo() {
        String imgPath = dataManager.getUserImagePath();
        LogTool.d(TAG, "imgPath=" + imgPath);
        if (!imgPath.contains(Constant.DEFALUT_PIC_HEAD)) {
//            imageShow.displayScreenWidthThumnailImage(getActivity(), imgPath, head_img);
            imageShow.displayImageHeadWidthThumnailImage(getActivity(), imgPath, user_head_img);
        } else {
            user_head_img.setImageResource(R.mipmap.icon_default_head);
        }
    }

    @Override
    public void setListener() {
        notifyLayout.setOnClickListener(this);
        my_collect_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);
        my_info_layout.setOnClickListener(this);
        kefu_layout.setOnClickListener(this);
        feedback_layout.setOnClickListener(this);
        clearCacheLayout.setOnClickListener(this);
        callPhoneLayout.setOnClickListener(this);
        commentLayout.setOnClickListener(this);
        bindingAccountLayout.setOnClickListener(this);

        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMyInfo();
        my_name.setText(TextUtils.isEmpty(dataManager.getUserName()) ? getResources().getString(R.string.ower) : dataManager.getUserName());
        my_account.setText(TextUtils.isEmpty(dataManager.getAccount()) ? "" : "账号：" + dataManager.getAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_layout:
                startActivity(NotifyActivity.class);
                break;
            case R.id.collect_layout:
                startActivity(CollectActivity.class);
                break;
            case R.id.frag_my_info_layout:
                startActivityForResult(UserInfoActivity_.class, REQUESTCODE_USERINFO);
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
            case R.id.clear_cache_layout:
                onClickCleanCache();
                break;
            case R.id.call_layout:
                UiHelper.callPhoneIntent(getContext(), "15927163098");
                break;
            case R.id.comment_layout:
                startActivity(CommentListActivity_.class);
                break;
            case R.id.binding_account_layout:
                startActivity(BindingAccountActivity_.class);
                break;
            default:
                break;
        }
    }

    /**
     * 清空缓存
     */
    private void onClickCleanCache() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("清空缓存");
        dialog.setMessage("确定清空缓存吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().clearAppCache();
                        cacheSizeView.setText("0KB");
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_new;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUESTCODE_USERINFO:
                initMyInfo();
                break;
        }
    }
}
