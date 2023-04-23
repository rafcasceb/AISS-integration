package aiss.gitminer.controller;

import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    IssueRepository repository;

    @GetMapping
    public List<Issue> findAll(){return repository.findAll();}

    @GetMapping("/{id}")
    public Issue findOne(@PathVariable long id){
        Optional<Issue> issue = repository.findById(id);
        return issue.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Issue create(@Valid @RequestBody Issue issue){
        Issue _issue = repository
                .save(new Issue(issue.getId(),
                        issue.getRefId(),
                        issue.getTitle(),
                        issue.getDescription(),
                        issue.getState(),
                        issue.getCreatedAt(),
                        issue.getUpdatedAt(),
                        issue.getClosedAt(),
                        issue.getLabels(),
                        issue.getAuthor(),
                        issue.getAssignee(),
                        issue.getUpvotes(),
                        issue.getDownvotes(),
                        issue.getWebUrl(),
                        issue.getComments()
                        ));
        return _issue;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Issue updated,@PathVariable long id){
        Optional<Issue> issueData = repository.findById(id);

        Issue _issue = issueData.get();
        _issue.setId(updated.getId());
        _issue.setRefId(updated.getRefId());
        _issue.setTitle(updated.getTitle());
        _issue.setDescription(updated.getDescription());
        _issue.setState(updated.getState());
        _issue.setCreatedAt(updated.getCreatedAt());
        _issue.setUpdatedAt((updated.getUpdatedAt()));
        _issue.setClosedAt(updated.getClosedAt());
        _issue.setLabels(updated.getLabels());
        _issue.setAuthor(updated.getAuthor());
        _issue.setAssignee(updated.getAssignee());
        _issue.setUpvotes(updated.getUpvotes());
        _issue.setWebUrl(updated.getWebUrl());
        _issue.setComments(updated.getComments());

        repository.save(_issue);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@PathVariable long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}
