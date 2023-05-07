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

    String token = "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";


    @Test
    @DisplayName("Get user")
    void getUsers(){
        User user = service.getUser("octocat", token);
        assertEquals(user.getName().trim(), "The Octocat", "Name doesn't match.");
    }

}
