package aiss.GitHubMiner.models.GitMinerInput;

import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.models.projectsModels.Project;
import aiss.GitHubMiner.services.CommitService;
import aiss.GitHubMiner.services.IssueService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

public class GitMinerInput {

    @Id
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("commits")
    private List<CommitInput> commits;
    @JsonProperty("issues")
    private List<IssueInput> issues;


    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public List<CommitInput> getCommits() {return commits;}

    public void setCommits(List<CommitInput> commits) {this.commits = commits;}

    public List<IssueInput> getIssues() {return issues;}

    public void setIssues(List<IssueInput> issues) {this.issues = issues;}


    @Autowired
    IssueService issueService;
    @Autowired
    CommitService commitService;

    public GitMinerInput(Project project){
        List<CommitInput> commitInputs = getCommitsOfProject(project);
        List<IssueInput> issueInputs = getIssuesOfProject(project);

        this.id = project.getId();
        this.name = project.getName();
        this.url = project.getUrl();
        this.commits = commitInputs;
        this.issues = issueInputs;
    }


    private List<CommitInput> getCommitsOfProject(Project project){
        List<Commit> commits = commitService.getCommits(project.getOwner().getLogin(), project.getName());
        List<CommitInput> commitInputs = commits.stream().map(c -> new CommitInput(c)).collect(Collectors.toList());
        return commitInputs;
    }
    private List<IssueInput> getIssuesOfProject(Project project){
        List<Issue> issues = issueService.getIssues(project.getOwner().getLogin(), project.getName());
        List<IssueInput> issueInputs = issues.stream().map(i -> new IssueInput(i)).collect(Collectors.toList());
        return issueInputs;
    }

}
