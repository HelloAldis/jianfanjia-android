package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.guest
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-09 17:42
 */
public class GetRecommendDiarySetRequest extends BaseRequest {

    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
