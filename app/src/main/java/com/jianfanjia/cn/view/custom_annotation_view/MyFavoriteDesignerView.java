package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 09:39
 */
@EViewGroup(R.layout.list_item_my_favorite_designer)
public class MyFavoriteDesignerView extends LinearLayout {

    @ViewById(R.id.ltm_myfavdesi_name)
    TextView ltm_myfavdesi_name;

    @ViewById(R.id.ltm_myfavdesi_head)
    ImageView ltm_myfavdesi_head;

    @ViewById(R.id.ltm_myfavdesi_score)
    RatingBar ltm_myfavdesi_score;

    public MyFavoriteDesignerView(Context context) {
        super(context);
    }

    public void bind(DesignerInfo designerInfo) {
        ltm_myfavdesi_name.setText(TextUtils.isEmpty(designerInfo.getUsername()) ? getResources().getString(R.string.designer) : designerInfo.getUsername());
        String imageid = designerInfo.getImageid();
        if(!TextUtils.isEmpty(imageid)){
            ImageLoader.getInstance().displayImage(Url.GET_IMAGE + imageid,ltm_myfavdesi_head);
        }else{
            ImageLoader.getInstance().displayImage(Constant.DEFALUT_DESIGNER_PIC,ltm_myfavdesi_head);
        }
        ltm_myfavdesi_score.setRating(designerInfo.getScore());
    }
}
