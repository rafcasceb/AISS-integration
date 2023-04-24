package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Comments.Comment;
import aiss.GitLabMiner.Service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService service;

    String token = "glpat-EubL6mXBLo7cMyP4nDkm";

    @Test
    @DisplayName("Get comments test")
    void getComments() {
        List<Comment> comments = service.getComments("17960074","505", token);
        assertEquals(comments.get(0).getId(), 1345067735,"The id of the comment is not correct.");
    }

    @Test
    @DisplayName("Get comments by pages test")
    void getCommentsPagination() {
        int maxPages = 2;
        List<Comment> comments = service.getCommentsPagination("17960074","505", maxPages, token);
        assertTrue(comments.size() <= 20 * maxPages, "Page limit exceeded.");
    }


    @Test
    @DisplayName("Get comment with id test")
    void getCommentId() {
        Comment c = service.getCommentId("17960074","505", token, "1345067735");
        assertEquals(c.getId(), 1345067735, "The id of the comment is not correct.");
    }

}
