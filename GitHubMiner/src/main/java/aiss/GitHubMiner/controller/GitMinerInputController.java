package aiss.GitHubMiner.controller;

import aiss.GitHubMiner.models.GitMinerInput.GitMinerInput;
import aiss.GitHubMiner.models.commentsModels.Comment;
import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.models.projectsModels.Project;
import aiss.GitHubMiner.models.usersModels.User;
import aiss.GitHubMiner.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("githubminer/projects")
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
    RestTemplate RestTemplate;

    String token = "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";

    // owner = organization = project.getOwner().getLogin()
    // repository = project.getName()


    @GetMapping("/{owner}")
    public List<GitMinerInput> findAll(@PathVariable() String owner,
                                       @RequestParam(required = false) Integer maxPages,
                                       @RequestParam(required = false) Integer sinceCommits,
                                       @RequestParam(required = false) Integer sinceIssues) {

        List<GitMinerInput> res = new ArrayList<>();
        List<Project> projects = ProjectService.getProjectsPagination(owner, maxPages, token);

        for (Project p: projects) {
            String repoName = p.getName();
            res.add(findOne(owner, repoName, maxPages, sinceCommits, sinceIssues));
        }

        return res;
    }


    @GetMapping("/{owner}/{repoName}")
    public GitMinerInput findOne(@PathVariable() String owner,
                                 @PathVariable() String repoName,
                                 @RequestParam(required = false) Integer maxPages,
                                 @RequestParam(required = false) Integer sinceCommits,
                                 @RequestParam(required = false) Integer sinceIssues) {

        Project project = ProjectService.getProject(owner, repoName, token);
        List<Commit> commits = CommitService.getCommitsPagination(owner, repoName, token, sinceCommits, maxPages);
        List<Issue> issues = IssueService.getIssuesPagination(owner, repoName, token, sinceIssues ,maxPages);

        Map<String,List<Comment>> issuesComments = new HashMap<>();
        for (Issue issue: issues){
            List<Comment> commentsOfIssue = CommentService.getCommentsPagination(owner, repoName, maxPages, token);
            for (Comment comment: commentsOfIssue){
                User fullUserComment = UserService.getUser(comment.getUser().getLogin(), token);
                comment.getUser().setName(fullUserComment.getName());
            }
            issuesComments.put(issue.getId(), commentsOfIssue);

            User fullUserIssue = UserService.getUser(issue.getUser().getLogin(), token);
            issue.getUser().setName(fullUserIssue.getName());

            if(issue.getAssignee()!= null){
                User fullAssigneeIssue = UserService.getUser(issue.getAssignee().getLogin(), token);
                issue.getAssignee().setName(fullAssigneeIssue.getName());
            }
        }

        return new GitMinerInput(project, commits, issues, issuesComments);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{owner}")
    public List<GitMinerInput> createAll(@PathVariable() String owner,
                                         @RequestParam(required = false) Integer maxPages,
                                         @RequestParam(required = false) Integer sinceCommits,
                                         @RequestParam(required = false) Integer sinceIssues) {

        List<GitMinerInput> projects = findAll(owner, maxPages, sinceCommits, sinceIssues);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/gitminer/projects";

        for(GitMinerInput g: projects){
            HttpEntity<GitMinerInput> request = new HttpEntity<GitMinerInput>(g, headers);
            RestTemplate.postForObject(url, request, GitMinerInput.class);
        }

        return projects;
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{owner}/{repoName}")
    public GitMinerInput createOne(@PathVariable() String owner,
                                   @PathVariable() String repoName,
                                   @RequestParam(required = false) Integer maxPages,
                                   @RequestParam(required = false) Integer sinceCommits,
                                   @RequestParam(required = false) Integer sinceIssues){

        System.out.println("posting 1");

        GitMinerInput project = findOne(owner, repoName, maxPages, sinceCommits, sinceIssues);

        System.out.println(project.getName());
        System.out.println(project.getUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/gitminer/projects";

        HttpEntity<GitMinerInput> request = new HttpEntity<GitMinerInput>(project, headers);

            GitMinerInput createdProject = RestTemplate.postForObject(url, request, GitMinerInput.class);
            return createdProject;

    }
}
