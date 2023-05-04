package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Auxiliary.Auth;
import aiss.GitLabMiner.Auxiliary.Pagination;
import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.Issues.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects/";

    public List<Issue> getIssues (String id){
        Issue[] issuesArray = restTemplate
                .getForObject(baseUri + id + "/issues",Issue[].class);
        return Arrays.stream(issuesArray).toList();
    }

    private ResponseEntity<Issue[]> getRequest (String uri, HttpEntity<?> header){
        ResponseEntity<Issue[]> response = restTemplate
                .exchange(uri,
                        HttpMethod.GET,
                        header,
                        Issue[].class);
        return response;
    }

    public List<Issue> getIssuesPagination (String id, String token, Integer sinceIssues, Integer maxPages){

        List<Issue> issues = new ArrayList<>();
        boolean hasMorePages = true;
        int page = 1;

        if (maxPages == null) {
            maxPages = 2;
        }

        if (sinceIssues == null) {
            sinceIssues = 20;
        }

        LocalDateTime since = LocalDateTime.now().minusDays(sinceIssues);
        HttpEntity<?> header = Auth.buildHeader(token);

        while (hasMorePages && page <= maxPages){

            String uri = baseUri + id + "/issues?updated_after=" + since + "&page=" + page;
            try {
                ResponseEntity<Issue[]> response = getRequest(uri, header);
                issues.addAll(Arrays.asList(response.getBody()));
                hasMorePages = Pagination.hasMorePages(response);
                page++;

            } catch (HttpClientErrorException.NotFound e) {
                // Manejar la excepción 404
                issues.clear();
                return issues;
            }
        }
        return issues;
    }

}
