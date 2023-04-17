package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Auxiliary.Auth;
import aiss.GitLabMiner.Models.Projects.Project;
import aiss.GitLabMiner.Models.Projects.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects";

    public List<Project> getAllProjects(String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Project[]> response = restTemplate.exchange(
                baseUri,
                HttpMethod.GET,
                request,
                Project[].class
        );
        return Arrays.stream(response.getBody()).toList();
    }
    public Project getProject(String projectId, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Project[]> response = restTemplate.exchange(
                baseUri + "/" + projectId,
                HttpMethod.GET,
                request,
                Project[].class
        );
        return Arrays.stream(response.getBody()).toList().get(0);
    }
}
