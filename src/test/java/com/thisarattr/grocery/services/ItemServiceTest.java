package com.thisarattr.grocery.services;

import com.thisarattr.grocery.exception.APIException;
import com.thisarattr.grocery.repositories.Category;
import com.thisarattr.grocery.repositories.CategoryRepository;
import com.thisarattr.grocery.repositories.Item;
import com.thisarattr.grocery.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldDelegateToRepoWhenCreateItem() throws APIException {
        Item item = createItem();
        Category category = new Category();
        when(categoryRepository.findById(item.getCategories().iterator().next().getId())).thenReturn(Optional.of(category));

        itemService.create(item);

        assertThat(item.getCategories().iterator().next(), is(category));
        verify(categoryRepository).findById(1L);
        verify(itemRepository).save(item);
    }

    @Test
    public void shouldCreateItemWhenCategoryNotNotPresent() throws APIException {
        Item item = createItem();
        item.setCategories(null);

        itemService.create(item);

        assertNull(item.getCategories());
        verify(categoryRepository, never()).findById(any());
        verify(itemRepository).save(item);
    }

    @Test
    public void shouldFailCreateItemWhenMandatoryPriceNotPresent() throws APIException {
        Item item = createItem();
        item.setPrice(null);

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("name, sku and price are mandatory fields");

        itemService.create(item);

        verify(categoryRepository, never()).findById(any());
        verify(itemRepository, never()).save(item);
    }

    @Test
    public void shouldFailCreateItemWhenMandatoryNameNotPresent() throws APIException {
        Item item = createItem();
        item.setName(null);

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("name, sku and price are mandatory fields");

        itemService.create(item);

        verify(categoryRepository, never()).findById(any());
        verify(itemRepository, never()).save(item);
    }

    @Test
    public void shouldFailCreateItemWhenMandatorySkeNotPresent() throws APIException {
        Item item = createItem();
        item.setSku(null);

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("name, sku and price are mandatory fields");

        itemService.create(item);

        verify(categoryRepository, never()).findById(any());
        verify(itemRepository, never()).save(item);
    }

    @Test
    public void shouldFailCreateItemWhenInvalidCategoryProvided() throws APIException {
        Item item = createItem();
        when(categoryRepository.findById(item.getCategories().iterator().next().getId())).thenReturn(Optional.empty());

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("invalid category 1");

        itemService.create(item);

        verify(categoryRepository).findById(1L);
        verify(itemRepository, never()).save(item);
    }

    @Test
    public void shouldDelegateToRepoWhenGetItemInvoked() throws APIException {
        long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(new Item()));

        itemService.getItem(itemId);

        verify(itemRepository).findById(itemId);
    }

    @Test
    public void shouldFailGetItemWhenItemNotFound() throws APIException {
        long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Item not found for id: 1");

        itemService.getItem(itemId);

        verify(itemRepository).findById(itemId);
    }

    @Test
    public void shouldDelegateToRepoWhenDeleteItemInvoked() throws APIException {
        long itemId = 1L;

        itemService.deleteItem(itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    public void shouldUpdateItem() throws APIException {
        String updatedName = "updated name";
        Long itemId = 1L;
        Item updateItem = createItem();
        updateItem.setName(updatedName);
        updateItem.setId(itemId);
        Category category = updateItem.getCategories().iterator().next();

        Item dbItem = createItem();
        dbItem.setId(itemId);
        when(itemRepository.findById(updateItem.getId())).thenReturn(Optional.of(dbItem));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        Item item = itemService.updateItem(updateItem.getId(), updateItem);

        assertThat(item.getName(), is(updatedName));
        verify(itemRepository).findById(itemId);
        verify(categoryRepository).findById(category.getId());
    }

    @Test
    public void shouldFailUpdateItemItemIdMismatch() throws APIException {
        String updatedName = "updated name";
        Long itemId = 1L;
        Item updateItem = createItem();
        updateItem.setName(updatedName);
        updateItem.setId(itemId);
        Category category = updateItem.getCategories().iterator().next();

        Item dbItem = createItem();
        dbItem.setId(itemId);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Item id mismatch with payload");

        Item item = itemService.updateItem(123L, updateItem);

        assertThat(item.getName(), is(updatedName));
        verify(itemRepository, never()).findById(itemId);
        verify(categoryRepository).findById(category.getId());

    }

    @Test
    public void shouldFailUpdateItemNotFound() throws APIException {
        String updatedName = "updated name";
        Long itemId = 1L;
        Item updateItem = createItem();
        updateItem.setName(updatedName);
        updateItem.setId(itemId);
        Category category = updateItem.getCategories().iterator().next();

        when(itemRepository.findById(updateItem.getId())).thenReturn(Optional.empty());
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Item not found");

        Item item = itemService.updateItem(itemId, updateItem);

        assertThat(item.getName(), is(updatedName));
        verify(itemRepository).findById(itemId);
        verify(categoryRepository).findById(category.getId());
    }

    @Test
    public void shouldListItemsWhenParamsNotPresent() throws APIException {
        Item item = createItem();
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        List<Item> items = itemService.listItems(null);

        assertThat(items.get(0), is(item));
        verify(itemRepository).findAll();
        verify(categoryRepository, never()).findByNameIgnoreCase(any());
    }

    @Test
    public void shouldListItemsWhenParamsPresent() throws APIException {
        String searchParam = "dairy";
        Item item = createItem();
        Category category = new Category();
        category.setName(searchParam);
        Set<Item> dbItems = new HashSet<>();
        dbItems.add(item);
        category.setItems(dbItems);
        when(categoryRepository.findByNameIgnoreCase(searchParam)).thenReturn(Optional.of(category));

        List<Item> items = itemService.listItems(searchParam);

        assertThat(items.get(0), is(item));
        verify(itemRepository, never()).findAll();
        verify(categoryRepository).findByNameIgnoreCase(searchParam);
    }

    @Test
    public void shouldFailListItemsWhenParamsCriteriaNotFound() throws APIException {
        String searchParam = "dairy";
        when(categoryRepository.findByNameIgnoreCase(searchParam)).thenReturn(Optional.empty());

        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Category not found for name: dairy");

        itemService.listItems(searchParam);

        verify(itemRepository, never()).findAll();
        verify(categoryRepository).findByNameIgnoreCase(searchParam);
    }


    private Item createItem() {
        Category category = new Category();
        category.setId(1L);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        Item item = new Item();
        item.setName("Lamb Chops");
        item.setDescription("Lamb Chops 500g");
        item.setDimensions("15*15cm");
        item.setMadeIn("Australia");
        item.setPrice(new Double(10.00));
        item.setSku("34872387sf834723");
        item.setCategories(categories);
        item.setCreatedOn(LocalDateTime.now());
        return item;
    }
}