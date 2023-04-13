package aiss.GitHubMiner.services;

import aiss.GitHubMiner.models.usersModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/users/";

    public User getUser(String userName){
        User user = restTemplate.getForObject(baseUri + userName, User.class);
        return user;
    }

    public User postUser(String userName){
        User user = getUser(userName);
        // TODO Post this user in GitMiner
        return user;
    }

}
