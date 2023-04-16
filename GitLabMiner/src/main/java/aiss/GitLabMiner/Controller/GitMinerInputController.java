package aiss.GitLabMiner.Controller;

import aiss.GitLabMiner.Models.Commits.Commit;
import aiss.GitLabMiner.Models.GitMinerInput.CommitInput;
import aiss.GitLabMiner.Service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/commits")

public class GitMinerInputController {

    @Autowired
    CommitService Commitservice;
    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/{id}")
    public List<Commit> findAll (@PathVariable String id){
        List<Commit> commits = Commitservice.getCommits(id);
        return  commits;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public CommitInput create (@PathVariable String id){
        List<Commit> commits = Commitservice.getCommits(id);
        Commit commit = commits.get(0);
        CommitInput commitInput = new CommitInput(commit);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/api/commits";
        HttpEntity<CommitInput> request =
                new HttpEntity<CommitInput>(commitInput, headers);
        restTemplate.postForObject(url, request, CommitInput.class);
        return  commitInput;
    }
}
