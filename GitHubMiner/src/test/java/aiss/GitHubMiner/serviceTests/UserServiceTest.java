package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.usersModels.User;
import aiss.GitHubMiner.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    @DisplayName("Get user")
    void getUsers(){
        User user = service.getUser("rafcasceb");
        System.out.println(user.getName());
    }
}