package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.services.CommitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class CommitServiceTest {
    @Autowired
    CommitService service;

    String token = "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";

    @Test
    @DisplayName( "Get commits test")
    void getCommits() {
        List<Commit> commits = service.getCommits("spring-projects", "spring-framework", token);
        for(Commit c : commits){
            assertNotEquals(c.getAuthor(), null, "Some commit doesn't exist.");
        }
    }

    @Test
    @DisplayName( "Get commits with pagination test")
    void getCommitsPagination() {

        Integer sinceCommits = 60;
        Integer maxPages = 6;
        Integer contentLimit = maxPages * 30; // A page has 30 elements by default in gitHub
        String owner = "spring-projects";
        String repo = "spring-framework";

        List<Commit> commits = service
                .getCommitsPagination(owner, repo, token, sinceCommits,maxPages);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime limit = LocalDateTime.now().minusDays(sinceCommits);

        for(Commit c : commits){
            LocalDateTime commitDate = LocalDateTime
                    .parse(c.getCommit().getCommitter().getDate(), formatter);

            assertEquals(commitDate.isAfter(limit), true, "Commit date before limit");

            assertEquals(c.getCommit().getUrl().contains(owner)
                    && c.getCommit().getUrl().contains(repo),
                    true, "This commit doesn't belong to the requested repository");
        }
        assertEquals(commits.size()<=contentLimit, true, "Page limit exceeded");

    }
}
