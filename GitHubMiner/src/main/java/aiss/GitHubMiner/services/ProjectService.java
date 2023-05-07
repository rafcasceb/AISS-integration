package aiss.GitHubMiner.services;

import aiss.GitHubMiner.auxiliary.Auth;
import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.models.projectsModels.Project;
import aiss.GitHubMiner.models.usersModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    String baseUriOne = "https://api.github.com/repos/";
    String baseUriAll = "https://api.github.com/orgs/";

    // owner = organization

    public Project getProject (String owner, String repoName, String token){
        /*
        Project project = restTemplate.getForObject(
                baseUriOne + owner + "/" + repoName,
                Project.class);
        return project;
        */
        HttpEntity<?> header = Auth.buildHeader(token);
        ResponseEntity<Project> response = restTemplate.exchange(
                baseUriOne + owner + "/" + repoName,
                HttpMethod.GET,
                header,
                Project.class);
        return response.getBody();
    }

    public List<Project> getAllProjects(String org, String token){
        /*
        Project[] projects = restTemplate.getForObject(
                baseUriAll + org + "/repos",
                Project[].class);
        return Arrays.stream(projects).toList();
        */
        HttpEntity<?> header = Auth.buildHeader(token);
        ResponseEntity<Project[]> response = restTemplate.exchange(
                baseUriAll + org + "/repos",
                HttpMethod.GET,
                header,
                Project[].class);
        return Arrays.stream(response.getBody()).toList();
    }

    public List<Project> getProjectsPagination(String org, Integer maxPages, String token) {
        // Manual pagination
        int projectsPerPage = 20;
        List<Project> allProjects = getAllProjects(org, token);
        return allProjects.stream().limit(maxPages * projectsPerPage).collect(Collectors.toList());
    }

}
