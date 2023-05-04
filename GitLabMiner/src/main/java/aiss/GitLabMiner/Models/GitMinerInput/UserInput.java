package aiss.GitLabMiner.Models.GitMinerInput;

import aiss.GitLabMiner.Models.Issues.Author;
import aiss.GitLabMiner.Models.Users.User;
import aiss.GitLabMiner.Service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class UserInput {

    @Autowired
    UserService UserService;

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    @NotEmpty(message = "The username cannot be empty")
    private String username;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("web_url")
    private String webUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }



    public UserInput () {}

    public UserInput (User user){
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.webUrl = user.getWebUrl();
    }

    public UserInput(aiss.GitLabMiner.Models.Comments.Author author) {
        this.id = author.getId().toString();
        this.username = author.getUsername();
        this.name = author.getName();
        this.avatarUrl = author.getAvatarUrl();
        this.webUrl = author.getWebUrl();
    }

    public UserInput (Author author){
        this.id = author.getId().toString();
        this.username = author.getUsername();
        this.name = author.getName();
        this.avatarUrl = author.getAvatarUrl().toString();
        this.webUrl = author.getWebUrl();
    }
}
