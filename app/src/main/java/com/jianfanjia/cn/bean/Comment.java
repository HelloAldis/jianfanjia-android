package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: Comment
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:01
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = -7792774353344872678L;
    private List<CommentInfo> comments;
    private int total;

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
