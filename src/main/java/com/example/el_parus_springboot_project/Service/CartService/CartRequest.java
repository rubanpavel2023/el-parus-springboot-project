package com.example.el_parus_springboot_project.Service.CartService;

import com.example.el_parus_springboot_project.Entity.Cart.CartArticleMap;

import java.util.List;

public class CartRequest {

    private List<CartArticleMap> items;

    public List<CartArticleMap> getItems() {
        return items;
    }

    public void setItems(List<CartArticleMap> items) {
        this.items = items;
    }
}
