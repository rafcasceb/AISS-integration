package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Users.User;
import aiss.GitLabMiner.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    @DisplayName("Get user test")
    void getUser(){
        User user = service.getUser("jack_smith", "glpat-EWrMxiW1vhazpsMAsc4A");
        System.out.println(user.getName());
    }
}