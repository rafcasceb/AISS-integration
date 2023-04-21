package aiss.GitHubMiner.services;

import aiss.GitHubMiner.models.projectsModels.Project;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Project getProject (String owner, String repoName){
        Project project = restTemplate.getForObject(
                baseUriOne + owner + "/" + repoName,
                Project.class);
        return project;
    }

    public List<Project> getAllProjects(String org){
        Project[] projects = restTemplate.getForObject(
                baseUriAll + org + "/repos",
                Project[].class);
        return Arrays.stream(projects).toList();
    }

    public List<Project> getProjectsPagination(String org, Integer maxPages) {
        int projectsPerPage = 20;
        List<Project> allProjects = getAllProjects(org);
        return allProjects.stream().limit(maxPages * projectsPerPage).collect(Collectors.toList());
    }

}