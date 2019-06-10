package com.thisarattr.grocery.integration;

import com.thisarattr.grocery.models.ListItemResponse;
import com.thisarattr.grocery.models.LoginRequest;
import com.thisarattr.grocery.models.LoginResponse;
import com.thisarattr.grocery.models.Response;
import com.thisarattr.grocery.repositories.Category;
import com.thisarattr.grocery.repositories.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void shouldListItems() {
        ListItemResponse listItemRes = restTemplate.getForObject(getUrl("/items"), ListItemResponse.class);

        assertThat(listItemRes.getItems(), hasSize(3));
    }

    @Test
    public void shouldListItemsByCategoryName() {
        ListItemResponse listItemRes = restTemplate.getForObject(getUrl("/items?category=dairy"), ListItemResponse.class);

        assertThat(listItemRes.getItems(), hasSize(2));
    }

    @Test
    public void shouldFailListItemsByCategoryNameWhenCategoryNotExists() {
        ResponseEntity<String> listItemRes = restTemplate.getForEntity(getUrl("/items?category=test"), String.class);

        assertThat(listItemRes.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(listItemRes.getBody(), containsString("{ \"message\": \"Category not found for name: test\" }"));
    }

    @Test
    public void shouldGetItemById() {
        Item item = restTemplate.getForObject(getUrl("/items/1"), Item.class);

        assertThat(item.getName(), is("Coca Cola"));
    }

    @Test
    public void shouldReturnNotFoundWhenGetItemByIdForNonExistingId() {
        ResponseEntity<String> item = restTemplate.getForEntity(getUrl("/items/10"), String.class);

        assertThat(item.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(item.getBody(), containsString("{ \"message\": \"Item not found for id: 10\" }"));
    }

    @Test
    public void shouldCreateItem() {
        LoginResponse loginResponse = login();
        Item item = createItem();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + loginResponse.getJwt());
        HttpEntity<Item> createItemReq = new HttpEntity<>(item, headers);

        ResponseEntity<Item> itemEntity = restTemplate.exchange(getUrl("/admin/items"), HttpMethod.POST, createItemReq, Item.class);

        assertThat(itemEntity.getStatusCode(), is(HttpStatus.OK));
        Item itemCreated = itemEntity.getBody();

        assertThat(itemCreated.getName(), is(item.getName()));
        assertThat(itemCreated.getDescription(), is(item.getDescription()));
        assertThat(itemCreated.getMadeIn(), is(item.getMadeIn()));
        assertThat(itemCreated.getDimensions(), is(item.getDimensions()));
        assertThat(itemCreated.getSku(), is(item.getSku()));
        assertThat(itemCreated.getPrice(), is(item.getPrice()));
        assertThat(itemCreated.getCategories(), hasSize(1));
        assertThat(itemCreated.getCategories().iterator().next().getId(), is(item.getCategories().iterator().next().getId()));

    }

    @Test
    public void shouldFailCreateItemWhenMandatoryFieldsAreMissing() {
        LoginResponse loginResponse = login();
        Item item = createItem();
        item.setPrice(null);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + loginResponse.getJwt());
        HttpEntity<Item> createItemReq = new HttpEntity<>(item, headers);

        ResponseEntity<String> itemEntity = restTemplate.exchange(getUrl("/admin/items"), HttpMethod.POST, createItemReq, String.class);

       assertThat(itemEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
       assertThat(itemEntity.getBody(), containsString("{ \"message\": \"name, sku and price are mandatory fields\" }"));
    }

    @Test
    public void shouldUpdateItem() {
        LoginResponse loginResponse = login();
        String descUpdated = "desc updated";
        Item item = restTemplate.getForObject(getUrl("/items/1"), Item.class);
        item.setDescription(descUpdated);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + loginResponse.getJwt());
        HttpEntity<Item> updateItemReq = new HttpEntity<>(item, headers);

        ResponseEntity<Item> itemEntity = restTemplate.exchange(getUrl("/admin/items/1"), HttpMethod.POST, updateItemReq, Item.class);

        assertThat(itemEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(itemEntity.getBody().getDescription(), is(descUpdated));

        Item fetchItem = restTemplate.getForObject(getUrl("/items/1"), Item.class);
        assertThat(fetchItem.getDescription(), is(descUpdated));
    }

    @Test
    public void shouldFailUpdateItemWhenIdInvalid() {
        LoginResponse loginResponse = login();
        String descUpdated = "desc updated";
        Item item = restTemplate.getForObject(getUrl("/items/1"), Item.class);
        item.setDescription(descUpdated);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + loginResponse.getJwt());
        HttpEntity<Item> updateItemReq = new HttpEntity<>(item, headers);

        ResponseEntity<String> itemEntity = restTemplate.exchange(getUrl("/admin/items/2"), HttpMethod.POST, updateItemReq, String.class);

        assertThat(itemEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(itemEntity.getBody(), containsString("{ \"message\": \"Item id mismatch with payload\" }"));
    }

    @Test
    public void shouldDeleteItem() {
        LoginResponse loginResponse = login();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + loginResponse.getJwt());
        HttpEntity<Item> deleteItemReq = new HttpEntity<>(null, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(getUrl("/admin/items/1"), HttpMethod.DELETE, deleteItemReq, Response.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getId(), is(1L));
    }

    @Test
    public void shouldFailDeleteItemWhenIdInvalid() {
        LoginResponse loginResponse = login();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + loginResponse.getJwt());
        HttpEntity<Item> deleteItemReq = new HttpEntity<>(null, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(getUrl("/admin/items/100"), HttpMethod.DELETE, deleteItemReq, Response.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody().getMessage(), containsString("No class com.thisarattr.grocery.repositories.Item entity with id 100 exists!"));
    }

    private LoginResponse login() {
        LoginRequest loginReq = new LoginRequest("user", "password");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, new LinkedMultiValueMap<>());
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange(getUrl("/login"), HttpMethod.POST, loginReqEntity, LoginResponse.class);

        return loginResEntity.getBody();
    }

    private String getUrl(String path) {
        return "http://localhost:" + port + "/api/v1" + path;
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