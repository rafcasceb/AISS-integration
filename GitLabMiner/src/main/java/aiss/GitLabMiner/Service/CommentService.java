package aiss.GitLabMiner.Service;

import aiss.GitLabMiner.Models.Comments.Comment;
import aiss.GitLabMiner.Models.Commits.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    String baseUri = "https://gitlab.com/api/v4/projects/";
    public List<Comment> getComments (String id, String commit){

        Comment[] commentArray = restTemplate
                .getForObject(baseUri + id + "/repository/commits/" + commit + "/comments", Comment[].class);
        return Arrays.stream(commentArray).toList();

    }
}
