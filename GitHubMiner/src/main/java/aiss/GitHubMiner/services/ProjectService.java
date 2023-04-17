package aiss.GitHubMiner.services;

import aiss.GitHubMiner.auxiliary.Auth;
import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.projectsModels.Projects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/";

    public Projects getProject (String projectId, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Projects[]> response = restTemplate.exchange(
                baseUri + "projects/" + projectId,
                HttpMethod.GET, request, Projects[].class);
        return Arrays.stream(response.getBody()).toList().get(0);
    }

    public List<Projects> getAllProjects (String owner, String repo, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Projects[]> response = restTemplate.exchange(
                baseUri + "repos/" + owner + "/" + repo + "/" + "projects",
                HttpMethod.GET, request, Projects[].class);
        return Arrays.stream(response.getBody()).toList();
    }

    public List<Projects> getOrgProjects (String org){
        Projects[] projectsArray = restTemplate
                .getForObject(baseUri + "orgs/" + org + "/projects", Projects[].class);
        return Arrays.stream(projectsArray).toList();
    }

}

