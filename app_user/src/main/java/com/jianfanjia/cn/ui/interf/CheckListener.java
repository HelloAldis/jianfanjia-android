package com.jianfanjia.cn.ui.interf;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public interface CheckListener {
    void getItemData(int position, String designerid);

    void getCheckedData(List<String> designerids);
}
