package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Service.CommitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommitServiceTest {

    @Autowired
    CommitService service;

    @Test
    @DisplayName( "Get commits test")
    void getCommits() {
        List<Commit> commits = service.getCommits("4207231");
        for (Commit c: commits){
            assertNotEquals(c.getId(), null, "Some commit doesn't exist");
        }
        System.out.println(commits);
    }

    @Test
    @DisplayName("Get commits with pagination test")
    void getCommitsPagination() {

        String id = "4207231";
        Integer since = 20;
        Integer maxPages = 10;
        Integer contentLimit = maxPages*20;

        List<Commit> commits = service
                .getCommitsPagination(id,"glpat-EWrMxiW1vhazpsMAsc4A",
                        since, maxPages);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        LocalDateTime limit = LocalDateTime.now().minusDays(since);

        for(Commit c : commits){
            LocalDateTime commitDate = LocalDateTime
                    .parse(c.getCommittedDate(), formatter);

            //System.out.println(c.getCommittedDate());
            assertTrue(commitDate.isAfter(limit), "Commit date before limit.");
        }
        assertTrue(commits.size() <= contentLimit, "Page limit exceeded.");
    }

}
