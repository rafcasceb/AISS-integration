package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Models.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/users?username=";

    public User getUser(String userName, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<User[]> request = new HttpEntity<User[]>(headers);
        ResponseEntity<User[]> response = restTemplate.exchange(
                baseUri + userName,
                HttpMethod.GET,
                request,
                User[].class
        );

        return Arrays.stream(response.getBody()).toList().get(0);
    }


}
