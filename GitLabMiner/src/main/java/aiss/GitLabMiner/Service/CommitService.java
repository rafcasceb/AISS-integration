package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Auxiliary.Auth;
import aiss.GitLabMiner.Auxiliary.Pagination;
import aiss.GitLabMiner.Models.Commits.Commit;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects/";
    public List<Commit> getCommits (String id){

        Commit[] commitArray = restTemplate
                .getForObject(baseUri + id + "/repository/commits", Commit[].class);
        return Arrays.stream(commitArray).toList();

    }

    private ResponseEntity<Commit[]> getRequest (String uri, HttpEntity<?> header){
        ResponseEntity<Commit[]> response = restTemplate
                .exchange(uri,
                        HttpMethod.GET,
                        header,
                        Commit[].class);
        return response;
    }

    public List<Commit> getCommitsPagination (String id, String token, Integer sinceCommits, Integer maxPages){

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

        while (hasMorePages && page <= maxPages){

            String uri = baseUri + id + "/repository/commits?since=" + since + "&page=" + page;

            try {
                ResponseEntity<Commit[]> response = getRequest(uri,header);
                commits.addAll(Arrays.asList(response.getBody()));
                hasMorePages = Pagination.hasMorePages(response);
                page ++;
            }
            catch (HttpClientErrorException.NotFound e) {
                // Manejar la excepción 404
                commits.clear();
                return commits;
            }
        }
        return commits;
    }

}
