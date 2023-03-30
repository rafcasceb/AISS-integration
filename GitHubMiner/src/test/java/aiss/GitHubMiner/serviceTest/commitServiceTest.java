package aiss.GitHubMiner.serviceTest;

import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.services.CommitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class commitServiceTest {
    @Autowired
    CommitService service;

    @Test
    @DisplayName( "Get commits with pagination test")
    void getCommitsPagination() {
        List<Commit> commits = service
                .getCommitsPagination("spring-projects", "spring-framework",
                        "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",
                        5,5);

        for(Commit c : commits){
            System.out.println(c.getCommit().getAuthor().getDate());
        }

    }
}
