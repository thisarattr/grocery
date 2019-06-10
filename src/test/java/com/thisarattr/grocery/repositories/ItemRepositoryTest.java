package com.thisarattr.grocery.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldGetUser() {
        Iterable<Category> categoryList = categoryRepository.findAll();
        Category category = categoryList.iterator().next();
        Set<Category> catSet = new HashSet<Category>();
        catSet.add(category);

        Item item = new Item();
        item.setName("Lamb Chops");
        item.setDescription("Lamb Chops 500g");
        item.setDimensions("15*15cm");
        item.setMadeIn("Australia");
        item.setPrice(new Double(10.00));
        item.setSku("34872387sf834723");
        item.setCategories(catSet);
        item.setCreatedOn(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);

        assertNotNull(item.getId());
        assertNotNull(item.getCreatedOn());
        assertNotNull(item.getUpdatedOn());

        Optional<Item> optionalItem = itemRepository.findById(savedItem.getId());
        if(!optionalItem.isPresent()) {
            fail("Save item not found");
        }
        Item retrievedItem = optionalItem.get();

        assertThat(retrievedItem.getId(), is(item.getId()));
        assertThat(retrievedItem.getCreatedOn(), is(item.getCreatedOn()));
        assertThat(retrievedItem.getUpdatedOn(), is(item.getUpdatedOn()));
        assertThat(retrievedItem.getCategories(), is(item.getCategories()));
        assertThat(retrievedItem.getName(), is(item.getName()));
        assertThat(retrievedItem.getPrice(), is(item.getPrice()));
        assertThat(retrievedItem.getSku(), is(item.getSku()));
        assertThat(retrievedItem.getDescription(), is(item.getDescription()));
        assertThat(retrievedItem.getDimensions(), is(item.getDimensions()));
        assertThat(retrievedItem.getMadeIn(), is(item.getMadeIn()));
    }

    @Test
    public void shouldGetAllUser() {
        List<Item> itemList = new ArrayList<>();
        itemRepository.findAll().forEach(itemList::add);

        assertThat(itemList, hasSize(3));
    }
}
