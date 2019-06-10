package com.thisarattr.grocery.models;

import com.thisarattr.grocery.repositories.Category;

import java.util.List;

public class ListCategoryResponse {

    List<Category> categories;

    public ListCategoryResponse() {
    }
    public ListCategoryResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
