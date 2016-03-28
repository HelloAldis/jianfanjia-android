package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Name: CommentList
 * User: fengliang
 * Date: 2015-10-28
 * Time: 17:01
 */
public class CommentList implements Serializable {

    private static final long serialVersionUID = -7792774353344872678L;

    private List<Comment> comments;
    private int total;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
