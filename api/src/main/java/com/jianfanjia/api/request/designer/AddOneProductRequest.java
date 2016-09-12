package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.designer
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 17:13
 */
public class AddOneProductRequest extends BaseRequest {

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
