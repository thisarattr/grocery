package com.thisarattr.grocery.repositories;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldGetAllUser() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        assertThat(users, hasSize(2));
    }

    @Test
    public void shouldCreateUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole("role");
        userRepository.save(user);

        assertNotNull(user.getId());
        assertNotNull(user.getCreatedOn());
        assertNotNull(user.getUpdatedOn());

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if(!optionalUser.isPresent()) {
            fail("Saved user not found");
        }

        User retrievedUser = optionalUser.get();

        assertThat(retrievedUser.getUsername(), is(user.getUsername()));
        assertThat(retrievedUser.getPassword(), is(user.getPassword()));
        assertThat(retrievedUser.getUsername(), is(user.getUsername()));
        assertThat(retrievedUser.getId(), is(user.getId()));
        assertThat(retrievedUser.getCreatedOn(), is(user.getCreatedOn()));
        assertThat(retrievedUser.getUpdatedOn(), is(user.getUpdatedOn()));
    }

    @Test
    public void shouldFailWhenMandatoryFieldsNotPresent() {
        User user = new User();
        user.setPassword("password");
        user.setRole("role");

        expectedEx.expect(DataIntegrityViolationException.class);

        userRepository.save(user);
    }
}
