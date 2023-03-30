package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Models.Commits.Commit;
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
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects/";
    public List<Commit> getCommits (String id){

        Commit[] commitArray = restTemplate
                .getForObject(baseUri + id + "/repository/commits", Commit[].class);
        return Arrays.stream(commitArray).toList();

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

        Date currentDate = new Date();
        Date limitDate = new Date(currentDate.getTime() - sinceCommits * 24 * 60 * 60 * 1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        while (hasMorePages && page <= maxPages){

            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(baseUri + id + "/repository/commits")
                    .queryParam("page", page);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Commit[]> response = restTemplate
                    .exchange(uriBuilder.toUriString(),
                            HttpMethod.GET,
                            entity,
                            Commit[].class);

            List<Commit> pageCommits = Arrays
                    .asList(response.getBody());

            for (Commit commit : pageCommits) {
                Date commitDate = null;
                try {
                    commitDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(commit.getCommittedDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (commitDate != null && commitDate.after(limitDate)) {
                    commits.add(commit);
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
        return commits;
    }

}
