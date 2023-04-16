
package aiss.GitHubMiner.models.commentsModels;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "html_url",
    "url",
    "id",
    "node_id",
    "body",
    "path",
    "position",
    "line",
    "commit_id",
    "user",
    "created_at",
    "updated_at",
    "author_association"
})
@Generated("jsonschema2pojo")
public class Comment {

    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("url")
    private String url;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("node_id")
    private String nodeId;
    @JsonProperty("body")
    private String body;
    @JsonProperty("path")
    private String path;
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("line")
    private Integer line;
    @JsonProperty("commit_id")
    private String commitId;
    @JsonProperty("user")
    private User user;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("author_association")
    private String authorAssociation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("html_url")
    public String getHtmlUrl() {
        return htmlUrl;
    }

    @JsonProperty("html_url")
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("node_id")
    public String getNodeId() {
        return nodeId;
    }

    @JsonProperty("node_id")
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("position")
    public Integer getPosition() {
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Integer position) {
        this.position = position;
    }

    @JsonProperty("line")
    public Integer getLine() {
        return line;
    }

    @JsonProperty("line")
    public void setLine(Integer line) {
        this.line = line;
    }

    @JsonProperty("commit_id")
    public String getCommitId() {
        return commitId;
    }

    @JsonProperty("commit_id")
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("author_association")
    public String getAuthorAssociation() {
        return authorAssociation;
    }

    @JsonProperty("author_association")
    public void setAuthorAssociation(String authorAssociation) {
        this.authorAssociation = authorAssociation;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
