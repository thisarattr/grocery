package com.thisarattr.grocery.integration;

import com.thisarattr.grocery.models.ListCategoryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void shouldReturnAllCategories() {
        ListCategoryResponse listCategoryResponse = restTemplate.getForObject(getUrl("/categories"), ListCategoryResponse.class);

        assertThat(listCategoryResponse.getCategories(), hasSize(5));
    }

    private String getUrl(String path) {
        return "http://localhost:" + port + "/api/v1" + path;
    }
}