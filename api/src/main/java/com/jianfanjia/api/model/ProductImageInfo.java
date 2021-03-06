package com.jianfanjia.api.model;

/**
 * Created by Aldis on 16/3/28.
 */
public class ProductImageInfo extends BaseModel {
    private String description;
    private String imageid;
    private String section;
    private String _id;
    private boolean isMenuOpen = false;//

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public void setIsMenuOpen(boolean isMenuOpen) {
        this.isMenuOpen = isMenuOpen;
    }
}
