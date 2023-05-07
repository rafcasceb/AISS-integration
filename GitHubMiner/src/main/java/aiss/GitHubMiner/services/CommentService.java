package aiss.GitHubMiner.services;

import aiss.GitHubMiner.auxiliary.Auth;
import aiss.GitHubMiner.models.commentsModels.Comment;
import aiss.GitHubMiner.models.usersModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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


    public Comment getCommentsId (String owner, String repo, String id, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Comment> response = restTemplate.exchange(
                baseUri + owner + "/" + repo + "/issues/comments/" + id,
                HttpMethod.GET,
                request,
                Comment.class);
        return response.getBody();
    }

    public List<Comment> getAllComments (String owner, String repo, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Comment[]> commentsArray = restTemplate.exchange(
                baseUri + owner + "/" + repo + "/issues/comments",
                HttpMethod.GET,
                request,
                Comment[].class);
        return Arrays.stream(commentsArray.getBody()).toList();
    }

    public List<Comment> getCommentsPagination(String owner, String repo, Integer maxPages, String token){
        // Manual pagination
        int commentsByPage = 20;
        int limit = 1;
        if (maxPages!=null) limit = maxPages;
        return getAllComments(owner,repo,token).stream().limit(limit * commentsByPage).collect(Collectors.toList());
    }

}
