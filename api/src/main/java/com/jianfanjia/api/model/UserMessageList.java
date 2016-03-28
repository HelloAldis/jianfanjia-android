package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class UserMessageList implements Serializable {
    private static final long serialVersionUID = 0L;

    private List<UserMessage> list;
    private int total;

    public List<UserMessage> getList() {
        return list;
    }

    public void setList(List<UserMessage> list) {
        this.list = list;
    }
}
