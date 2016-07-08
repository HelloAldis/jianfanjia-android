package com.jianfanjia.cn.bean;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-08 17:17
 */
public class DiarySetStageItem {

    private String itemName;//阶段名称
    private int count;//日记数量
    private boolean isCheck;//当前是否被选中
    private boolean isHasDiary;//是否有日记

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isHasDiary() {
        return isHasDiary;
    }

    public void setHasDiary(boolean hasDiary) {
        isHasDiary = hasDiary;
    }
}
