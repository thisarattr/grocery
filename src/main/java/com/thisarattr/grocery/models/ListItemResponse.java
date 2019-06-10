package com.thisarattr.grocery.models;

import com.thisarattr.grocery.repositories.Item;

import java.util.List;

public class ListItemResponse {

    List<Item> items;

    public ListItemResponse() {
    }

    public ListItemResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
