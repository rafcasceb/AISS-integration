package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Comments.Comment;
import aiss.GitLabMiner.Service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService service;

    @Test
    @DisplayName( "Get comments test")
    void getComments() {
        List<Comment> comments = service.getComments("17960074","505", "glpat-EubL6mXBLo7cMyP4nDkm");
        System.out.println(comments);
    }

    @Test
    @DisplayName( "Get comment with id test")
    void getCommentId() {
        Comment c = service.getCommentId("17960074",
                "505", "glpat-EubL6mXBLo7cMyP4nDkm", "1345067735");
        System.out.println(c);
    }

}
