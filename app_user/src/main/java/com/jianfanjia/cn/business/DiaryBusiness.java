package com.jianfanjia.cn.business;

import android.animation.Animator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.interf.AddFavoriteCallback;

/**
 * Description: com.jianfanjia.cn.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-16 10:45
 */
public class DiaryBusiness {

    public static final int UPLOAD_MAX_PIC_COUNT = 9;//上传最大的图片数为9张
    public static final int REQUIRE_REFRESH_TIME = 1000 * 60 * 5;//老数据刷新时间间隔

    public static String getDiarySetDes(DiarySetInfo diarySetInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(diarySetInfo.getHouse_area() + "㎡  ");
        String showHouseType = BusinessCovertUtil.convertHouseTypeToShow(diarySetInfo.getHouse_type());
        if (!TextUtils.isEmpty(showHouseType)) {
            stringBuilder.append(showHouseType + "  ");
        }
        String showDecStyle = BusinessCovertUtil.convertDecStyleToShow(diarySetInfo.getDec_style());
        if (!TextUtils.isEmpty(showDecStyle)) {
            stringBuilder.append(showDecStyle + "  ");
        }
        String showWorkType = BusinessCovertUtil.convertWorktypeToShow(diarySetInfo.getWork_type());
        if (!TextUtils.isEmpty(showWorkType)) {
            stringBuilder.append(showWorkType);
        }
        return stringBuilder.toString();
    }

    public static String getShowDiarySectionLabel(String sectionLabel) {
        return sectionLabel + "阶段";
    }

    public static boolean isDiarySetStageFinish(String section) {
        if (!TextUtils.isEmpty(section) && section.equals("入住")) {
            return true;
        }
        return false;
    }

    public static String getCommentCountShow(int count) {
        if (count == 0) return MyApplication.getInstance().getString(R.string.commentText);
        return count + "";
    }

    public static String getFavoriteCountShow(int count) {
        if (count == 0) {
            return MyApplication.getInstance().getString(R.string.add_favorite);
        }
        return count + "";
    }

    public static void setFavoriteAction(final ImageView imageView, LinearLayout canClickLayout, boolean isfavorite,
                                         final AddFavoriteCallback addFavoriteCallback) {
        if (isfavorite) {
            imageView.setImageResource(R.mipmap.icon_diary_favorite1);
            canClickLayout.setOnClickListener(null);
        } else {
            imageView.setImageResource(R.mipmap.icon_diary_favorite0);
            canClickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiHelper.imageAddFavoriteAnim(imageView, new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            imageView.setImageResource(R.mipmap.icon_diary_favorite1);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (addFavoriteCallback != null) {
                                addFavoriteCallback.addFavoriteAction();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            });
        }
    }

}
