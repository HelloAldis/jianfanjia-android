package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.guest
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-08-16 09:32
 */
public class PostUserRequirementRequest extends BaseRequest{

    private String phone;
    private String name;
    private String district;
    private int house_area;
    private int total_price;
    private String designerid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getHouse_area() {
        return house_area;
    }

    public void setHouse_area(int house_area) {
        this.house_area = house_area;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }
}
