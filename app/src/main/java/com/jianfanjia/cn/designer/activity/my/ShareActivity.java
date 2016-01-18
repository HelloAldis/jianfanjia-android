package com.jianfanjia.cn.designer.activity.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.tools.ImageUtil;
import com.jianfanjia.cn.designer.view.MainHeadView;

import java.io.IOException;

public class ShareActivity extends BaseActivity implements OnClickListener,
        OnLongClickListener {
    private static final String TAG = ShareActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ImageView mIvCode = null;// 当前版本

    @Override
    public void initView() {
        initMainHeadView();
        mIvCode = (ImageView) findViewById(R.id.share_qr);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_share_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.share_title));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        mIvCode.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.icon_jianfanjia_qr);
        try {
            ImageUtil.saveImageToSD(ShareActivity.this, Constant.IMAG_PATH
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