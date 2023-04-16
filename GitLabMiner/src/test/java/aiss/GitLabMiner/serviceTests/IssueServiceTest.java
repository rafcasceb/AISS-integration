package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.Issues.Issue;
import aiss.GitLabMiner.Service.*;
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
    aiss.GitLabMiner.Service.IssueService service;

    @Test
    @DisplayName( "Get issues test")

    void getIssues() {
        List<Issue> issues = service
                .getIssues("17960074");
        for(Issue i : issues){
            System.out.println(i.getDescription());

        }

    }

    @Test
    @DisplayName( "Get issues with pagination test")
    void getIssuesPagination() {
        String id = "4207231";
        Integer since = 900;
        Integer maxPages = 10;
        Integer contentLimit = maxPages*30;

        List<Issue> issues = service
                .getIssuesPagination(id,"glpat-EWrMxiW1vhazpsMAsc4A",
                        since, maxPages);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        LocalDateTime limit = LocalDateTime.now().minusDays(since);

        for(Issue i : issues){
            LocalDateTime issueDate = LocalDateTime
                    .parse(i.getCreatedAt(), formatter);

            //System.out.println(c.getCommittedDate());
            assertEquals(issueDate.isAfter(limit), true, "Issue date before limit");

        }
        assertEquals(issues.size()<=contentLimit, true, "Page limit exceeded");
    }
}