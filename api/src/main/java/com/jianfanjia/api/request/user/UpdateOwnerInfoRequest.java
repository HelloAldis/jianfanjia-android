package com.jianfanjia.api.request.user;

import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: UpdateOwnerInfoRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 16:08
 */

public class UpdateOwnerInfoRequest extends BaseRequest {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
