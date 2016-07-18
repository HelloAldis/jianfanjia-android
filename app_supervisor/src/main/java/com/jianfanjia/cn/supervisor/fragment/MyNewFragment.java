package com.jianfanjia.cn.supervisor.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.my.AboutActivity;
import com.jianfanjia.cn.supervisor.activity.my.UserInfoActivity;
import com.jianfanjia.cn.supervisor.application.MyApplication;
import com.jianfanjia.cn.supervisor.base.BaseFragment;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description:我的
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyNewFragment extends BaseFragment {
    private static final String TAG = MyNewFragment.class.getName();
    public static final int REQUESTCODE_USERINFO = 0;

    @Bind(R.id.setting_layout)
    RelativeLayout setting_layout;

    @Bind(R.id.frag_my_info_layout)
    RelativeLayout my_info_layout;

    @Bind(R.id.clear_cache_layout)
    RelativeLayout clearCacheLayout;

    @Bind(R.id.call_layout)
    RelativeLayout callPhoneLayout;


    @Bind(R.id.cache_size)
    TextView cacheSizeView;

    @Bind(R.id.setting_scrollview)
    ScrollView scrollView;

    @Bind(R.id.head_img)
    ImageView head_img;

    @Bind(R.id.user_head_img)
    ImageView user_head_img;

    @Bind(R.id.frag_my_name)
    TextView my_name;

    @Bind(R.id.frag_my_account)
    TextView my_account;

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

    private void initView() {

        cacheSizeView.setText(UiHelper.caculateCacheSize());
        //动态计算imageview的宽高
        head_img.setLayoutParams(new FrameLayout.LayoutParams((int) TDevice.getScreenWidth(), (int) (880 / (1242 /
                TDevice.getScreenWidth()))));
    }

    private void initMyInfo() {
        String imgPath = dataManager.getUserImagePath();
        LogTool.d("imgPath=" + imgPath);
        if (!imgPath.contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayImageHeadWidthThumnailImage(getActivity(), imgPath, user_head_img);
        } else {
            user_head_img.setImageResource(R.mipmap.icon_default_head);
        }
    }

    private void setListener() {
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @OnClick({R.id.frag_my_info_layout, R.id
            .setting_layout, R.id.clear_cache_layout, R.id.call_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_my_info_layout:
                IntentUtil.startActivityForResult(this, UserInfoActivity.class, REQUESTCODE_USERINFO);
                break;
            case R.id.setting_layout:
                startActivity(AboutActivity.class);
                break;
            case R.id.clear_cache_layout:
                onClickCleanCache();
                break;
            case R.id.call_layout:
                UiHelper.callPhoneIntent(getContext(), getString(R.string.app_phone));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initMyInfo();
        my_name.setText(TextUtils.isEmpty(dataManager.getUserName()) ? getResources().getString(R.string.ower) :
                dataManager.getUserName());
        my_account.setText(TextUtils.isEmpty(dataManager.getAccount()) ? "" : "手机号：" + dataManager.getAccount());
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


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_new;
    }

}
