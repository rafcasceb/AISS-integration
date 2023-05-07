package aiss.GitHubMiner.models.GitMinerInput;

import aiss.GitHubMiner.models.commentsModels.Comment;
import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.models.projectsModels.Project;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GitMinerInput {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("web_url")
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


    public GitMinerInput(){}

    public GitMinerInput(Project project, List<Commit> commits, List<Issue> issues, Map<String,List<Comment>> issuesComments) {
        this.id = project.getId();
        this.name = project.getName();
        this.url = project.getUrl();
        this.commits = commits.stream().map(c -> new CommitInput(c))
                .collect(Collectors.toList());
        this.issues = issues.stream().map(i -> new IssueInput(i, issuesComments.get(i.getId())))
                .collect(Collectors.toList());
    }

}
