package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.commentsModels.Comment;
import aiss.GitHubMiner.services.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService service;

    String token = "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";

    @Test
    @DisplayName("Get all comments")
    void getAllComments() {
        List<Comment> comments = service.getAllComments("spring-projects", "spring-framework", token);
        for(Comment c : comments){
            assertNotEquals(c.getId(), null, "Some comment doesn't exist.");
            System.out.println(c.getId());
        }
    }

    @Test
    @DisplayName("Get one comment with id")
    void getOneComment() {
        Comment c = service.getCommentsId("spring-projects", "spring-framework", "453284026", token);
        assertEquals(c.getId().toString(), "453284026", "The id is not correct");
        System.out.println(c.getBody());
    }

}