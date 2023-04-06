package aiss.GitHubMiner.auxiliary;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class Auth {

    public static HttpEntity<?> buildHeader (String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return entity;
    }
}
