package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: DesignerWorksInfo
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:41
 */
public class DesignerWorksInfo implements Serializable {
    private static final long serialVersionUID = 5438941915418246822L;
    private List<Product> products;
    private int total;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
