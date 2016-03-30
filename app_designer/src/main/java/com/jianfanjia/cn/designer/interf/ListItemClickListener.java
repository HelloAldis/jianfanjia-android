package com.jianfanjia.cn.designer.interf;

import com.jianfanjia.cn.designer.bean.OrderDesignerInfo;

/**
 * Created by Administrator on 2015/10/15.
 */
public interface ListItemClickListener {

    void onMaxClick(int position);

    void onMinClick(int position);

    void onItemClick(int itemPosition, OrderDesignerInfo orderDesignerInfo);

    void onClick();

}
