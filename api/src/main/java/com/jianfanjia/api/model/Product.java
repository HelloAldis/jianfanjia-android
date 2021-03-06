package com.jianfanjia.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class Product extends BaseModel {
    private String _id;
    private String province;
    private String city;
    private String district;
    private String cell;
    private String house_type;
    private String business_house_type;
    private int house_area;
    private String dec_style;
    private String dec_type;
    private String work_type;
    private float total_price;
    private String description;
    private String designerid;
    private int __v;
    private long auth_date;
    private long create_at;
    private String auth_type;
    private int favorite_count;
    private int view_count;
    private List<ProductImageInfo> images = new ArrayList<>();
    private List<ProductImageInfo> plan_images = new ArrayList<>();
    private String cover_imageid;
    private Designer designer;
    private boolean is_my_favorite;
    private boolean is_deleted;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public int getHouse_area() {
        return house_area;
    }

    public void setHouse_area(int house_area) {
        this.house_area = house_area;
    }

    public String getDec_style() {
        return dec_style;
    }

    public void setDec_style(String dec_style) {
        this.dec_style = dec_style;
    }

    public String getDec_type() {
        return dec_type;
    }

    public void setDec_type(String dec_type) {
        this.dec_type = dec_type;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public long getAuth_date() {
        return auth_date;
    }

    public void setAuth_date(long auth_date) {
        this.auth_date = auth_date;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public List<ProductImageInfo> getImages() {
        return images;
    }

    public void setImages(List<ProductImageInfo> images) {
        this.images = images;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public boolean is_my_favorite() {
        return is_my_favorite;
    }

    public void setIs_my_favorite(boolean is_my_favorite) {
        this.is_my_favorite = is_my_favorite;
    }

    public List<ProductImageInfo> getPlan_images() {
        return plan_images;
    }

    public void setPlan_images(List<ProductImageInfo> plan_images) {
        this.plan_images = plan_images;
    }

    public String getCover_imageid() {
        return cover_imageid;
    }

    public void setCover_imageid(String cover_imageid) {
        this.cover_imageid = cover_imageid;
    }

    public String getBusiness_house_type() {
        return business_house_type;
    }

    public void setBusiness_house_type(String business_house_type) {
        this.business_house_type = business_house_type;
    }

    public boolean is_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
}
