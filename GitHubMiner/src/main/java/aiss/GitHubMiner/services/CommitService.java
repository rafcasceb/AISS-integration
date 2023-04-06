package aiss.GitHubMiner.services;

import aiss.GitHubMiner.auxiliary.Auth;
import aiss.GitHubMiner.auxiliary.Pagination;
import aiss.GitHubMiner.models.commitsModels.Commit;
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
public class CommitService {

    @Autowired
    RestTemplate restTemplate;
    String baseUri = "https://api.github.com/repos/";

    public List<Commit> getCommits(String owner, String repo){

        Commit[] commitArray = restTemplate
                .getForObject(baseUri + owner + "/" + repo + "/commits", Commit[].class);

        return  Arrays.stream(commitArray).toList();

    }

    public ResponseEntity<Commit[]> getRequest (String uri, HttpEntity<?> header ){
        ResponseEntity<Commit[]> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                header,
                Commit[].class
        );
        return response;
    }

    public List<Commit> getCommitsPagination(String owner, String repo, String token, Integer sinceCommits, Integer maxPages) {

        List<Commit> commits = new ArrayList<>();
        boolean hasMorePages = true;
        int page = 1;

        if (maxPages == null) {
            maxPages = 2;
        }
        if (sinceCommits == null) {
            sinceCommits = 2;
        }

        LocalDateTime since = LocalDateTime.now().minusDays(sinceCommits);
        HttpEntity<?> header = Auth.buildHeader(token);

        while (hasMorePages && page <= maxPages) {

            System.out.println("Page: " + page);

            String uri = baseUri + owner + "/" + repo + "/commits?since=" + since + "&page=" + page;
            ResponseEntity<Commit[]> response = getRequest(uri,header);
            commits.addAll(Arrays.asList(response.getBody()));

            hasMorePages = Pagination.hasMorePages(response);
            page = page + 1;
        }
        return commits;
    }

}
