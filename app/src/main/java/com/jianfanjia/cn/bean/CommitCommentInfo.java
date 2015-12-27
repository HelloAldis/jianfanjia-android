package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @author zhanghao
 * @ClassName: CommitCommentInfo
 * @Description: 上传评论内容
 * @date 2015-9-7 下午7:12
 */
public class CommitCommentInfo implements Serializable {

    private static final long serialVersionUID = -511542355191874885L;
    private String _id;

    private String section;

    private String item;

    private String content;

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
