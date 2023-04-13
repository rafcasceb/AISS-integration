package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.usersModels.User;
import aiss.GitHubMiner.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    @DisplayName("Get user")
    void getUsers(){
        User user = service.getUser("rafcasceb");
        assertEquals(user.getName().trim(), "Rafael Castillo Cebolla", "Name doesn't match.");
    }

    /*
    @Test
    @DisplayName("Post user to GitMiner from GitHub")
    void postUserTest(){
        User user = service.postUser("rafcasceb");
        // If the post works, it will return the posted user
        assertEquals(user.getName().trim(), "Rafael Castillo Cebolla", "Name doesn't match.");
    }
    */
}