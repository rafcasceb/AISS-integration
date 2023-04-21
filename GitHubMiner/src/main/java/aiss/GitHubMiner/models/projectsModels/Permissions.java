
package aiss.GitHubMiner.models.projectsModels;

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
    "pull",
    "push",
    "admin"
})
@Generated("jsonschema2pojo")
public class Permissions {

    @JsonProperty("pull")
    private Boolean pull;
    @JsonProperty("push")
    private Boolean push;
    @JsonProperty("admin")
    private Boolean admin;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("pull")
    public Boolean getPull() {
        return pull;
    }

    @JsonProperty("pull")
    public void setPull(Boolean pull) {
        this.pull = pull;
    }

    @JsonProperty("push")
    public Boolean getPush() {
        return push;
    }

    @JsonProperty("push")
    public void setPush(Boolean push) {
        this.push = push;
    }

    @JsonProperty("admin")
    public Boolean getAdmin() {
        return admin;
    }

    @JsonProperty("admin")
    public void setAdmin(Boolean admin) {
        this.admin = admin;
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
