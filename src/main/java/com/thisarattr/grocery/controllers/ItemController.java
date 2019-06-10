package com.thisarattr.grocery.controllers;

import com.thisarattr.grocery.exception.APIException;
import com.thisarattr.grocery.models.ListItemResponse;
import com.thisarattr.grocery.models.Response;
import com.thisarattr.grocery.repositories.Item;
import com.thisarattr.grocery.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/admin/items", method = RequestMethod.POST)
    public Item createItem(@RequestBody Item item) throws APIException {
        return itemService.create(item);
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ListItemResponse listItems(@RequestParam(value = "category", required = false) String categoryName) throws APIException {
        return new ListItemResponse(itemService.listItems(categoryName));
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public Item getItem(@PathVariable Long id) throws APIException {
        return itemService.getItem(id);
    }

    @RequestMapping(value = "/admin/items/{id}", method = RequestMethod.POST)
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) throws APIException {
        return itemService.updateItem(id, item);
    }

    @RequestMapping(value = "/admin/items/{id}", method = RequestMethod.DELETE)
    public Response deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return new Response(id, "Item deleted");
    }

}
