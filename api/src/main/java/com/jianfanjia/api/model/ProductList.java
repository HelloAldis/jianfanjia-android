package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class ProductList extends BaseModel {
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
