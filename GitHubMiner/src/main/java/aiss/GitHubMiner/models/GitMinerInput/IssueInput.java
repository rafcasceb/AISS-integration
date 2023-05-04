package aiss.GitHubMiner.models.GitMinerInput;

import aiss.GitHubMiner.models.commentsModels.Comment;
import aiss.GitHubMiner.models.issuesModels.Issue;
import aiss.GitHubMiner.services.CommentService;
import aiss.GitHubMiner.services.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Issue")
public class IssueInput {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("ref_id")
    private String refId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    @Column(columnDefinition="TEXT")
    private String description;
    @JsonProperty("state")
    private String state;

    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("closed_at")
    private String closedAt;
    @JsonProperty("labels")
    @ElementCollection
    private List<String> labels;
    @JsonProperty("author")
    //@NotEmpty(message = "The author of the issue cannot be empty")
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    @OneToOne(cascade=CascadeType.ALL)
    private UserInput author;
    @JsonProperty("assignee")
    @JoinColumn(name = "assignee_id",referencedColumnName = "id")
    @OneToOne(cascade=CascadeType.ALL)
    private UserInput assignee;
    @JsonProperty("upvotes")
    private Integer upvotes;
    @JsonProperty("downvotes")
    private Integer downvotes;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("comments")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "issueId")
    private List<CommentInput> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public UserInput getAuthor() {
        return author;
    }

    public void setAuthor(UserInput author) {
        this.author = author;
    }

    public UserInput getAssignee() {
        return assignee;
    }

    public void setAssignee(UserInput assignee) {
        this.assignee = assignee;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<CommentInput> getComments() {
        return comments;
    }

    public void setComments(List<CommentInput> comments) {
        this.comments = comments;
    }



    @Autowired
    CommentService service;

    public IssueInput(){}

    public IssueInput(Issue issue, List<Comment> comments){
        this.id = issue.getId().toString();
        this.refId = issue.getNodeId();
        this.title = issue.getTitle();
        this.description = issue.getBody();
        this.state = issue.getState();
        this.createdAt = issue.getCreatedAt();
        this.updatedAt = issue.getCreatedAt();
        this.closedAt = issue.getClosedAt().toString();
        this.labels = issue.getLabels().stream().map(l -> l.getId()).collect(Collectors.toList());
        this.upvotes = null;  // Doesn't exist
        this.downvotes = null;  // Doesn't exist
        this.author = new UserInput(issue.getUser());
        this.assignee = new UserInput(issue.getAssignee());
        this.comments = comments.stream().map(c -> new CommentInput(c)).collect(Collectors.toList());
    }

}
