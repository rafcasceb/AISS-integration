package aiss.GitHubMiner.models.GitMinerInput;

import aiss.GitHubMiner.models.commitsModels.Commit;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "Commit")
public class CommitInput {

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;

    @JsonProperty("message")
    @Column(columnDefinition = "TEXT")
    private String message;
    @JsonProperty("author_name")
    @NotEmpty(message = "Author name cannot be empty.")
    private String authorName;
    @JsonProperty("author_email")
    private String authorEmail;
    @JsonProperty("authored_date")
    @NotEmpty(message = "Author date cannot be empty.")
    private String authoredDate;
    @JsonProperty("committer_name")
    @NotEmpty(message = "Committer name cannot be empty.")
    private String committerName;
    @JsonProperty("committer_email")
    private String committerEmail;

    @JsonProperty("committed_date")
    @NotEmpty(message = "Committer date cannot be empty.")
    private String committedDate;

    @JsonProperty("web_url")
    @NotEmpty(message = "URL cannot be empty." +
            "")
    private String webUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthoredDate() {
        return authoredDate;
    }

    public void setAuthoredDate(String authoredDate) {
        this.authoredDate = authoredDate;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }

    public String getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(String committedDate) {
        this.committedDate = committedDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }


    public CommitInput(){}

    public CommitInput(Commit commit){
        String message = commit.getCommit().getMessage();

        this.id = commit.getNodeId();
        this.title = message.split("/n")[0];
        this.message = message.contains("/n")
                ? message.split("/n")[1]
                : "";
        this.authorName = commit.getCommit().getAuthor().getName();
        this.authorEmail = commit.getCommit().getAuthor().getName();
        this.authoredDate = commit.getCommit().getAuthor().getDate();
        this.committerName = commit.getCommit().getCommitter().getName();
        this.committerEmail = commit.getCommit().getCommitter().getEmail();
        this.committedDate = commit.getCommit().getCommitter().getDate();
        this.webUrl = commit.getUrl();
    }

}
