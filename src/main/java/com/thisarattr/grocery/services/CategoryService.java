package com.thisarattr.grocery.services;

import com.thisarattr.grocery.exception.APIException;
import com.thisarattr.grocery.models.ListCategoryResponse;
import com.thisarattr.grocery.repositories.Category;
import com.thisarattr.grocery.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryRepository.findAll().forEach(categoryList::add);

        return categoryList;
    }

    public Category getCategory(String name) throws APIException {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new APIException(400, "Category not found for name: " + name));
    }
}
