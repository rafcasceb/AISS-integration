package aiss.GitHubMiner.services;

import aiss.GitHubMiner.models.commentsModels.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://api.github.com/repos/";

    public List<Comment> getAllComments (String owner, String repo){
        Comment[] commentsArray = restTemplate
                .getForObject(baseUri + owner + "/" + repo + "/issues/comments", Comment[].class);
        return Arrays.stream(commentsArray).toList();
    }

    public Comment getCommentsId (String owner, String repo, String id){
        return restTemplate.getForObject(baseUri + owner + "/" + repo + "/issues/comments/" + id, Comment.class);
    }

    public List<Comment> getCommentsPagination(String owner, String repo, Integer maxPages){
        int commentsByPage = 20;
        return getAllComments(owner,repo).stream().limit(maxPages * commentsByPage).collect(Collectors.toList());
    }

}
