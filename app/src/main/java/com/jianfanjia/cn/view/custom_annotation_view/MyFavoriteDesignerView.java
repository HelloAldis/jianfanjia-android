package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.BaseAnnotationView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 09:39
 */
@EViewGroup(R.layout.list_item_my_favorite_designer)
public class MyFavoriteDesignerView extends BaseAnnotationView {

    @ViewById(R.id.ltm_myfavdesi_name)
    TextView ltm_myfavdesi_name;

    @ViewById(R.id.ltm_myfavdesi_head)
    ImageView ltm_myfavdesi_head;

    @ViewById(R.id.ltm_myfavdesi_score)
    RatingBar ltm_myfavdesi_score;

    @ViewById(R.id.designerinfo_auth)
    ImageView authView;

    public MyFavoriteDesignerView(Context context) {
        super(context);
    }

    public void bind(DesignerInfo designerInfo) {
        ltm_myfavdesi_name.setText(TextUtils.isEmpty(designerInfo.getUsername()) ? getResources().getString(R.string.designer) : designerInfo.getUsername());
        String imageid = designerInfo.getImageid();
        LogTool.d(this.getClass().getName(), designerInfo.getUsername() + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context,imageid,ltm_myfavdesi_head);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, ltm_myfavdesi_head);
//            ImageLoader.getInstance().displayImage(Constant.DEFALUT_OWNER_PIC, ltm_myfavdesi_head, options);
        }
        ltm_myfavdesi_score.setRating((int) (designerInfo.getRespond_speed() + designerInfo.getService_attitude()) / 2);
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            authView.setVisibility(View.VISIBLE);
        } else {
            authView.setVisibility(View.GONE);
        }
    }
}
