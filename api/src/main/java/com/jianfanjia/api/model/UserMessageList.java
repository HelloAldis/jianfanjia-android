package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class UserMessageList extends BaseModel {

    private List<UserMessage> list;
    private int total;

    public List<UserMessage> getList() {
        return list;
    }

    public void setList(List<UserMessage> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
