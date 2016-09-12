package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.designer
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-31 14:10
 */
public class AddImageToCheckRequest extends BaseRequest {

    private String _id;
    private String section;
    private String key;
    private String imageid;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }
}
