package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Models.Issues.Issue;
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

        Date currentDate = new Date();
        Date limitDate = new Date(currentDate.getTime() - sinceIssues * 24 * 60 * 60 * 1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        while (hasMorePages && page <= maxPages){

            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(baseUri + id + "/issues")
                    .queryParam("page", page);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Issue[]> response = restTemplate
                    .exchange(uriBuilder.toUriString(),
                            HttpMethod.GET,
                            entity,
                            Issue[].class);

            List<Issue> pageIssues = Arrays
                    .asList(response.getBody());

            for (Issue issue : pageIssues) {
                Date issueDate = null;
                try {
                    issueDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(issue.getCreatedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (issueDate != null && issueDate.after(limitDate)) {
                    issues.add(issue);
                }
            }

            String linkHeader = response
                    .getHeaders().getFirst("Link");

            if(linkHeader == null || !linkHeader.contains("rel=\"next\"")){
                hasMorePages = false;
            }else{
                page ++;
            }
        }
        return issues;
    }

}
