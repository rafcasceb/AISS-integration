package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.services.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        List<Issue> issues = service
                .getIssuesPagination("spring-projects", "spring-framework",
                        "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",
                        null,null);

        for(Issue i : issues){
            System.out.println(i.getCreatedAt());
        }

    }
}