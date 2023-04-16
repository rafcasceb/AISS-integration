package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Comments.Comment;
import aiss.GitLabMiner.Service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService service;

    @Test
    @DisplayName( "Get comments test")
    void getComments() {
        List<Comment> comments = service.getComments("4207231","4f87c2d8e5054b0d9ca4c9c1680a8a90307071c8");
        System.out.println(comments);
    }

}
