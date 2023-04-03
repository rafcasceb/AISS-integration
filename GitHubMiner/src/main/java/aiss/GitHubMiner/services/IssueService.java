package aiss.GitHubMiner.services;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/repos/";
    public List<Issue> getIssues (String owner,String repo){

        Issue[] issuesArray = restTemplate
                .getForObject(baseUri + owner + "/" + repo + "/issues", Issue[].class);
        return Arrays.stream(issuesArray).toList();

    }

    public List<Issue> getIssuesPagination (String owner,String repo, String token, Integer sinceIssues, Integer maxPages){

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
                    .fromUriString(baseUri + owner + "/" + repo + "/issues")
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
                    issueDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(issue.getCreatedAt());
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
