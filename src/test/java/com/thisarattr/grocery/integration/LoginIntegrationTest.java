package com.thisarattr.grocery.integration;

import com.thisarattr.grocery.models.LoginRequest;
import com.thisarattr.grocery.models.LoginResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void shouldLoginWhenCorrectCredentialsProvided() {
        LoginRequest loginReq = new LoginRequest("user", "password");
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange(getUrl("/login"), HttpMethod.POST, loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(200));
        assertNotNull(loginResEntity.getBody());
        assertNotNull(loginResEntity.getBody().getJwt());
    }

    @Test
    public void shouldFailLoginWhenIncorrectCredentialsProvided() {
        LoginRequest loginReq = new LoginRequest("user", "password1");
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange(getUrl("/login"), HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(401));
        assertNotNull(loginResEntity.getBody());
        assertThat(loginResEntity.getBody().getMessage(), is("Incorrect username or password"));
    }

    @Test
    public void shouldFailLoginWhenMandatoryFieldsNotProvided() {
        LoginRequest loginReq = new LoginRequest("user", null);
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange(getUrl("/login"), HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(400));
        assertNotNull(loginResEntity.getBody());
        assertThat(loginResEntity.getBody().getMessage(), is("Username and password are mandatory"));
    }

    private String getUrl(String path) {
        return "http://localhost:" + port + "/api/v1" + path;
    }
}
