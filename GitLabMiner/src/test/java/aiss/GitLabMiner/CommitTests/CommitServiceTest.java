package aiss.GitLabMiner.CommitTests;

import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Service.CommitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommitServiceTest {

    @Autowired
    CommitService service;

    @Test
    @DisplayName( "Get commits test")
    void getCommits() {
        List<Commit> commits = service
                .getCommits("4207231");
        System.out.println(commits);
    }

    @Test
    @DisplayName( "Get commits with pagination test")
    void getCommitsPagination() {
        List<Commit> commits = service
                .getCommitsPagination("4207231","glpat-EWrMxiW1vhazpsMAsc4A",
                        5, 5);

        for(Commit c : commits){
            System.out.println(c.getCommittedDate());
        }
    }

}
