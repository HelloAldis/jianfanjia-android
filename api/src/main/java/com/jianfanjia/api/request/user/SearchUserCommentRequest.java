package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-10 10:00
 */
public class SearchUserCommentRequest extends BaseRequest {
    private int from;
    private int limit;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
