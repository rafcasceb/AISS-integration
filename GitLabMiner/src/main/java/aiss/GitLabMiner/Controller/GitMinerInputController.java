package aiss.GitLabMiner.Controller;

import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.GitMinerInput.CommitInput;
import aiss.GitLabMiner.Models.GitMinerInput.GitMinerInput;
import aiss.GitLabMiner.Models.Issues.Issue;
import aiss.GitLabMiner.Models.Projects.Project;
import aiss.GitLabMiner.Service.CommitService;
import aiss.GitLabMiner.Service.IssueService;
import aiss.GitLabMiner.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projects")

public class GitMinerInputController {

    @Autowired
    CommitService CommitService;
    @Autowired
    ProjectService ProjectService;
    @Autowired
    IssueService IssueService;
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
            List<Commit> commits = CommitService.getCommitsPagination(id, token, sinceCommits, maxPages);
            List<Issue> issues = IssueService.getIssuesPagination(id, token, sinceIssues, maxPages);
            res.add(new GitMinerInput(p,commits,issues));
        }

        return res;
    }

    @GetMapping("/{id}")
    public GitMinerInput findOne(@PathVariable String id,
                           @RequestParam(required = false) Integer sinceCommits,
                           @RequestParam(required = false) Integer sinceIssues,
                           @RequestParam(required = false) Integer maxPages) {

        Project project = ProjectService.getProject(id, token);
        List<Commit> commits = CommitService.getCommitsPagination(id, token, sinceCommits, maxPages);
        List<Issue> issues = IssueService.getIssuesPagination(id, token, sinceIssues, maxPages);

        return new GitMinerInput(project, commits, issues);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public List<GitMinerInput> createAll (@RequestParam(required = false) Integer sinceCommits,
                                 @RequestParam(required = false) Integer sinceIssues,
                                 @RequestParam(required = false) Integer maxPages ){

        List<GitMinerInput> projects = findAll(sinceCommits, sinceIssues, maxPages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/api/projects";

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
        String url = "http://localhost:8080/api/projects";

        HttpEntity<GitMinerInput> request =
                    new HttpEntity<GitMinerInput>(project, headers);
        restTemplate.postForObject(url, request, GitMinerInput.class);

        return  project;
    }
}
