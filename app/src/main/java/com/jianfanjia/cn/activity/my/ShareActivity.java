package com.jianfanjia.cn.activity.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.FileUtil;
import com.jianfanjia.common.tool.ImageUtil;

public class ShareActivity extends SwipeBackActivity implements OnLongClickListener {
    private static final String TAG = ShareActivity.class.getName();

    @Bind(R.id.my_share_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.share_qr)
    ImageView mIvCode;// 当前版本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setListener();
    }

    private void initView() {
        initMainHeadView();
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.share_title));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    private void setListener() {
        mIvCode.setOnLongClickListener(this);
    }

    @OnClick(R.id.head_back_layout)
    public void onClick() {
        appManager.finishActivity(this);
    }

    @Override
    public boolean onLongClick(View v) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.icon_jianfanjia_qr);
        try {
            ImageUtil.saveImageToSD(ShareActivity.this, FileUtil.IMAG_PATH
                    + "myqr.jpg", bitmap, 100);
            makeTextShort(getResources().getString(R.string.save_image_success));
        } catch (IOException e) {
            e.printStackTrace();
            makeTextShort(getResources().getString(R.string.save_image_failure));
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share;
    }
}