package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.services.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IssueServiceTest {

    @Autowired
    IssueService service;

    @Test
    @DisplayName( "Get issues test")

    void getIssues() {

        List<Issue> issues = service
                .getIssues("spring-projects", "spring-framework"
                        );
        for(Issue i : issues){
            System.out.println(i.getCreatedAt());

        }

    }

    @Test
    @DisplayName( "Get issues with pagination test")
    void getIssuesPagination() {
        Integer sinceIssues = 60;
        Integer maxPages = 6;
        Integer contentLimit = maxPages * 30; // A page has 30 elements by default in gitHub
        String owner = "spring-projects";
        String repo = "spring-framework";

        List<Issue> issues = service
                .getIssuesPagination(owner, repo,
                        "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",
                        sinceIssues,maxPages);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime limit = LocalDateTime.now().minusDays(sinceIssues);

        for(Issue i: issues){
            LocalDateTime issueDate = LocalDateTime
                    .parse(i.getCreatedAt(),formatter);

            assertEquals(issueDate.isAfter(limit), true, "Issue date before limit");

            assertEquals(i.getUrl().contains(owner)
                            && i.getUrl().contains(repo),
                    true, "This issue doesn't belong to the requested repository");
        }
        assertEquals(issues.size()<=contentLimit, true, "Page limit exceeded");

    }
}