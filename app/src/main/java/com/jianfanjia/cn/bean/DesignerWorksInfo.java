package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerWorksInfo
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:41
 */
public class DesignerWorksInfo implements Serializable {
    private Product product;
    private String total;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
