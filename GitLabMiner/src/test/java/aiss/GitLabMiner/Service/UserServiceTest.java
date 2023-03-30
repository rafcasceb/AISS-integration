package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Models.Users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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