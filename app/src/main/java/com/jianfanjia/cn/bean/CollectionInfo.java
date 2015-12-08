package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: CollectionInfo
 * User: fengliang
 * Date: 2015-12-08
 * Time: 16:26
 */
public class CollectionInfo implements Serializable {
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
