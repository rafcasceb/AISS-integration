package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Issues.Issue;
import aiss.GitLabMiner.Service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        List<Issue> issues = service
                .getIssuesPagination("17960074","glpat-EWrMxiW1vhazpsMAsc4A",null,null
                );

        for(Issue i : issues){
            System.out.println(i.getCreatedAt());
        }

    }
}