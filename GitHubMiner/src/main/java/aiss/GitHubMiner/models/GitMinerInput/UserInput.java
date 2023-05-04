package aiss.GitHubMiner.models.GitMinerInput;

import aiss.GitHubMiner.models.issuesModels.Assignee;
import aiss.GitHubMiner.models.usersModels.User;
import aiss.GitHubMiner.services.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "GMUser")     // Watch out: User is a reserved keyword in H2
public class UserInput {

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



    @Autowired
    UserService service;

    public UserInput(){}

    public UserInput(User user){
        this.id = user.getId().toString();
        this.username = user.getLogin();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.webUrl = user.getUrl();
    }

    public UserInput(aiss.GitHubMiner.models.commentsModels.User user){
        this.id = user.getId().toString();
        this.username = user.getLogin();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.webUrl = user.getUrl();
    }

    public UserInput(aiss.GitHubMiner.models.issuesModels.User user){
        this.id = user.getId().toString();
        this.username = user.getLogin();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.webUrl = user.getUrl();
    }

    public UserInput(Assignee user){
        this.id = user.getId().toString();
        this.username = user.getLogin();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.webUrl = user.getUrl();
    }


}

