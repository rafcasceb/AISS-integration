package aiss.GitHubMiner.services;

import aiss.GitHubMiner.auxiliary.Auth;
import aiss.GitHubMiner.models.usersModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/users/";

    public User getUser(String userName, String token){
        HttpEntity<?> header = Auth.buildHeader(token);
        ResponseEntity<User> response = restTemplate.exchange(
                baseUri + userName,
                HttpMethod.GET,
                header,
                User.class);
        return response.getBody();
    }

}
