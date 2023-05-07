package aiss.GitHubMiner.services;

import aiss.GitHubMiner.auxiliary.Auth;
import aiss.GitHubMiner.auxiliary.Pagination;
import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.issuesModels.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/repos/";


    private ResponseEntity<Issue[]> getRequest (String uri, HttpEntity<?> header ){
        ResponseEntity<Issue[]> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                header,
                Issue[].class
        );
        return response;
    }

    public List<Issue> getIssues (String owner,String repo, String token){
        /*
        Issue[] issuesArray = restTemplate
                .getForObject(baseUri + owner + "/" + repo + "/issues", Issue[].class);
        return Arrays.stream(issuesArray).toList();
        */
        HttpEntity<?> header = Auth.buildHeader(token);
        ResponseEntity<Issue[]> issuesArray = getRequest(baseUri + owner + "/" + repo + "/issues", header);
        return Arrays.stream(issuesArray.getBody()).toList();
    }



    public List<Issue> getIssuesPagination (String owner,String repo, String token, Integer sinceIssues, Integer maxPages){

        List<Issue> issues = new ArrayList<>();
        boolean hasMorePages = true;
        int page = 1;

        if (maxPages == null) {
            maxPages = 2;
        }
        if (sinceIssues == null) {
            sinceIssues = 2;
        }

        LocalDateTime since = LocalDateTime.now().minusDays(sinceIssues);
        HttpEntity<?> header = Auth.buildHeader(token);

        while (hasMorePages && page <= maxPages) {
            System.out.println("Page: " + page);

            String uri = baseUri + owner + "/" + repo + "/issues?since=" + since + "&page=" + page;
            ResponseEntity<Issue[]> response = getRequest(uri,header);
            issues.addAll(Arrays.asList(response.getBody()));

            hasMorePages = Pagination.hasMorePages(response);
            page = page + 1;
        }
        return issues;
    }

}
