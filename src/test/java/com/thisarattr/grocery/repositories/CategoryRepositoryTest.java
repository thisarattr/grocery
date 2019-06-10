package com.thisarattr.grocery.repositories;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldGetAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryRepository.findAll().forEach(categoryList::add);

        assertThat(categoryList, hasSize(5));
    }

    @Test
    public void shouldCreateCategories() {
        String categoryName = "test name";
        String categoryDesc = "test description";
        Category category = new Category();
        category.setName(categoryName);
        category.setDescription(categoryDesc);
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedOn());
        assertNotNull(category.getUpdatedOn());

        Optional<Category> getCat = categoryRepository.findById(category.getId());
        if(!getCat.isPresent()) {
            fail("Saved category, was not found: " + category.getId());
        }

        Category retrievedCategory = getCat.get();
        assertThat(retrievedCategory.getName(), is(categoryName));
        assertThat(retrievedCategory.getDescription(), is(categoryDesc));
        assertThat(retrievedCategory.getId(), is(category.getId()));
        assertThat(retrievedCategory.getCreatedOn(), is(category.getCreatedOn()));
        assertThat(retrievedCategory.getUpdatedOn(), is(category.getUpdatedOn()));
    }

    @Test
    public void shouldFailWhenMandatoryFieldsNotPresent() {
        String categoryDesc = "test description";
        Category category = new Category();
        category.setDescription(categoryDesc);

        expectedEx.expect(DataIntegrityViolationException.class);

        categoryRepository.save(category);
    }

    @Test
    public void shouldFindCategoryByName() {
        String name = "dairy";
        Optional<Category> cats = categoryRepository.findByNameIgnoreCase(name);
        if(!cats.isPresent()) {
            fail("category not found: " + name);
        }
        assertNotNull(cats.get());
        assertThat(cats.get().getItems(), hasSize(2));
    }

}
