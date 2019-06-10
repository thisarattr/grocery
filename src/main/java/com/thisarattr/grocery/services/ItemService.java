package com.thisarattr.grocery.services;

import com.thisarattr.grocery.exception.APIException;
import com.thisarattr.grocery.repositories.Category;
import com.thisarattr.grocery.repositories.CategoryRepository;
import com.thisarattr.grocery.repositories.Item;
import com.thisarattr.grocery.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    public Item create(Item item) throws APIException {
        validateItem(item);
        return itemRepository.save(item);
    }

    public Item getItem(Long itemId) throws APIException {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST.value(), "Item not found for id: " + itemId));
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public List<Item> listItems(String categoryName) throws APIException {
        List<Item> itemList = new ArrayList<>();
        if (categoryName == null) {
            itemRepository.findAll().forEach(itemList::add);
        } else {
            Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                    .orElseThrow(() -> new APIException(400, "Category not found for name: " + categoryName));
            category.getItems().forEach(itemList::add);
        }

        return itemList;
    }

    @Transactional
    public Item updateItem(Long id, Item item) throws APIException {
        validateItem(item);

        if (item.getId() == null || !id.equals(item.getId())) {
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Item id mismatch with payload");
        }

        Item retrieved = itemRepository.findById(id).orElseThrow(() -> new APIException(400, "Item not found"));

        //set values to be saved
        retrieved.setName(item.getName());
        retrieved.setSku(item.getSku());
        retrieved.setCategories(item.getCategories());
        retrieved.setPrice(item.getPrice());
        retrieved.setDescription(item.getDescription());
        retrieved.setMadeIn(item.getMadeIn());
        retrieved.setDimensions(item.getDimensions());

        return retrieved;
    }

    private void validateItem(Item item) throws APIException {
        if (item.getName() == null || item.getSku() == null || item.getPrice() == null) {
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "name, sku and price are mandatory fields");
        }
        if (item.getCategories() != null) {
            Set<Category> fetchedCategories = new HashSet<>();
            for (Category c : item.getCategories()) {
                fetchedCategories.add(categoryRepository.findById(c.getId()).orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST.value(), "invalid category " + c.getId())));
            }
            item.setCategories(fetchedCategories);
        }
    }
}
