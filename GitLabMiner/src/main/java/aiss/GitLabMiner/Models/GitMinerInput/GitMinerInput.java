package aiss.GitLabMiner.Models.GitMinerInput;
import java.util.List;

public class GitMinerInput {

    private String id;
    private String name;
    private String web_url;

    List<CommitInput> Commits;
    List<IssuesInput> issues;


}
