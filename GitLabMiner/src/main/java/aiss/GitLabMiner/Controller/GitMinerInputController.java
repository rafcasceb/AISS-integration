package aiss.GitLabMiner.Controller;

import aiss.GitLabMiner.Models.Comments.Author;
import aiss.GitLabMiner.Models.Comments.Comment;
import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.GitMinerInput.CommitInput;
import aiss.GitLabMiner.Models.GitMinerInput.GitMinerInput;
import aiss.GitLabMiner.Models.Issues.Issue;
import aiss.GitLabMiner.Models.Projects.Project;
import aiss.GitLabMiner.Models.Users.User;
import aiss.GitLabMiner.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gitlabminer/projects")

public class GitMinerInputController {

    @Autowired
    CommitService CommitService;
    @Autowired
    ProjectService ProjectService;
    @Autowired
    IssueService IssueService;
    @Autowired
    CommentService CommentService;

    @Autowired
    UserService UserService;
    @Autowired
    RestTemplate restTemplate;

    String token = "glpat-EWrMxiW1vhazpsMAsc4A";

    @GetMapping()
    public List<GitMinerInput> findAll(@RequestParam(required = false) Integer maxPages,
                                       @RequestParam(required = false) Integer sinceCommits,
                                       @RequestParam(required = false) Integer sinceIssues) {

        List<GitMinerInput> res = new ArrayList<>();
        List<Project> projects = ProjectService.getProjectsPagination(maxPages, token);

        for (Project p : projects) {
            String id = p.getId().toString();
            res.add(findOne(id,maxPages,sinceCommits,sinceIssues));
        }

        return res;
    }

    @GetMapping("/{id}")
    public GitMinerInput findOne(@PathVariable String id,
                           @RequestParam(required = false) Integer sinceCommits,
                           @RequestParam(required = false) Integer sinceIssues,
                           @RequestParam(required = false) Integer maxPages) {

        Project project = ProjectService.getProject(id, token);
        System.out.println(project + " " + project.getId());
        List<Commit> commits = CommitService.getCommitsPagination(id, token, sinceCommits, maxPages);
        List<Issue> issues = IssueService.getIssuesPagination(id, token, sinceIssues, maxPages);
        System.out.println(issues);

        Map<Integer,List<Comment>> issueComments = new HashMap<>();

        for (Issue issue : issues) {

            List<Comment> commentsOfIssue = CommentService.getCommentsPagination(
                    project.getId().toString(),issue.getIid().toString(),maxPages,token);
            issueComments.put(issue.getId(), commentsOfIssue);

            for (Comment comment : commentsOfIssue){

                User user = UserService.getUser(comment.getAuthor().getId().toString(), token);
                comment.getAuthor().setAvatarUrlUrl(user.getAvatarUrl());
                comment.getAuthor().setWebUrl(user.getWebUrl());
            }
        }




        return new GitMinerInput(project, commits, issues, issueComments);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public List<GitMinerInput> createAll (@RequestParam(required = false) Integer sinceCommits,
                                 @RequestParam(required = false) Integer sinceIssues,
                                 @RequestParam(required = false) Integer maxPages ){

        List<GitMinerInput> projects = findAll(sinceCommits, sinceIssues, maxPages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/gitminer/projects";

        for(GitMinerInput g : projects){
            HttpEntity<GitMinerInput> request =
                    new HttpEntity<GitMinerInput>(g, headers);
            restTemplate.postForObject(url, request, GitMinerInput.class);
        }

        return  projects;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public GitMinerInput createOne (@PathVariable String id,
                                    @RequestParam(required = false) Integer sinceCommits,
                                    @RequestParam(required = false) Integer sinceIssues,
                                    @RequestParam(required = false) Integer maxPages ){

        GitMinerInput project = findOne(id, sinceCommits, sinceIssues, maxPages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/gitminer/projects";

        HttpEntity<GitMinerInput> request = new HttpEntity<>(project, headers);

        try {
            GitMinerInput createdProject = restTemplate.postForObject(url, request, GitMinerInput.class);
            return createdProject;
        } catch (HttpStatusCodeException e) {
            // Manejar el error en la solicitud POST
            throw new ResponseStatusException(
                    HttpStatus.valueOf(e.getRawStatusCode()),
                    "Error en la solicitud POST al endpoint de GitMiner", e
            );
        }
    }
}
