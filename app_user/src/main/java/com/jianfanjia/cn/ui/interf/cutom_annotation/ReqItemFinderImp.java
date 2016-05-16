package com.jianfanjia.cn.ui.interf.cutom_annotation;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;

/**
 * Description: com.jianfanjia.cn.interf.cutom_annotation
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 09:44
 */
public class ReqItemFinderImp implements ReqItemFinder {

    protected String[] arr_district;

    protected String[] arr_worktype;

    protected String[] arr_housetype;

    protected String[] arr_person;

    protected String[] arr_love_designerstyle;

    protected String[] arr_lovestyle;

    protected String[] arr_desisex;

    protected String[] arr_busihousetype;

    public ReqItemFinderImp(Context context) {
        arr_district = context.getResources().getStringArray(R.array.arr_district);
        arr_worktype = context.getResources().getStringArray(R.array.arr_show_worktype);
        arr_housetype = context.getResources().getStringArray(R.array.arr_housetype);
        arr_person = context.getResources().getStringArray(R.array.arr_person);
        arr_love_designerstyle = context.getResources().getStringArray(R.array.arr_love_designerstyle);
        arr_lovestyle = context.getResources().getStringArray(R.array.arr_decstyle);
        arr_desisex = context.getResources().getStringArray(R.array.arr_desisex);
        arr_busihousetype = context.getResources().getStringArray(R.array.arr_busi_housetype);
    }

    @Override
    public List<ItemMap> findAll(int requirecode) {
        switch (requirecode) {
            case Constant.REQUIRECODE_CITY:
                return getListByStringArray(arr_district);
            case Constant.REQUIRECODE_HOUSETYPE:
                return getListByStringArray(arr_housetype);
            case Constant.REQUIRECODE_PERSONS:
                return getListByStringArray(arr_person);
            case Constant.REQUIRECODE_LOVEDESISTYLE:
                return getListByStringArray(arr_love_designerstyle);
            case Constant.REQUIRECODE_BUSI_DECORATETYPE:
                return getListByStringArrayHasOther(arr_busihousetype);
            case Constant.REQUIRECODE_LOVESTYLE:
                return getListByStringArray(arr_lovestyle);
            case Constant.REQUIRECODE_DESISEX:
                return getListByStringArray(arr_desisex);
            case Constant.REQUIRECODE_WORKTYPE:
                return getListByStringArray(arr_worktype);
        }
        return null;
    }

    public List<ItemMap> getListByStringArray(String[] strings) {
        List<ItemMap> itemMaps = new ArrayList<>();
        ItemMap itemMap = null;
        for (int i = 0; i < strings.length; i++) {
            itemMap = new ItemMap(i + "", strings[i]);
            itemMaps.add(itemMap);
        }
        return itemMaps;
    }

    //属性中有其他字段的解析方法
    public List<ItemMap> getListByStringArrayHasOther(String[] strings) {
        List<ItemMap> itemMaps = new ArrayList<>();
        ItemMap itemMap = null;
        for (int i = 0; i < strings.length - 1; i++) {
            itemMap = new ItemMap(i + "", strings[i]);
            itemMaps.add(itemMap);
        }
        itemMap = new ItemMap("9999", strings[strings.length - 1]);
        itemMaps.add(itemMap);
        return itemMaps;
    }


    public static class ItemMap implements Serializable {
        public final String key;
        public final String value;

        public ItemMap(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
