package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: DesignerHomePageRequest
 * User: fengliang
 * Date: 2015-10-20
 * Time: 16:27
 */
public class DesignerHomePageRequest extends BaseRequest {

    private String designerid;

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }
}
