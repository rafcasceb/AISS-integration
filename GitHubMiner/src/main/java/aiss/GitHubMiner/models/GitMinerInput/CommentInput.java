package aiss.GitHubMiner.models.GitMinerInput;

import aiss.GitHubMiner.models.commentsModels.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentInput {

    @JsonProperty("id")
    private String id;

    @JsonProperty("body")
    private String body;

    @JsonProperty("user")
    private UserInput author;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UserInput getAuthor() {
        return author;
    }

    public void setAuthor(UserInput author) {
        this.author = author;
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



    public CommentInput(){}

    public CommentInput(Comment comment){
        this.id = comment.getId().toString();
        this.body = comment.getBody();
        this.author = new UserInput(comment.getUser());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
