package aiss.GitHubMiner.services;

import aiss.GitHubMiner.models.commentsModels.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/repos/";
    public List<Comment> getAllComments (String owner, String repo){

        Comment[] commentsArray = restTemplate
                .getForObject(baseUri + owner + "/" + repo + "/comments", Comment[].class);
        return Arrays.stream(commentsArray).toList();

    }

    public Comment getCommentsId (String owner, String repo, String id){

        Comment c= restTemplate.getForObject(baseUri + owner + "/" + repo + "/comments/" + id, Comment.class);
        return c;

    }

}
