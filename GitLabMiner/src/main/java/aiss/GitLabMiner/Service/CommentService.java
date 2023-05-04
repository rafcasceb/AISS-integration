package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Auxiliary.Auth;
import aiss.GitLabMiner.Models.Comments.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects/";

    public List<Comment> getComments (String projectId, String issue_iid, String token){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Comment[]> response = restTemplate.exchange(
                baseUri +  projectId + "/issues/" + issue_iid + "/notes",
                HttpMethod.GET, request, Comment[].class);
        return Arrays.stream(response.getBody()).toList();
    }

    public List<Comment> getCommentsPagination(String projectId, String issue_iid, Integer maxPages, String token) {
        int commentsPerPage = 20;
        HttpEntity<?> request = Auth.buildHeader(token);

        try {
            ResponseEntity<Comment[]> response = restTemplate.exchange(
                    baseUri +  projectId + "/issues/" + issue_iid + "/notes",
                    HttpMethod.GET, request, Comment[].class
            );
            if (maxPages == null) maxPages = 2;
            return Arrays.stream(response.getBody()).limit(maxPages * commentsPerPage).toList();
        } catch (HttpClientErrorException.NotFound e) {
            // Manejar la excepción 404
            return Collections.emptyList();
        }
    }

    public Comment getCommentId (String projectId, String issue_iid, String token, String commentId){
        HttpEntity<?> request = Auth.buildHeader(token);
        ResponseEntity<Comment> response = restTemplate.exchange(
                baseUri +  projectId + "/issues/" + issue_iid + "/notes/" + commentId,
                HttpMethod.GET, request, Comment.class);
        return response.getBody();
    }

}
