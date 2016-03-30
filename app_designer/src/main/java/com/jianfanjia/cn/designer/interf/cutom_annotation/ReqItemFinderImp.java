package com.jianfanjia.cn.designer.interf.cutom_annotation;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.config.Constant;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringArrayRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.interf.cutom_annotation
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 09:44
 */
@EBean
public class ReqItemFinderImp implements ReqItemFinder {

    @StringArrayRes(R.array.arr_district)
    protected String[] arr_district;

    @StringArrayRes(R.array.arr_worktype)
    protected String[] arr_worktype;

    @StringArrayRes(R.array.arr_housetype)
    protected String[] arr_housetype;

    @StringArrayRes(R.array.arr_person)
    protected String[] arr_person;

    @StringArrayRes(R.array.arr_love_designerstyle)
    protected String[] arr_love_designerstyle;

    @StringArrayRes(R.array.arr_decstyle)
    protected String[] arr_lovestyle;

    @StringArrayRes(R.array.arr_desisex)
    protected String[] arr_desisex;

    @StringArrayRes(R.array.arr_busi_housetype)
    protected String[] arr_busihousetype;

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

    public List<ItemMap> getListByStringArray(String[] strings){
        List<ItemMap> itemMaps = new ArrayList<>();
        ItemMap itemMap = null;
        for(int i = 0; i<strings.length ; i++){
            itemMap = new ItemMap(i + "",strings[i]);
            itemMaps.add(itemMap);
        }
        return itemMaps;
    }

    //属性中有其他字段的解析方法
    public List<ItemMap> getListByStringArrayHasOther(String[] strings){
        List<ItemMap> itemMaps = new ArrayList<>();
        ItemMap itemMap = null;
        for(int i = 0; i<strings.length-1; i++){
            itemMap = new ItemMap(i + "",strings[i]);
            itemMaps.add(itemMap);
        }
        itemMap = new ItemMap("9999",strings[strings.length-1]);
        itemMaps.add(itemMap);
        return itemMaps;
    }


    public static class ItemMap implements Serializable{
        public final String key;
        public final String value;

        public ItemMap(String key,String value){
            this.key = key;
            this.value = value;
        }
    }
}
