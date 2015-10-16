package com.jianfanjia.cn.interf.cutom_annotation;

import com.jianfanjia.cn.activity.EditRequirementActivity;
import com.jianfanjia.cn.activity.R;

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

    @StringArrayRes(R.array.arr_decoratestyle)
    protected String[] arr_decoratestyle;

    @StringArrayRes(R.array.arr_housetype)
    protected String[] arr_housetype;

    @StringArrayRes(R.array.arr_person)
    protected String[] arr_person;

    @StringArrayRes(R.array.arr_love_designerstyle)
    protected String[] arr_love_designerstyle;

    @StringArrayRes(R.array.arr_lovestyle)
    protected String[] arr_lovestyle;

    @Override
    public List<ItemMap> findAll(int requirecode) {
        switch (requirecode) {
            case EditRequirementActivity.REQUIRECODE_CITY:
                return getListByStringArray(arr_district);
            case EditRequirementActivity.REQUIRECODE_HOUSETYPE:
                return getListByStringArray(arr_housetype);
            case EditRequirementActivity.REQUIRECODE_PERSONS:
                return getListByStringArray(arr_person);
            case EditRequirementActivity.REQUIRECODE_LOVEDESISTYLE:
                return getListByStringArray(arr_love_designerstyle);
            case EditRequirementActivity.REQUIRECODE_DECORATETYPE:
                return getListByStringArray(arr_decoratestyle);
            case EditRequirementActivity.REQUIRECODE_LOVESTYLE:
                return getListByStringArray(arr_lovestyle);
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

    public static class ItemMap implements Serializable{
        public final String key;
        public final String value;

        public ItemMap(String key,String value){
            this.key = key;
            this.value = value;
        }
    }
}
