package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @author zhanghao
 * @version 1.0
 * @Description: 此类是工地实体类
 * @date 2015-8-19 下午4:20:58
 */
public class OwnerSiteInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String userPhone;// 业主电话

    private String designerPhone;// 设计师电话

    private String city;// 所在城市

    private String district;// 所在区

    private String villageName;// 小区名称

    private String houseStyle;// 户型

    private String houseSize;// 房子面积

    private String loveStyle;// 风格喜好

    private String decorationStyle;// 装修类型

    private String decorationBudget;// 装修预算

    private String startDate;// 开工日期

    private int totalDate;// 总工期

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDesignerPhone() {
        return designerPhone;
    }

    public void setDesignerPhone(String designerPhone) {
        this.designerPhone = designerPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getHouseStyle() {
        return houseStyle;
    }

    public void setHouseStyle(String houseStyle) {
        this.houseStyle = houseStyle;
    }

    public String getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(String houseSize) {
        this.houseSize = houseSize;
    }

    public String getLoveStyle() {
        return loveStyle;
    }

    public void setLoveStyle(String loveStyle) {
        this.loveStyle = loveStyle;
    }

    public String getDecorationStyle() {
        return decorationStyle;
    }

    public void setDecorationStyle(String decorationStyle) {
        this.decorationStyle = decorationStyle;
    }

    public String getDecorationBudget() {
        return decorationBudget;
    }

    public void setDecorationBudget(String decorationBudget) {
        this.decorationBudget = decorationBudget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getTotalDate() {
        return totalDate;
    }

    public void setTotalDate(int totalDate) {
        this.totalDate = totalDate;
    }

}
