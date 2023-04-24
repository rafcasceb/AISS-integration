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
    @DisplayName("Get user by name test.")
    void getUserName(){
        User user = service.getUserByName("jack_smith", token);
        assertEquals(user.getName().trim(), "Jack Smith", "Name doesn't match.");
    }

    @Test
    @DisplayName("Get user by ID test.")
    void getUserId(){
        User user = service.getUser("5023502", token);
        System.out.println(user);
        assertEquals(user.getName().trim(), "Jack Smith", "Name doesn't match.");
    }
    
}
