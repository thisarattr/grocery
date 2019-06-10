package com.thisarattr.grocery.services;

import com.thisarattr.grocery.exception.APIException;
import com.thisarattr.grocery.repositories.User;
import com.thisarattr.grocery.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class UserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${security.jwt.secret}")
    private String secretKey;
    @Value("${security.jwt.expiry.millis}")
    private Long jwtExpiry;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bcryptPasswordEncoder;
    }

    public String login(String username, String password) throws APIException {
        if (username == null || password == null) {
            log.debug("login failed, username or password missing");
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Username and password are mandatory");
        }

        Optional<User> user = userRepository.findByUsername(username.trim().toLowerCase());
        if (!user.isPresent()) {
            log.debug("login failed, user not found for the user: " + username);
            throw new APIException(HttpStatus.UNAUTHORIZED.value(), "User not found");
        }

        if (!bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            log.debug("login failed, incorrect password for the user: " + username);
            throw new APIException(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password");
        }

        Date issuedAt = new Date();
        String jwt =  Jwts.builder()
                .setSubject(user.get().getUsername())
                .claim("role", user.get().getRole())
                .claim("username", user.get().getUsername())
                .setId("jti" + RandomStringUtils.random(7, true, true))
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + jwtExpiry))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuer("abc grocery")
                .compact();

        return jwt;
    }
}
