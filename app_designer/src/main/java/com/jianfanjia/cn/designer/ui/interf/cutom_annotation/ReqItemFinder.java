package com.jianfanjia.cn.designer.ui.interf.cutom_annotation;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 */
public interface ReqItemFinder {

    List<ReqItemFinderImp.ItemMap> findAll(int requirecode);
}
