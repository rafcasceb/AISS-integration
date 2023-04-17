package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.commentsModels.Comment;
import aiss.GitHubMiner.services.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService service;

    @Test
    @DisplayName("Get all comments")
    void getAllComments() {
        List<Comment> comments = service.getAllComments("spring-projects", "spring-framework");
        for(Comment c : comments){
            System.out.println(c.getId());

        }
    }

    @Test
    @DisplayName("Get one comment with id")

    void getOneComment() {
        Comment c = service.getCommentsId("spring-projects", "spring-framework", "2504073");
        System.out.println(c.getBody());
    }

}