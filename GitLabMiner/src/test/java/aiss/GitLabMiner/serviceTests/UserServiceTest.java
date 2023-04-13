package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Users.User;
import aiss.GitLabMiner.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    String token = "glpat-EWrMxiW1vhazpsMAsc4A";

    @Test
    @DisplayName("Get user test")
    void getUser(){
        User user = service.getUser("jack_smith", token);
        assertEquals(user.getName().trim(), "Jack Smith", "Name doesn't match.");
    }

    /*
    @Test
    @DisplayName("TEST - Post user to GitMiner from GitLab")
    void postUserTest(){
        User user = service.postUser("jack_smith", token);
        // If the post works, it will return the posted user
        assertEquals(user.getName().trim(), "Jack Smith", "Name doesn't match.");
    }
    */
}