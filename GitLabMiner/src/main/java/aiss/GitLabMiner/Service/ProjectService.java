package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Auxiliary.Auth;
import aiss.GitLabMiner.Auxiliary.Pagination;
import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.Projects.Project;
import aiss.GitLabMiner.Models.Projects.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects";

    private ResponseEntity<Project[]> getRequest (String uri, HttpEntity<?> header){
        ResponseEntity<Project[]> response = restTemplate
                .exchange(uri,
                        HttpMethod.GET,
                        header,
                        Project[].class);
        return response;
    }
    public List<Project> getAllProjects(String token){
        HttpEntity<?> header = Auth.buildHeader(token);
        ResponseEntity<Project[]> response = getRequest(baseUri, header);
        return Arrays.stream(response.getBody()).toList();
    }

    public List<Project> getProjectsPagination (Integer maxPages, String token){

        List<Project> projects = new ArrayList<>();
        boolean hasMorePages = true;
        int page = 1;

        if (maxPages == null) {
            maxPages = 2;
        }

        HttpEntity<?> header = Auth.buildHeader(token);

        while (hasMorePages && page <= maxPages){

            String uri = baseUri + "?page=" + page;
            ResponseEntity<Project[]> response = getRequest(uri,header);

            projects.addAll(Arrays.asList(response.getBody()));
            hasMorePages = Pagination.hasMorePages(response);
            page ++;
        }
        return projects;
    }
    public Project getProject(String projectId, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Project> response = restTemplate.exchange(
                baseUri + "/" + projectId,
                HttpMethod.GET,
                request,
                Project.class
        );
        return response.getBody();
    }
}
