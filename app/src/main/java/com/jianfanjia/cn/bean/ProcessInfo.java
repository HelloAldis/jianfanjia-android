package com.jianfanjia.cn.bean;

import com.jianfanjia.cn.bean.CheckInfo.Imageid;
import com.jianfanjia.cn.tools.LogTool;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhanghao
 * @class ProcessInfo
 * @Decription 此类是工地信息类
 * @date 2015-8-28 15:30
 */
public class ProcessInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _id;
    private String requirementid;
    private String final_planid;
    private String final_designerid;
    private String province;
    private String city;
    private String district;
    private String cell;
    private String house_type;
    private String house_area;
    private String dec_style;
    private String work_type;
    private String total_price;
    private long start_at;
    private String duration;

    private String userid;

    private User user;

    private String going_on;

    private ArrayList<SectionInfo> sections;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoing_on() {
        return going_on;
    }

    public void setGoing_on(String going_on) {
        this.going_on = going_on;
    }

    public ArrayList<SectionInfo> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionInfo> sections) {
        this.sections = sections;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public String getFinal_planid() {
        return final_planid;
    }

    public void setFinal_planid(String final_planid) {
        this.final_planid = final_planid;
    }

    public String getFinal_designerid() {
        return final_designerid;
    }

    public void setFinal_designerid(String final_designerid) {
        this.final_designerid = final_designerid;
    }

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

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }

    public String getDec_style() {
        return dec_style;
    }

    public void setDec_style(String dec_style) {
        this.dec_style = dec_style;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public SectionInfo getSectionInfoByName(String sectionName) {
        LogTool.d("SectionInfo", sectionName);
        if (sections != null) {
            for (SectionInfo sectionInfo : sections) {
                if (sectionInfo.getName().equals(sectionName)) {
                    return sectionInfo;
                }
            }
        }
        return null;
    }

    public void addImageToItem(String section, String item, String imageId) {
        if (sections != null) {
            for (SectionInfo sectionInfo : sections) {
                if (sectionInfo.getName().equals(section)) {
                    LogTool.d("SectionInfo", section);
                    for (SectionItemInfo sectionItemInfo : sectionInfo.getItems()) {
                        if (sectionItemInfo.getName().equals(item)) {
                            LogTool.d("SectionItem", item);
                            sectionItemInfo.addImageToItem(imageId);
                        }
                    }
                }
            }
        }
    }

    public void addImageToCheck(String section, String key, String imageId) {
        if (sections != null) {
            for (SectionInfo sectionInfo : sections) {
                if (sectionInfo.getName().equals(section)) {
                    if (!section.equals("kai_gong") && !section.equals("chai_gai")) {
                        LogTool.d("SectionInfo", section + "--" + key + imageId);
                        sectionInfo.getYs().addImageId(new Imageid(key, imageId));
                    }
                }
            }
        }
    }

    public boolean deleteCheckImage(String section, String key) {
        boolean flag = false;
        if (sections != null) {
            for (SectionInfo sectionInfo : sections) {
                if (sectionInfo.getName().equals(section)) {
                    if (!section.equals("kai_gong") && !section.equals("chai_gai")) {
                        flag = sectionInfo.getYs().deleteImageIdBykey(key);
                        LogTool.d("SectionInfo", section + "--" + key + flag);
                    }
                }
            }
        }
        return flag;
    }

    public ArrayList<Imageid> getImageidsByName(String section) {
        if (sections != null) {
            for (SectionInfo sectionInfo : sections) {
                if (sectionInfo.getName().equals(section)) {
                    LogTool.d("SectionInfo", section);
                    if (!section.equals("kai_gong") && !section.equals("chai_gai")) {
                        return sectionInfo.getYs().getImages();
                    }
                }
            }
        }
        return null;
    }


}
