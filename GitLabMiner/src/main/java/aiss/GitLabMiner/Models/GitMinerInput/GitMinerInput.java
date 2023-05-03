package aiss.GitLabMiner.Models.GitMinerInput;
import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.Issues.Issue;
import aiss.GitLabMiner.Models.Projects.Project;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

public class GitMinerInput {

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("web_url")
    private String web_url;
    @JsonProperty("commits")
    List<CommitInput> commits;
    @JsonProperty("issues")
    List<IssuesInput> issues;


    public GitMinerInput (String id, String name, String web_url,
                          List<CommitInput> commits, List <IssuesInput> issues){
        this.id = id;
        this.name = name;
        this.web_url = web_url;
        this.commits = commits;
        this.issues = issues;
    }

    String token = "glpat-EWrMxiW1vhazpsMAsc4A";

    public GitMinerInput (Project project,
                          List<Commit> commits, List<Issue> issues){

        this.id = project.getId().toString();
        this.name = project.getName();
        this.web_url = project.getWebUrl();

        List<CommitInput> auxCommits = new ArrayList<>();
        for (Commit c : commits){
            auxCommits.add(new CommitInput(c));
        }
        this.commits = auxCommits;

        List<IssuesInput> auxIssues = new ArrayList<>();
        for (Issue i : issues){
            auxIssues.add(new IssuesInput(i, project.getId()));
        }
        this.issues = auxIssues;
    }


}
