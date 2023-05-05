package aiss.GitLabMiner.Models.GitMinerInput;

import aiss.GitLabMiner.Models.Comments.Comment;
import aiss.GitLabMiner.Models.Issues.Issue;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class IssuesInput {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ref_id")
    private String refId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
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
    private List<String> labels;
    @JsonProperty("author")
    private UserInput author;
    @JsonProperty("assignee")
    private UserInput assignee;
    @JsonProperty("upvotes")
    private Integer upvotes;
    @JsonProperty("downvotes")
    private Integer downvotes;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("comments")
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


    public IssuesInput () {}

    public IssuesInput (Issue issue, Integer projectId, List<Comment> issueComments){

        this.id = issue.getId().toString();
        this.refId = issue.getIid().toString();
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.state = issue.getState();
        this.createdAt = issue.getCreatedAt();
        this.updatedAt = issue.getCreatedAt();

        if(issue.getClosedAt() != null){
            this.closedAt = issue.getClosedAt().toString();
        } else {
            this.closedAt = "not closed";
        }

        this.labels = issue.getLabels();
        this.author = new UserInput(issue.getAuthor());
        this.upvotes = issue.getUpvotes();
        this.downvotes = issue.getDownvotes();
        this.webUrl = issue.getWebUrl();

        List<CommentInput> comments = new ArrayList<>();
        for(Comment c: issueComments){
            comments.add(new CommentInput(c.getId().toString(),
                    c.getBody(), new UserInput(c.getAuthor()),
                    c.getCreatedAt(),c.getUpdatedAt()));
        }
        this.comments = comments;
    }



}
