package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.designer
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-20 11:37
 */
public class UpdateDesignerInfoRequest extends BaseRequest {

    private Designer designer;

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }
}
