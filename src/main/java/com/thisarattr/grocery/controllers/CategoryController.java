package com.thisarattr.grocery.controllers;

import com.thisarattr.grocery.models.ListCategoryResponse;
import com.thisarattr.grocery.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ListCategoryResponse listItems() {
        return new ListCategoryResponse(categoryService.listCategories());
    }

}
