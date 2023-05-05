package aiss.GitLabMiner.Models.GitMinerInput;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentInput {

    @JsonProperty("id")
    private String id;
    @JsonProperty("body")
    private String body;

    @JsonProperty("author")
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



    public CommentInput () {}

    public CommentInput (String id, String body, UserInput author, String created, String updated) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.createdAt = created;
        this.updatedAt = updated;
    }

}
