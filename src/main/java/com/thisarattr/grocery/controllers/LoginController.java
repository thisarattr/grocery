package com.thisarattr.grocery.controllers;

import com.thisarattr.grocery.exception.APIException;
import com.thisarattr.grocery.models.LoginRequest;
import com.thisarattr.grocery.models.LoginResponse;
import com.thisarattr.grocery.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private String Test_test;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Login will validate credentials and if success return the jwt token for subsequent api calls.
     * @param loginRequest request object with username and password
     * @return 200 if login success, 400 if mandatory fields are not provided
     *          401 if user not found in the database or incorrect password.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws APIException {
        String jwt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new LoginResponse(jwt, loginRequest.getUsername());
    }
}
